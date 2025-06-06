package fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;

import fr.rhumun.game.worldcraftopengl.Game;
import org.lwjgl.BufferUtils;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Random;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

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
        Game.GAME.debug("Creating one button texture...");

        ByteBuffer buffer = BufferUtils.createByteBuffer(width * height * 4);
        Random random = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                boolean border = x < 2 || y < 2 || x >= width - 2 || y >= height - 2;
                if (border) {
                    buffer.put((byte) 0).put((byte) 0).put((byte) 0).put((byte) 255);
                } else {
                    int g = 150 + random.nextInt(30);
                    buffer.put((byte) g).put((byte) g).put((byte) g).put((byte) 255);
                }
            }
        }

        return buffer;
    }

    public static void saveBufferToImage(ByteBuffer buffer, int width, int height, String filePath) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int index = (y * width + x) * 4;

                int r = buffer.get(index) & 0xFF;
                int g = buffer.get(index + 1) & 0xFF;
                int b = buffer.get(index + 2) & 0xFF;
                int a = buffer.get(index + 3) & 0xFF;

                int rgba = ((a & 0xFF) << 24) |
                        ((r & 0xFF) << 16) |
                        ((g & 0xFF) << 8)  |
                        (b & 0xFF);

                image.setRGB(x, y, rgba);
            }
        }

        try {
            ImageIO.write(image, "png", new File(filePath));
            System.out.println("Image sauvegardée : " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
