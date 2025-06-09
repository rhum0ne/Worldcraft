package fr.rhumun.game.worldcraftopengl.entities.physics;

import fr.rhumun.game.worldcraftopengl.entities.Entity;
import fr.rhumun.game.worldcraftopengl.entities.MovingEntity;
import fr.rhumun.game.worldcraftopengl.entities.Player;
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
        if (block != null) {
            float groundFriction = block.getMaterial().getMaterial().getFriction();
            entity.getVelocity().mul(groundFriction, 1, groundFriction);
        } else {
            if(entity.isFlying()) entity.getVelocity().mul(AIR_FRICTION_FLYING);
            else entity.getVelocity().mul(AIR_FRICTION);
        }

        Block inside = entity.getWorld().getBlockAt(entity.getLocation().getX(), entity.getLocation().getY(), entity.getLocation().getZ(), false);
        if(inside != null && inside.getMaterial() != null && inside.getMaterial().isLiquid()){
            float liquidFriction = inside.getMaterial().getMaterial().getFriction();
            entity.getVelocity().mul(liquidFriction);
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
        //System.out.println("Velocity: " + player.getVelocity());
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
        if(player.isFlying()){
            player.getVelocity().setComponent(1, Math.min(player.getVelocity().get(1), 0.30f));
        }
    }

    public static void applyGravityFor(Entity entity) {
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

        float yawCos = (float) Math.cos(Math.toRadians(entity.getLocation().getYaw()));
        float yawSin = (float) Math.sin(Math.toRadians(entity.getLocation().getYaw()));
        entity.addX((yawCos*v.x - yawSin*v.z));
        entity.addY(v.y);
        entity.addZ((yawCos*v.z + yawSin*v.x));

        if(v.x == 0 && v.z == 0) return;

        if(entity instanceof Player player && tick++ % stepSoundFrequency == 0) {
            Block block = entity.getBlockDown();
            if (block != null && block.getMaterial() != null ) player.playSound(block.getMaterial().getBreakSound(), 0.2f);
        }
    }
}
