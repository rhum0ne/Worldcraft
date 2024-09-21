package fr.rhumun.game.worldcraftopengl.outputs.audio;

import lombok.Getter;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.TEXTURES_PATH;

@Getter
public enum Sound {
    GRASS(loadSound("grass.ogg")),
    SAND(loadSound("sand.ogg")),
    STONE(loadSound("stone.ogg")),
    WOOD(loadSound("wood.ogg"));

    private final int id;
    Sound(final int id){
        this.id = id;
    }

    private static int loadSound(final String path){
        AudioManager audioManager = GAME.getAudioManager();
        return audioManager.createSource(audioManager.loadSound(TEXTURES_PATH + "sounds\\" + path));
    }
}
