package org.bukkit.generator;

import java.util.Random;
import org.bukkit.Chunk;
import org.bukkit.World;

/**
 * A block populator is responsible for generating a small area of blocks.
 * For example, generating glowstone inside the nether or generating dungeons full of treasure
 */
public abstract class BlockPopulator {
    /**
     * Populates an area of blocks at or around the given chunk.
     *
     * The chunks directly surrounding the specified chunk must already exist.
     *
     * @param world The world to generate in
     * @param random The random generator to use
     * @param chunk The chunk to generate for
     */
    public abstract void populate(World world, Random random, Chunk source);
}
