package net.minecraft.server;

public interface IInventory {

    int q_();

    ItemStack c_(int i);

    ItemStack a(int i, int j);

    void a(int i, ItemStack itemstack);

    String c();

    int r_();

    void i();

    boolean a_(EntityHuman entityhuman);

    public abstract ItemStack[] getContents(); // CraftBukkit
}
