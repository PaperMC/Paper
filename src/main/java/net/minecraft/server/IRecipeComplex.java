package net.minecraft.server;

public abstract class IRecipeComplex implements RecipeCrafting {

    private final MinecraftKey a;

    public IRecipeComplex(MinecraftKey minecraftkey) {
        this.a = minecraftkey;
    }

    @Override
    public MinecraftKey getKey() {
        return this.a;
    }

    @Override
    public boolean isComplex() {
        return true;
    }

    @Override
    public ItemStack getResult() {
        return ItemStack.b;
    }

    // CraftBukkit start
    @Override
    public org.bukkit.inventory.Recipe toBukkitRecipe() {
        return new org.bukkit.craftbukkit.inventory.CraftComplexRecipe(this);
    }
    // CraftBukkit end
}
