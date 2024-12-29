package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.blocks.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.CreativeInventoryGui;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
public class Player {
    Game game;

    private final SavedChunksManager savedChunksManager = new SavedChunksManager(this);

    private final Location location;

    private final int reach = 5;

    private boolean isFlying = false;
    private boolean isSneaking = false;
    private boolean isSprinting = false;

    private final int jumpForce = 2;

    private final int walkSpeed = 5;
    private final int sneakSpeed = 3;
    private final int sprintSpeed = 8;
    private float accelerationByTick = 0.2f;

    private final int[] movements = new int[3];
    private final Vector3f velocity = new Vector3f(0, 0, 0);


    private int selectedSlot;
    private Inventory inventory;

    public Player(Game game){
        this(game, 0, 0, 0, 0, 0);
    }

    public Player(Game game, double x, double y, double z){
        this(game, x, y, z, 0, 0);
    }

    public Player(Game game, double x, double y, double z, float yaw, float pitch){
        this.game = game;
        this.location = new Location(game.getWorld(),x, y, z, yaw, pitch);
        this.inventory = new Inventory(this);
        //this.normal = Vector.fromYawPitch(yaw, pitch);

    }
    public Block getBlockToPlace() {

        float stepSize = 0.2F;

        Vector3f direction = getRayDirection();
        Vector3f start = new Vector3f((float) location.getX(), (float) location.getY(), (float) location.getZ());
        Vector3f hitPosition;
        Block block = null;
        //System.out.println("Starting at: " + start);
        //System.out.println("Direction: " + direction);

        // Start the iteration from zero
        for (float distance = 0; distance < reach; distance += stepSize) {
            // Calculate the current position based on start point and direction
            hitPosition = new Vector3f(start).add(new Vector3f(direction).mul(distance));
            //System.out.println("Checking position: " + hitPosition);

            Block block1 = game.getWorld().getBlockAt(hitPosition, true);
            if(block1 == null) return null;

            if (block1.getMaterial() != null) {
                return block;
            }
            // Check for prop at the current position
            block = block1;
        }
        return null;
    }


    public Block getSelectedBlock() {
        float stepSize = 0.2F;

        Vector3f direction = getRayDirection();
        Vector3f start = new Vector3f((float) location.getX(), (float) location.getY(), (float) location.getZ());

        //System.out.println("Starting at: " + start);
        //System.out.println("Direction: " + direction);

        // Start the iteration from zero
        for (float distance = 0; distance < reach; distance += stepSize) {
            // Calculate the current position based on start point and direction
            Vector3f currentPosition = new Vector3f(start).add(new Vector3f(direction).mul(distance));
            //System.out.println("Checking position: " + currentPosition);

            // Check for prop at the current position
            Block block = game.getWorld().getBlockAt(currentPosition, true);

            if (block != null && block.getMaterial() != null) {
                //System.out.println("Player's pos : " + this.getLocation().getX() + " " + this.getLocation().getY() + " " + this.getLocation().getZ() + " ");
                //System.out.println("Found prop at position: " + currentPosition);
                return block;
            }
        }
        //System.out.println("No prop found within range.");
        return null;
    }



    private Vector3f getRayDirection() {
        return new Vector3f(
                (float) Math.cos(Math.toRadians(location.getYaw())) * (float) Math.cos(Math.toRadians(location.getPitch())),
                (float) Math.sin(Math.toRadians(location.getPitch())),
                (float) Math.sin(Math.toRadians(location.getYaw())) * (float) Math.cos(Math.toRadians(location.getPitch()))
        ).normalize();
    }

    public void addX(double a){
        this.location.addX(a);
        game.graphicModule.getCamera().pos.add((float) a, 0, 0);
    }
    public void addZ(double a){
        this.location.addZ(a);
        game.graphicModule.getCamera().pos.add(0, 0, (float) a);
    }
    public void addY(double a){
        this.location.addY(a);
        game.graphicModule.getCamera().pos.add(0, (float) a, 0);
    }
    public void setYaw(float a){
        this.location.setYaw(a);
    }
    public void setPitch(float a){
        this.location.setPitch(a);
    }

    private void onMove(){

    }

    public void placeBlock(final Item item){
        // Déterminer la face du bloc où le joueur a cliqué
        Block block = this.getBlockToPlace();
        if (block == null) {
            System.out.println("Impossible de déterminer la face du bloc.");
            return;
        }
        Material material = item.getMaterial();

        // Appeler la méthode pour placer le bloc dans le jeu
        block.setModel(item.getModel()).setMaterial(material);
        this.playSound(material.getSound());
    }

    public void breakBlock(){
        Block block = this.getSelectedBlock();
        if(block == null || block.getMaterial() == null) return;
        this.playSound(block.getMaterial().getSound());
        block.setMaterial(null);
    }

    public void playSound(final Sound sound){
        this.game.getAudioManager().playSound(sound);
    }

    public Block getBlockDown(){
        return this.getLocation().getWorld().getBlockAt(this.getLocation().getX(), this.getLocation().getY()-1.6f, this.getLocation().getZ(), false);
    }

    public boolean hasBlockDown(){
        Block block = this.getBlockDown();
        return block != null && block.getMaterial() != null;
    }

    public int getSpeed(){
        if(isSneaking) return sneakSpeed;
        if(isSprinting) return sprintSpeed;
        return walkSpeed;
    }

    public void jump() {
        if(this.velocity.get(1) == 0)
            this.getVelocity().add(0, (float)jumpForce/5, 0);
    }

    public void setSelectedSlot(int slot){
        this.selectedSlot=slot;
        this.game.getGraphicModule().getGuiModule().setSelectedSlot(slot);
    }

    public Item getSelectedItem(){
        return this.inventory.getItem(this.selectedSlot);
    }

    public void addItem(Item item){
        this.getInventory().setFreeSlot(item);
        updateInventory();
    }

    public void updateInventory(){
        if(this.game.graphicModule != null)
            this.game.graphicModule.getGuiModule().updateInventory(this);
    }

    public void openInventory(){
        game.setPaused(true);
        game.graphicModule.getGuiModule().openGUI(new CreativeInventoryGui());
    }

    public boolean hasOpenedInventory(){
        return game.graphicModule.getGuiModule().hasGUIOpened();
    }

    public void closeInventory() {
        game.getGraphicModule().getGuiModule().closeGUI();
    }
}
