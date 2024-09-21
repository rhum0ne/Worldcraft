package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.worlds.structures.Structure;

public class Update  extends Control{

    public Update(){
        super(false);
    }
    @Override
    public void onKeyPressed(Player player) {
        System.out.println("INFO : RELOADING CHUNKS");
        player.getSavedChunksManager().loadChunks(player.getLocation().getChunk());
        System.out.print("Player's position: ");
        System.out.print(player.getLocation().getX() + " ");
        System.out.print(player.getLocation().getY() + " ");
        System.out.println(player.getLocation().getZ());
        System.out.println(player.getLocation().getChunk().toString());

        player.getLocation().getWorld().spawnStructure(Structure.TREE, (int) player.getLocation().getX(), (int) player.getLocation().getY()-1, (int) player.getLocation().getZ());
    }

    @Override
    public void onKeyReleased(Player player) {

    }
}
