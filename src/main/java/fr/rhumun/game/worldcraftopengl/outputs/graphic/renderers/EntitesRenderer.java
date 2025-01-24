package fr.rhumun.game.worldcraftopengl.outputs.graphic.renderers;

import fr.rhumun.game.worldcraftopengl.entities.Player;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.GraphicModule;

public class EntitesRenderer extends Renderer{

    private final Player player;

    public EntitesRenderer(GraphicModule graphicModule, Player player) {
        super(graphicModule);
        this.player = player;
    }

    public void update(){

    }

    @Override
    public void render() {

    }

    @Override
    public void cleanup() {

    }
}
