package net.minecraft.server;
// CraftBukkit start - the whole file!

public class ItemMobSpawner extends ItemWithAuxData {

    public  ItemMobSpawner(int id) {
        super(id, Block.MOB_SPAWNER);
    }

    // interact
    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int x, int y, int z, int face) {

        // super.interact (for ItemBlock this normally attempts to place it)
        if (!super.a(itemstack, entityhuman, world, x, y, z, face)) return false;

        // Adjust the coords for the face clicked.
        if (face == 0) { y--; }
        else if (face == 1) { y++; }
        else if (face == 2) { z--; }
        else if (face == 3) { z++; }
        else if (face == 4) { x--; }
        else if (face == 5) { x++; }

        // Set the remembered datavalue for the spawner
        TileEntity entity = world.getTileEntity(x, y, z);
        if (entity instanceof TileEntityMobSpawner) {
            ((TileEntityMobSpawner) entity).setId(itemstack.getData());
            return true;
        }

        return false;
    }
}
// CraftBukkit end - the whole file!