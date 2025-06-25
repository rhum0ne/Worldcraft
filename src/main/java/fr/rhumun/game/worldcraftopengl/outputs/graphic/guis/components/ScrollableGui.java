package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

/**
 * GUI that reacts to mouse wheel scrolling.
 */
public interface ScrollableGui {
    /**
     * Called when the mouse wheel is scrolled while this GUI is open.
     * @param yoffset amount of wheel movement (positive for up, negative for down)
     */
    void onScroll(double yoffset);
}
