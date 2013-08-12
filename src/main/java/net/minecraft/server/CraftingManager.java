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
        this.recipes.add(new RecipeMapClone());
        this.recipes.add(new RecipeMapExtend());
        this.recipes.add(new RecipeFireworks());
        this.registerShapedRecipe(new ItemStack(Item.PAPER, 3), new Object[] { "###", Character.valueOf('#'), Item.SUGAR_CANE});
        this.registerShapelessRecipe(new ItemStack(Item.BOOK, 1), new Object[] { Item.PAPER, Item.PAPER, Item.PAPER, Item.LEATHER});
        this.registerShapelessRecipe(new ItemStack(Item.BOOK_AND_QUILL, 1), new Object[] { Item.BOOK, new ItemStack(Item.INK_SACK, 1, 0), Item.FEATHER});
        this.registerShapedRecipe(new ItemStack(Block.FENCE, 2), new Object[] { "###", "###", Character.valueOf('#'), Item.STICK});
        this.registerShapedRecipe(new ItemStack(Block.COBBLE_WALL, 6, 0), new Object[] { "###", "###", Character.valueOf('#'), Block.COBBLESTONE});
        this.registerShapedRecipe(new ItemStack(Block.COBBLE_WALL, 6, 1), new Object[] { "###", "###", Character.valueOf('#'), Block.MOSSY_COBBLESTONE});
        this.registerShapedRecipe(new ItemStack(Block.NETHER_FENCE, 6), new Object[] { "###", "###", Character.valueOf('#'), Block.NETHER_BRICK});
        this.registerShapedRecipe(new ItemStack(Block.FENCE_GATE, 1), new Object[] { "#W#", "#W#", Character.valueOf('#'), Item.STICK, Character.valueOf('W'), Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Block.JUKEBOX, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Block.WOOD, Character.valueOf('X'), Item.DIAMOND});
        this.registerShapedRecipe(new ItemStack(Item.LEASH, 2), new Object[] { "~~ ", "~O ", "  ~", Character.valueOf('~'), Item.STRING, Character.valueOf('O'), Item.SLIME_BALL});
        this.registerShapedRecipe(new ItemStack(Block.NOTE_BLOCK, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Block.WOOD, Character.valueOf('X'), Item.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Block.BOOKSHELF, 1), new Object[] { "###", "XXX", "###", Character.valueOf('#'), Block.WOOD, Character.valueOf('X'), Item.BOOK});
        this.registerShapedRecipe(new ItemStack(Block.SNOW_BLOCK, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.SNOW_BALL});
        this.registerShapedRecipe(new ItemStack(Block.SNOW, 6), new Object[] { "###", Character.valueOf('#'), Block.SNOW_BLOCK});
        this.registerShapedRecipe(new ItemStack(Block.CLAY, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.CLAY_BALL});
        this.registerShapedRecipe(new ItemStack(Block.BRICK, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.CLAY_BRICK});
        this.registerShapedRecipe(new ItemStack(Block.GLOWSTONE, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.GLOWSTONE_DUST});
        this.registerShapedRecipe(new ItemStack(Block.QUARTZ_BLOCK, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.QUARTZ});
        this.registerShapedRecipe(new ItemStack(Block.WOOL, 1), new Object[] { "##", "##", Character.valueOf('#'), Item.STRING});
        this.registerShapedRecipe(new ItemStack(Block.TNT, 1), new Object[] { "X#X", "#X#", "X#X", Character.valueOf('X'), Item.SULPHUR, Character.valueOf('#'), Block.SAND});
        this.registerShapedRecipe(new ItemStack(Block.STEP, 6, 3), new Object[] { "###", Character.valueOf('#'), Block.COBBLESTONE});
        this.registerShapedRecipe(new ItemStack(Block.STEP, 6, 0), new Object[] { "###", Character.valueOf('#'), Block.STONE});
        this.registerShapedRecipe(new ItemStack(Block.STEP, 6, 1), new Object[] { "###", Character.valueOf('#'), Block.SANDSTONE});
        this.registerShapedRecipe(new ItemStack(Block.STEP, 6, 4), new Object[] { "###", Character.valueOf('#'), Block.BRICK});
        this.registerShapedRecipe(new ItemStack(Block.STEP, 6, 5), new Object[] { "###", Character.valueOf('#'), Block.SMOOTH_BRICK});
        this.registerShapedRecipe(new ItemStack(Block.STEP, 6, 6), new Object[] { "###", Character.valueOf('#'), Block.NETHER_BRICK});
        this.registerShapedRecipe(new ItemStack(Block.STEP, 6, 7), new Object[] { "###", Character.valueOf('#'), Block.QUARTZ_BLOCK});
        this.registerShapedRecipe(new ItemStack(Block.WOOD_STEP, 6, 0), new Object[] { "###", Character.valueOf('#'), new ItemStack(Block.WOOD, 1, 0)});
        this.registerShapedRecipe(new ItemStack(Block.WOOD_STEP, 6, 2), new Object[] { "###", Character.valueOf('#'), new ItemStack(Block.WOOD, 1, 2)});
        this.registerShapedRecipe(new ItemStack(Block.WOOD_STEP, 6, 1), new Object[] { "###", Character.valueOf('#'), new ItemStack(Block.WOOD, 1, 1)});
        this.registerShapedRecipe(new ItemStack(Block.WOOD_STEP, 6, 3), new Object[] { "###", Character.valueOf('#'), new ItemStack(Block.WOOD, 1, 3)});
        this.registerShapedRecipe(new ItemStack(Block.LADDER, 3), new Object[] { "# #", "###", "# #", Character.valueOf('#'), Item.STICK});
        this.registerShapedRecipe(new ItemStack(Item.WOOD_DOOR, 1), new Object[] { "##", "##", "##", Character.valueOf('#'), Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Block.TRAP_DOOR, 2), new Object[] { "###", "###", Character.valueOf('#'), Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Item.IRON_DOOR, 1), new Object[] { "##", "##", "##", Character.valueOf('#'), Item.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Item.SIGN, 3), new Object[] { "###", "###", " X ", Character.valueOf('#'), Block.WOOD, Character.valueOf('X'), Item.STICK});
        this.registerShapedRecipe(new ItemStack(Item.CAKE, 1), new Object[] { "AAA", "BEB", "CCC", Character.valueOf('A'), Item.MILK_BUCKET, Character.valueOf('B'), Item.SUGAR, Character.valueOf('C'), Item.WHEAT, Character.valueOf('E'), Item.EGG});
        this.registerShapedRecipe(new ItemStack(Item.SUGAR, 1), new Object[] { "#", Character.valueOf('#'), Item.SUGAR_CANE});
        this.registerShapedRecipe(new ItemStack(Block.WOOD, 4, 0), new Object[] { "#", Character.valueOf('#'), new ItemStack(Block.LOG, 1, 0)});
        this.registerShapedRecipe(new ItemStack(Block.WOOD, 4, 1), new Object[] { "#", Character.valueOf('#'), new ItemStack(Block.LOG, 1, 1)});
        this.registerShapedRecipe(new ItemStack(Block.WOOD, 4, 2), new Object[] { "#", Character.valueOf('#'), new ItemStack(Block.LOG, 1, 2)});
        this.registerShapedRecipe(new ItemStack(Block.WOOD, 4, 3), new Object[] { "#", Character.valueOf('#'), new ItemStack(Block.LOG, 1, 3)});
        this.registerShapedRecipe(new ItemStack(Item.STICK, 4), new Object[] { "#", "#", Character.valueOf('#'), Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Block.TORCH, 4), new Object[] { "X", "#", Character.valueOf('X'), Item.COAL, Character.valueOf('#'), Item.STICK});
        this.registerShapedRecipe(new ItemStack(Block.TORCH, 4), new Object[] { "X", "#", Character.valueOf('X'), new ItemStack(Item.COAL, 1, 1), Character.valueOf('#'), Item.STICK});
        this.registerShapedRecipe(new ItemStack(Item.BOWL, 4), new Object[] { "# #", " # ", Character.valueOf('#'), Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Item.GLASS_BOTTLE, 3), new Object[] { "# #", " # ", Character.valueOf('#'), Block.GLASS});
        this.registerShapedRecipe(new ItemStack(Block.RAILS, 16), new Object[] { "X X", "X#X", "X X", Character.valueOf('X'), Item.IRON_INGOT, Character.valueOf('#'), Item.STICK});
        this.registerShapedRecipe(new ItemStack(Block.GOLDEN_RAIL, 6), new Object[] { "X X", "X#X", "XRX", Character.valueOf('X'), Item.GOLD_INGOT, Character.valueOf('R'), Item.REDSTONE, Character.valueOf('#'), Item.STICK});
        this.registerShapedRecipe(new ItemStack(Block.ACTIVATOR_RAIL, 6), new Object[] { "XSX", "X#X", "XSX", Character.valueOf('X'), Item.IRON_INGOT, Character.valueOf('#'), Block.REDSTONE_TORCH_ON, Character.valueOf('S'), Item.STICK});
        this.registerShapedRecipe(new ItemStack(Block.DETECTOR_RAIL, 6), new Object[] { "X X", "X#X", "XRX", Character.valueOf('X'), Item.IRON_INGOT, Character.valueOf('R'), Item.REDSTONE, Character.valueOf('#'), Block.STONE_PLATE});
        this.registerShapedRecipe(new ItemStack(Item.MINECART, 1), new Object[] { "# #", "###", Character.valueOf('#'), Item.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Item.CAULDRON, 1), new Object[] { "# #", "# #", "###", Character.valueOf('#'), Item.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Item.BREWING_STAND, 1), new Object[] { " B ", "###", Character.valueOf('#'), Block.COBBLESTONE, Character.valueOf('B'), Item.BLAZE_ROD});
        this.registerShapedRecipe(new ItemStack(Block.JACK_O_LANTERN, 1), new Object[] { "A", "B", Character.valueOf('A'), Block.PUMPKIN, Character.valueOf('B'), Block.TORCH});
        this.registerShapedRecipe(new ItemStack(Item.STORAGE_MINECART, 1), new Object[] { "A", "B", Character.valueOf('A'), Block.CHEST, Character.valueOf('B'), Item.MINECART});
        this.registerShapedRecipe(new ItemStack(Item.POWERED_MINECART, 1), new Object[] { "A", "B", Character.valueOf('A'), Block.FURNACE, Character.valueOf('B'), Item.MINECART});
        this.registerShapedRecipe(new ItemStack(Item.MINECART_TNT, 1), new Object[] { "A", "B", Character.valueOf('A'), Block.TNT, Character.valueOf('B'), Item.MINECART});
        this.registerShapedRecipe(new ItemStack(Item.MINECART_HOPPER, 1), new Object[] { "A", "B", Character.valueOf('A'), Block.HOPPER, Character.valueOf('B'), Item.MINECART});
        this.registerShapedRecipe(new ItemStack(Item.BOAT, 1), new Object[] { "# #", "###", Character.valueOf('#'), Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Item.BUCKET, 1), new Object[] { "# #", " # ", Character.valueOf('#'), Item.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Item.FLOWER_POT, 1), new Object[] { "# #", " # ", Character.valueOf('#'), Item.CLAY_BRICK});
        this.registerShapedRecipe(new ItemStack(Item.FLINT_AND_STEEL, 1), new Object[] { "A ", " B", Character.valueOf('A'), Item.IRON_INGOT, Character.valueOf('B'), Item.FLINT});
        this.registerShapedRecipe(new ItemStack(Item.BREAD, 1), new Object[] { "###", Character.valueOf('#'), Item.WHEAT});
        this.registerShapedRecipe(new ItemStack(Block.WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Block.WOOD, 1, 0)});
        this.registerShapedRecipe(new ItemStack(Block.BIRCH_WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Block.WOOD, 1, 2)});
        this.registerShapedRecipe(new ItemStack(Block.SPRUCE_WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Block.WOOD, 1, 1)});
        this.registerShapedRecipe(new ItemStack(Block.JUNGLE_WOOD_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), new ItemStack(Block.WOOD, 1, 3)});
        this.registerShapedRecipe(new ItemStack(Item.FISHING_ROD, 1), new Object[] { "  #", " #X", "# X", Character.valueOf('#'), Item.STICK, Character.valueOf('X'), Item.STRING});
        this.registerShapedRecipe(new ItemStack(Item.CARROT_STICK, 1), new Object[] { "# ", " X", Character.valueOf('#'), Item.FISHING_ROD, Character.valueOf('X'), Item.CARROT}).c();
        this.registerShapedRecipe(new ItemStack(Block.COBBLESTONE_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Block.COBBLESTONE});
        this.registerShapedRecipe(new ItemStack(Block.BRICK_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Block.BRICK});
        this.registerShapedRecipe(new ItemStack(Block.STONE_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Block.SMOOTH_BRICK});
        this.registerShapedRecipe(new ItemStack(Block.NETHER_BRICK_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Block.NETHER_BRICK});
        this.registerShapedRecipe(new ItemStack(Block.SANDSTONE_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Block.SANDSTONE});
        this.registerShapedRecipe(new ItemStack(Block.QUARTZ_STAIRS, 4), new Object[] { "#  ", "## ", "###", Character.valueOf('#'), Block.QUARTZ_BLOCK});
        this.registerShapedRecipe(new ItemStack(Item.PAINTING, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Item.STICK, Character.valueOf('X'), Block.WOOL});
        this.registerShapedRecipe(new ItemStack(Item.ITEM_FRAME, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Item.STICK, Character.valueOf('X'), Item.LEATHER});
        this.registerShapedRecipe(new ItemStack(Item.GOLDEN_APPLE, 1, 0), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Item.GOLD_INGOT, Character.valueOf('X'), Item.APPLE});
        this.registerShapedRecipe(new ItemStack(Item.GOLDEN_APPLE, 1, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Block.GOLD_BLOCK, Character.valueOf('X'), Item.APPLE});
        this.registerShapedRecipe(new ItemStack(Item.CARROT_GOLDEN, 1, 0), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Item.GOLD_NUGGET, Character.valueOf('X'), Item.CARROT});
        this.registerShapedRecipe(new ItemStack(Item.SPECKLED_MELON, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Item.GOLD_NUGGET, Character.valueOf('X'), Item.MELON});
        this.registerShapedRecipe(new ItemStack(Block.LEVER, 1), new Object[] { "X", "#", Character.valueOf('#'), Block.COBBLESTONE, Character.valueOf('X'), Item.STICK});
        this.registerShapedRecipe(new ItemStack(Block.TRIPWIRE_SOURCE, 2), new Object[] { "I", "S", "#", Character.valueOf('#'), Block.WOOD, Character.valueOf('S'), Item.STICK, Character.valueOf('I'), Item.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Block.REDSTONE_TORCH_ON, 1), new Object[] { "X", "#", Character.valueOf('#'), Item.STICK, Character.valueOf('X'), Item.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Item.DIODE, 1), new Object[] { "#X#", "III", Character.valueOf('#'), Block.REDSTONE_TORCH_ON, Character.valueOf('X'), Item.REDSTONE, Character.valueOf('I'), Block.STONE});
        this.registerShapedRecipe(new ItemStack(Item.REDSTONE_COMPARATOR, 1), new Object[] { " # ", "#X#", "III", Character.valueOf('#'), Block.REDSTONE_TORCH_ON, Character.valueOf('X'), Item.QUARTZ, Character.valueOf('I'), Block.STONE});
        this.registerShapedRecipe(new ItemStack(Item.WATCH, 1), new Object[] { " # ", "#X#", " # ", Character.valueOf('#'), Item.GOLD_INGOT, Character.valueOf('X'), Item.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Item.COMPASS, 1), new Object[] { " # ", "#X#", " # ", Character.valueOf('#'), Item.IRON_INGOT, Character.valueOf('X'), Item.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Item.MAP_EMPTY, 1), new Object[] { "###", "#X#", "###", Character.valueOf('#'), Item.PAPER, Character.valueOf('X'), Item.COMPASS});
        this.registerShapedRecipe(new ItemStack(Block.STONE_BUTTON, 1), new Object[] { "#", Character.valueOf('#'), Block.STONE});
        this.registerShapedRecipe(new ItemStack(Block.WOOD_BUTTON, 1), new Object[] { "#", Character.valueOf('#'), Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Block.STONE_PLATE, 1), new Object[] { "##", Character.valueOf('#'), Block.STONE});
        this.registerShapedRecipe(new ItemStack(Block.WOOD_PLATE, 1), new Object[] { "##", Character.valueOf('#'), Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Block.IRON_PLATE, 1), new Object[] { "##", Character.valueOf('#'), Item.IRON_INGOT});
        this.registerShapedRecipe(new ItemStack(Block.GOLD_PLATE, 1), new Object[] { "##", Character.valueOf('#'), Item.GOLD_INGOT});
        this.registerShapedRecipe(new ItemStack(Block.DISPENSER, 1), new Object[] { "###", "#X#", "#R#", Character.valueOf('#'), Block.COBBLESTONE, Character.valueOf('X'), Item.BOW, Character.valueOf('R'), Item.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Block.DROPPER, 1), new Object[] { "###", "# #", "#R#", Character.valueOf('#'), Block.COBBLESTONE, Character.valueOf('R'), Item.REDSTONE});
        this.registerShapedRecipe(new ItemStack(Block.PISTON, 1), new Object[] { "TTT", "#X#", "#R#", Character.valueOf('#'), Block.COBBLESTONE, Character.valueOf('X'), Item.IRON_INGOT, Character.valueOf('R'), Item.REDSTONE, Character.valueOf('T'), Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Block.PISTON_STICKY, 1), new Object[] { "S", "P", Character.valueOf('S'), Item.SLIME_BALL, Character.valueOf('P'), Block.PISTON});
        this.registerShapedRecipe(new ItemStack(Item.BED, 1), new Object[] { "###", "XXX", Character.valueOf('#'), Block.WOOL, Character.valueOf('X'), Block.WOOD});
        this.registerShapedRecipe(new ItemStack(Block.ENCHANTMENT_TABLE, 1), new Object[] { " B ", "D#D", "###", Character.valueOf('#'), Block.OBSIDIAN, Character.valueOf('B'), Item.BOOK, Character.valueOf('D'), Item.DIAMOND});
        this.registerShapedRecipe(new ItemStack(Block.ANVIL, 1), new Object[] { "III", " i ", "iii", Character.valueOf('I'), Block.IRON_BLOCK, Character.valueOf('i'), Item.IRON_INGOT});
        this.registerShapelessRecipe(new ItemStack(Item.EYE_OF_ENDER, 1), new Object[] { Item.ENDER_PEARL, Item.BLAZE_POWDER});
        this.registerShapelessRecipe(new ItemStack(Item.FIREBALL, 3), new Object[] { Item.SULPHUR, Item.BLAZE_POWDER, Item.COAL});
        this.registerShapelessRecipe(new ItemStack(Item.FIREBALL, 3), new Object[] { Item.SULPHUR, Item.BLAZE_POWDER, new ItemStack(Item.COAL, 1, 1)});
        this.registerShapedRecipe(new ItemStack(Block.DAYLIGHT_DETECTOR), new Object[] { "GGG", "QQQ", "WWW", Character.valueOf('G'), Block.GLASS, Character.valueOf('Q'), Item.QUARTZ, Character.valueOf('W'), Block.WOOD_STEP});
        this.registerShapedRecipe(new ItemStack(Block.HOPPER), new Object[] { "I I", "ICI", " I ", Character.valueOf('I'), Item.IRON_INGOT, Character.valueOf('C'), Block.CHEST});
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

        if (i == 2 && itemstack.id == itemstack1.id && itemstack.count == 1 && itemstack1.count == 1 && Item.byId[itemstack.id].usesDurability()) {
            Item item = Item.byId[itemstack.id];
            int k = item.getMaxDurability() - itemstack.j();
            int l = item.getMaxDurability() - itemstack1.j();
            int i1 = k + l + item.getMaxDurability() * 5 / 100;
            int j1 = item.getMaxDurability() - i1;

            if (j1 < 0) {
                j1 = 0;
            }

            // CraftBukkit start - Construct a dummy repair recipe
            ItemStack result = new ItemStack(itemstack.id, 1, j1);
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
