package fr.rhumun.game.worldcraftopengl.outputs.audio;

import lombok.Getter;

import java.util.ArrayList;

@Getter
public enum SoundPack {

    STONE(new ArrayList<>()),
    STONE_DIG(new ArrayList<>()),
    GRASS(new ArrayList<>()),
    SAND(new ArrayList<>()),
    WOOD(new ArrayList<>()),
    CLOTH(new ArrayList<>()),
    GRAVEL(new ArrayList<>()),
    SNOW(new ArrayList<>()),
    GLASS(new ArrayList<>()),
    GLASS_BREAK(new ArrayList<>()),
    WET_GRASS(new ArrayList<>()),
    DOOR_OPEN(new ArrayList<>()),
    DOOR_CLOSE(new ArrayList<>());


    final ArrayList<Sound> sounds;
    SoundPack(ArrayList<Sound> sounds){
        this.sounds = sounds;
    }

    public void add(Sound sound){
        sounds.add(sound);
    }

    public Sound getRandom(){
        return sounds.get((int) (Math.random() * sounds.size()));
    }

}
