package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import fr.rhumun.game.worldcraftopengl.blocks.Block;
import fr.rhumun.game.worldcraftopengl.blocks.Material;
import fr.rhumun.game.worldcraftopengl.blocks.Model;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

@Getter
public class Player {
    Game game;

    private final SavedChunksManager savedChunksManager = new SavedChunksManager(this);

    private final Location location;

    private final int maxDistance = 5;
    private final int speed = 5;

    @Setter
    private Material selectedMaterial = Material.DIRT;

    public Player(Game game){
        this(game, 0, 0, 0, 0, 0);
    }

    public Player(Game game, double x, double y, double z){
        this(game, x, y, z, 0, 0);
    }

    public Player(Game game, double x, double y, double z, float yaw, float pitch){
        this.game = game;
        this.location = new Location(x, y, z, yaw, pitch);
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
        for (float distance = 0; distance < maxDistance; distance += stepSize) {
            // Calculate the current position based on start point and direction
            hitPosition = new Vector3f(start).add(new Vector3f(direction).mul(distance));
            //System.out.println("Checking position: " + hitPosition);

            Block block1 = game.getWorld().getBlockAt(hitPosition);
            if(block1 == null) return null;

            if (block1.getMaterial() != null) {
                //System.out.println("Found prop at position: " + hitPosition);
                break;
            }
            // Check for prop at the current position
            block = game.getWorld().getBlockAt(hitPosition);
        }

        return block;
    }


    private Block getSelectedBlock() {
        float stepSize = 0.2F;

        Vector3f direction = getRayDirection();
        Vector3f start = new Vector3f((float) location.getX(), (float) location.getY(), (float) location.getZ());

        //System.out.println("Starting at: " + start);
        //System.out.println("Direction: " + direction);

        // Start the iteration from zero
        for (float distance = 0; distance < maxDistance; distance += stepSize) {
            // Calculate the current position based on start point and direction
            Vector3f currentPosition = new Vector3f(start).add(new Vector3f(direction).mul(distance));
            //System.out.println("Checking position: " + currentPosition);

            // Check for prop at the current position
            Block block = game.getWorld().getBlockAt(currentPosition);

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



    public void moveForward(double a){
        double move = 0.01;
        double i =0;
        while(i<a) {
            this.addX(move * Math.cos(Math.toRadians(this.location.getYaw())));
            this.addZ(move * Math.sin(Math.toRadians(this.location.getYaw())));
            i+=move;
        }
        this.onMove();
    }
    public void moveBackward(double a){
        double move = 0.01;
        double i =0;
        while(i<a) {
            this.addX(-move * Math.cos(Math.toRadians(this.location.getYaw())));
            this.addZ(-move * Math.sin(Math.toRadians(this.location.getYaw())));
            i+=move;
        }
        this.onMove();
    }
    public void moveRight(double a){
        double move = 0.01;
        double i =0;
        while(i<a) {
            this.addX(-move * Math.sin(Math.toRadians(this.location.getYaw())));
            this.addZ(move * Math.cos(Math.toRadians(this.location.getYaw())));
            i+=move;
        }
        this.onMove();
    }
    public void moveLeft(double a){
        double move = 0.01;
        double i =0;
        while(i<a) {
            this.addX(move * Math.sin(Math.toRadians(this.location.getYaw())));
            this.addZ(-move * Math.cos(Math.toRadians(this.location.getYaw())));
            i+=move;
        }
        this.onMove();
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
        //this.normal.setYaw(this.location.getYaw());
    }
    public void setPitch(float a){
        this.location.setPitch(a);
        //this.normal.setPitch(this.location.getPitch());
    }

    private void onMove(){

    }

    public void placeBlock(final Material material){
        // Déterminer la face du bloc où le joueur a cliqué
        Block block = this.getBlockToPlace();
        if (block == null) {
            System.out.println("Impossible de déterminer la face du bloc.");
            return;
        }


        // Appeler la méthode pour placer le bloc dans le jeu
        block.setModel(Model.BLOCK).setMaterial(material);
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
}
