package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.entities.Player;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public enum Controls {

    FORWARD(new MoveForward()),
    BACKWARD(new MoveBackward()),
    MOVE_UP(new Jump()),
    MOVE_DOWN(new Sneak()),
    LEFT(new MoveLeft()),
    RIGHT(new MoveRight()),
    LEFT_CLICK(new LeftClick()),
    RIGHT_CLICK(new RightClick()),
    MIDDLE_CLICK(new MiddleClick()),
    UPDATE(new Update()),
    FLY(new Fly()),
    SPRINT(new Sprint()),
    ESCAPE(new Escape()),
    HIDE_GUIS(new HideGUIS()),
    SHOW_FPS(new ShowFPS()),
    SHOW_TRIANGLES(new ShowTrianlges()),
    INVENTORY(new OpenInventory()),
    ENTER(new Enter()),
    DROP_ITEM(new DropItem()),
    WORLD_RENDER_BOOL(new WorldRendererBool()),
    TOGGLE_HITBOXES(new ToggleHitboxes());

    static final HashMap<Integer, Controls> KEYS = new HashMap<>();

    final Control function;

    Controls(Control key){
        function = key;
    }
    public static void init(){
        for(int i=0; i<150; i++){
            KEYS.put(i, null);
        }

        add(GLFW_KEY_W, FORWARD);
        add(GLFW_KEY_S, BACKWARD);
        add(GLFW_KEY_D, RIGHT);
        add(GLFW_KEY_A, LEFT);
        add(GLFW_KEY_SPACE, MOVE_UP);
        add(GLFW_KEY_LEFT_SHIFT, MOVE_DOWN);
        add(GLFW_MOUSE_BUTTON_1, LEFT_CLICK);
        add(GLFW_MOUSE_BUTTON_2, RIGHT_CLICK);
        add(GLFW_MOUSE_BUTTON_3, MIDDLE_CLICK);
        add(GLFW_KEY_T, UPDATE);
        add(GLFW_KEY_F, FLY);
        add(GLFW_KEY_LEFT_CONTROL, SPRINT);
        add(GLFW_KEY_ESCAPE, ESCAPE);
        add(GLFW_KEY_F1, HIDE_GUIS);
        add(GLFW_KEY_F3, SHOW_FPS);
        add(GLFW_KEY_F4, SHOW_TRIANGLES);
        add(GLFW_KEY_E, INVENTORY);
        add(GLFW_KEY_ENTER, ENTER);
        add(GLFW_KEY_Q, DROP_ITEM);
        add(GLFW_KEY_F5, WORLD_RENDER_BOOL);
        add(GLFW_KEY_F7, TOGGLE_HITBOXES);
    }

    private static void add(int code, Controls control){ KEYS.put(code, control); }

    public static Controls get(int key){ return KEYS.get(key); }
    public void press(Player player){ this.function.testOnKeyPressed(player);}
    public void release(Player player){ this.function.testOnKeyReleased(player);}

    public boolean isRepeatable(){ return this.function.isRepeatable(); }

    public static boolean exists(int keyCode){ return KEYS.containsKey(keyCode) && KEYS.get(keyCode) != null; }

}
