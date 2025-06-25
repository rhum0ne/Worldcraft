package fr.rhumun.game.worldcraftopengl.commands;

import fr.rhumun.game.worldcraftopengl.entities.*;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

/**
 * Command spawning various entities at the player's position.
 */
public class SummonCommand extends Command {

    private interface Spawner {
        Entity spawn(double x, double y, double z);
    }

    private static final Map<String, Spawner> ENTITIES = new HashMap<>();

    static {
        ENTITIES.put("otter", (x, y, z) -> new OtterEntity(x, y, z, 0, 0));
        ENTITIES.put("ninja_skeleton", (x, y, z) -> new NinjaSkeletonEntity(x, y, z, 0, 0));
        ENTITIES.put("rocky", (x, y, z) -> new RockyEntity(x, y, z, 0, 0));
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (args.length < 1) {
            GAME.sendMessage(sender, "Usage: /summon <" + String.join(", ", ENTITIES.keySet()) + ">");
            return;
        }

        String type = args[0].toLowerCase(Locale.ROOT);
        Spawner spawner = ENTITIES.get(type);
        if (spawner == null) {
            GAME.sendMessage(sender, "Unknown entity '" + type + "'. Available: " + String.join(", ", ENTITIES.keySet()));
            return;
        }

        double x = sender.getLocation().getX();
        double y = sender.getLocation().getY();
        double z = sender.getLocation().getZ();

        Entity e = spawner.spawn(x, y, z);
        sender.getLocation().getWorld().addEntity(e);
        GAME.sendMessage(sender, "Summoned " + type + ".");
    }
}
