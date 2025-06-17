package fr.rhumun.game.worldcraftopengl.worlds.generators;

import fr.rhumun.game.worldcraftopengl.worlds.AbstractChunk;
import fr.rhumun.game.worldcraftopengl.worlds.Chunk;
import fr.rhumun.game.worldcraftopengl.worlds.LightChunk;
import fr.rhumun.game.worldcraftopengl.worlds.World;
import lombok.Getter;

import java.util.concurrent.*;

import static fr.rhumun.game.worldcraftopengl.Game.GAME;

@Getter
public abstract class WorldGenerator {

    private final World world;
    private final ExecutorService executor;
    private final ConcurrentLinkedDeque<AbstractChunk> toGenerate = new ConcurrentLinkedDeque<>();
    private final int maxConcurrentGenerations = Math.max(2, Runtime.getRuntime().availableProcessors() / 2);
    private final int waterHigh = 70;

    private final int waterHigh = 70;

    public WorldGenerator(World world) {
        this.world = world;

        ThreadFactory factory = r -> {
            Thread thread = new Thread(r);
            thread.setPriority(Thread.MIN_PRIORITY);
            return thread;
        };

        this.executor = Executors.newFixedThreadPool(maxConcurrentGenerations, factory);
    }

    private void tryGenerate(AbstractChunk chunk) {
        if (chunk.isGenerated()) return;

        try {
            executor.submit(chunk::generate);
        } catch (Exception e) {
            GAME.errorLog(e);
        }
    }

    public abstract void generate(Chunk chunk);
    public abstract void generate(LightChunk chunk);

    public abstract void populate(Chunk chunk);

    public void processChunkQueue() {
        AbstractChunk chunk;
        while ((chunk = toGenerate.pollLast()) != null) {
            if (!chunk.isGenerated()) {
                tryGenerate(chunk);
            }
        }
    }

    public void addToGenerate(AbstractChunk chunk) {
        toGenerate.offer(chunk);
    }

    public void shutdown() {
        executor.shutdown();
    }

    public void forceGenerate(Chunk chunk) {
        this.tryGenerate(chunk);
    }
}