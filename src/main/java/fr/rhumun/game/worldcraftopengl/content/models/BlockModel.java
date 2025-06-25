package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.BoxHitbox;
import fr.rhumun.game.worldcraftopengl.entities.physics.hitbox.Hitbox;
import fr.rhumun.game.worldcraftopengl.worlds.Block;
import fr.rhumun.game.worldcraftopengl.content.materials.blocks.types.RotableMaterial;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.content.Model.load;

public class BlockModel extends AbstractModel implements ModelHitbox{

    public BlockModel() {
        super(load("block.obj"), true);
    }

    @Override
    public void setBlockDataOnPlace(Block block, Vector3f hitPosition, Vector3f direction) {
        if(!(block.getMaterial() instanceof RotableMaterial)){
            block.setState(0);
            return;
        }
        // Trouver la face touchée en regardant quel axe a la plus grande valeur absolue
        float absX = Math.abs(direction.x);
        float absY = Math.abs(direction.y);
        float absZ = Math.abs(direction.z);

        int state;

        if (absX > absZ) {
            // X dominant → Est/Ouest
            state = (direction.x > 0) ? 1 : 3; // 1 = Est, 2 = Ouest
        } else {
            // Z dominant → Nord/Sud
            state = (direction.z > 0) ? 0 : 2; // 5 = Sud, 6 = Nord
        }

        block.setState(state);
    }

    @Override
    public Hitbox getHitbox(Block block) {
        return BoxHitbox.fullBlock();
    }
}
