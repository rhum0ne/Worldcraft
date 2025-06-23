package fr.rhumun.game.worldcraftopengl.commands;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.commands.GamemodeCommand;
import fr.rhumun.game.worldcraftopengl.commands.SummonCommand;

import java.util.*;

/**
 * Registry of all available commands.
 */
public final class Commands {
    private static final Map<String, Command> COMMANDS = new HashMap<>();

    static {
        register("summon", new SummonCommand());
        register("gamemode", new GamemodeCommand());
    }

    private Commands() {
    }

    /** Register a new command. */
    public static void register(String name, Command command) {
        COMMANDS.put(name.toLowerCase(Locale.ROOT), command);
    }

    /** Execute a command line. The line may start with '/'. */
    public static boolean execute(Player sender, String line) {
        if (line.startsWith("/")) {
            line = line.substring(1);
        }
        if (line.isEmpty()) {
            return false;
        }
        String[] split = line.split("\\s+");
        Command cmd = COMMANDS.get(split[0].toLowerCase(Locale.ROOT));
        if (cmd == null) {
            return false;
        }
        String[] args = Arrays.copyOfRange(split, 1, split.length);
        cmd.execute(sender, args);
        return true;
    }

    /** Returns the registered command names. */
    public static Set<String> getCommandNames() {
        return COMMANDS.keySet();
    }
}
