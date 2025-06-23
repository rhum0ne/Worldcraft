package fr.rhumun.game.worldcraftopengl.entities.physics;

import fr.rhumun.game.worldcraftopengl.content.materials.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.entities.Entity;
import fr.rhumun.game.worldcraftopengl.entities.MovingEntity;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import org.joml.Vector3f;

public class Movements {
    private static final float DEFAULT_GRAVITY = 9.81f;
    private static final float AIR_FRICTION = 0.98f; // Constante de frottement dans l'air
    private static final float AIR_FRICTION_FLYING = 0.5f; // Constante de frottement dans l'air
    private static final float GROUND_FRICTION = 0.6f; // Frottement au sol

    private static int tick = 0;
    private static int stepSoundFrequency = 15;

    public static void applyMovements(Entity entity) {
        entity.updateSwimmingState();

        if (!entity.isFlying() && !entity.isNoClipping()) {
            applyGravityFor(entity);

        }
        updateVelocity(entity);
// Appliquer la gravité si le joueur ne vole pas

        move(entity);
    }

    private static void updateVelocity(Entity entity) {
        //System.out.println("Movements : " + Arrays.toString(player.getMovements()));

        // Appliquer le frottement de l'air

        Block block = entity.getBlockDown();
        if (block != null && block.getMaterial() != null) {
            // Appliquer le frottement du matériau au sol
            float groundFriction = block.getMaterial().getFriction();
            entity.getVelocity().mul(groundFriction, 1, groundFriction);
        } else {
            if(entity.isSwimming()) {
                float density = entity.getLiquidDensity();
                entity.getVelocity().mul(Math.max(0f, 1 - 0.2f * density));
            } else if(entity.isFlying()) {
                entity.getVelocity().mul(AIR_FRICTION_FLYING);
            } else {
                entity.getVelocity().mul(AIR_FRICTION);
            }
        }

        if(entity instanceof MovingEntity mEntity) {
            // Mise à jour de la vélocité en fonction des mouvements du joueur
            entity.getVelocity().add(
                    mEntity.getMovements()[0] * entity.getAccelerationByTick(),
                    mEntity.getMovements()[1] * entity.getAccelerationByTick(),
                    mEntity.getMovements()[2] * entity.getAccelerationByTick()
            );
        }

        // Limiter les très faibles valeurs de vélocité à zéro
        thresholdVelocity(entity);

        // Normaliser la vitesse pour ne pas dépasser la vitesse maximale
        normalizeVelocity(entity);
    }

    private static void thresholdVelocity(Entity player) {
        // Limiter les petites valeurs de vitesse pour éviter les mouvements flottants
        if (Math.abs(player.getVelocity().get(0)) < 0.0001f) player.getVelocity().setComponent(0, 0);
        if (Math.abs(player.getVelocity().get(1)) < 0.0001f) player.getVelocity().setComponent(1, 0);
        if (Math.abs(player.getVelocity().get(2)) < 0.0001f) player.getVelocity().setComponent(2, 0);
    }

    private static void normalizeVelocity(Entity player) {
        // Normaliser la vitesse pour respecter la vitesse maximale du joueur
        float speed = player.getVelocity().length();
        float s = player.getSpeed() / 20.0f;
        if (speed > s) {
            player.getVelocity().div(speed, 1, speed).mul(s, 1, s);
        }
        if(player instanceof Player p && p.isSwimming()){
            float density = p.getLiquidDensity();
            float limit = 0.1f / Math.max(0.0001f, density);
            float y = Math.max(Math.min(player.getVelocity().get(1), limit), -limit);
            player.getVelocity().setComponent(1, y);
        } else if(player.isFlying()){
            player.getVelocity().setComponent(1, Math.min(player.getVelocity().get(1), 0.30f));
        }
    }

    public static void applyGravityFor(Entity entity) {
        if(entity instanceof Player p && p.isSwimming()){
            float density = p.getLiquidDensity();
            if(entity.hasBlockDown() && entity.getVelocity().get(1) < 0){
                entity.getVelocity().setComponent(1, 0);
            }else{
                entity.getVelocity().add(0, -DEFAULT_GRAVITY / (Math.max(0.0001f, density) * 2000.0f), 0);
            }
            return;
        }

        if (entity.hasBlockDown() && entity.getVelocity().get(1) < 0) {
            // Empêcher le joueur de passer à travers le sol
            entity.getVelocity().setComponent(1, 0);
        } else {
            // Appliquer la gravité vers le bas
            entity.getVelocity().add(0, -DEFAULT_GRAVITY / 750.0f, 0);
        }
    }

    public static void move(Entity entity) {
        // Déplacement sur les trois axes
        Vector3f v = entity.getVelocity();
        if(v.x == 0 && v.y == 0 && v.z == 0) return;

        if(entity instanceof Player p && p.isSwimming()) {
            float yaw = (float) Math.toRadians(entity.getLocation().getYaw());
            float pitch = (float) Math.toRadians(entity.getLocation().getPitch());

            float cosYaw = (float) Math.cos(yaw);
            float sinYaw = (float) Math.sin(yaw);
            float cosPitch = (float) Math.cos(pitch);
            float sinPitch = (float) Math.sin(pitch);

            float fx = cosYaw * cosPitch;
            float fy = sinPitch;
            float fz = sinYaw * cosPitch;

            float rx = -sinYaw;
            float rz = cosYaw;

            float moveX = fx * v.x + rx * v.z;
            float moveY = fy * v.x + v.y;
            float moveZ = fz * v.x + rz * v.z;

            entity.addX(moveX);
            entity.addY(moveY);
            entity.addZ(moveZ);
        } else {
            float yawCos = (float) Math.cos(Math.toRadians(entity.getLocation().getYaw()));
            float yawSin = (float) Math.sin(Math.toRadians(entity.getLocation().getYaw()));
            entity.addX((yawCos*v.x - yawSin*v.z));
            entity.addY(v.y);
            entity.addZ((yawCos*v.z + yawSin*v.x));
        }

        if(v.x == 0 && v.z == 0) return;

        if(entity instanceof Player player && tick++ % stepSoundFrequency == 0) {
            Block block = entity.getBlockDown();
            if (block != null && block.getMaterial() != null && block.getMaterial() instanceof PlaceableMaterial pM) player.playSound(pM.getBreakSound(), 0.2f);
        }
    }
}
