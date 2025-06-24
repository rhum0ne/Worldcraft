package fr.rhumun.game.worldcraftopengl.commands;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import fr.rhumun.game.worldcraftopengl.worlds.utils.DayNightCycle;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

/**
 * Command to control the world time and the day/night cycle.
 */
public class TimeCommand extends Command {
    @Override
    public void execute(Player sender, String[] args) {
        World world = sender.getLocation().getWorld();
        if (args.length < 1) {
            GAME.sendMessage(sender, "Usage: /time <start|stop|set>");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "start" -> {
                world.setDayNightCycle(true);
                GAME.sendMessage(sender, "Day/night cycle started");
            }
            case "stop" -> {
                world.setDayNightCycle(false);
                GAME.sendMessage(sender, "Day/night cycle stopped");
            }
            case "set" -> {
                if (args.length < 2) {
                    GAME.sendMessage(sender, "Usage: /time set <value|day|night|midnight|noon>");
                    return;
                }
                String value = args[1].toLowerCase();
                int ticks;
                switch (value) {
                    case "day" -> ticks = 1000;
                    case "noon" -> ticks = 6000;
                    case "night" -> ticks = 13000;
                    case "midnight" -> ticks = 18000;
                    default -> {
                        try {
                            ticks = Integer.parseInt(value);
                        } catch (NumberFormatException e) {
                            GAME.sendMessage(sender, "Invalid time value: " + value);
                            return;
                        }
                    }
                }
                world.setWorldTime(ticks);
                GAME.sendMessage(sender, "Time set to " + DayNightCycle.ticksToTime(ticks));
            }
            default -> GAME.sendMessage(sender, "Unknown subcommand: " + args[0]);
        }
    }
}
