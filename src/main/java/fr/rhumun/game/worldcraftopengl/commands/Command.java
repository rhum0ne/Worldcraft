package fr.rhumun.game.worldcraftopengl.commands;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;

/**
 * Basic command class executed by a player.
 */
public abstract class Command {
    /**
     * Executes the command.
     *
     * @param sender the player executing the command
     * @param args   the command arguments (without the command name)
     */
    public abstract void execute(Player sender, String[] args);
}
