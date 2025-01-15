package fr.rhumun.game.worldcraftopengl.worlds;

public class ChunkGenerator extends Thread{
    Chunk chunk;
    public ChunkGenerator(Chunk chunk) {
        this.chunk = chunk;
    }


    @Override
    public void run(){
        chunk.getWorld().getGenerator().generate(chunk);
        //this.populate(chunk);
        chunk.setGenerated(true);
    }
}
