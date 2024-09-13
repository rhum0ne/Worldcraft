module fr.rhumun.game.worldcraftopengl {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.lwjgl.glfw;
    requires org.lwjgl.opengl;

    opens fr.rhumun.game.worldcraftopengl to javafx.fxml;
    exports fr.rhumun.game.worldcraftopengl;
}