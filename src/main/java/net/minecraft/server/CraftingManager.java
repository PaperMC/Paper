package net.minecraft.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class CraftingManager {

    private static final CraftingManager a = new CraftingManager();
    private List b = new ArrayList();

    public static final CraftingManager a() {
        return a;
    }

    private CraftingManager() {
        (new RecipesTools()).a(this);
        (new RecipesWeapons()).a(this);
        (new RecipeIngots()).a(this);
        (new RecipesFood()).a(this);
        (new RecipesCrafting()).a(this);
        (new RecipesArmor()).a(this);
        (new RecipesDyes()).a(this);
        this.a(new ItemStack(Item.PAPER, 3), new Object[] { "###", Character.valueOf('#'), Item.SUGAR_CANE});
        this.a(new ItemStack(Item.BOOK, 1), new Object[] { "#", "#", "#", Character.valueOf('#'), Item.PAPER});
        this.a(new ItemStack(Block.FENCE, 2), new Object[] { "###", "###", Character.valueOf('#'), Item.STICK});
        this.a(new ItemStack(Block.JUKEBOX, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Block.WOOD, Character.valueOf('X'), Item.DIAMOND});
        this.a(new ItemStack(Block.NOTE_BLOCK, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Block.WOOD, Character.valueOf('X'), Item.REDSTONE});
        this.a(new ItemStack(Block.BOOKSHELF, 1), new Object[] { "###", "XXX", "###", Character.valueOf('#'), Block.WOOD, Character.valueOf('X'), Item.BOOK});
        this.a(new ItemStack(Block.SNOW_BLOCK, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.SNOW_BALL});
        this.a(new ItemStack(Block.CLAY, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.CLAY_BALL});
        this.a(new ItemStack(Block.BRICK, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.CLAY_BRICK});
        this.a(new ItemStack(Block.GLOWSTONE, 1), new Object[] { "###", "###", "###", Character.valueOf('#'), Item.GLOWSTONE_DUST});
        this.a(new ItemStack(Block.WOOL, 1), new Object[] { "###", "###", "###", Character.valueOf('#'), Item.STRING});
        this.a(new ItemStack(Block.TNT, 1), new Object[] { "X#X", "#X#", "X#X", Character.valueOf('X'), Item.SULPHUR, Character.valueOf('#'), Block.SAND});
        this.a(new ItemStack(Block.STEP, 3, 3), new Object[] { "###", Character.valueOf('#'), Block.COBBLESTONE});
        this.a(new ItemStack(Block.STEP, 3, 0), new Object[] { "###", Character.valueOf('#'), Block.STONE});
        this.a(new ItemStack(Block.STEP, 3, 1), new Object[] { "###", Character.valueOf('#'), Block.SANDSTONE});
        this.a(new ItemStack(Block.STEP, 3, 2), new Object[] { "###", Character.valueOf('#'), Block.WOOD});
        this.a(new ItemStack(Block.LADDER, 2), new Object[] { "# #", "###", "# #", Character.valueOf('#'), Item.STICK});
        this.a(new ItemStack(Item.WOOD_DOOR, 1), new Object[] { "##", "##", "##", Character.valueOf('#'), Block.WOOD});
        this.a(new ItemStack(Item.IRON_DOOR, 1), new Object[] { "##", "##", "##", Character.valueOf('#'), Item.IRON_INGOT});
        this.a(new ItemStack(Item.SIGN, 1), new Object[] { "###", "###", " X ", Character.valueOf('#'), Block.WOOD, Character.valueOf('X'), Item.STICK});
        this.a(new ItemStack(Item.CAKE, 1), new Object[] { "AAA", "BEB", "CCC", Character.valueOf('A'), Item.MILK_BUCKET, Character.valueOf('B'), Item.SUGAR, Character.valueOf('C'), Item.WHEAT, Character.valueOf('E'), Item.EGG});
        this.a(new ItemStack(Item.SUGAR, 1), new Object[] { "#", Character.valueOf('#'), Item.SUGAR_CANE});
        this.a(new ItemStack(Block.WOOD, 4), new Object[] { "#", Character.valueOf('#'), Block.LOG});
        this.a(new ItemStack(Item.STICK, 4), new Object[] { "#", "#", Character.valueOf('#'), Block.WOOD});
        this.a(new ItemStack(Block.TORCH, 4), new Object[] { "X", "#", Character.valueOf('X'), Item.COAL, Character.valueOf('#'), Item.STICK});
        this.a(new ItemStack(Block.TORCH, 4), new Object[] { "X", "#", Character.valueOf('X'), new ItemStack(Item.COAL, 1, 1), Character.valueOf('#'), Item.STICK});
        this.a(new ItemStack(Item.BOWL, 4), new Object[] { "# #", " # ", Character.valueOf('#'), Block.WOOD});
        this.a(new ItemStack(Block.RAILS, 16), new Object[] { "X X", "X#X", "X X", Character.valueOf('X'), Item.IRON_INGOT, Character.valueOf('#'), Item.STICK});
        this.a(new ItemStack(Block.GOLDEN_RAIL, 6), new Object[] { "X X", "X#X", "XRX", Character.valueOf('X'), Item.GOLD_INGOT, Character.valueOf('R'), Item.REDSTONE, Character.valueOf('#'), Item.STICK});
        this.a(new ItemStack(Block.DETECTOR_RAIL, 6), new Object[] { "X X", "X#X", "XRX", Character.valueOf('X'), Item.IRON_INGOT, Character.valueOf('R'), Item.REDSTONE, Character.valueOf('#'), Block.STONE_PLATE});
        this.a(new ItemStack(Item.MINECART, 1), new Object[] { "# #", "###", Character.valueOf('#'), Item.IRON_INGOT});
        this.a(new ItemStack(Block.JACK_O_LANTERN, 1), new Object[] { "A", "B", Character.valueOf('A'), Block.PUMPKIN, Character.valueOf('B'), Block.TORCH});
        this.a(new ItemStack(Item.STORAGE_MINECART, 1), new Object[] { "A", "B", Character.valueOf('A'), Block.CHEST, Character.valueOf('B'), Item.MINECART});
        this.a(new ItemStack(Item.POWERED_MINECART, 1), new Object[] { "A", "B", Character.valueOf('A'), Block.FURNACE, Character.valueOf('B'), Item.MINECART});
        this.a(new ItemStack(Item.BOAT, 1), new Object[] { "# #", "###", Character.valueOf('#'), Block.WOOD});
        this.a(new ItemStack(Item.BUCKET, 1), new Object[] { "# #", " # ", Character.valueOf('#'), Item.IRON_INGOT});
        this.a(new ItemStack(Item.FLINT_AND_STEEL, 1), new Object[] { "A ", " B", Character.valueOf('A'), Item.IRON_INGOT, Character.valueOf('B'), Item.FLINT});
        this.a(new ItemStack(Item.BREAD, 1), new Object[] { "###", Character.valueOf('#'), Item.WHEAT});
        this.a(new ItemStack(Block.WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Block.WOOD});
        this.a(new ItemStack(Item.FISHING_ROD, 1), new Object[] { "  #", " #X", "# X", Character.valueOf('#'), Item.STICK, Character.valueOf('X'), Item.STRING});
        this.a(new ItemStack(Block.COBBLESTONE_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Block.COBBLESTONE});
        this.a(new ItemStack(Item.PAINTING, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Item.STICK, Character.valueOf('X'), Block.WOOL});
        this.a(new ItemStack(Item.GOLDEN_APPLE, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Block.GOLD_BLOCK, Character.valueOf('X'), Item.APPLE});
        this.a(new ItemStack(Block.LEVER, 1), new Object[] { "X", "#", Character.valueOf('#'), Block.COBBLESTONE, Character.valueOf('X'), Item.STICK});
        this.a(new ItemStack(Block.REDSTONE_TORCH_ON, 1), new Object[] { "X", "#", Character.valueOf('#'), Item.STICK, Character.valueOf('X'), Item.REDSTONE});
        this.a(new ItemStack(Item.DIODE, 1), new Object[] { "#X#", "III", Character.valueOf('#'), Block.REDSTONE_TORCH_ON, Character.valueOf('X'), Item.REDSTONE, Character.valueOf('I'), Block.STONE});
        this.a(new ItemStack(Item.WATCH, 1), new Object[] { " # ", "#X#", " # ", Character.valueOf('#'), Item.GOLD_INGOT, Character.valueOf('X'), Item.REDSTONE});
        this.a(new ItemStack(Item.COMPASS, 1), new Object[] { " # ", "#X#", " # ", Character.valueOf('#'), Item.IRON_INGOT, Character.valueOf('X'), Item.REDSTONE});
        this.a(new ItemStack(Block.STONE_BUTTON, 1), new Object[] { "#", "#", Character.valueOf('#'), Block.STONE});
        this.a(new ItemStack(Block.STONE_PLATE, 1), new Object[] { "##", Character.valueOf('#'), Block.STONE});
        this.a(new ItemStack(Block.WOOD_PLATE, 1), new Object[] { "##", Character.valueOf('#'), Block.WOOD});
        this.a(new ItemStack(Block.DISPENSER, 1), new Object[] { "###", "#X#", "#R#", Character.valueOf('#'), Block.COBBLESTONE, Character.valueOf('X'), Item.BOW, Character.valueOf('R'), Item.REDSTONE});
        this.a(new ItemStack(Item.BED, 1), new Object[] { "###", "XXX", Character.valueOf('#'), Block.WOOL, Character.valueOf('X'), Block.WOOD});
        Collections.sort(this.b, new RecipeSorter(this));
        System.out.println(this.b.size() + " recipes");
    }

    public void a(ItemStack itemstack, Object[] aobject) { // CraftBukkit - default -> public and Object... -> Object[]
        String s = "";
        int i = 0;
        int j = 0;
        int k = 0;

        if (aobject[i] instanceof String[]) {
            String[] astring = (String[]) ((String[]) aobject[i++]);

            for (int l = 0; l < astring.length; ++l) {
                String s1 = astring[l];

                ++k;
                j = s1.length();
                s = s + s1;
            }
        } else {
            while (aobject[i] instanceof String) {
                String s2 = (String) aobject[i++];

                ++k;
                j = s2.length();
                s = s + s2;
            }
        }

        HashMap hashmap;

        for (hashmap = new HashMap(); i < aobject.length; i += 2) {
            Character character = (Character) aobject[i];
            ItemStack itemstack1 = null;

            if (aobject[i + 1] instanceof Item) {
                itemstack1 = new ItemStack((Item) aobject[i + 1]);
            } else if (aobject[i + 1] instanceof Block) {
                itemstack1 = new ItemStack((Block) aobject[i + 1], 1, -1);
            } else if (aobject[i + 1] instanceof ItemStack) {
                itemstack1 = (ItemStack) aobject[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int i1 = 0; i1 < j * k; ++i1) {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(Character.valueOf(c0))) {
                aitemstack[i1] = ((ItemStack) hashmap.get(Character.valueOf(c0))).j();
            } else {
                aitemstack[i1] = null;
            }
        }

        this.b.add(new ShapedRecipes(j, k, aitemstack, itemstack));
    }

    public void b(ItemStack itemstack, Object[] aobject) { // CraftBukkit - default -> public and Object... -> Object[]
        ArrayList arraylist = new ArrayList();
        Object[] aobject1 = aobject;
        int i = aobject.length;

        for (int j = 0; j < i; ++j) {
            Object object = aobject1[j];

            if (object instanceof ItemStack) {
                arraylist.add(((ItemStack) object).j());
            } else if (object instanceof Item) {
                arraylist.add(new ItemStack((Item) object));
            } else {
                if (!(object instanceof Block)) {
                    throw new RuntimeException("Invalid shapeless recipy!");
                }

                arraylist.add(new ItemStack((Block) object));
            }
        }

        this.b.add(new ShapelessRecipes(itemstack, arraylist));
    }

    public ItemStack a(InventoryCrafting inventorycrafting) {
        for (int i = 0; i < this.b.size(); ++i) {
            CraftingRecipe craftingrecipe = (CraftingRecipe) this.b.get(i);

            if (craftingrecipe.a(inventorycrafting)) {
                return craftingrecipe.b(inventorycrafting);
            }
        }

        return null;
    }

    public List b() {
        return this.b;
    }
}
