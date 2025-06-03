package fr.rhumun.game.worldcraftopengl.worlds.generators.utils.trees;

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
        // Hauteur du tronc (entre 4 et 6 blocs, valeur typique pour les chênes)
        int trunkHeight = 5;

        // Rayon maximal du feuillage
        int radius = 2;

        // Création du tronc
        for (int y = 0; y < trunkHeight; y++) {
            chunk.get(chunkX, chunkY + y, chunkZ)
                    .setMaterial(log)
                    .setModel(Model.CYLINDER); // Tronc vertical
        }

        // Création du feuillage
        int leavesStartY = chunkY + trunkHeight - 2; // Feuillage commence un peu avant le sommet
        int leavesEndY = chunkY + trunkHeight; // Feuillage dépasse légèrement le sommet

        for (int y = leavesStartY; y < leavesEndY; y++) {
            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if(y<trunkHeight && x==0 && z==0) continue;
                    if(Math.abs(x)<2 && Math.abs(z)<2){
                        chunk.get(chunkX + x, y+2, chunkZ + z)
                                .setMaterial(leaves)
                                .setModel(Model.BLOCK); // Feuilles cubiques
                    }

                    chunk.get(chunkX + x, y, chunkZ + z)
                            .setMaterial(leaves)
                            .setModel(Model.BLOCK); // Feuilles cubiques
                }
            }
        }
    }



}
