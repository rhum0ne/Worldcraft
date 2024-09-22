package fr.rhumun.game.worldcraftopengl.outputs.graphic;

import org.lwjgl.nuklear.*;
import org.lwjgl.system.MemoryStack;

import static org.lwjgl.nuklear.Nuklear.*;
import static org.lwjgl.opengl.GL11.*;

import static org.lwjgl.nuklear.Nuklear.NK_ANTI_ALIASING_ON;
import static org.lwjgl.system.MemoryUtil.nmemAlloc;
import static org.lwjgl.system.MemoryUtil.nmemFree;

public class GuiModule {

    private NkContext ctx; // Contexte Nuklear
    private NkRect result;

    public void init(){
        // Création du contexte Nuklear
        ctx = NkContext.create();

        // Setup Nuklear avec GLFW et OpenGL
        NkAllocator allocator = NkAllocator.create()
                .alloc((handle, old, size) -> nmemAlloc(size));

        NkBuffer cmds = NkBuffer.create();
        nk_init(ctx, allocator, null);

        // Taille de la fenêtre
        nk_buffer_init(cmds, allocator, 4 * 1024);

        // Set window scaling (facultatif)
        NkVec2 scaling = NkVec2.create();
        scaling.set(1.0f, 1.0f);
        //nk_style_set_font(ctx, nk_style_get_font(ctx));

        result = NkRect.create();
        System.out.println("Nuklear initialisé avec succès !");
    }


    public void renderNuklear() {
        // Désactiver certains états OpenGL pour éviter les conflits avec Nuklear
        glDisable(GL_DEPTH_TEST);  // Désactiver le test de profondeur pour dessiner l'UI 2D
        glEnable(GL_BLEND);        // Activer le blending pour les éléments transparents
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);  // Configurer la fonction de blending

        try (MemoryStack stack = MemoryStack.stackPush()) {
            NkContext ctx = this.ctx;

            // 1. Démarrer une nouvelle interface Nuklear
            nk_input_begin(ctx);

            // Gérer les événements Nuklear ici (clavier, souris, etc.)
            // Exemple : gérer les entrées GLFW dans Nuklear

            nk_input_end(ctx);

            // 2. Définir les éléments de l'interface utilisateur
            if (nk_begin(ctx, "Demo", nk_rect(50, 50, 220, 220, result), NK_WINDOW_TITLE)) {
                nk_layout_row_static(ctx, 30, 80, 1);

                if (nk_button_label(ctx, "Bouton 1")) {
                    System.out.println("Bouton 1 cliqué !");
                }

                if (nk_button_label(ctx, "Bouton 2")) {
                    System.out.println("Bouton 2 cliqué !");
                }
            }
            nk_end(ctx);

            // 3. Rendre l'interface utilisateur Nuklear
            //nk_glfw3_render(NK_ANTI_ALIASING_ON);
        }
    }


}
