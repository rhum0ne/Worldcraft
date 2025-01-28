package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.content.items.Item;
import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
@Setter
public abstract class Entity {
    private final Game game;

    private Location location;

    private final int reach = 5;

    private boolean isFlying = false;
    private boolean isSneaking = false;
    private boolean isSprinting = false;

    private final int jumpForce = 2;

    private final int walkSpeed = 3;
    private final int sneakSpeed = 2;
    private final int sprintSpeed = 5;
    private float accelerationByTick = 0.1f;

    private final int[] movements = new int[3];
    private final Vector3f velocity = new Vector3f(0, 0, 0);

    private float radius = 0.25f;
    private float height = 1.8f;


    public Entity(Game game, int reach, float radius, float height, int walkSpeed, int sneakSpeed, int sprintSpeed, float accelerationByTick, int jumpForce, double x, double y, double z, float yaw, float pitch) {
        this.location = new Location(game.getWorld(),x, y, z, yaw, pitch);
        this.game = game;
    }

    public void setLocation(Location loc){
        this.location = new Location(loc);
    }



    Vector3f getRayDirection() {
        return new Vector3f(
                (float) Math.cos(Math.toRadians(this.getLocation().getYaw())) * (float) Math.cos(Math.toRadians(this.getLocation().getPitch())),
                (float) Math.sin(Math.toRadians(this.getLocation().getPitch())),
                (float) Math.sin(Math.toRadians(this.getLocation().getYaw())) * (float) Math.cos(Math.toRadians(this.getLocation().getPitch()))
        ).normalize();
    }

    public void addX(double a){
        if ((this.hasBlockInDirection(new Vector3f((float) a, 0, 0)) && !Game.NO_CLIP)) {
            return;
        }
        this.getLocation().addX(a);
        this.onMove();
    }
    public void addZ(double a){
        if ((this.hasBlockInDirection(new Vector3f( 0, 0, (float)a)) && !Game.NO_CLIP)) {
            return;
        }
        this.getLocation().addZ(a);
        this.onMove();
    }
    public void addY(double a){
        if(!Game.NO_CLIP && a>0 && this.hasBlockTop()){
            return;
        } else if(!Game.NO_CLIP && a<0 && this.hasBlockDown()) {
            return;
        }
        this.getLocation().addY(a);
        this.onMove();
    }
    public void setYaw(float a){
        this.getLocation().setYaw(a);
        this.onMove();
    }
    public void setPitch(float a){
        this.getLocation().setPitch(a);
        this.onMove();
    }

    protected void onMove(){

    }

    public Block getBlockInDirection(Vector3f direction, int yLevel) {
        if(yLevel > this.height){
            this.game.errorLog("Trying to get block at yLevel for an Entity with height " + this.height + "\nreturning null...");
            return null;
        }
        // Normaliser le vecteur pour garantir qu'il a une norme de 1
        Vector3f normalizedDirection = direction.normalize();

        // Calculer les coordonnées en fonction du rayon du joueur
        double targetX = this.getLocation().getX() + normalizedDirection.get(0) * this.radius;
        double targetY = this.getLocation().getY() - yLevel;
        double targetZ = this.getLocation().getZ() + normalizedDirection.get(2) * this.radius;

        // Retourner le bloc à cette position
        return this.getLocation().getWorld().getBlockAt(targetX, targetY, targetZ, false);
    }

    public boolean hasBlockInDirection(Vector3f direction) {
        for(int y=0; y<this.height; y++) {
            Block block = this.getBlockInDirection(direction, y);
            if( block != null && block.getMaterial() != null) return true;
        }
        return false;
    }


    public Block getBlockDown(){
        return this.getLocation().getWorld().getBlockAt(this.getLocation().getX(), this.getLocation().getY()-1.6f, this.getLocation().getZ(), false);
    }

    public boolean hasBlockDown(){
        Block block = this.getBlockDown();
        return block != null && block.getMaterial() != null;
    }

