package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.Item;
import fr.rhumun.game.worldcraftopengl.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.Button;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.Component;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.FontLoader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.Gui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.Crossair;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.HotBarGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ShaderUtils;
import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.Game.*;
import static org.lwjgl.opengl.GL20.*;

@Getter
@Setter
public class GuiModule {

    private final GraphicModule graphicModule;
    private final FontLoader fontLoader;

    private final List<Gui> hud = new ArrayList<Gui>();
    private final HotBarGui hotbar;
    private Gui gui;
    private Item selectedItem;


    private Matrix4f uiProjectionMatrix;
    private final float virtualWidth = 1920.0f;
    private final float virtualHeight = 1080.0f;

    private int cursorX;
    private int cursorY;

    public GuiModule(GraphicModule graphicModule) {
        this.graphicModule = graphicModule;
        try {
            GAME.log("Loading font...");
            this.fontLoader = new FontLoader(TEXTURES_PATH + "hud\\font.ttf");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.hud.add(new Crossair());
        this.hud.add(this.hotbar = new HotBarGui());
    }

    public void init(){
        this.fontLoader.loadFont();

        for(Gui gui : hud)
            gui.init();


        if(gui != null)
            gui.init();
    }

    public void resize(float width, float height) {
        float scaleY = height / virtualHeight; // Échelle basée sur la hauteur
        float virtualWidthScaled = virtualWidth * scaleY; // Largeur virtuelle ajustée à l'échelle

        // Calcul des offsets pour centrer l'interface
        float offsetX = (width - virtualWidthScaled) / 2.0f;

        // Matrice de projection pour l'UI
        uiProjectionMatrix = new Matrix4f()
                .ortho2D(0, width, height, 0) // Fixe l'espace virtuel
                .translate(offsetX, 0, 0)         // Applique le décalage pour centrer
                .scale(scaleY);                           // Applique l'échelle uniformément

        glUseProgram(ShaderUtils.PLAN_SHADERS.id);
        int projection = glGetUniformLocation(ShaderUtils.PLAN_SHADERS.id, "projection");
        glUniformMatrix4fv(projection, false, uiProjectionMatrix.get(new float[16]));

        glUseProgram(ShaderUtils.TEXT_SHADER.id);
        projection = glGetUniformLocation(ShaderUtils.TEXT_SHADER.id, "projection");
        glUniformMatrix4fv(projection, false, uiProjectionMatrix.get(new float[16]));

        graphicModule.setWidth((int) width);
        graphicModule.setHeight((int) height);
    }


    public void render(){
        if(!SHOWING_GUIS) return;

        glEnable(GL_BLEND);
        glDisable(GL_DEPTH_TEST);


        if(gui != null){
            if(gui.isClosed()){
                gui.cleanup();
                this.gui = null;

            } else gui.render();
        }else{
            for(Gui gui : hud)
                gui.render();
        }

        glDisable(GL_BLEND);
        glEnable(GL_DEPTH_TEST);
    }

    public void cleanup(){
        for(Gui gui : hud)
            gui.cleanup();


        if(gui != null)
            gui.cleanup();
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

    public void cursorMove(double xpos, double ypos) {
        if (!hasGUIOpened()) return;

        float scaleY =  graphicModule.getHeight() / virtualHeight; // Échelle Y basée sur la fenêtre
        float virtualWidthScaled = virtualWidth * scaleY;              // Largeur virtuelle ajustée à l'échelle
        float offsetX = (graphicModule.getWidth() - virtualWidthScaled) / 2.0f;

//        System.out.println(scaleX);
//        System.out.println(scaleY);

        // Convertir les coordonnées OpenGL en pixels pour l'interface utilisateur
        cursorX = (int) ((xpos - offsetX) / scaleY);
        cursorY = (int) (ypos / scaleY);

        // Limiter les coordonnées à l'espace UI virtuel
//        cursorX = Math.max(0, Math.min(cursorX, (int) virtualWidth));
//        cursorY = Math.max(0, Math.min(cursorY, (int) virtualHeight));

        //System.out.println(cursorX + " - " + cursorY);
    }


    public void rightClick(Player player) {
        if(!hasGUIOpened()) return;

        System.out.println("RIGHT CLICK");

//        for(Component component : this.gui.getComponents()){
//            if(component instanceof Button button) {
//                if(component.isCursorIn())
//                    button.onClick(player);
//            }
//        }
    }

    public void leftClick(Player player) {
        if(!hasGUIOpened()) return;

        for(Component component : this.gui.getComponents()){
            if(component instanceof Button button) {
                if(component.isCursorIn()){
                    button.onClick(player);
                    break;
                }
            }
        }
    }
}
