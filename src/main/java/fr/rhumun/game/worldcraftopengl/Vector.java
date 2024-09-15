package fr.rhumun.game.worldcraftopengl;

import lombok.Getter;
import lombok.Setter;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

@Getter
@Setter
public class Vector {

    double x;
    double y;
    double z;

    public static Vector fromYawPitch(double yaw, double pitch){
        yaw = Math.toRadians(yaw);
        pitch = Math.toRadians(pitch);
        return new Vector(cos(yaw), sin(pitch), sin(yaw));
    }

    public Vector(double x, double y, double z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public double scalaire(Vector v){
        return this.x*v.x + this.y*v.y + this.z*v.z;
    }

    public Vector vec(Vector v){
        return new Vector(y*v.z-v.y*z, z*v.x-v.z*x, x*v.y-v.x*y);
    }

    public boolean isSameDirection(Vector v){
        return Math.signum(x) == Math.signum(v.x) &&
                Math.signum(y) == Math.signum(v.y) &&
                Math.signum(z) == Math.signum(v.z);
    }

    public void multiply(double a){
        this.x*=a;
        this.y*=a;
        this.z*=a;
    }

    public void setYaw(double yaw) {
        this.x = cos(Math.toRadians(yaw));
        this.z = sin(Math.toRadians(yaw));
    }

    public void setPitch(double pitch) {
        this.y = sin(Math.toRadians(pitch));
    }
}
