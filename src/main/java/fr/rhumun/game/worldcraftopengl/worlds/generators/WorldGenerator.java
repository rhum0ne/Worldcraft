package fr.rhumun.game.worldcraftopengl.worlds.generators;

import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;

import java.util.concurrent.*;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
public abstract class WorldGenerator {

    private final World world;
    private final ExecutorService executor;
    private final ConcurrentLinkedDeque<Chunk> toGenerate = new ConcurrentLinkedDeque<>();
    private final int maxConcurrentGenerations = 1; // Limiter les tâches actives

    public WorldGenerator(World world) {
        this.world = world;

        ThreadFactory factory = r -> {
            Thread thread = new Thread(r);
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        };

        this.executor = Executors.newFixedThreadPool(maxConcurrentGenerations, factory);
    }

    public void tryGenerate(Chunk chunk) {
        if (chunk.isGenerated()) return;

        try {
            executor.submit(chunk::generate);
        } catch (Exception e) {
            GAME.errorLog(e.getMessage());
        }
    }

    public abstract void generate(Chunk chunk);

    public abstract void populate(Chunk chunk);

    public void processChunkQueue() {
        while (!toGenerate.isEmpty()) {
            Chunk chunk = toGenerate.pollLast();
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