package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ShaderUtils;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;

public class Button extends Component{
    private boolean hovered = false;
    private boolean clicked = false;
    public Button(float x, float y, float x2, float y2, Texture texture) {
        super(x, y, x2, y2, texture);
    }
    @Override
    public void render() {
        // Définissez la couleur en fonction de l'état
//        if (hovered) {
//            glColor4f(1.0f, 1.0f, 0.8f, 1.0f); // Couleur plus claire si survolée
//        } else if (clicked) {
//            glColor4f(0.8f, 0.8f, 0.8f, 1.0f); // Couleur sombre si cliquée
//        } else {
//            glColor4f(1.0f, 1.0f, 1.0f, 1.0f); // Couleur normale
//        }

        super.render();
    }

    @Override
    public void cleanup() {

    }

    public void update() {
        // Mise à jour de l'état du bouton
        if (isMouseOver()) {
            hovered = true;
            if (isMouseClicked()) {
                clicked = true;
                onClick();
            }
        } else {
            hovered = false;
            clicked = false;
        }
    }

    public void onClick() {
        System.out.println("Bouton cliqué !");
    }

    private boolean isMouseOver() {
//        // Implémentez la détection de survol de la souris
//        double mouseX = getMouseX();
//        double mouseY = getMouseY();
//        return mouseX >= x && mouseX <= x + width && mouseY >= y && mouseY <= y + height;

        return false;
    }

    private boolean isMouseClicked() {
        // Implémentez la détection de clic (par exemple avec GLFW)
//        return glfwGetMouseButton(GAME.getWindow(), GLFW_MOUSE_BUTTON_1) == GLFW_PRESS;

        return false;
    }
}
