package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.GameState;
import fr.rhumun.game.worldcraftopengl.content.items.ItemStack;
import fr.rhumun.game.worldcraftopengl.entities.player.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.*;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.ChatGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.DebugMenu;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.HealthGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.HungerGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.FontLoader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.Crossair;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.HotBarGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.GLStateManager;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderManager;
import lombok.Getter;
import lombok.Setter;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL20;

import java.util.ArrayList;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.Game.*;
import static org.lwjgl.opengl.GL11C.GL_BLEND;
import static org.lwjgl.opengl.GL11C.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11C.glBlendFunc;
import static org.lwjgl.opengl.GL11C.glEnable;
import static org.lwjgl.opengl.GL20.*;

@Getter
@Setter
public class GuiModule {

    private final GraphicModule graphicModule;
    private final FontLoader fontLoader;

    private final List<Gui> hud = new ArrayList<Gui>();

    private final DebugMenu debugMenu;
    private final ChatGui chat;
    private final HotBarGui hotbar;
    private final HealthGui health;
    private final HungerGui hunger;
    private final SelectedItemDisplay selectedItemDisplay;
    private Gui gui;
    private ItemStack selectedItem;


    private Matrix4f uiProjectionMatrix;
    private final float[] matrixBuffer = new float[16];
    private final float virtualWidth = 1920.0f;
    private final float virtualHeight = 1080.0f;

    private int cursorX;
    private int cursorY;

    public GuiModule(GraphicModule graphicModule) {
        this.graphicModule = graphicModule;
        this.debugMenu = new DebugMenu();
        this.chat = new ChatGui(graphicModule.getGame());
        try {
            GAME.log("Loading font...");
            this.fontLoader = new FontLoader(TEXTURES_PATH + "hud\\vcr_osd.ttf");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        this.hud.add(new Crossair());
        this.hud.add(this.hotbar = new HotBarGui());
        this.hud.add(this.health = new HealthGui());
        this.hud.add(this.hunger = new HungerGui());
        this.selectedItemDisplay = new SelectedItemDisplay();
    }

    public void init(){
        this.fontLoader.loadFont();

        for(Gui gui : hud)
            gui.init();

        this.selectedItemDisplay.init();


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

        uiProjectionMatrix.get(matrixBuffer);
        GLStateManager.useProgram(ShaderManager.PLAN_SHADERS.id);
        int projection = glGetUniformLocation(ShaderManager.PLAN_SHADERS.id, "projection");
        glUniformMatrix4fv(projection, false, matrixBuffer);

        GLStateManager.useProgram(ShaderManager.TEXT_SHADER.id);
        projection = glGetUniformLocation(ShaderManager.TEXT_SHADER.id, "projection");
        glUniformMatrix4fv(projection, false, matrixBuffer);

        if(gui instanceof Resizable guiEvent){
            guiEvent.resize();
        }
    }


    public void render(){
        if(!SHOWING_GUIS) return;

        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_DEPTH_TEST);


        if(this.debugMenu.isShowed()) this.debugMenu.render();
        if(this.chat.isShowed()) this.chat.render();

        if(gui != null){
            if(gui.isClosed()){
                gui.cleanup();
                this.gui = null;

            } else {
                gui.render();
                if(selectedItem != null) {
                    selectedItemDisplay.setItem(selectedItem);
                    selectedItemDisplay.render();
                }
            }
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

        selectedItemDisplay.cleanup();
    }

    public void updateInventory(Player player){

    }

    public void setSelectedSlot(int slot) {
        this.hotbar.setSelectedSlot(slot);
    }

    public void openGUI(Gui gui) {
        this.gui = gui;
        if(graphicModule.getGame().getGameState() == GameState.RUNNING)
            graphicModule.getGame().setPaused(true);
    }

    public void closeGUI(){
        this.gui.close();
        this.graphicModule.getGame().setPaused(false);

        if(selectedItem != null){
            Game.GAME.getWorld().spawnItem(selectedItem, graphicModule.getPlayer().getLocation());
            selectedItem = null;
            this.selectedItemDisplay.setItem(null);
        }
    }

    public boolean hasGUIOpened() {
        return this.gui != null;
    }

    public void cursorMove(double xpos, double ypos) {
        if (!hasGUIOpened()) return;

        float scaleY =  graphicModule.getHeight() / virtualHeight; // Échelle Y basée sur la fenêtre
        float virtualWidthScaled = virtualWidth * scaleY;              // Largeur virtuelle ajustée à l'échelle
        float offsetX = (graphicModule.getWidth() - virtualWidthScaled) / 2.0f;

        // Convertir les coordonnées OpenGL en pixels pour l'interface utilisateur
        cursorX = (int) ((xpos - offsetX) / scaleY);
        cursorY = (int) (ypos / scaleY);
    }


    public void rightClick(Player player) {
        if(!hasGUIOpened()) return;

        for(Component component : this.gui.getComponents()){
            if(component instanceof ClickableSlot slot) {
                if(component.isCursorIn() && component.isVisible()){
                    ItemStack selected = getSelectedItem();
                    if(selected == null) break;

                    ItemStack current = slot.getItem();
                    if(current == null) {
                        slot.setItem(new ItemStack(selected.getMaterial(), selected.getModel(), 1));
                        selected.remove(1);
                        if(selected.isEmpty()) setSelectedItem(null);
                    } else if(current.isSame(selected) && !current.isFull()) {
                        current.add(1);
                        selected.remove(1);
                        if(selected.isEmpty()) setSelectedItem(null);
                    }
                    break;
                }
            }
        }
    }

    public void leftClick(Player player) {
        if(!hasGUIOpened()) return;

        for(Component component : this.gui.getComponents()){
            if(component instanceof Button button) {
                if(component.isCursorIn() && component.isVisible()){
                    button.click(player);
                    break;
                }
            }
        }
    }
}
