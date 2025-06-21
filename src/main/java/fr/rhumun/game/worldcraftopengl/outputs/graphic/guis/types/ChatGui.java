package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.TextComponent;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ChatGui extends Gui {

    private final TextComponent enteredText;
    private final TextComponent chat;
    private boolean isShowed = false;

    private final Game game;

    public ChatGui(Game game) {
        super(0, 800, 800, 280, null);

        this.game = game;
        this.enteredText = this.addText(0, 100, "");
        this.chat = this.addText(0, 0, "");
    }

    public void print(String msg){
        this.chat.print(msg);
    }

    public void println(String msg){ this.print("\n" + msg);}
    public String getEnteredTextString(){ return this.enteredText.getText(); }

    public void processMessageSending(Player player){
        String text = this.getEnteredTextString();

        game.log(text);

        if(text.isEmpty()) return;
        if(text.startsWith("/")){
            game.processCommand(text);
        }
        else {
            game.sendMessage(player, text);
        }

        this.enteredText.setText("");
    }
}
