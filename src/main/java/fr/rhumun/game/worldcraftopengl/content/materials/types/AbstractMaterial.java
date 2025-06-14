package fr.rhumun.game.worldcraftopengl.content.materials.types;

import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.audio.Sound;
import lombok.Getter;

import java.util.Arrays;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
public abstract class AbstractMaterial {

    final int id;
    private final float friction;
    private final float density;
    private final Texture[] textures = new Texture[6];

    public AbstractMaterial(Texture texture) {this(texture, 0.1f, 1f);}

    public AbstractMaterial(Texture texture, float friction) {
        this(texture, friction, 1f);
    }

    public AbstractMaterial(Texture texture, float friction, float density) {
        this.id = Material.createID();
        this.friction = friction;
        this.density = density;
        Arrays.fill(textures, texture);

        GAME.debug("Created material " + id + " with texture " + texture.getName());
    }

    public void setTopTexture(Texture texture) {
        this.textures[4] = texture;
    }

    public void setBottomTexture(Texture texture) {
        this.textures[5] = texture;
    }

    public void setTopAndBottomTexture(Texture texture) {
        this.textures[4] = texture;
        this.textures[5] = texture;
    }

    public void setLeftTexture(Texture texture) {
        this.textures[1] = texture;
    }

    public void setRightTexture(Texture texture) {
        this.textures[3] = texture;
    }

    public void setFrontTexture(Texture texture) {
        this.textures[2] = texture;
    }

    public void setBackTexture(Texture texture) {
        this.textures[0] = texture;
    }

    public Texture getTexture(int i) {
        return textures[i];
    }

    public Texture getTopTexture() {return textures[4];}
    public Texture getBottomTexture() {return textures[5];}
    public Texture getLeftTexture() {return textures[1];}
    public Texture getRightTexture() {return textures[3];}
    public Texture getFrontTexture() {return textures[2];}
    public Texture getBackTexture() {return textures[0];}

    public Texture getTextureFromFaceWithNormal(float x, float y, float z) {
        // Normales des 6 faces des blocs
        //POUR LES CYLINDRE ETC VOIR SI SIMPLEMENT RETIRER LES && ... = 0 fonctionnerait pas mal

        if (x == 1.0f && y == 0.0f && z == 0.0f) {
            return textures[2]; // Face avant (positive X)
        } else if (x == -1.0f && y == 0.0f && z == 0.0f) {
            return textures[0]; // Face arrière (négative X)
        } else if (x == 0.0f && y == 1.0f && z == 0.0f) {
            return textures[4]; // Face supérieure (positive Y)
        } else if (x == 0.0f && y == -1.0f && z == 0.0f) {
            return textures[5]; // Face inférieure (négative Y)
        } else if (x == 0.0f && y == 0.0f && z == 1.0f) {
            return textures[1]; // Face droite (positive Z)
        } else if (x == 0.0f && y == 0.0f && z == -1.0f) {
            return textures[3]; // Face gauche (négative Z)
        }
        return this.getTexture(); // Si la normale ne correspond à aucune face
    }

    public Texture getTexture() {return textures[0];}

    public abstract Sound getPlaceSound();
    public abstract Sound getBreakSound();
    public abstract OpacityType getOpacity();
}
