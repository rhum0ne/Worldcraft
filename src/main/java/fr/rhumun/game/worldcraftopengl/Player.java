package fr.rhumun.game.worldcraftopengl;

import lombok.Getter;

@Getter
public class Player {
    Game game;

    private final Location location;
    private final Vector normal;

    private final int speed = 5;

    public Player(Game game){
        this(game, 0, 0, 0, 0, 0);
    }

    public Player(Game game, double x, double y, double z){
        this(game, x, y, z, 0, 0);
    }

    public Player(Game game, double x, double y, double z, double yaw, double pitch){
        this.game = game;
        this.location = new Location(x, y, z, yaw, pitch);
        this.normal = Vector.fromYawPitch(yaw, pitch);

    }

    public void moveForward(){
        this.addX((double) this.speed /100.0 * 1);
    }

    public void addX(double a){ this.location.addX(a); }
    public void addZ(double a){ this.location.addZ(a); }
    public void addY(double a){ this.location.addY(a); }
    public void addYaw(double a){
        this.location.addYaw(a);
        this.normal.setYaw(this.location.getYaw());
    }
    public void addPitch(double a){
        this.location.addPitch(a);
        this.normal.setPitch(this.location.getPitch());
    }
}
