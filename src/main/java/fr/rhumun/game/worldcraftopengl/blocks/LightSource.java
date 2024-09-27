package fr.rhumun.game.worldcraftopengl.blocks;

public interface LightSource {

    /**
     * To know before use:
     * - 0 equals to no light visible
     * - 255 equals to normal value
     * @return RGB color of the light
     */
    public short getRed();
    public short getGreen();
    public short getBlue();

}
