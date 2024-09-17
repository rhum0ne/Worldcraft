package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.props.Block;
import lombok.Getter;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.Game.SHOW_DISTANCE;

@Getter
public class Player {
    Game game;

    private final SavedChunksManager savedChunksManager = new SavedChunksManager(this);

    private final Location location;
    private final Vector normal;

    private final int maxDistance = 5;
    private final int speed = 5;

    public Player(Game game){
        this(game, 0, 0, 0, 0, 0);
    }

    public Player(Game game, double x, double y, double z){
        this(game, x, y, z, 0, 0);
    }

    public Player(Game game, double x, double y, double z, float yaw, float pitch){
        this.game = game;
        this.location = new Location(x, y, z, yaw, pitch);
        this.normal = Vector.fromYawPitch(yaw, pitch);

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


    public Block getSelectedBlock() {
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



    public Vector3f getRayDirection() {
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
    }
    public void moveBackward(double a){
        double move = 0.01;
        double i =0;
        while(i<a) {
            this.addX(-move * Math.cos(Math.toRadians(this.location.getYaw())));
            this.addZ(-move * Math.sin(Math.toRadians(this.location.getYaw())));
            i+=move;
        }
    }
    public void moveRight(double a){
        double move = 0.01;
        double i =0;
        while(i<a) {
            this.addX(-move * Math.sin(Math.toRadians(this.location.getYaw())));
            this.addZ(move * Math.cos(Math.toRadians(this.location.getYaw())));
            i+=move;
        }
    }
    public void moveLeft(double a){
        double move = 0.01;
        double i =0;
        while(i<a) {
            this.addX(move * Math.sin(Math.toRadians(this.location.getYaw())));
            this.addZ(-move * Math.cos(Math.toRadians(this.location.getYaw())));
            i+=move;
        }
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
        this.normal.setYaw(this.location.getYaw());
    }
    public void setPitch(float a){
        this.location.setPitch(a);
        this.normal.setPitch(this.location.getPitch());
    }
}
