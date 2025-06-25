package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;

public class Update  extends Control{

    public Update(){
        super(false);
    }
    @Override
    public void onKeyPressed(Player player) {
        System.out.print("Player's position: ");
        System.out.print(player.getLocation().getX() + " ");
        System.out.print(player.getLocation().getY() + " ");
        System.out.println(player.getLocation().getZ());

        System.out.println("Players cameras : " + player.getLocation().getYaw() + " " + player.getLocation().getPitch());

        System.out.println(player.getLocation().getChunk().toString());

        player.getLocation().getChunk().debugChunk();

        player.updateInventory();
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
