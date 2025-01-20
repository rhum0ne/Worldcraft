package fr.rhumun.game.worldcraftopengl.worlds.generators;

import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;

import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Getter
public abstract class WorldGenerator {

    private final World world;
    private final ExecutorService executor;
    private final ConcurrentLinkedDeque<Chunk> toGenerate = new ConcurrentLinkedDeque<>();
    private final int maxConcurrentGenerations = 4; // Limiter les tâches actives

    public WorldGenerator(World world) {
        this.world = world;
        this.executor = Executors.newFixedThreadPool(maxConcurrentGenerations);
    }

    public void tryGenerate(Chunk chunk) {
        if (chunk.isGenerated()) return;

        try {
            executor.submit(chunk::generate);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public abstract void generate(Chunk chunk);

    public abstract void populate(Chunk chunk);

    public void processChunkQueue() {
        while (!toGenerate.isEmpty()) {
            Chunk chunk = toGenerate.poll();
            if (chunk != null && !chunk.isGenerated()) {
                tryGenerate(chunk);
            }
        }
    }

    public void addToGenerate(Chunk chunk) {
        toGenerate.offer(chunk);
    }

    public void shutdown() {
        executor.shutdown();
    }
}