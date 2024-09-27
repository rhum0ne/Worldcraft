module fr.rhumun.game.worldcraftopengl {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.lwjgl.glfw;
    requires org.lwjgl.opengl;
    requires static lombok;
    requires org.lwjgl.stb;
    requires org.joml;
    requires org.lwjgl.openal;
    requires org.lwjgl.nuklear;
    requires java.datatransfer;
    requires java.desktop;
    requires org.lwjgl.bgfx;
    requires org.lwjgl.opengles;

    opens fr.rhumun.game.worldcraftopengl to javafx.fxml;
    exports fr.rhumun.game.worldcraftopengl;
    exports fr.rhumun.game.worldcraftopengl.outputs.graphic;
    opens fr.rhumun.game.worldcraftopengl.outputs.graphic to javafx.fxml;
    exports fr.rhumun.game.worldcraftopengl.blocks;
    opens fr.rhumun.game.worldcraftopengl.blocks to javafx.fxml;
    exports fr.rhumun.game.worldcraftopengl.worlds;
    opens fr.rhumun.game.worldcraftopengl.worlds to javafx.fxml;
    exports fr.rhumun.game.worldcraftopengl.outputs.graphic.utils;
    opens fr.rhumun.game.worldcraftopengl.outputs.graphic.utils to javafx.fxml;
    exports fr.rhumun.game.worldcraftopengl.worlds.generators;
    opens fr.rhumun.game.worldcraftopengl.worlds.generators to javafx.fxml;
    exports fr.rhumun.game.worldcraftopengl.blocks.materials;
    opens fr.rhumun.game.worldcraftopengl.blocks.materials to javafx.fxml;
}