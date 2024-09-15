package fr.rhumun.game.worldcraftopengl;

import lombok.Getter;

@Getter
public class Location {

    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;

    public Location(double x, double y, double z, double yaw, double pitch){
        this.x = x;
        this.y = y;
        this.z = z;
        this. yaw = yaw;
        this.pitch = pitch;
    }

    public void addX(double a){ this.x +=a; System.out.println("x += " + a); }
    public void addZ(double a){ this.z +=a; System.out.println("z += " + a); }
    public void addY(double a){ this.y +=a; System.out.println("y += " + a); }
    public void addYaw(double a){
        this.yaw +=a;
        if(yaw < -90) yaw = -90;
        if(yaw > 90) yaw = 90;

        System.out.println("yaw += " + a);
    }
    public void addPitch(double a){
        this.pitch +=a;
        if(pitch < 0) pitch = 259;
        if(pitch >= 360) pitch = 0;

        System.out.println("pitch += " + a);
    }

    public double getDistanceFrom(Location loc){
        return Math.sqrt(Math.pow(x-loc.x, 2) + Math.pow(y-loc.y, 2) + Math.pow(z-loc.z, 2));
    }
}
