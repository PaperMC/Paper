package net.minecraft.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.bukkit.craftbukkit.event.CraftEventFactory; // CraftBukkit

public class CraftingManager {

    private static final CraftingManager a = new CraftingManager();
    // CraftBukkit start
    public List recipes = new ArrayList(); // private -> public
    public IRecipe lastRecipe;
    public org.bukkit.inventory.InventoryView lastCraftView;
    // CraftBukkit end

    public static final CraftingManager getInstance() {
        return a;
    }

    // CraftBukkit - private -> public
    public CraftingManager() {
        (new RecipesTools()).a(this);
        (new RecipesWeapons()).a(this);
        (new RecipeIngots()).a(this);
        (new RecipesFood()).a(this);
        (new RecipesCrafting()).a(this);
        (new RecipesArmor()).a(this);
        (new RecipesDyes()).a(this);
        this.recipes.add(new RecipeArmorDye());
        this.recipes.add(new RecipeBookClone());
        this.recipes.add(new RecipeMapClone());
        this.recipes.add(new RecipeMapExtend());
        this.recipes.add(new RecipeFireworks());
        this.registerShapedRecipe(new ItemStack(Items.PAPER, 3), new Object[] { "###", Character.valueOf('#'), Items.SUGAR_CANE});
        this.registerShapelessRecipe(new ItemStack(Items.BOOK, 1), new Object[] { Items.PAPER, Items.PAPER, Items.PAPER, Items.LEATHER});
        this.registerShapelessRecipe(new ItemStack(Items.BOOK_AND_QUILL, 1), new Object[] { Items.BOOK, new ItemStack(Items.INK_SACK, 1, 0), Items.FEATHER});
        this.registerShapedRecipe(new ItemStack(Blocks.FENCE, 2), new Object[] { "###", "###", Character.valueOf('#'), Items.STICK});
        this.registerShapedRecipe(new ItemStack(Blocks.COBBLE_WALL, 6, 0), new Object[] { "###", "###", Character.valueOf('#'), Blocks.COBBLESTONE});
        this.registerShapedRecipe(new ItemStack(Blocks.COBBLE_WALL, 6, 1), new Object[] { "###", "###", Character.valueOf('#'), Blocks.MOSSY_COBBLESTONE});
        this.registerShapedRecipe(new ItemStack(Blocks.NETHER_FENCE, 6), new Object[] { "###", "###", Character.valueOf('#'), Blocks.NETHER_BRICK});
        this.registerShapedRecipe(new ItemStack(Blocks.FENCE_GATE, 1), new Object[] { "#W#", "#W#", Character.valueOf('#'), Items.STICK, Character.valueOf('W'), Blocks.WOOD});
        this.registerShapedRecipe(new ItemStack(Blocks.JUKEBOX, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Blocks.WOOD, Character.valueOf('X'), Items.DIAMOND});
        this.registerShapedRecipe(new ItemStack(Items.LEASH, 2), new Object[] { "~~ ", "~O ", "  ~", Character.valueOf('~'), Items.STRING, Character.valueOf('O'), Items.SLIME_BALL});
        this.registerShapedRecipe(new ItemStack(Blocks.NOTE_BLOCK, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Blocks.WOOD, Character.valueOf('X'), Items.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Blocks.BOOKSHELF, 1), new Object[] { "###", "XXX", "###", Character.valueOf('#'), Blocks.WOOD, Character.valueOf('X'), Items.BOOK});
        this.registerShapedRecipe(new ItemStack(Blocks.SNOW_BLOCK, 1), new Object[] { "##", "##", Character.valueOf('#'), Items.SNOW_BALL});
        this.registerShapedRecipe(new ItemStack(Blocks.SNOW, 6), new Object[] { "###", Character.valueOf('#'), Blocks.SNOW_BLOCK});
        this.registerShapedRecipe(new ItemStack(Blocks.CLAY, 1), new Object[] { "##", "##", Character.valueOf('#'), Items.CLAY_BALL});
        this.registerShapedRecipe(new ItemStack(Blocks.BRICK, 1), new Object[] { "##", "##", Character.valueOf('#'), Items.CLAY_BRICK});
        this.registerShapedRecipe(new ItemStack(Blocks.GLOWSTONE, 1), new Object[] { "##", "##", Character.valueOf('#'), Items.GLOWSTONE_DUST});
        this.registerShapedRecipe(new ItemStack(Blocks.QUARTZ_BLOCK, 1), new Object[] { "##", "##", Character.valueOf('#'), Items.QUARTZ});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOL, 1), new Object[] { "##", "##", Character.valueOf('#'), Items.STRING});
        this.registerShapedRecipe(new ItemStack(Blocks.TNT, 1), new Object[] { "X#X", "#X#", "X#X", Character.valueOf('X'), Items.SULPHUR, Character.valueOf('#'), Blocks.SAND});
        this.registerShapedRecipe(new ItemStack(Blocks.STEP, 6, 3), new Object[] { "###", Character.valueOf('#'), Blocks.COBBLESTONE});
        this.registerShapedRecipe(new ItemStack(Blocks.STEP, 6, 0), new Object[] { "###", Character.valueOf('#'), Blocks.STONE});
        this.registerShapedRecipe(new ItemStack(Blocks.STEP, 6, 1), new Object[] { "###", Character.valueOf('#'), Blocks.SANDSTONE});
        this.registerShapedRecipe(new ItemStack(Blocks.STEP, 6, 4), new Object[] { "###", Character.valueOf('#'), Blocks.BRICK});
        this.registerShapedRecipe(new ItemStack(Blocks.STEP, 6, 5), new Object[] { "###", Character.valueOf('#'), Blocks.SMOOTH_BRICK});
        this.registerShapedRecipe(new ItemStack(Blocks.STEP, 6, 6), new Object[] { "###", Character.valueOf('#'), Blocks.NETHER_BRICK});
        this.registerShapedRecipe(new ItemStack(Blocks.STEP, 6, 7), new Object[] { "###", Character.valueOf('#'), Blocks.QUARTZ_BLOCK});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD_STEP, 6, 0), new Object[] { "###", Character.valueOf('#'), new ItemStack(Blocks.WOOD, 1, 0)});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD_STEP, 6, 2), new Object[] { "###", Character.valueOf('#'), new ItemStack(Blocks.WOOD, 1, 2)});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD_STEP, 6, 1), new Object[] { "###", Character.valueOf('#'), new ItemStack(Blocks.WOOD, 1, 1)});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD_STEP, 6, 3), new Object[] { "###", Character.valueOf('#'), new ItemStack(Blocks.WOOD, 1, 3)});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD_STEP, 6, 4), new Object[] { "###", Character.valueOf('#'), new ItemStack(Blocks.WOOD, 1, 4)});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD_STEP, 6, 5), new Object[] { "###", Character.valueOf('#'), new ItemStack(Blocks.WOOD, 1, 5)});
        this.registerShapedRecipe(new ItemStack(Blocks.LADDER, 3), new Object[] { "# #", "###", "# #", Character.valueOf('#'), Items.STICK});
        this.registerShapedRecipe(new ItemStack(Items.WOOD_DOOR, 1), new Object[] { "##", "##", "##", Character.valueOf('#'), Blocks.WOOD});
        this.registerShapedRecipe(new ItemStack(Blocks.TRAP_DOOR, 2), new Object[] { "###", "###", Character.valueOf('#'), Blocks.WOOD});
        this.registerShapedRecipe(new ItemStack(Items.IRON_DOOR, 1), new Object[] { "##", "##", "##", Character.valueOf('#'), Items.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Items.SIGN, 3), new Object[] { "###", "###", " X ", Character.valueOf('#'), Blocks.WOOD, Character.valueOf('X'), Items.STICK});
        this.registerShapedRecipe(new ItemStack(Items.CAKE, 1), new Object[] { "AAA", "BEB", "CCC", Character.valueOf('A'), Items.MILK_BUCKET, Character.valueOf('B'), Items.SUGAR, Character.valueOf('C'), Items.WHEAT, Character.valueOf('E'), Items.EGG});
        this.registerShapedRecipe(new ItemStack(Items.SUGAR, 1), new Object[] { "#", Character.valueOf('#'), Items.SUGAR_CANE});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD, 4, 0), new Object[] { "#", Character.valueOf('#'), new ItemStack(Blocks.LOG, 1, 0)});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD, 4, 1), new Object[] { "#", Character.valueOf('#'), new ItemStack(Blocks.LOG, 1, 1)});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD, 4, 2), new Object[] { "#", Character.valueOf('#'), new ItemStack(Blocks.LOG, 1, 2)});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD, 4, 3), new Object[] { "#", Character.valueOf('#'), new ItemStack(Blocks.LOG, 1, 3)});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD, 4, 4), new Object[] { "#", Character.valueOf('#'), new ItemStack(Blocks.LOG2, 1, 0)});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD, 4, 5), new Object[] { "#", Character.valueOf('#'), new ItemStack(Blocks.LOG2, 1, 1)});
        this.registerShapedRecipe(new ItemStack(Items.STICK, 4), new Object[] { "#", "#", Character.valueOf('#'), Blocks.WOOD});
        this.registerShapedRecipe(new ItemStack(Blocks.TORCH, 4), new Object[] { "X", "#", Character.valueOf('X'), Items.COAL, Character.valueOf('#'), Items.STICK});
        this.registerShapedRecipe(new ItemStack(Blocks.TORCH, 4), new Object[] { "X", "#", Character.valueOf('X'), new ItemStack(Items.COAL, 1, 1), Character.valueOf('#'), Items.STICK});
        this.registerShapedRecipe(new ItemStack(Items.BOWL, 4), new Object[] { "# #", " # ", Character.valueOf('#'), Blocks.WOOD});
        this.registerShapedRecipe(new ItemStack(Items.GLASS_BOTTLE, 3), new Object[] { "# #", " # ", Character.valueOf('#'), Blocks.GLASS});
        this.registerShapedRecipe(new ItemStack(Blocks.RAILS, 16), new Object[] { "X X", "X#X", "X X", Character.valueOf('X'), Items.IRON_INGOT, Character.valueOf('#'), Items.STICK});
        this.registerShapedRecipe(new ItemStack(Blocks.GOLDEN_RAIL, 6), new Object[] { "X X", "X#X", "XRX", Character.valueOf('X'), Items.GOLD_INGOT, Character.valueOf('R'), Items.REDSTONE, Character.valueOf('#'), Items.STICK});
        this.registerShapedRecipe(new ItemStack(Blocks.ACTIVATOR_RAIL, 6), new Object[] { "XSX", "X#X", "XSX", Character.valueOf('X'), Items.IRON_INGOT, Character.valueOf('#'), Blocks.REDSTONE_TORCH_ON, Character.valueOf('S'), Items.STICK});
        this.registerShapedRecipe(new ItemStack(Blocks.DETECTOR_RAIL, 6), new Object[] { "X X", "X#X", "XRX", Character.valueOf('X'), Items.IRON_INGOT, Character.valueOf('R'), Items.REDSTONE, Character.valueOf('#'), Blocks.STONE_PLATE});
        this.registerShapedRecipe(new ItemStack(Items.MINECART, 1), new Object[] { "# #", "###", Character.valueOf('#'), Items.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Items.CAULDRON, 1), new Object[] { "# #", "# #", "###", Character.valueOf('#'), Items.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Items.BREWING_STAND, 1), new Object[] { " B ", "###", Character.valueOf('#'), Blocks.COBBLESTONE, Character.valueOf('B'), Items.BLAZE_ROD});
        this.registerShapedRecipe(new ItemStack(Blocks.JACK_O_LANTERN, 1), new Object[] { "A", "B", Character.valueOf('A'), Blocks.PUMPKIN, Character.valueOf('B'), Blocks.TORCH});
        this.registerShapedRecipe(new ItemStack(Items.STORAGE_MINECART, 1), new Object[] { "A", "B", Character.valueOf('A'), Blocks.CHEST, Character.valueOf('B'), Items.MINECART});
        this.registerShapedRecipe(new ItemStack(Items.POWERED_MINECART, 1), new Object[] { "A", "B", Character.valueOf('A'), Blocks.FURNACE, Character.valueOf('B'), Items.MINECART});
        this.registerShapedRecipe(new ItemStack(Items.MINECART_TNT, 1), new Object[] { "A", "B", Character.valueOf('A'), Blocks.TNT, Character.valueOf('B'), Items.MINECART});
        this.registerShapedRecipe(new ItemStack(Items.MINECART_HOPPER, 1), new Object[] { "A", "B", Character.valueOf('A'), Blocks.HOPPER, Character.valueOf('B'), Items.MINECART});
        this.registerShapedRecipe(new ItemStack(Items.BOAT, 1), new Object[] { "# #", "###", Character.valueOf('#'), Blocks.WOOD});
        this.registerShapedRecipe(new ItemStack(Items.BUCKET, 1), new Object[] { "# #", " # ", Character.valueOf('#'), Items.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Items.FLOWER_POT, 1), new Object[] { "# #", " # ", Character.valueOf('#'), Items.CLAY_BRICK});
        this.registerShapelessRecipe(new ItemStack(Items.FLINT_AND_STEEL, 1), new Object[] { new ItemStack(Items.IRON_INGOT, 1), new ItemStack(Items.FLINT, 1)});
        this.registerShapedRecipe(new ItemStack(Items.BREAD, 1), new Object[] { "###", Character.valueOf('#'), Items.WHEAT});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Blocks.WOOD, 1, 0)});
        this.registerShapedRecipe(new ItemStack(Blocks.BIRCH_WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Blocks.WOOD, 1, 2)});
        this.registerShapedRecipe(new ItemStack(Blocks.SPRUCE_WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Blocks.WOOD, 1, 1)});
        this.registerShapedRecipe(new ItemStack(Blocks.JUNGLE_WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Blocks.WOOD, 1, 3)});
        this.registerShapedRecipe(new ItemStack(Blocks.ACACIA_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Blocks.WOOD, 1, 4)});
        this.registerShapedRecipe(new ItemStack(Blocks.DARK_OAK_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Blocks.WOOD, 1, 5)});
        this.registerShapedRecipe(new ItemStack(Items.FISHING_ROD, 1), new Object[] { "  #", " #X", "# X", Character.valueOf('#'), Items.STICK, Character.valueOf('X'), Items.STRING});
        this.registerShapedRecipe(new ItemStack(Items.CARROT_STICK, 1), new Object[] { "# ", " X", Character.valueOf('#'), Items.FISHING_ROD, Character.valueOf('X'), Items.CARROT}).c();
        this.registerShapedRecipe(new ItemStack(Blocks.COBBLESTONE_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.COBBLESTONE});
        this.registerShapedRecipe(new ItemStack(Blocks.BRICK_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.BRICK});
        this.registerShapedRecipe(new ItemStack(Blocks.STONE_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.SMOOTH_BRICK});
        this.registerShapedRecipe(new ItemStack(Blocks.NETHER_BRICK_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.NETHER_BRICK});
        this.registerShapedRecipe(new ItemStack(Blocks.SANDSTONE_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.SANDSTONE});
        this.registerShapedRecipe(new ItemStack(Blocks.QUARTZ_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Blocks.QUARTZ_BLOCK});
        this.registerShapedRecipe(new ItemStack(Items.PAINTING, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Items.STICK, Character.valueOf('X'), Blocks.WOOL});
        this.registerShapedRecipe(new ItemStack(Items.ITEM_FRAME, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Items.STICK, Character.valueOf('X'), Items.LEATHER});
        this.registerShapedRecipe(new ItemStack(Items.GOLDEN_APPLE, 1, 0), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Items.GOLD_INGOT, Character.valueOf('X'), Items.APPLE});
        this.registerShapedRecipe(new ItemStack(Items.GOLDEN_APPLE, 1, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Blocks.GOLD_BLOCK, Character.valueOf('X'), Items.APPLE});
        this.registerShapedRecipe(new ItemStack(Items.CARROT_GOLDEN, 1, 0), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Items.GOLD_NUGGET, Character.valueOf('X'), Items.CARROT});
        this.registerShapedRecipe(new ItemStack(Items.SPECKLED_MELON, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Items.GOLD_NUGGET, Character.valueOf('X'), Items.MELON});
        this.registerShapedRecipe(new ItemStack(Blocks.LEVER, 1), new Object[] { "X", "#", Character.valueOf('#'), Blocks.COBBLESTONE, Character.valueOf('X'), Items.STICK});
        this.registerShapedRecipe(new ItemStack(Blocks.TRIPWIRE_SOURCE, 2), new Object[] { "I", "S", "#", Character.valueOf('#'), Blocks.WOOD, Character.valueOf('S'), Items.STICK, Character.valueOf('I'), Items.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Blocks.REDSTONE_TORCH_ON, 1), new Object[] { "X", "#", Character.valueOf('#'), Items.STICK, Character.valueOf('X'), Items.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Items.DIODE, 1), new Object[] { "#X#", "III", Character.valueOf('#'), Blocks.REDSTONE_TORCH_ON, Character.valueOf('X'), Items.REDSTONE, Character.valueOf('I'), Blocks.STONE});
        this.registerShapedRecipe(new ItemStack(Items.REDSTONE_COMPARATOR, 1), new Object[] { " # ", "#X#", "III", Character.valueOf('#'), Blocks.REDSTONE_TORCH_ON, Character.valueOf('X'), Items.QUARTZ, Character.valueOf('I'), Blocks.STONE});
        this.registerShapedRecipe(new ItemStack(Items.WATCH, 1), new Object[] { " # ", "#X#", " # ", Character.valueOf('#'), Items.GOLD_INGOT, Character.valueOf('X'), Items.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Items.COMPASS, 1), new Object[] { " # ", "#X#", " # ", Character.valueOf('#'), Items.IRON_INGOT, Character.valueOf('X'), Items.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Items.MAP_EMPTY, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Items.PAPER, Character.valueOf('X'), Items.COMPASS});
        this.registerShapedRecipe(new ItemStack(Blocks.STONE_BUTTON, 1), new Object[] { "#", Character.valueOf('#'), Blocks.STONE});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD_BUTTON, 1), new Object[] { "#", Character.valueOf('#'), Blocks.WOOD});
        this.registerShapedRecipe(new ItemStack(Blocks.STONE_PLATE, 1), new Object[] { "##", Character.valueOf('#'), Blocks.STONE});
        this.registerShapedRecipe(new ItemStack(Blocks.WOOD_PLATE, 1), new Object[] { "##", Character.valueOf('#'), Blocks.WOOD});
        this.registerShapedRecipe(new ItemStack(Blocks.IRON_PLATE, 1), new Object[] { "##", Character.valueOf('#'), Items.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Blocks.GOLD_PLATE, 1), new Object[] { "##", Character.valueOf('#'), Items.GOLD_INGOT});
        this.registerShapedRecipe(new ItemStack(Blocks.DISPENSER, 1), new Object[] { "###", "#X#", "#R#", Character.valueOf('#'), Blocks.COBBLESTONE, Character.valueOf('X'), Items.BOW, Character.valueOf('R'), Items.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Blocks.DROPPER, 1), new Object[] { "###", "# #", "#R#", Character.valueOf('#'), Blocks.COBBLESTONE, Character.valueOf('R'), Items.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Blocks.PISTON, 1), new Object[] { "TTT", "#X#", "#R#", Character.valueOf('#'), Blocks.COBBLESTONE, Character.valueOf('X'), Items.IRON_INGOT, Character.valueOf('R'), Items.REDSTONE, Character.valueOf('T'), Blocks.WOOD});
        this.registerShapedRecipe(new ItemStack(Blocks.PISTON_STICKY, 1), new Object[] { "S", "P", Character.valueOf('S'), Items.SLIME_BALL, Character.valueOf('P'), Blocks.PISTON});
        this.registerShapedRecipe(new ItemStack(Items.BED, 1), new Object[] { "###", "XXX", Character.valueOf('#'), Blocks.WOOL, Character.valueOf('X'), Blocks.WOOD});
        this.registerShapedRecipe(new ItemStack(Blocks.ENCHANTMENT_TABLE, 1), new Object[] { " B ", "D#D", "###", Character.valueOf('#'), Blocks.OBSIDIAN, Character.valueOf('B'), Items.BOOK, Character.valueOf('D'), Items.DIAMOND});
        this.registerShapedRecipe(new ItemStack(Blocks.ANVIL, 1), new Object[] { "III", " i ", "iii", Character.valueOf('I'), Blocks.IRON_BLOCK, Character.valueOf('i'), Items.IRON_INGOT});
        this.registerShapelessRecipe(new ItemStack(Items.EYE_OF_ENDER, 1), new Object[] { Items.ENDER_PEARL, Items.BLAZE_POWDER});
        this.registerShapelessRecipe(new ItemStack(Items.FIREBALL, 3), new Object[] { Items.SULPHUR, Items.BLAZE_POWDER, Items.COAL});
        this.registerShapelessRecipe(new ItemStack(Items.FIREBALL, 3), new Object[] { Items.SULPHUR, Items.BLAZE_POWDER, new ItemStack(Items.COAL, 1, 1)});
        this.registerShapedRecipe(new ItemStack(Blocks.DAYLIGHT_DETECTOR), new Object[] { "GGG", "QQQ", "WWW", Character.valueOf('G'), Blocks.GLASS, Character.valueOf('Q'), Items.QUARTZ, Character.valueOf('W'), Blocks.WOOD_STEP});
        this.registerShapedRecipe(new ItemStack(Blocks.HOPPER), new Object[] { "I I", "ICI", " I ", Character.valueOf('I'), Items.IRON_INGOT, Character.valueOf('C'), Blocks.CHEST});
        // Collections.sort(this.recipes, new RecipeSorter(this)); // CraftBukkit - moved below
        this.sort(); // CraftBukkit - call new sort method
    }

    // CraftBukkit start
    public void sort() {
        Collections.sort(this.recipes, new RecipeSorter(this));
    }
    // CraftBukkit end

    // CraftBukkit - default -> public
    public ShapedRecipes registerShapedRecipe(ItemStack itemstack, Object... aobject) {
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
                itemstack1 = new ItemStack((Block) aobject[i + 1], 1, 32767);
            } else if (aobject[i + 1] instanceof ItemStack) {
                itemstack1 = (ItemStack) aobject[i + 1];
            }

            hashmap.put(character, itemstack1);
        }

        ItemStack[] aitemstack = new ItemStack[j * k];

        for (int i1 = 0; i1 < j * k; ++i1) {
            char c0 = s.charAt(i1);

            if (hashmap.containsKey(Character.valueOf(c0))) {
                aitemstack[i1] = ((ItemStack) hashmap.get(Character.valueOf(c0))).cloneItemStack();
            } else {
                aitemstack[i1] = null;
            }
        }

        ShapedRecipes shapedrecipes = new ShapedRecipes(j, k, aitemstack, itemstack);

        this.recipes.add(shapedrecipes);
        return shapedrecipes;
    }

    // CraftBukkit - default -> public
    public void registerShapelessRecipe(ItemStack itemstack, Object... aobject) {
        ArrayList arraylist = new ArrayList();
        Object[] aobject1 = aobject;
        int i = aobject.length;

        for (int j = 0; j < i; ++j) {
            Object object = aobject1[j];

            if (object instanceof ItemStack) {
                arraylist.add(((ItemStack) object).cloneItemStack());
            } else if (object instanceof Item) {
                arraylist.add(new ItemStack((Item) object));
            } else {
                if (!(object instanceof Block)) {
                    throw new RuntimeException("Invalid shapeless recipy!");
                }

                arraylist.add(new ItemStack((Block) object));
            }
        }

        this.recipes.add(new ShapelessRecipes(itemstack, arraylist));
    }

    public ItemStack craft(InventoryCrafting inventorycrafting, World world) {
        int i = 0;
        ItemStack itemstack = null;
        ItemStack itemstack1 = null;

        int j;

        for (j = 0; j < inventorycrafting.getSize(); ++j) {
            ItemStack itemstack2 = inventorycrafting.getItem(j);

            if (itemstack2 != null) {
                if (i == 0) {
                    itemstack = itemstack2;
                }

                if (i == 1) {
                    itemstack1 = itemstack2;
                }

                ++i;
            }
        }

        if (i == 2 && itemstack.getItem() == itemstack1.getItem() && itemstack.count == 1 && itemstack1.count == 1 && itemstack.getItem().usesDurability()) {
            Item item = itemstack.getItem();
            int k = item.getMaxDurability() - itemstack.j();
            int l = item.getMaxDurability() - itemstack1.j();
            int i1 = k + l + item.getMaxDurability() * 5 / 100;
            int j1 = item.getMaxDurability() - i1;

            if (j1 < 0) {
                j1 = 0;
            }

            // CraftBukkit start - Construct a dummy repair recipe
            ItemStack result = new ItemStack(itemstack.getItem(), 1, j1);
            List<ItemStack> ingredients = new ArrayList<ItemStack>();
            ingredients.add(itemstack.cloneItemStack());
            ingredients.add(itemstack1.cloneItemStack());
            ShapelessRecipes recipe = new ShapelessRecipes(result.cloneItemStack(), ingredients);
            inventorycrafting.currentRecipe = recipe;
            result = CraftEventFactory.callPreCraftEvent(inventorycrafting, result, lastCraftView, true);
            return result;
            // CraftBukkit end
        } else {
            for (j = 0; j < this.recipes.size(); ++j) {
                IRecipe irecipe = (IRecipe) this.recipes.get(j);

                if (irecipe.a(inventorycrafting, world)) {
                    // CraftBukkit start - INVENTORY_PRE_CRAFT event
                    inventorycrafting.currentRecipe = irecipe;
                    ItemStack result = irecipe.a(inventorycrafting);
                    return CraftEventFactory.callPreCraftEvent(inventorycrafting, result, lastCraftView, false);
                    // CraftBukkit end
                }
            }

            inventorycrafting.currentRecipe = null; // CraftBukkit - Clear recipe when no recipe is found
            return null;
        }
    }

    public List getRecipes() {
        return this.recipes;
    }
}
