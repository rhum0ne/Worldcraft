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

When a chunk unloads, its data is written on a background thread and the chunk
is deleted once the save completes. This prevents loading incomplete files while
keeping the main loop responsive.

