package fr.rhumun.game.worldcraftopengl.content.materials;

import fr.rhumun.game.worldcraftopengl.content.GuiTypes;
import fr.rhumun.game.worldcraftopengl.content.materials.opacity.OpacityType;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.PlaceableMaterial;
import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import lombok.Getter;
import lombok.Setter;

import java.util.Arrays;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter @Setter
public abstract class Material {

    static int maxID = 0;

    public static int createID(){
        return maxID++;
    }

    private final int id;
    private final float friction;
    private final float density;
    /** Number of ticks required to break the block. */
    private float durability;
    private final Texture[] textures = new Texture[6];

    public Material(Texture texture) {this(texture, 0.1f, 1f, 2f);}

    public Material(Texture texture, float friction) {
        this(texture, friction, 1f, 2f);
    }

    public Material(Texture texture, float friction, float density) {
        this(texture, friction, density, 2f);
    }

    public Material(Texture texture, float friction, float density, float durability) {
        this.id = createID();
        this.friction = friction;
        this.density = density;
        this.durability = durability;
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
    public OpacityType getOpacity(){ return this instanceof PlaceableMaterial pM ? pM.getOpacity() : OpacityType.OPAQUE; }
    public boolean isLiquid() { return this instanceof PlaceableMaterial pM && pM.getOpacity() == OpacityType.LIQUID; }

    protected void addToType(GuiTypes type){
        type.add(this);
    }

    public boolean showInCreativeInventory() { return true; }
}
