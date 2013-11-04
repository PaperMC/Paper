package net.minecraft.server;

public class ItemMinecart extends Item {

    private static final IDispenseBehavior b = new DispenseBehaviorMinecart();
    public int a;

    public ItemMinecart(int i) {
        this.maxStackSize = 1;
        this.a = i;
        this.a(CreativeModeTab.e);
        BlockDispenser.a.a(this, b);
    }

    public boolean interactWith(ItemStack itemstack, EntityHuman entityhuman, World world, int i, int j, int k, int l, float f, float f1, float f2) {
        if (BlockMinecartTrackAbstract.a(world.getType(i, j, k))) {
            if (!world.isStatic) {
                // CraftBukkit start - Minecarts
                org.bukkit.event.player.PlayerInteractEvent event = org.bukkit.craftbukkit.event.CraftEventFactory.callPlayerInteractEvent(entityhuman, org.bukkit.event.block.Action.RIGHT_CLICK_BLOCK, i, j, k, l, itemstack);

                if (event.isCancelled()) {
                    return false;
                }
                // CraftBukkit end
                EntityMinecartAbstract entityminecartabstract = EntityMinecartAbstract.a(world, (double) ((float) i + 0.5F), (double) ((float) j + 0.5F), (double) ((float) k + 0.5F), this.a);

                if (itemstack.hasName()) {
                    entityminecartabstract.a(itemstack.getName());
                }

                world.addEntity(entityminecartabstract);
            }

            --itemstack.count;
            return true;
        } else {
            return false;
        }
    }
}
