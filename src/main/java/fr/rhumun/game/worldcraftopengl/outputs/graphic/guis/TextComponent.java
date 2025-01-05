package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis;

import fr.rhumun.game.worldcraftopengl.blocks.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.shaders.ShaderUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TextComponent extends Component {

    private String text;
    private String showedText;

    public TextComponent(int x, int y, String text, Gui container) {
        super(x, y, 0, 0, null, container);
        this.text = text;
        updateVertices();
    }

    @Override
    public void update() {
        if(!text.equals(showedText))
            updateVertices();
    }

    @Override
    public int getShader() {
        return ShaderUtils.TEXT_SHADER.id;
    }

    @Override
    public void updateVertices() {
        if (text == null || text.isEmpty()) return;
        System.out.println("UPDATE TEXT");

        List<Float> verticesList = new ArrayList<>();
        List<Integer> indicesList = new ArrayList<>();
        int offset = 0; // Offset pour la position des indices
        int xCursor = getX();
        int yCursor = getY();

        // Récupère l'ID de la texture de l'atlas une seule fois
        int atlasTextureID = GuiCharacter.get('A').getAtlasTextureID(); // 'A' ou un autre caractère quelconque pour récupérer l'atlas

        for (char c : text.toCharArray()) {
            GuiCharacter glyph = GuiCharacter.get(c);
            if (glyph == null) continue;

            int glyphWidth = glyph.getWidth();
            int glyphHeight = glyph.getHeight();
            float xOffset = glyph.getXOffset();
            float yOffset = glyph.getYOffset();
            float advance = glyph.getAdvance();

            // Coordonnées de texture dans l'atlas
            float xStart = glyph.getXStart();
            float yStart = glyph.getYStart();
            float xEnd = glyph.getXEnd();
            float yEnd = glyph.getYEnd();

            System.out.println("adding " + c + " with atlas ID " + atlasTextureID);

            // Ajouter les vertices pour ce caractère
            verticesList.add((float) xCursor + xOffset);               // x gauche
            verticesList.add((float) yCursor - yOffset);               // y haut
            verticesList.add(0.0f);                                    // z
            verticesList.add(xStart);                                  // Texture x gauche
            verticesList.add(yStart);                                  // Texture y haut
            verticesList.add((float) atlasTextureID);                  // Texture ID (atlas)

            verticesList.add((float) xCursor + xOffset + glyphWidth);  // x droit
            verticesList.add((float) yCursor - yOffset);               // y haut
            verticesList.add(0.0f);                                    // z
            verticesList.add(xEnd);                                    // Texture x droit
            verticesList.add(yStart);                                  // Texture y haut
            verticesList.add((float) atlasTextureID);                  // Texture ID (atlas)

            verticesList.add((float) xCursor + xOffset);               // x gauche
            verticesList.add((float) yCursor - yOffset - glyphHeight); // y bas
            verticesList.add(0.0f);                                    // z
            verticesList.add(xStart);                                  // Texture x gauche
            verticesList.add(yEnd);                                    // Texture y bas
            verticesList.add((float) atlasTextureID);                  // Texture ID (atlas)

            verticesList.add((float) xCursor + xOffset + glyphWidth);  // x droit
            verticesList.add((float) yCursor - yOffset - glyphHeight); // y bas
            verticesList.add(0.0f);                                    // z
            verticesList.add(xEnd);                                    // Texture x droit
            verticesList.add(yEnd);                                    // Texture y bas
            verticesList.add((float) atlasTextureID);                  // Texture ID (atlas)

            // Ajouter les indices
            indicesList.add(offset + 0);
            indicesList.add(offset + 1);
            indicesList.add(offset + 2);
            indicesList.add(offset + 1);
            indicesList.add(offset + 3);
            indicesList.add(offset + 2);

            offset += 4;
            xCursor += advance; // Avancer le curseur horizontal
        }

        // Convertir en tableaux
        float[] verticesArray = new float[verticesList.size()];
        for (int i = 0; i < verticesArray.length; i++) verticesArray[i] = verticesList.get(i);
        this.setVertices(verticesArray);

        int[] indicesArray = new int[indicesList.size()];
        for (int i = 0; i < indicesArray.length; i++) indicesArray[i] = indicesList.get(i);
        this.setIndices(indicesArray);

        if (isInitialized()) updateVAO();
        this.showedText = text;
    }

}
