package net.minecraft.server;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class RecipesFurnace {

    private static final RecipesFurnace a = new RecipesFurnace();
    public Map recipes = new HashMap(); // CraftBukkit - private -> public
    private Map c = new HashMap();

    public static RecipesFurnace getInstance() {
        return a;
    }

    public RecipesFurnace() { // CraftBukkit - private -> public
        this.registerRecipe(Blocks.IRON_ORE, new ItemStack(Items.IRON_INGOT), 0.7F);
        this.registerRecipe(Blocks.GOLD_ORE, new ItemStack(Items.GOLD_INGOT), 1.0F);
        this.registerRecipe(Blocks.DIAMOND_ORE, new ItemStack(Items.DIAMOND), 1.0F);
        this.registerRecipe(Blocks.SAND, new ItemStack(Blocks.GLASS), 0.1F);
        this.a(Items.PORK, new ItemStack(Items.GRILLED_PORK), 0.35F);
        this.a(Items.RAW_BEEF, new ItemStack(Items.COOKED_BEEF), 0.35F);
        this.a(Items.RAW_CHICKEN, new ItemStack(Items.COOKED_CHICKEN), 0.35F);
        this.registerRecipe(Blocks.COBBLESTONE, new ItemStack(Blocks.STONE), 0.1F);
        this.a(Items.CLAY_BALL, new ItemStack(Items.CLAY_BRICK), 0.3F);
        this.registerRecipe(Blocks.CLAY, new ItemStack(Blocks.HARDENED_CLAY), 0.35F);
        this.registerRecipe(Blocks.CACTUS, new ItemStack(Items.INK_SACK, 1, 2), 0.2F);
        this.registerRecipe(Blocks.LOG, new ItemStack(Items.COAL, 1, 1), 0.15F);
        this.registerRecipe(Blocks.LOG2, new ItemStack(Items.COAL, 1, 1), 0.15F);
        this.registerRecipe(Blocks.EMERALD_ORE, new ItemStack(Items.EMERALD), 1.0F);
        this.a(Items.POTATO, new ItemStack(Items.POTATO_BAKED), 0.35F);
        this.registerRecipe(Blocks.NETHERRACK, new ItemStack(Items.NETHER_BRICK), 0.1F);
        EnumFish[] aenumfish = EnumFish.values();
        int i = aenumfish.length;

        for (int j = 0; j < i; ++j) {
            EnumFish enumfish = aenumfish[j];

            if (enumfish.i()) {
                this.a(new ItemStack(Items.RAW_FISH, 1, enumfish.a()), new ItemStack(Items.COOKED_FISH, 1, enumfish.a()), 0.35F);
            }
        }

        this.registerRecipe(Blocks.COAL_ORE, new ItemStack(Items.COAL), 0.1F);
        this.registerRecipe(Blocks.REDSTONE_ORE, new ItemStack(Items.REDSTONE), 0.7F);
        this.registerRecipe(Blocks.LAPIS_ORE, new ItemStack(Items.INK_SACK, 1, 4), 0.2F);
        this.registerRecipe(Blocks.QUARTZ_ORE, new ItemStack(Items.QUARTZ), 0.2F);
    }

    public void registerRecipe(Block block, ItemStack itemstack, float f) {
        this.a(Item.getItemOf(block), itemstack, f);
    }

    public void a(Item item, ItemStack itemstack, float f) {
        this.a(new ItemStack(item, 1, 32767), itemstack, f);
    }

    public void a(ItemStack itemstack, ItemStack itemstack1, float f) {
        this.recipes.put(itemstack, itemstack1);
        this.c.put(itemstack1, Float.valueOf(f));
    }

    public ItemStack getResult(ItemStack itemstack) {
        Iterator iterator = this.recipes.entrySet().iterator();

        Entry entry;

        do {
            if (!iterator.hasNext()) {
                return null;
            }

            entry = (Entry) iterator.next();
        } while (!this.a(itemstack, (ItemStack) entry.getKey()));

        return (ItemStack) entry.getValue();
    }

    private boolean a(ItemStack itemstack, ItemStack itemstack1) {
        return itemstack1.getItem() == itemstack.getItem() && (itemstack1.getData() == 32767 || itemstack1.getData() == itemstack.getData());
    }

    public Map getRecipes() {
        return this.recipes;
    }

    public float b(ItemStack itemstack) {
        Iterator iterator = this.c.entrySet().iterator();

        Entry entry;

        do {
            if (!iterator.hasNext()) {
                return 0.0F;
            }

            entry = (Entry) iterator.next();
        } while (!this.a(itemstack, (ItemStack) entry.getKey()));

        return ((Float) entry.getValue()).floatValue();
    }
}
