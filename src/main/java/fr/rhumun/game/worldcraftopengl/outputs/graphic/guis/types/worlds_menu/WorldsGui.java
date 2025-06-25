package fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu;

import fr.rhumun.game.worldcraftopengl.content.textures.Texture;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.FullscreenTiledGui;
import fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.components.ScrollableGui;
import fr.rhumun.game.worldcraftopengl.worlds.SaveManager;
import fr.rhumun.game.worldcraftopengl.worlds.WorldInfo;

import java.util.ArrayList;
import java.util.List;

import static fr.rhumun.game.worldcraftopengl.outputs.graphic.guis.types.worlds_menu.LoadWorldButton.VISIBLE_TOP;

public class WorldsGui extends FullscreenTiledGui implements ScrollableGui {

    private final List<LoadWorldButton> worldButtons = new ArrayList<>();
    private final CreateWorldButton createButton;
    private final BackButton backButton;
    private int scrollOffset = 0;

    public WorldsGui() {
        super(Texture.DARK_COBBLE);

        this.addText(0, -200, "Liste des Mondes");

        int y = VISIBLE_TOP;
        List<WorldInfo> infos = SaveManager.listWorldInfos();
        for (WorldInfo info : infos) {
            LoadWorldButton btn = new LoadWorldButton(0, y, this, info);
            this.addButton(btn);
            worldButtons.add(btn);
            y += 50;
        }

        backButton = new BackButton(100, 210, this);
        this.addButton(backButton);

        createButton = new CreateWorldButton(-100, 210, this);
        this.addButton(createButton);

        this.setAlignCenter(true);

        updatePositions();
    }

    @Override
    public void onScroll(double yoffset) {
        scrollOffset += yoffset > 0 ? 20 : -20;

        if(scrollOffset > 0) scrollOffset = 0;
        else if(scrollOffset < -50*worldButtons.size()) scrollOffset = -50*worldButtons.size();

        updatePositions();
    }

    private void updatePositions() {
        int y = VISIBLE_TOP + scrollOffset;
        for (LoadWorldButton btn : worldButtons) {
            setCoordinates(btn, 0, y);
            btn.getText().set2DCoordinates(0,0);
            y += 50;
        }
    }
}
