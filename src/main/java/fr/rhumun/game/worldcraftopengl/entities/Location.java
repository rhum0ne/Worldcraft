package fr.rhumun.game.worldcraftopengl.entities;

import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;
import lombok.Setter;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
public class Location {

    private double x;
    private double y;
    private double z;

    @Setter
    private float yaw;   // Rotation horizontale (gauche-droite)
    private float pitch; // Rotation verticale (haut-bas)

    private World world = GAME.getWorld();

    // Constantes pour éviter que la caméra ne se retourne
    private static final float PITCH_LIMIT = 89.0f;

    public Location(World world, double x, double y, double z){ this(world,x,y,z,-90, 0);}
    public Location(World world, double x, double y, double z, float yaw, float pitch){
        this.x = x;
        this.y = y;
        this.z = z;
        this. yaw = yaw;
        this.pitch = pitch;
        this.world = world;
    }

    public Location(Location loc) {
        this(loc.world, loc.x, loc.y, loc.z, loc.yaw, loc.pitch);
    }

    public Chunk getChunk(){
        return world.getChunkAt(x, z, false);
    }

    public void addX(double a){ this.x +=a; }
    public void addZ(double a){ this.z +=a; }
    public void addY(double a){ this.y +=a; }

    public int getXInt(){ return (int) Math.round(this.x); }
    public int getYInt(){ return (int) Math.round(this.y); }
    public int getZInt(){ return (int) Math.round(this.z); }

    public void setPitch(float pitch){
        if (pitch > PITCH_LIMIT) {
            this.pitch = PITCH_LIMIT;
        } else if (pitch < -PITCH_LIMIT) {
            this.pitch = -PITCH_LIMIT;
        } else {
            this.pitch = pitch;
        }
    }

    public double getDistanceFrom(Location loc){
        return getDistanceFrom(loc.x, loc.y, loc.z);
    }

    public double getDistanceFrom(double x, double y, double z){
        return Math.sqrt(Math.pow(x-this.x, 2) + Math.pow(y-this.y, 2) + Math.pow(z-this.z, 2));
    }

    public double getDistanceFrom(double x, double z){
        return Math.sqrt(Math.pow(x-this.x, 2) + Math.pow(z-this.z, 2));
    }

    public Vector3f getPositions(){
        return new Vector3f((float) x, (float) y, (float) z);
    }

    public boolean isMaxHeight() {
        return this.y == this.getWorld().getHeigth()-1;
    }
}
