package fr.rhumun.game.worldcraftopengl.controls;

import fr.rhumun.game.worldcraftopengl.Player;

import java.util.HashMap;

import static org.lwjgl.glfw.GLFW.*;

public enum Controls {

    FORWARD(new MoveForward()),
    BACKWARD(new MoveBackward()),
    LEFT(new MoveLeft()),
    RIGHT(new MoveRight());

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

    }

    private static void add(int code, Controls control){ KEYS.put(code, control); }

    public static Controls get(int key){ return KEYS.get(key); }
    public void press(Player player){ this.function.onKeyPressed(player);}
    public void release(Player player){ this.function.onKeyReleased(player);}

    public boolean isRepeatable(){ return this.function.isRepeatable(); }

    public static boolean exists(int keyCode){ return KEYS.containsKey(keyCode) && KEYS.get(keyCode) != null; }

}
