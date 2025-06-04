# Worldcraft

## Save format

World and chunk data are stored under `%APPDATA%/Worldcraft/worlds`.
Each world uses a directory named after its seed value.

```
%APPDATA%/Worldcraft/worlds/<seed>/
  world.dat         # metadata (seed, spawn position)
  chunks/
    <x>_<z>.bin     # serialized chunk data
```

Chunk files contain a sequence of material ids for all blocks of the chunk
followed by the biome name for every column. World metadata stores the seed
(long) and the spawn coordinates as integers.

When a chunk unloads, its data is written on a background thread using a
temporary file which is atomically moved into place when the write finishes.

Multiple IO threads are used so several chunks can be saved or loaded
concurrently. The chunk is removed only once the save succeeds,
preventing partial files while keeping the main loop responsive.

When loading, chunk data is read on a background thread. The game waits for any
pending write on the requested file before reading so data is always complete,
but loading itself no longer stalls the main loop.

Chunks are loaded and unloaded in short intervals (every 100&nbsp;ms) so the
world updates smoothly as the player moves.

The chunk is removed only once the save succeeds, preventing partial files while
keeping the main loop responsive.

When loading, the game waits for any pending write on the requested chunk file
to finish so data is always read completely.


