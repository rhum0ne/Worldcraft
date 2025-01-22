package fr.rhumun.game.worldcraftopengl.entities.physics;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.content.Block;
import org.joml.Vector3f;

public class Movements {
    private static final float DEFAULT_GRAVITY = 9.81f;
    private static final float AIR_FRICTION = 0.98f; // Constante de frottement dans l'air
    private static final float AIR_FRICTION_FLYING = 0.5f; // Constante de frottement dans l'air
    private static final float GROUND_FRICTION = 0.6f; // Frottement au sol

    public static void applyMovements(Player player) {
        updateVelocity(player);
// Appliquer la gravité si le joueur ne vole pas
        if (!player.isFlying() && !Game.NO_CLIP) {
            applyGravityFor(player);
        }
        move(player);
    }

    private static void updateVelocity(Player player) {
        //System.out.println("Movements : " + Arrays.toString(player.getMovements()));

        // Appliquer le frottement de l'air
        player.getVelocity().mul(AIR_FRICTION);
        if(player.isFlying()) player.getVelocity().mul(AIR_FRICTION_FLYING);

        Block block = player.getBlockDown();
        if (block != null && block.getMaterial() != null) {
            // Appliquer le frottement du matériau au sol
            float groundFriction = block.getMaterial().getMaterial().getFriction();
            player.getVelocity().mul(groundFriction, 1, groundFriction);
        }

        // Mise à jour de la vélocité en fonction des mouvements du joueur
        player.getVelocity().add(
                player.getMovements()[0] * player.getAccelerationByTick(),
                player.getMovements()[1] * player.getAccelerationByTick(),
                player.getMovements()[2] * player.getAccelerationByTick()
        );

        // Limiter les très faibles valeurs de vélocité à zéro
        thresholdVelocity(player);

        // Normaliser la vitesse pour ne pas dépasser la vitesse maximale
        normalizeVelocity(player);
        //System.out.println("Velocity: " + player.getVelocity());
    }

    private static void thresholdVelocity(Player player) {
        // Limiter les petites valeurs de vitesse pour éviter les mouvements flottants
        if (Math.abs(player.getVelocity().get(0)) < 0.0001f) player.getVelocity().setComponent(0, 0);
        if (Math.abs(player.getVelocity().get(1)) < 0.0001f) player.getVelocity().setComponent(1, 0);
        if (Math.abs(player.getVelocity().get(2)) < 0.0001f) player.getVelocity().setComponent(2, 0);
    }

    private static void normalizeVelocity(Player player) {
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

    public static void applyGravityFor(Player player) {
        if (player.hasBlockDown() && player.getVelocity().get(1) < 0) {
            // Empêcher le joueur de passer à travers le sol
            player.getVelocity().setComponent(1, 0);
        } else {
            // Appliquer la gravité vers le bas
            player.getVelocity().add(0, -DEFAULT_GRAVITY / 750.0f, 0);
        }
    }

    public static void move(Player player) {
        // Déplacement sur les trois axes
        Vector3f v = player.getVelocity();
        float yawCos = (float) Math.cos(Math.toRadians(player.getLocation().getYaw()));
        float yawSin = (float) Math.sin(Math.toRadians(player.getLocation().getYaw()));
        player.addX((yawCos*v.x - yawSin*v.z));
        player.addY(v.y);
        player.addZ((yawCos*v.z + yawSin*v.x));

//        moveOnAxis(player, 0); // Mouvement sur l'axe X
//        moveOnAxis(player, 1); // Mouvement sur l'axe Y
//        moveOnAxis(player, 2); // Mouvement sur l'axe Z
    }

    private static void moveOnAxis(Player player, int axis) {
        double moveStep = 0.0001;
        double i = 0;
        float velocity = player.getVelocity().get(axis);
        int sign = (velocity < 0) ? -1 : 1;

        float yawCos = (float) Math.cos(Math.toRadians(player.getLocation().getYaw()));
        float yawSin = (float) Math.sin(Math.toRadians(player.getLocation().getYaw()));

        // Répéter les mouvements selon la vélocité sur l'axe
        while (i < Math.abs(velocity)) {
            switch (axis) {
                case 0: // Axe X
//                    if ((player.hasBlockInDirection(new Vector3f(yawCos, 0, sign * yawSin)) && !Game.NO_CLIP)) {
//                        player.getVelocity().setComponent(0, 0);
//                        return;
//                    }
                    player.addX(sign * moveStep * yawCos);
                    player.addZ(sign * moveStep * yawSin);
                    break;
                case 1: // Axe Y
                    if (!Game.NO_CLIP && ((player.hasBlockDown() && velocity < 0) || (player.hasBlockTop()) && velocity>0) ) {
                        player.getVelocity().setComponent(1, 0);
                        return;
                    }
                    player.addY(sign * moveStep);
                    break;
                case 2: // Axe Z
//                    if ((player.hasBlockInDirection(new Vector3f(-sign * yawSin, 0, sign * yawCos)) && !Game.NO_CLIP)) {
//                        player.getVelocity().setComponent(2, 0);
//                        return;
//                    }
                    player.addX(-sign * moveStep * yawSin);
                    player.addZ(sign * moveStep * yawCos);
                    break;
            }
            i += moveStep;
        }
    }
}
