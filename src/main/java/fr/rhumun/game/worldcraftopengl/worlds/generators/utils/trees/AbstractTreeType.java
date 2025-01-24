package fr.rhumun.game.worldcraftopengl.worlds.generators.utils.trees;

import fr.rhumun.game.worldcraftopengl.content.Block;
import fr.rhumun.game.worldcraftopengl.content.Model;
import fr.rhumun.game.worldcraftopengl.content.materials.types.Material;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;

public abstract class AbstractTreeType {

    private final Material log;
    private final Material leaves;

    public AbstractTreeType(Material log, Material leaves) {
        this.log = log;
        this.leaves = leaves;
    }

    public void buildBasic(Chunk chunk, int chunkX, int chunkY, int chunkZ) {
        // Hauteur du tronc
        int trunkHeight = 5;

        // Rayon du feuillage
        int leavesRadius = 2;

        // Création du tronc
        for (int y = 0; y < trunkHeight; y++) {
            chunk.get(chunkX, chunkY + y, chunkZ)
                    .setMaterial(log)
                    .setModel(Model.CYLINDER);
        }

        // Création du feuillage
        int leavesStartY = chunkY + trunkHeight - 2; // Le feuillage commence deux blocs sous le sommet du tronc
        int leavesEndY = chunkY + trunkHeight;

        for (int y = leavesStartY; y <= leavesEndY; y++) {
            int radius = leavesRadius - (leavesEndY - y); // Réduire le rayon pour donner une forme arrondie

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    // Calculer la distance au centre
                    if (x * x + z * z <= radius * radius) {
                        // Vérifier si on ne remplace pas le tronc
                        if (!(x == 0 && z == 0 && y < chunkY + trunkHeight)) {
                            chunk.get(chunkX + x, y, chunkZ + z)
                                    .setMaterial(leaves)
                                    .setModel(Model.BLOCK);
                        }
                    }
                }
            }
        }
    }


}
