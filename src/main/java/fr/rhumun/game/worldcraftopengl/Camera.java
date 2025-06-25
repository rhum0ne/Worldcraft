package fr.rhumun.game.worldcraftopengl;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import lombok.AccessLevel;
import lombok.Getter;
import org.joml.Vector3f;

@Getter
public class Camera {
    private Vector3f front; // Direction vers laquelle la caméra pointe
    @Getter
    private Vector3f up;
    private Vector3f right;
    Vector3f worldUp = new Vector3f(0.0f, 1.0f, 0.0f); // Vecteur "up" pour la caméra

    private final Player player;
    @Getter(AccessLevel.NONE)
    private final Vector3f tmpPos = new Vector3f();

    public Camera(Player player){
        this.player = player;
        updateCameraVectors();
    }

    /**
     * Retrieves the current camera position. Callers must not mutate
     * the returned vector as it is reused for performance.
     *
     * @return immutable view of the camera position
     */
    public Vector3f getPos() {
        tmpPos.x = (float) player.getLocation().getX();
        tmpPos.y = (float) player.getLocation().getY();
        tmpPos.z = (float) player.getLocation().getZ();
        return tmpPos;
    }

    public void setYaw(float yaw) {
        this.player.setYaw(yaw);
        updateCameraVectors();  // Met à jour la direction de la caméra après modification de l'angle
    }

    public void setPitch(float pitch) {
        // Limiter l'angle de pitch pour éviter une rotation complète
        this.player.setPitch(pitch);
        updateCameraVectors();
    }

    public Vector3f getLookPoint() {
        // Retourne la position à laquelle la caméra regarde, calculée à partir de `front`
        return this.getPos().add(front);
    }

    public float getYaw(){ return this.player.getLocation().getYaw(); }

    public float getPitch(){ return this.player.getLocation().getPitch(); }

    // Méthode pour mettre à jour les vecteurs `front`, `right`, et `up` en fonction des angles `yaw` et `pitch`
    private void updateCameraVectors() {
        // Calculer le vecteur directionnel (front) en utilisant les angles
        front = new Vector3f();

        float yaw = this.player.getLocation().getYaw();
        float pitch = this.player.getLocation().getPitch();

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
