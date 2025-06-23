package fr.rhumun.game.worldcraftopengl.commands;

import fr.rhumun.game.worldcraftopengl.entities.player.Gamemode;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

/**
 * Command changing the player's gamemode.
 */
public class GamemodeCommand extends Command {
    @Override
    public void execute(Player sender, String[] args) {
        if (args.length < 1) {
            GAME.sendMessage(sender, "Usage: /gamemode <0|1>");
            return;
        }
        switch (args[0]) {
            case "0" -> {
                sender.setGamemode(Gamemode.SURVIVAL);
                GAME.sendMessage(sender, "Gamemode set to SURVIVAL");
            }
            case "1" -> {
                sender.setGamemode(Gamemode.CREATIVE);
                GAME.sendMessage(sender, "Gamemode set to CREATIVE");
            }
            default -> GAME.sendMessage(sender, "Unknown gamemode: " + args[0]);
        }
    }
}
