package net.minecraft.server;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class RecipeSmithing implements IRecipe<IInventory> {

    private final RecipeItemStack a;
    private final RecipeItemStack b;
    private final ItemStack c;
    private final MinecraftKey d;

    public RecipeSmithing(MinecraftKey minecraftkey, RecipeItemStack recipeitemstack, RecipeItemStack recipeitemstack1, ItemStack itemstack) {
        this.d = minecraftkey;
        this.a = recipeitemstack;
        this.b = recipeitemstack1;
        this.c = itemstack;
    }

    @Override
    public boolean a(IInventory iinventory, World world) {
        return this.a.test(iinventory.getItem(0)) && this.b.test(iinventory.getItem(1));
    }

    @Override
    public ItemStack a(IInventory iinventory) {
        ItemStack itemstack = this.c.cloneItemStack();
        NBTTagCompound nbttagcompound = iinventory.getItem(0).getTag();

        if (nbttagcompound != null) {
            itemstack.setTag(nbttagcompound.clone());
        }

        return itemstack;
    }

    @Override
    public ItemStack getResult() {
        return this.c;
    }

    public boolean a(ItemStack itemstack) {
        return this.b.test(itemstack);
    }

    @Override
    public MinecraftKey getKey() {
        return this.d;
    }

    @Override
    public RecipeSerializer<?> getRecipeSerializer() {
        return RecipeSerializer.u;
    }

    @Override
    public Recipes<?> g() {
        return Recipes.SMITHING;
    }

    public static class a implements RecipeSerializer<RecipeSmithing> {

        public a() {}

        @Override
        public RecipeSmithing a(MinecraftKey minecraftkey, JsonObject jsonobject) {
            RecipeItemStack recipeitemstack = RecipeItemStack.a((JsonElement) ChatDeserializer.t(jsonobject, "base"));
            RecipeItemStack recipeitemstack1 = RecipeItemStack.a((JsonElement) ChatDeserializer.t(jsonobject, "addition"));
            ItemStack itemstack = ShapedRecipes.a(ChatDeserializer.t(jsonobject, "result"));

            return new RecipeSmithing(minecraftkey, recipeitemstack, recipeitemstack1, itemstack);
        }

        @Override
        public RecipeSmithing a(MinecraftKey minecraftkey, PacketDataSerializer packetdataserializer) {
            RecipeItemStack recipeitemstack = RecipeItemStack.b(packetdataserializer);
            RecipeItemStack recipeitemstack1 = RecipeItemStack.b(packetdataserializer);
            ItemStack itemstack = packetdataserializer.n();

            return new RecipeSmithing(minecraftkey, recipeitemstack, recipeitemstack1, itemstack);
        }

        public void a(PacketDataSerializer packetdataserializer, RecipeSmithing recipesmithing) {
            recipesmithing.a.a(packetdataserializer);
            recipesmithing.b.a(packetdataserializer);
            packetdataserializer.a(recipesmithing.c);
        }
    }
}
