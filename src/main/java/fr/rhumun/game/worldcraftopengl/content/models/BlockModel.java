package fr.rhumun.game.worldcraftopengl.content.models;

import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.Mesh;
import org.joml.Vector3f;

import static fr.rhumun.game.worldcraftopengl.content.Model.load;

public class BlockModel extends AbstractModel {

    public BlockModel() {
        super(load("block.obj"), true);
    }

    @Override
    public void setBlockDataOnPlace(Block block, Vector3f hitPosition, Vector3f direction) {
        // Trouver la face touchée en regardant quel axe a la plus grande valeur absolue
        float absX = Math.abs(direction.x);
        float absY = Math.abs(direction.y);
        float absZ = Math.abs(direction.z);

        int state;

        if (absX > absY && absX > absZ) {
            // X dominant → Est/Ouest
            state = (direction.x > 0) ? 1 : 2; // 1 = Est, 2 = Ouest
        } else if (absY > absX && absY > absZ) {
            // Y dominant → Haut/Bas
            state = (direction.y > 0) ? 3 : 4; // 3 = Haut, 4 = Bas
        } else {
            // Z dominant → Nord/Sud
            state = (direction.z > 0) ? 5 : 6; // 5 = Sud, 6 = Nord
        }

        block.setState(state);
        System.out.println("New state: " + state);
    }

}
