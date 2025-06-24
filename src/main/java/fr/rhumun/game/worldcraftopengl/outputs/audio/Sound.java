package fr.rhumun.game.worldcraftopengl.outputs.audio;

import lombok.Getter;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;
import static fr.rhumun.game.worldcraftopengl.Game.TEXTURES_PATH;

@Getter
public enum Sound {
    GRASS1(loadSound("grass1.ogg"), SoundPack.GRASS),
    GRASS2(loadSound("grass2.ogg"), SoundPack.GRASS),
    GRASS3(loadSound("grass3.ogg"), SoundPack.GRASS),
    GRASS4(loadSound("grass4.ogg"), SoundPack.GRASS),

    SAND1(loadSound("sand1.ogg"), SoundPack.SAND),
    SAND2(loadSound("sand2.ogg"), SoundPack.SAND),
    SAND3(loadSound("sand3.ogg"), SoundPack.SAND),
    SAND4(loadSound("sand4.ogg"), SoundPack.SAND),

    STONE1(loadSound("stone1.ogg"), SoundPack.STONE),
    STONE2(loadSound("stone2.ogg"), SoundPack.STONE),
    STONE3(loadSound("stone3.ogg"), SoundPack.STONE),
    STONE4(loadSound("stone4.ogg"), SoundPack.STONE),

    STONE_DIG1(loadSound("stone1_dig.ogg"), SoundPack.STONE_DIG),
    STONE_DIG2(loadSound("stone2_dig.ogg"), SoundPack.STONE_DIG),
    STONE_DIG3(loadSound("stone3_dig.ogg"), SoundPack.STONE_DIG),
    STONE_DIG4(loadSound("stone4_dig.ogg"), SoundPack.STONE_DIG),
    STONE_DIG5(loadSound("stone5_dig.ogg"), SoundPack.STONE_DIG),
    STONE_DIG6(loadSound("stone6_dig.ogg"), SoundPack.STONE_DIG),
    STONE_DIG7(loadSound("stone7_dig.ogg"), SoundPack.STONE_DIG),
    STONE_DIG8(loadSound("stone8_dig.ogg"), SoundPack.STONE_DIG),

    CLOTH1(loadSound("cloth1.ogg"), SoundPack.CLOTH),
    CLOTH2(loadSound("cloth2.ogg"), SoundPack.CLOTH),
    CLOTH3(loadSound("cloth3.ogg"), SoundPack.CLOTH),
    CLOTH4(loadSound("cloth4.ogg"), SoundPack.CLOTH),

    GRAVEL1(loadSound("gravel1.ogg"), SoundPack.GRAVEL),
    GRAVEL2(loadSound("gravel2.ogg"), SoundPack.GRAVEL),
    GRAVEL3(loadSound("gravel3.ogg"), SoundPack.GRAVEL),
    GRAVEL4(loadSound("gravel4.ogg"), SoundPack.GRAVEL),

    SNOW1(loadSound("snow1.ogg"), SoundPack.SNOW),
    SNOW2(loadSound("snow2.ogg"), SoundPack.SNOW),
    SNOW3(loadSound("snow3.ogg"), SoundPack.SNOW),
    SNOW4(loadSound("snow4.ogg"), SoundPack.SNOW),

    GLASS1(loadSound("glass1.ogg"), SoundPack.GLASS),
    GLASS2(loadSound("glass2.ogg"), SoundPack.GLASS),
    GLASS3(loadSound("glass3.ogg"), SoundPack.GLASS),
    GLASS4(loadSound("glass4.ogg"), SoundPack.GLASS),

    GLASS_BREAK1(loadSound("glass1_dig.ogg"), SoundPack.GLASS_BREAK),
    GLASS_BREAK2(loadSound("glass2_dig.ogg"), SoundPack.GLASS_BREAK),
    GLASS_BREAK3(loadSound("glass3_dig.ogg"), SoundPack.GLASS_BREAK),
    GLASS_BREAK4(loadSound("glass4_dig.ogg"), SoundPack.GLASS_BREAK),

    WET_GRASS1(loadSound("wet_grass1.ogg"), SoundPack.WET_GRASS),
    WET_GRASS2(loadSound("wet_grass2.ogg"), SoundPack.WET_GRASS),
    WET_GRASS3(loadSound("wet_grass3.ogg"), SoundPack.WET_GRASS),
    WET_GRASS4(loadSound("wet_grass4.ogg"), SoundPack.WET_GRASS),

    WOOD1(loadSound("wood1.ogg"), SoundPack.WOOD),
    WOOD2(loadSound("wood2.ogg"), SoundPack.WOOD),
    WOOD3(loadSound("wood3.ogg"), SoundPack.WOOD),
    WOOD4(loadSound("wood4.ogg"), SoundPack.WOOD),

    DOOR_OPEN(loadSound("wooden_door\\open.ogg"), SoundPack.DOOR_OPEN),
    DOOR_OPEN_2(loadSound("wooden_door\\open2.ogg"), SoundPack.DOOR_OPEN),
    DOOR_OPEN_3(loadSound("wooden_door\\open3.ogg"), SoundPack.DOOR_OPEN),
    DOOR_OPEN_4(loadSound("wooden_door\\open4.ogg"), SoundPack.DOOR_OPEN),
    DOOR_CLOSE(loadSound("wooden_door\\close.ogg"), SoundPack.DOOR_CLOSE),
    DOOR_CLOSE_2(loadSound("wooden_door\\close2.ogg"), SoundPack.DOOR_CLOSE),
    DOOR_CLOSE_3(loadSound("wooden_door\\close3.ogg"), SoundPack.DOOR_CLOSE),
    DOOR_CLOSE_4(loadSound("wooden_door\\close4.ogg"), SoundPack.DOOR_CLOSE),
    DOOR_CLOSE_5(loadSound("wooden_door\\close5.ogg"), SoundPack.DOOR_CLOSE),
    DOOR_CLOSE_6(loadSound("wooden_door\\close6.ogg"), SoundPack.DOOR_CLOSE),

    HURT(loadSound("classic_hurt.ogg")),
    EAT(loadSound("eat.ogg")),
    DRINK(loadSound("drink.ogg")),

    MUSIC_1(loadSound("beta-song.ogg")),
    CLICK(loadSound("click.ogg"));

    private final int id;
    private final SoundPack soundPack;

    Sound(final int id, SoundPack soundPack){
        this.id = id;
        this.soundPack = soundPack;

        if(soundPack != null) soundPack.add(this);
    }
    Sound(final int id){
        this(id, null);
    }

    private static int loadSound(final String path){
        AudioManager audioManager = GAME.getAudioManager();
        return audioManager.createSource(audioManager.loadSound(TEXTURES_PATH + "sounds\\" + path));
    }
}
