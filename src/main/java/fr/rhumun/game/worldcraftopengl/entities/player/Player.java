package fr.rhumun.game.worldcraftopengl.entities.player;

import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.LoadedChunksManager;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.BreakEvent;
import fr.rhumun.game.worldcraftopengl.content.materials.items.types.BlockItemMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.items.types.ToolItemMaterial;
import fr.rhumun.game.worldcraftopengl.content.materials.Material;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.entities.Inventory;
import fr.rhumun.game.worldcraftopengl.entities.LivingEntity;
import fr.rhumun.game.worldcraftopengl.entities.MovingEntity;
import fr.rhumun.game.worldcraftopengl.entities.Entity;
import fr.rhumun.game.worldcraftopengl.entities.ItemEntity;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.player_inventory.PlayerInventoryGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.creative_inventory.CreativePlayerInventoryGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.death_menu.DeathGui;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.AxisAlignedBB;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;
import java.util.Iterator;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
@Setter
public class Player extends LivingEntity implements MovingEntity {

    private final LoadedChunksManager loadedChunksManager = new LoadedChunksManager(this);

    private int selectedSlot;
    private final Inventory inventory;
    /** Whether the player has creative privileges. */
    private Gamemode gamemode = Gamemode.SURVIVAL;

    private int maxFood = 20;
    private int food = 1;

    private int regenCounter = 0;

    private int maxSaturation = 128;
    private int saturation = 128;
    private int saturationCounter = 0;

    private int starvationCounter = 0;

    /** Delay in ticks between each item pickup check. */
    private static final int ITEM_PICKUP_DELAY = 120;
    private int itemPickupCounter = 0;

    public static int MOVE_SATURATION_COST = 1;
    public static int BREAK_SATURATION_COST = 4;
    public static int REGEN_SATURATION_COST = 10;

    private double lastX;
    private double lastY;
    private double lastZ;

    private final int[] movements = new int[3];

    private Block breakingBlock;
    private float breakingProgress;
    private boolean isBreaking;

    public Player(){
        this(0, 0, 0, 0, 0);
    }

    public Player(double x, double y, double z){
        this(x, y, z, 0, 0);
    }

    public Player(double x, double y, double z, float yaw, float pitch){
        super(5, 0.25f, 1.8f, 3, 1, 5, 0.2f, 2, x, y ,z, yaw, pitch);
        this.inventory = new Inventory(this);
        this.lastX = x;
        this.lastY = y;
        this.lastZ = z;

    }

