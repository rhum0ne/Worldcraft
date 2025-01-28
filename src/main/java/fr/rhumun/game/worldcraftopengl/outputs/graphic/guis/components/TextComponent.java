package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components;

import fr.rhumun.game.worldcraftopengl.Game;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.FontLoader;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.GuiCharacter;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.utils.ShaderUtils;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TextComponent extends Component {

    private String text;
    private String showedText;
    private final float[] rgba = new float[4];
    private int height =32;

    public TextComponent(int x, int y, String text, Gui container) {
        this(x, y, text, 255, 255, 255, 255, container);
    }
    public TextComponent(int x, int y, String text, int r, int g, int b, Gui container) {
        this(x, y, text, r, g, b, 255, container);
    }
    public TextComponent(int x, int y, String text, int r, int g, int b, int a, Gui container) {
        super(x, y, 0, 0, null, container);
        this.text = text;
        this.setRGBA(r, g, b, a);
    }

    public void setRGBA(int r, int g, int b, int a){
        this.rgba[0] = (float) r /255;
        this.rgba[1] = (float) g /255;
        this.rgba[2] = (float) b /255;
        this.rgba[3] = (float) a /255;
    }

    public void setRGB(int r, int g, int b){ this.setRGBA(r, g, b, 255);}

    @Override
    public void update() {
        if(!text.equals(showedText)) {
            updateVertices();
            updateVAO();
        }

    }

    @Override
    public void render(){
        ShaderUtils.TEXT_SHADER.setUniform("textColor", this.rgba);
        super.render();
    }

    @Override
    public int getShader() {
        return ShaderUtils.TEXT_SHADER.id;
    }
    public int getTextureArray(){ return FontLoader.TEXTURES_ARRAY; }
    @Override
    public void updateVertices() {
        if (text == null || text.isEmpty()) return;

        List<Float> verticesList = new ArrayList<>();
        List<Integer> indicesList = new ArrayList<>();
        int offset = 0; // Offset pour la position des indices
        int xCursor = getX();
        int yCursor = getY();

        // Récupère l'ID de la texture de l'atlas une seule fois

        for (char c : text.toCharArray()) {
            if(c == ' '){
                xCursor += 18; // Avancer le curseur horizontal
                continue;
            }

            if(c == '\n'){
                xCursor = getX(); // Avancer le curseur horizontal
                yCursor += 40;
                continue;
            }

            GuiCharacter glyph = GuiCharacter.get(c);
            if (glyph == null) continue;

            int atlasTextureID = glyph.getTextureID();
            int glyphWidth = glyph.getWidth() * Game.GUI_ZOOM;
            int glyphHeight = glyph.getHeight() * Game.GUI_ZOOM;
            float xOffset = glyph.getXOffset() * Game.GUI_ZOOM;
            float yOffset = glyph.getYOffset() * Game.GUI_ZOOM;
            float advance = glyph.getAdvance();

            int startY = this.height - glyphHeight;

            // Coordonnées de texture dans l'atlas
            float xStart = 0;//glyph.getXStart();
            float yStart = 1;//glyph.getYStart();
            float xEnd = 1;//glyph.getXEnd();
            float yEnd = 0;//glyph.getYEnd();

            //System.out.println("adding " + c + " with atlas ID " + atlasTextureID + " and advance: " + advance);

            // Ajouter les vertices pour ce caractère
            verticesList.add((float) xCursor + xOffset);               // x gauche
            verticesList.add((float) yCursor + startY - yOffset);               // y haut
            verticesList.add(0.0f);                                    // z
            verticesList.add(xStart);                                  // Texture x gauche
            verticesList.add(yStart);                                  // Texture y haut
            verticesList.add((float) atlasTextureID);                  // Texture ID (atlas)

            verticesList.add((float) xCursor + xOffset + glyphWidth);  // x droit
            verticesList.add((float) yCursor + startY - yOffset);               // y haut
            verticesList.add(0.0f);                                    // z
            verticesList.add(xEnd);                                    // Texture x droit
            verticesList.add(yStart);                                  // Texture y haut
            verticesList.add((float) atlasTextureID);                  // Texture ID (atlas)

            verticesList.add((float) xCursor + xOffset);               // x gauche
            verticesList.add((float) yCursor + startY - yOffset - glyphHeight); // y bas
            verticesList.add(0.0f);                                    // z
            verticesList.add(xStart);                                  // Texture x gauche
            verticesList.add(yEnd);                                    // Texture y bas
            verticesList.add((float) atlasTextureID);                  // Texture ID (atlas)

            verticesList.add((float) xCursor + xOffset + glyphWidth);  // x droit
            verticesList.add((float) yCursor + startY - yOffset - glyphHeight); // y bas
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
            xCursor += advance/48; // Avancer le curseur horizontal
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
