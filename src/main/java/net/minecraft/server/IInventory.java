package net.minecraft.server;

public interface IInventory {

    int h_();

    ItemStack a(int i);

    ItemStack b(int i, int j);

    void a(int i, ItemStack itemstack);

    String b();

    int c();

    void d();

    boolean a_(EntityHuman entityhuman);

    public abstract ItemStack[] getContents(); // CraftBukkit
}
