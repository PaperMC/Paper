package net.minecraft.server;

public interface IInventory {

    public abstract int h_();

    public abstract ItemStack a(int i);

    public abstract ItemStack b(int i, int j);

    public abstract void a(int i, ItemStack itemstack);

    public abstract String b();

    public abstract int c();

    public abstract void d();

    public abstract boolean a_(EntityPlayer entityplayer);

    public abstract ItemStack[] getContents(); // CraftBukkit
}
