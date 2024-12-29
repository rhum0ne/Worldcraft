package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.Crossair;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.HotBarGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ShaderUtils;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.Game.SHOWING_GUIS;
import static org.lwjgl.opengl.GL20.*;

public class GuiModule {

    private final GraphicModule graphicModule;

    private final List<Gui> hud = new ArrayList<Gui>();
    private final HotBarGui hotbar;
    //private final List<Gui> guis = new ArrayList<Gui>();
    private Gui gui;
    private Matrix4f uiProjectionMatrix;

    private final float virtualWidth = 1920.0f;
    private final float virtualHeight = 1080.0f;

    public GuiModule(GraphicModule graphicModule) {
        this.graphicModule = graphicModule;

        this.hud.add(new Crossair());
        this.hud.add(this.hotbar = new HotBarGui());
    }

    public void init(){
        for(Gui gui : hud)
            gui.getRenderer().init();


        if(gui != null)
            gui.getRenderer().init();
    }

    public void resize(float width, float height) {
        float scaleY = height / virtualHeight; // Échelle basée sur la hauteur
        float virtualWidthScaled = virtualWidth * scaleY; // Largeur virtuelle ajustée à l'échelle

        // Calcul des offsets pour centrer l'interface
        float offsetX = (width - virtualWidthScaled) / 2.0f;

        // Matrice de projection pour l'UI
        uiProjectionMatrix = new Matrix4f()
                .ortho2D(0, width, height, 0) // Fixe l'espace virtuel
                .translate(offsetX / scaleY, 0, 0)         // Applique le décalage pour centrer
                .scale(scaleY);                           // Applique l'échelle uniformément

        glUseProgram(ShaderUtils.PLAN_SHADERS.id);
        int projection = glGetUniformLocation(ShaderUtils.PLAN_SHADERS.id, "projection");
        glUniformMatrix4fv(projection, false, uiProjectionMatrix.get(new float[16]));
    }


    public void render(){
        if(!SHOWING_GUIS) return;

        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);

        for(Gui gui : hud)
            gui.render();


        if(gui != null){
            if(gui.isClosed()){
                gui.getRenderer().cleanup();
                this.gui = null;

            } else gui.render();
        }

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public void cleanup(){
        for(Gui gui : hud)
            gui.getRenderer().cleanup();


        if(gui != null)
            gui.getRenderer().cleanup();
    }

    public void updateInventory(Player player){

    }

    public void setSelectedSlot(int slot) {
        this.hotbar.setSelectedSlot(slot);
    }

    public void openGUI(Gui gui) {
        this.gui = gui;
    }

    public void closeGUI(){
        this.gui.close();
        this.graphicModule.getGame().setPaused(false);
    }

    public boolean hasGUIOpened() {
        return this.gui != null;
    }
}
