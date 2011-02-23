package net.minecraft.server;

public interface IInventory {

    int m_();

    ItemStack c_(int i);

    ItemStack a(int i, int j);

    void a(int i, ItemStack itemstack);

    String c();

    int n_();

    void h();

    boolean a_(EntityHuman entityhuman);

    public abstract ItemStack[] getContents(); // CraftBukkit
}
