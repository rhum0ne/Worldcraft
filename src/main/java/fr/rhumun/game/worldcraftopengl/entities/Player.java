package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.LoadedChunksManager;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.creative_inventory.CreativeInventoryGui;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.AxisAlignedBB;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
public class Player extends Entity implements MovingEntity{

    private final LoadedChunksManager loadedChunksManager = new LoadedChunksManager(this);

    private int selectedSlot;
    private final Inventory inventory;

    private final int[] movements = new int[3];


    public Player(Game game){
        this(game, 0, 0, 0, 0, 0);
    }

    public Player(Game game, double x, double y, double z){
        this(game, x, y, z, 0, 0);
    }

    public Player(Game game, double x, double y, double z, float yaw, float pitch){
        super(game, 5, 0.25f, 1.8f, 3, 1, 5, 0.2f, 2, x, y ,z, yaw, pitch);
        this.inventory = new Inventory(this);

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

    public void setFlying(boolean state){
        if(!state) this.getMovements()[1] = 0;
        super.setFlying(state);
    }

    @Override
    protected void onMove(){
        getGame().getData().setPlayerPos(this.getLocation());
    }

    public Block getBlockToPlace() {
        Vector3f direction = this.getRayDirection();
        Vector3f pos = new Vector3f((float) this.getLocation().getX(), (float) this.getLocation().getY(), (float) this.getLocation().getZ());
        Block block = null;

        for (float distance = 0; distance < this.getReach(); distance += RAY_STEP) {
            Block block1 = this.getGame().getWorld().getBlockAt(pos, true);
            if(block1 == null) return null;

            if (block1.getMaterial() != null) {
                return block;
            }
            block = block1;
            pos.add(direction.x * RAY_STEP, direction.y * RAY_STEP, direction.z * RAY_STEP);
        }
        return null;
    }

    @Override
    public void placeBlockAt(Item item, Block block, Vector3f hitPosition, Vector3f direction){
        super.placeBlockAt(item, block, hitPosition, direction);
        this.playSound(item.getMaterial().getPlaceSound());
    }

    @Override
    public Material breakBlock(){
        Material mat = super.breakBlock();
        if(mat==null) return null;
        this.playSound(mat.getMaterial().getBreakSound());
        return mat;
    }

    public void playSound(final Sound sound){
        this.getGame().getAudioManager().playSound(sound);
    }

    public void playSound(final Sound sound, final float pitch){ this.getGame().getAudioManager().playSound(sound, pitch);}

    public void setSelectedSlot(int slot){
        this.selectedSlot=slot;
        this.getGame().getGraphicModule().getGuiModule().setSelectedSlot(slot);
    }

    public Item getSelectedItem(){
        return this.inventory.getItem(this.selectedSlot);
    }

    public void addItem(Item item){
        this.getInventory().setFreeSlot(item);
        updateInventory();
    }

    public void updateInventory(){
        if(this.getGame().getGraphicModule() != null)
            this.getGame().getGraphicModule().getGuiModule().updateInventory(this);
    }

    public void openInventory(){
        this.openGui(new CreativeInventoryGui());
    }

    public void openGui(Gui gui){
        getGame().setPaused(true);
        getGame().getGraphicModule().getGuiModule().openGUI(gui);
    }

    public boolean hasOpenedInventory(){
        return getGame().getGraphicModule().getGuiModule().hasGUIOpened();
    }

    public void closeInventory() {
        getGame().getGraphicModule().getGuiModule().closeGUI();
    }

    public Block getBlockDown(){
        Block block = this.getLocation().getWorld().getBlockAt(
                this.getLocation().getX(),
                this.getLocation().getY() - 1.6f,
                this.getLocation().getZ(),
                false
        );
        return block != null && block.getMaterial() != null && !block.getMaterial().isLiquid()
                ? block : null;
    }

    public Block getBlockTop(){
        return this.getLocation().getWorld().getBlockAt(this.getLocation().getX(), this.getLocation().getY()-1.6f+this.getHeight(), this.getLocation().getZ(), false);
    }

        this.swimming = swimming;
    }
}