    public Block getBlockTop(){
        return this.getLocation().getWorld().getBlockAt(this.getLocation().getX(), this.getLocation().getY()-1.6f+this.height, this.getLocation().getZ(), false);
    }

    public boolean hasBlockTop(){
        Block block = this.getBlockTop();
        return block != null && block.getMaterial() != null;
    }

    public int getSpeed(){
        if(isSneaking) return sneakSpeed;
        if(isSprinting) return sprintSpeed;
        return walkSpeed;
    }

    public void jump() {
        if(this.velocity.get(1) == 0)
            this.getVelocity().add(0, (float)jumpForce/9, 0);
    }


    public Block getSelectedBlock() {
        float stepSize = 0.02F;

        Vector3f direction = getRayDirection();
        Vector3f start = new Vector3f((float) this.getLocation().getX(), (float) this.getLocation().getY(), (float) this.getLocation().getZ());

        //System.out.println("Starting at: " + start);
        //System.out.println("Direction: " + direction);

        // Start the iteration from zero
        for (float distance = 0; distance < this.getReach(); distance += stepSize) {
            // Calculate the current position based on start point and direction
            Vector3f currentPosition = new Vector3f(start).add(new Vector3f(direction).mul(distance));
            //System.out.println("Checking position: " + currentPosition);

            // Check for prop at the current position
            Block block = this.getGame().getWorld().getBlockAt(currentPosition, true);

            if (block != null && block.getMaterial() != null && !block.getMaterial().isLiquid()) {
                //System.out.println("Player's pos : " + this.getLocation().getX() + " " + this.getLocation().getY() + " " + this.getLocation().getZ() + " ");
                //System.out.println("Found prop at position: " + currentPosition);
                return block;
            }
        }
        //System.out.println("No prop found within range.");
        return null;
    }

    public void placeBlock(final Item item){
        // Déterminer la face du bloc où le joueur a cliqué

        float stepSize = 0.02F;

        Vector3f direction = getRayDirection();
        Vector3f start = new Vector3f((float) this.getLocation().getX(), (float) this.getLocation().getY(), (float) this.getLocation().getZ());
        Vector3f hitPosition = null;
        Block block = null;
        Block block1 = null;
        //System.out.println("Starting at: " + start);
        //System.out.println("Direction: " + direction);

        // Start the iteration from zero
        for (float distance = 0; distance < this.getReach(); distance += stepSize) {
            // Calculate the current position based on start point and direction
            Vector3f hitPosition1 = new Vector3f(start).add(new Vector3f(direction).mul(distance));
            //System.out.println("Checking position: " + hitPosition);

            block1 = this.getLocation().getWorld().getBlockAt(hitPosition1, true);
            if(block1 == null) break;

            if (block1.isCliquable()) {
                break;
            }
            // Check for prop at the current position
            block = block1;
            hitPosition = hitPosition1;
        }

        if(block1 == null || !block1.isCliquable()) return;
        placeBlockAt(item, block, hitPosition, direction);
    }

    protected void placeBlockAt(Item item, Block block, Vector3f hitPosition, Vector3f direction){
        if (block == null) {
            System.out.println("Impossible de déterminer la face du bloc.");
            return;
        }

        Material material = item.getMaterial();

        Model model = item.getModel();
        // Appeler la méthode pour placer le bloc dans le jeu
        if(model == Model.SLAB){
            float y = (float) (hitPosition.y() - Math.floor(hitPosition.y()));
            if(y>0.5f) block.setState(1);
            else if(y<0.5f) block.setState(0);
            else model = Model.BLOCK;
        }
        block.setModel(model).setMaterial(material);
    }

    public Material breakBlock(){
        Block block = this.getSelectedBlock();
        if(block == null || block.getMaterial() == null) return null;
        block.setMaterial(null);
        return block.getMaterial();
    }
}
