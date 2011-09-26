package net.minecraft.server;

public class ItemMobSpawner extends ItemLog {

    public  ItemMobSpawner(int i) {
        super(i, Block.MOB_SPAWNER);
    }
    
    public boolean a(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l) {
        if(!super.a(itemstack, entityhuman, world, i, j, k, l)) return false;
        System.out.println("Placed the spawner, checking it's entity");
        if (l == 0) {
            --j;
        }

        if (l == 1) {
            ++j;
        }

        if (l == 2) {
            --k;
        }

        if (l == 3) {
            ++k;
        }

        if (l == 4) {
            --i;
        }

        if (l == 5) {
            ++i;
        }
        TileEntity entity = world.getTileEntity(i, j, k);
        System.out.println(entity);
        if (entity instanceof TileEntityMobSpawner) {
            System.out.println("Got a valid spawner, attempt to set its type");
            ((TileEntityMobSpawner)entity).setId(itemstack.getData());
            return true;
        }
        else return false;
    }
}
