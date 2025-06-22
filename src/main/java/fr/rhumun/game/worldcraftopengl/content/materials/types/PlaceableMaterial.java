package fr.rhumun.game.worldcraftopengl.content.materials.types;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;

/**
 * Represents a material that can be placed by the player.
 * Provides the sounds played on placement and break as well as
 * the opacity of the block.
 */
public interface PlaceableMaterial {
    /**
     * Sound played when the block is placed.
     *
     * @return place sound
     */
    Sound getPlaceSound();

    /**
     * Sound played when the block is broken.
     *
     * @return break sound
     */
    Sound getBreakSound();

    /**
     * Retrieves the opacity type of the material.
     *
     * @return opacity type
     */
    OpacityType getOpacity();
}
