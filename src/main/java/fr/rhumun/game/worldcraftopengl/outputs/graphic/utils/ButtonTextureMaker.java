package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;

import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.Random;

/**
 * Utility class to generate simple button textures.
 * It creates a buffer containing a two pixels black border with
 * a subtle light grey filling.
 */
public final class ButtonTextureMaker {

    private ButtonTextureMaker() { }

    /**
     * Creates the pixel buffer for a button texture.
     *
     * @param width  width in pixels
     * @param height height in pixels
     * @return buffer containing RGBA pixels
     */
    public static ByteBuffer create(int width, int height) {
        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        Random random = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean border = x < 2 || y < 2 || x >= width - 2 || y >= height - 2;
                if (border) {
                    buffer.put((byte) 0).put((byte) 0).put((byte) 0).put((byte) 255);
                } else {
                    int g = 200 + random.nextInt(30);
                    buffer.put((byte) g).put((byte) g).put((byte) g).put((byte) 255);
                }
            }
        }
        buffer.flip();
        return buffer;
    }
}
