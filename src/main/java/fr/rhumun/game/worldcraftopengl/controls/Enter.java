package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.ChatGui;

public class Enter extends Control{

    public Enter(){
        super(false, true);
    }
    @Override
    public void onKeyPressed(Player player) {
        ChatGui chat = getGame().getGraphicModule().getGuiModule().getChat();
        if(chat.isShowed()){
            chat.processMessageSending(player);
            chat.setShowed(false);
            getGame().setPaused(false);
            return;
        }
        chat.setShowed(true);
        getGame().setPaused(true);
    }

    @Override
    public void onKeyReleased(Player player) {}
}
