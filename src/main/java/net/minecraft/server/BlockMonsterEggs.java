package net.minecraft.server;

import java.util.Random;

import net.minecraft.util.org.apache.commons.lang3.tuple.ImmutablePair;

import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason; // CraftBukkit

public class BlockMonsterEggs extends Block {

    public static final String[] a = new String[] { "stone", "cobble", "brick", "mossybrick", "crackedbrick", "chiseledbrick"};

    public BlockMonsterEggs() {
        super(Material.CLAY);
        this.c(0.0F);
        this.a(CreativeModeTab.c);
    }

    public void postBreak(World world, int i, int j, int k, int l) {
        if (!world.isStatic) {
            EntitySilverfish entitysilverfish = new EntitySilverfish(world);

            entitysilverfish.setPositionRotation((double) i + 0.5D, (double) j, (double) k + 0.5D, 0.0F, 0.0F);
            world.addEntity(entitysilverfish, SpawnReason.SILVERFISH_BLOCK); // CraftBukkit - add SpawnReason
            entitysilverfish.s();
        }

        super.postBreak(world, i, j, k, l);
    }

    public int a(Random random) {
        return 0;
    }

    public static boolean a(Block block) {
        return block == Blocks.STONE || block == Blocks.COBBLESTONE || block == Blocks.SMOOTH_BRICK;
    }

    public static int a(Block block, int i) {
        if (i == 0) {
            if (block == Blocks.COBBLESTONE) {
                return 1;
            }

            if (block == Blocks.SMOOTH_BRICK) {
                return 2;
            }
        } else if (block == Blocks.SMOOTH_BRICK) {
            switch (i) {
                case 1:
                    return 3;

                case 2:
                    return 4;

                case 3:
                    return 5;
            }
        }

        return 0;
    }

    public static ImmutablePair b(int i) {
        switch (i) {
            case 1:
                return new ImmutablePair(Blocks.COBBLESTONE, Integer.valueOf(0));

            case 2:
                return new ImmutablePair(Blocks.SMOOTH_BRICK, Integer.valueOf(0));

            case 3:
                return new ImmutablePair(Blocks.SMOOTH_BRICK, Integer.valueOf(1));

            case 4:
                return new ImmutablePair(Blocks.SMOOTH_BRICK, Integer.valueOf(2));

            case 5:
                return new ImmutablePair(Blocks.SMOOTH_BRICK, Integer.valueOf(3));

            default:
                return new ImmutablePair(Blocks.STONE, Integer.valueOf(0));
        }
    }

    protected ItemStack j(int i) {
        switch (i) {
            case 1:
                return new ItemStack(Blocks.COBBLESTONE);

            case 2:
                return new ItemStack(Blocks.SMOOTH_BRICK);

            case 3:
                return new ItemStack(Blocks.SMOOTH_BRICK, 1, 1);

            case 4:
                return new ItemStack(Blocks.SMOOTH_BRICK, 1, 2);

            case 5:
                return new ItemStack(Blocks.SMOOTH_BRICK, 1, 3);

            default:
                return new ItemStack(Blocks.STONE);
        }
    }

    public void dropNaturally(World world, int i, int j, int k, int l, float f, int i1) {
        if (!world.isStatic) {
            EntitySilverfish entitysilverfish = new EntitySilverfish(world);

            entitysilverfish.setPositionRotation((double) i + 0.5D, (double) j, (double) k + 0.5D, 0.0F, 0.0F);
            world.addEntity(entitysilverfish, SpawnReason.SILVERFISH_BLOCK); // CraftBukkit - add SpawnReason
            entitysilverfish.s();
        }
    }

    public int getDropData(World world, int i, int j, int k) {
        return world.getData(i, j, k);
    }
}
