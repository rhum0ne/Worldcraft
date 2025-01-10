package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.Image;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.GUI_ZOOM;

public class HotBarGui extends Gui {

    private static final int WIDTH = 364;
    private static final int HEIGHT = 44;
    private final Image selectedSlotImage;

    public HotBarGui() {
        //super(-0.5f,  -0.45f, 0.5f, -0.57f, Texture.HOTBAR);
        super((1920 - GUI_ZOOM*WIDTH) / 2, 1080 - GUI_ZOOM*HEIGHT, WIDTH, HEIGHT, Texture.HOTBAR);
        this.setItemContainer(GAME.getPlayer().getInventory());

        //LE GUI ZOOM*11 est hasardeux et ne fonctionne que pour une taille de GUI de 2
        for(int i=0; i<9; i++)
            this.createSlot(getXForSlot(i), 7);

        this.selectedSlotImage = this.createImage(getXForSelectedSlot(0), -2, 48, 46, Texture.SELECTED_SLOT);
    }

    private int getXForSlot(int slot){
        return (7 + (slot * 40));
    }

    private int getXForSelectedSlot(int slot){
        return (slot * 40 -2);
    }

    public void setSelectedSlot(int slot){
        this.setCoordinates(this.selectedSlotImage, getXForSelectedSlot(slot), -2);
    }
}
