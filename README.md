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
The chunk is removed only once the save succeeds, preventing partial files while
keeping the main loop responsive.

When loading, chunk data is read on a background thread. The game waits for any
pending write on the requested file before reading so data is always complete,
but loading itself no longer stalls the main loop.

