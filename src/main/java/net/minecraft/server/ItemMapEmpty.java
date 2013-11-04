package net.minecraft.server;

public class ItemMapEmpty extends ItemWorldMapBase {

    protected ItemMapEmpty() {
        this.a(CreativeModeTab.f);
    }

    public ItemStack a(ItemStack itemstack, World world, EntityHuman entityhuman) {
        World worldMain = world.getServer().getServer().worlds.get(0); // CraftBukkit - store reference to primary world
        ItemStack itemstack1 = new ItemStack(Items.MAP, 1, worldMain.b("map")); // CraftBukkit - use primary world for maps
        String s = "map_" + itemstack1.getData();
        WorldMap worldmap = new WorldMap(s);

        worldMain.a(s, (PersistentBase) worldmap); // CraftBukkit - use primary world for maps
        worldmap.scale = 0;
        int i = 128 * (1 << worldmap.scale);

        worldmap.centerX = (int) (Math.round(entityhuman.locX / (double) i) * (long) i);
        worldmap.centerZ = (int) (Math.round(entityhuman.locZ / (double) i) * (long) i);
        worldmap.map = (byte) ((WorldServer) world).dimension; // CraftBukkit - use bukkit dimension
        worldmap.c();

        org.bukkit.craftbukkit.event.CraftEventFactory.callEvent(new org.bukkit.event.server.MapInitializeEvent(worldmap.mapView)); // CraftBukkit

        --itemstack.count;
        if (itemstack.count <= 0) {
            return itemstack1;
        } else {
            if (!entityhuman.inventory.pickup(itemstack1.cloneItemStack())) {
                entityhuman.drop(itemstack1, false);
            }

            return itemstack;
        }
    }
}
