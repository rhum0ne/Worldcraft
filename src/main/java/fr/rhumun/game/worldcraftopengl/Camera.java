package fr.rhumun.game.worldcraftopengl;

import lombok.Getter;
import org.joml.Vector3f;

@Getter
public class Camera {
    @Getter
    Vector3f pos; // Position de la caméra
    private Vector3f front; // Direction vers laquelle la caméra pointe
    @Getter
    private Vector3f up;
    private Vector3f right;
    Vector3f worldUp = new Vector3f(0.0f, 1.0f, 0.0f); // Vecteur "up" pour la caméra

    private float yaw;   // Rotation horizontale (gauche-droite)
    private float pitch; // Rotation verticale (haut-bas)

    // Constantes pour éviter que la caméra ne se retourne
    private static final float PITCH_LIMIT = 89.0f;


    public Camera(Player player){
        pos = new Vector3f((float) player.getLocation().getX(), (float) player.getLocation().getY(), (float) player.getLocation().getZ());
        this.yaw = -90.0f;  // Initialement, la caméra pointe vers l'axe Z négatif
        this.pitch = 0.0f;   // Aucune rotation verticale par défaut
        updateCameraVectors();
    }

    public void setYaw(float yaw) {
        this.yaw = yaw;
        updateCameraVectors();  // Met à jour la direction de la caméra après modification de l'angle
    }

    public void setPitch(float pitch) {
        // Limiter l'angle de pitch pour éviter une rotation complète
        if (pitch > PITCH_LIMIT) {
            this.pitch = PITCH_LIMIT;
        } else if (pitch < -PITCH_LIMIT) {
            this.pitch = -PITCH_LIMIT;
        } else {
            this.pitch = pitch;
        }
        updateCameraVectors();
    }

    public Vector3f getLookPoint() {
        // Retourne la position à laquelle la caméra regarde, calculée à partir de `front`
        return new Vector3f(pos).add(front);
    }

    // Méthode pour mettre à jour les vecteurs `front`, `right`, et `up` en fonction des angles `yaw` et `pitch`
    private void updateCameraVectors() {
        // Calculer le vecteur directionnel (front) en utilisant les angles
        front = new Vector3f();
        front.x = (float) Math.cos(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        front.y = (float) Math.sin(Math.toRadians(pitch));
        front.z = (float) Math.sin(Math.toRadians(yaw)) * (float) Math.cos(Math.toRadians(pitch));
        front.normalize();  // Normaliser le vecteur directionnel

        // Calculer le vecteur de droite (right) via le produit vectoriel de front et du vecteur "up" du monde
        right = new Vector3f(front).cross(worldUp).normalize();

        // Calculer le nouveau vecteur "up" en croisant "right" et "front"
        up = new Vector3f(right).cross(front).normalize();
    }
}
