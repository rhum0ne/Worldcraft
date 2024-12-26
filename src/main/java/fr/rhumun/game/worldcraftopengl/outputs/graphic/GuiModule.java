package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.Crossair;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.HotBarGui;

import java.util.ArrayList;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.Game.SHOWING_GUIS;
import static org.lwjgl.opengl.GL20.*;

public class GuiModule {

    private final GraphicModule graphicModule;

    private final List<Gui> hud = new ArrayList<Gui>();
    private final List<Gui> guis = new ArrayList<Gui>();

    public GuiModule(GraphicModule graphicModule) {
        this.graphicModule = graphicModule;

        this.hud.add(new Crossair());
        this.hud.add(new HotBarGui());
    }

    public void init(){
        for(Gui gui : hud)
            gui.getRenderer().init();


        for(Gui gui : guis)
            gui.getRenderer().init();
    }

    public void render(){
        if(!SHOWING_GUIS) return;

        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);

        for(Gui gui : hud)
            gui.render();


        for(Gui gui : guis)
            gui.render();

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public void cleanup(){
        for(Gui gui : hud)
            gui.getRenderer().cleanup();


        for(Gui gui : guis)
            gui.getRenderer().cleanup();
    }

    public void updateInventory(Player player){

    }

}