    @Override
    public AxisAlignedBB getBoundingBox() {
        float minX = (float) (this.getLocation().getX() - getRadius());
        float minY = (float) (this.getLocation().getY() - 1.6f);
        float maxX = (float) (this.getLocation().getX() + getRadius());
        float maxY = minY + getHeight();
        float minZ = (float) (this.getLocation().getZ() - getRadius());
        float maxZ = (float) (this.getLocation().getZ() + getRadius());
        return new AxisAlignedBB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public void setFlying(boolean state) {
        if (!state) this.getMovements()[1] = 0;
        super.setFlying(state);
    }

    @Override
    protected void onMove(){
        GAME.getData().setPlayerPos(this.getLocation());
    }

    public Block getBlockToPlace() {
        Vector3f direction = this.getRayDirection();
        Vector3f pos = new Vector3f((float) this.getLocation().getX(), (float) this.getLocation().getY(), (float) this.getLocation().getZ());
        Block block = null;

        for (float distance = 0; distance < this.getReach(); distance += RAY_STEP) {
            Block block1 = GAME.getWorld().getBlockAt(pos, true);
            if(block1 == null) return null;

            if (!block1.isAir()) {
                return block;
            }
            block = block1;
            pos.add(direction.x * RAY_STEP, direction.y * RAY_STEP, direction.z * RAY_STEP);
        }
        return null;
    }

    @Override
    public void placeBlockAt(ItemStack item, Block block, Vector3f hitPosition, Vector3f direction){
        if(item.getMaterial() instanceof PlaceableMaterial pM){
            super.placeBlockAt(item, block, hitPosition, direction);
            this.playSound(pM.getPlaceSound());
        }
        else if(item.getMaterial() instanceof BlockItemMaterial bM){
            super.placeBlockAt(item, block, hitPosition, direction);
            this.playSound(bM.getPlaceSound());
        }
        else {
            GAME.warn("Trying to place a non-placeable material : " + item.getMaterial() + " at " + hitPosition + " for player " + this.getLocation());
        }

        if(!this.isInCreativeMode()) {
            item.remove(1);
            if(item.isEmpty()) {
                this.inventory.setItem(this.selectedSlot, null);
            }
            updateInventory();
        }
    }

    @Override
    public Material breakBlock() {
        Block target = getSelectedBlock();
        Material mat = super.breakBlock();
        if (mat == null) return null;
        if(mat instanceof PlaceableMaterial pM) {
            this.playSound(pM.getBreakSound());
            if(!this.isInCreativeMode() && target != null && pM.isLootable()) {
                this.getWorld().spawnItem(new ItemStack(mat), target.getLocation());
            }
        }

        if(mat instanceof BreakEvent e) e.onBreak(target, this);
        consumeSaturation(BREAK_SATURATION_COST);

        return mat;
    }

    public LivingEntity getEntityInSight() {
        Vector3f direction = this.getRayDirection();
        Vector3f pos = new Vector3f((float) this.getLocation().getX(), (float) this.getLocation().getY(), (float) this.getLocation().getZ());

        for (float distance = 0; distance < this.getReach(); distance += RAY_STEP) {
            for (Entity e : this.getWorld().getEntities()) {
                if (e == this || !(e instanceof LivingEntity le)) continue;
                AxisAlignedBB bb = e.getBoundingBox();
                if (pos.x >= bb.minX && pos.x <= bb.maxX &&
                    pos.y >= bb.minY && pos.y <= bb.maxY &&
                    pos.z >= bb.minZ && pos.z <= bb.maxZ) {
                    return le;
                }
            }
            pos.add(direction.x * RAY_STEP, direction.y * RAY_STEP, direction.z * RAY_STEP);
        }
        return null;
    }
  
    public void playSound(final Sound sound){
        GAME.getAudioManager().playSound(sound);
    }

    public void playSound(final Sound sound, final float pitch){ GAME.getAudioManager().playSound(sound, pitch);}

    public void setSelectedSlot(int slot){
        this.selectedSlot=slot;
        GAME.getGraphicModule().getGuiModule().setSelectedSlot(slot);
    }

    /**
     * Sets the selected slot without updating any GUI.
     * Used during asynchronous loading before the render thread is ready.
     */
    public void setSelectedSlotRaw(int slot) {
        this.selectedSlot = slot;
    }

    public ItemStack getSelectedItem(){
        return this.inventory.getItem(this.selectedSlot);
    }

    public void addItem(ItemStack item){
        this.getInventory().setFreeSlot(item);
        updateInventory();
    }
  
    public void updateInventory(){
        if(GAME.getGraphicModule() != null)
            GAME.getGraphicModule().getGuiModule().updateInventory(this);
    }

    public void startBreaking() {
        Block target = getSelectedBlock();
        if (target == null || target.isAir()) {
            return;
        }
        if (target != breakingBlock) {
            breakingBlock = target;
            breakingProgress = 0f;
        }
        isBreaking = true;
    }

    public void stopBreaking() {
        isBreaking = false;
        breakingProgress = 0f;
        breakingBlock = null;
    }

    private void updateBreaking() {
        if (!isBreaking) return;
        if (breakingBlock == null || breakingBlock.isAir()) {
            stopBreaking();
            return;
        }
        if (getSelectedBlock() != breakingBlock) {
            stopBreaking();
            return;
        }

        float increment = 1f;
        ItemStack held = getSelectedItem();
        if (held != null && held.getMaterial() instanceof ToolItemMaterial tool) {
            if (tool.getType() == breakingBlock.getMaterial().getToolType()) {
                increment = tool.getLevel();
            }
        }
        breakingProgress += increment / (breakingBlock.getMaterial().getDurability()*60f);
        if (breakingProgress >= 1f) {
            breakBlock();
            stopBreaking();
            if(getSelectedBlock() != null) startBreaking();
        }
    }

    public int getBreakingStage() {
        if( breakingBlock == null || breakingBlock.isAir()) return 0;
        return (int) (breakingProgress * 10);
    }

    private void updateItemPickup() {
        itemPickupCounter++;
        if (itemPickupCounter < ITEM_PICKUP_DELAY) return;
        itemPickupCounter = 0;

        Iterator<Entity> it = this.getWorld().getEntities().iterator();
        while (it.hasNext()) {
            Entity e = it.next();
            if (e instanceof ItemEntity item) {
                double dx = e.getLocation().getX() - this.getLocation().getX();
                double dy = e.getLocation().getY() - this.getLocation().getY();
                double dz = e.getLocation().getZ() - this.getLocation().getZ();
                if (dx * dx + dy * dy + dz * dz <= 4) { // within 2 blocks
                    this.addItem(new ItemStack(item.getMaterial(), item.getModel()));
                    it.remove();
                }
            }
        }
    }

    /**
     * Opens the appropriate inventory depending on the player's mode.
     * In creative mode, the inventory is combined with the give menu.
     */
    public void openInventory(){
        if (isInCreativeMode()) {
            this.openGui(new CreativePlayerInventoryGui());
        } else {
            this.openGui(new PlayerInventoryGui());
        }
    }

    public void openGui(Gui gui){
        GAME.setPaused(true);
        GAME.getGraphicModule().getGuiModule().openGUI(gui);
    }

    public boolean hasOpenedInventory(){
        return GAME.getGraphicModule().getGuiModule().hasGUIOpened();
    }

    public void closeInventory() {
        GAME.getGraphicModule().getGuiModule().closeGUI();
    }

    public Block getBlockDown() {
        Block block = this.getLocation().getWorld().getBlockAt(
                this.getLocation().getX(),
                this.getLocation().getY() - 1.6f,
                this.getLocation().getZ(),
                false
        );
        return block != null && !block.isAir() && !block.getMaterial().isLiquid()
                ? block : null;
    }

    public Block getBlockTop() {
        return this.getLocation().getWorld().getBlockAt(this.getLocation().getX(), this.getLocation().getY() - 1.6f + this.getHeight(), this.getLocation().getZ(), false);
    }

    @Override
    public void update() {
        boolean moved = this.getLocation().getX() != lastX ||
                        this.getLocation().getY() != lastY ||
                        this.getLocation().getZ() != lastZ;

        lastX = this.getLocation().getX();
        lastY = this.getLocation().getY();
        lastZ = this.getLocation().getZ();

        if (moved) {
            updateSaturation();
        }

        updateFallDamage();
        updateHealth();
        updateFood();
        updateSaturation();
        updateBreaking();
        updateItemPickup();
    }


    protected void updateHealth() {
        if (health < maxHealth) {
            regenCounter++;
            if (regenCounter >= 100) {
                health++;
                this.consumeSaturation(REGEN_SATURATION_COST);
                regenCounter = 0;
            }
        } else {
            regenCounter = 0;
        }
    }

    private void updateFood() {
        if (food > 0) {
            starvationCounter = 0;
        } else {
            starvationCounter++;
            if (starvationCounter >= 100) {
                damage(2);
                starvationCounter = 0;
            }
        }
    }

    public void consumeFood(int amount) {
        if (amount <= 0) return;
        food -= amount;
        if (food < 0) food = 0;
    }

    public void addFood(int amount) {
        if (amount <= 0) return;
        food = Math.min(maxFood, food + amount);
    }

    private void updateSaturation() {
        saturationCounter++;
        if (saturationCounter >= 150) {
            consumeSaturation(1);
            saturationCounter = 0;
        }
    }

    @Override
    public void damage(int amout){
        if(this.isInCreativeMode()) return;

        super.damage(amout);
        this.playSound(Sound.HURT);
    }

    public void consumeSaturation(int amount) {
        if (amount <= 0) return;
        saturation -= amount;
        if (saturation <= 0) {
            saturation = maxSaturation;
            consumeFood(1);
        }
    }

    public void addSaturation(int amount) {
        if (amount <= 0) return;
        saturation = Math.min(maxSaturation, saturation + amount);
    }

    @Override
    public void updateSwimmingState(){
        boolean swimming = isInsideLiquid();
        if(!swimming && this.isSwimming() && !this.isFlying()) {
            this.movements[1] = 0;
        }
        this.setSwimming(swimming);
    }

    public boolean isInCreativeMode(){
        return this.gamemode == Gamemode.CREATIVE;
    }

    @Override
    public void kill() {
        this.openGui(new fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.death_menu.DeathGui());
    }

    /**
     * Respawn the player at the world's spawn location with full health and hunger.
     * Closes the death screen when done.
     */
    public void respawn() {
        setHealth(getMaxHealth());
        setFood(getMaxFood());
        setSaturation(getMaxSaturation());
        getWorld().spawnPlayer(this);
        this.getVelocity().set(0, 0, 0);
        this.closeInventory();
    }

    public boolean isHungry() {
        return !this.isInCreativeMode() && this.food < this.maxFood;
    }

    public void reset() {
        this.getInventory().clear();
        this.setHealth(this.getMaxHealth());
        this.setFood(this.getMaxFood());
        this.setSaturation(this.getMaxSaturation());
        this.setGamemode(Gamemode.SURVIVAL);
        this.setSelectedSlot(0);
        this.setFlying(false);
        this.setSwimming(false);
    }
}
