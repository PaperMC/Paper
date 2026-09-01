package io.papermc.paper.inventory.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.PlayerEquipment;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.recipebook.ServerPlaceRecipe;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

@VanillaFeature
class RecipeAutofillPlacementTest {

    private static final boolean CRAFT_ONE = false;
    private static final boolean CRAFT_MAX = true;

    // ---- ingredient builders -------------------------------------------------------------------

    private static Ingredient regular(final Item item) {
        return Ingredient.of(item);
    }

    private static Ingredient exact(final ItemStack... stacks) {
        return Ingredient.ofStacks(List.of(stacks));
    }

    private static Ingredient predicate(final Predicate<ItemStack> pred, final ItemStack example) {
        final Ingredient ingredient = Ingredient.ofStacks(List.of(example));
        ingredient.stackPredicate = pred;
        return ingredient;
    }

    private static ItemStack stack(final Item item, final int count) {
        return new ItemStack(item, count);
    }

    private static ItemStack damaged(final Item item, final int damage) {
        final ItemStack s = new ItemStack(item);
        s.setDamageValue(damage);
        return s;
    }

    private static ItemStack renamed(final Item item, final int count, final String name) {
        final ItemStack s = new ItemStack(item, count);
        s.set(DataComponents.CUSTOM_NAME, net.minecraft.network.chat.Component.literal(name));
        return s;
    }

    private record PlaceResult(RecipeBookMenu.PostPlaceAction action, List<ItemStack> grid, Inventory inventory) {
        boolean placed() {
            return this.action == RecipeBookMenu.PostPlaceAction.NOTHING;
        }

        boolean ghost() {
            return this.action == RecipeBookMenu.PostPlaceAction.PLACE_GHOST_RECIPE;
        }

        int gridCount(final Item item) {
            int total = 0;
            for (final ItemStack s : this.grid) {
                if (s.is(item)) {
                    total += s.getCount();
                }
            }
            return total;
        }

        int inventoryCount(final Item item) {
            int total = 0;
            for (final ItemStack s : this.inventory.getNonEquipmentItems()) {
                if (s.is(item)) {
                    total += s.getCount();
                }
            }
            return total;
        }

        long filledSlots() {
            return this.grid.stream().filter(s -> !s.isEmpty()).count();
        }
    }

    private static PlaceResult autofill(
        final List<Ingredient> ingredients, final int gridW, final int gridH,
        final List<ItemStack> inventory, final boolean useMaxItems
    ) {
        final CraftingRecipe recipe = mock(CraftingRecipe.class);
        when(recipe.placementInfo()).thenReturn(PlacementInfo.create(ingredients));
        return place(recipe, gridW, gridH, inventory, useMaxItems, List.of(), false);
    }

    private static PlaceResult autofillShaped(
        final List<Ingredient> ingredients, final int recipeW, final int recipeH,
        final int gridW, final int gridH, final List<ItemStack> inventory, final boolean useMaxItems
    ) {
        final ShapedRecipe recipe = mock(ShapedRecipe.class);
        when(recipe.getWidth()).thenReturn(recipeW);
        when(recipe.getHeight()).thenReturn(recipeH);
        when(recipe.placementInfo()).thenReturn(PlacementInfo.create(ingredients));
        return place(recipe, gridW, gridH, inventory, useMaxItems, List.of(), false);
    }

    private static PlaceResult autofillPattern(
        final List<Optional<Ingredient>> pattern, final int recipeW, final int recipeH,
        final int gridW, final int gridH, final List<ItemStack> inventory, final boolean useMaxItems
    ) {
        final ShapedRecipe recipe = mock(ShapedRecipe.class);
        when(recipe.getWidth()).thenReturn(recipeW);
        when(recipe.getHeight()).thenReturn(recipeH);
        when(recipe.placementInfo()).thenReturn(PlacementInfo.createFromOptionals(pattern));
        return place(recipe, gridW, gridH, inventory, useMaxItems, List.of(), false);
    }

    private static PlaceResult autofillFurnace(
        final Ingredient ingredient, final List<ItemStack> inventory, final boolean useMaxItems
    ) {
        final Inventory inv = new Inventory(null, new PlayerEquipment(null));
        for (int i = 0; i < inventory.size(); i++) {
            inv.setItem(i, inventory.get(i).copy());
        }

        final SimpleContainer furnace = new SimpleContainer(3); // input=0, fuel=1, result=2
        final Slot inputSlot = new Slot(furnace, 0, 0, 0);
        final Slot resultSlot = new Slot(furnace, 2, 0, 0);
        final List<Slot> clear = List.of(inputSlot, resultSlot);

        final AbstractCookingRecipe recipe = mock(AbstractCookingRecipe.class);
        when(recipe.placementInfo()).thenReturn(PlacementInfo.create(List.of(ingredient)));
        final RecipeHolder<AbstractCookingRecipe> holder = new RecipeHolder<>(null, recipe);

        final ServerPlaceRecipe.CraftingMenuAccess<AbstractCookingRecipe> menu = new ServerPlaceRecipe.CraftingMenuAccess<>() {
            @Override
            public void fillCraftSlotsStackedContents(final StackedItemContents stackedContents) {
                if (!inputSlot.getItem().isEmpty()) {
                    stackedContents.accountSimpleStack(inputSlot.getItem());
                }
            }

            @Override
            public void clearCraftingContent() {
                clear.forEach(s -> s.set(ItemStack.EMPTY));
            }

            @Override
            public boolean recipeMatches(final RecipeHolder<AbstractCookingRecipe> recipe) {
                return false;
            }
        };

        final RecipeBookMenu.PostPlaceAction action = ServerPlaceRecipe.placeRecipe(
            menu, 1, 1, List.of(inputSlot), clear, inv, holder, useMaxItems, true
        );
        return new PlaceResult(action, List.of(inputSlot.getItem()), inv);
    }

    private static PlaceResult place(
        final CraftingRecipe recipe, final int gridW, final int gridH,
        final List<ItemStack> inventory, final boolean useMaxItems,
        final List<ItemStack> presetGrid, final boolean recipeMatches
    ) {
        final Inventory inv = new Inventory(null, new PlayerEquipment(null));
        for (int i = 0; i < inventory.size(); i++) {
            inv.setItem(i, inventory.get(i).copy());
        }

        final SimpleContainer gridContainer = new SimpleContainer(gridW * gridH);
        for (int i = 0; i < presetGrid.size(); i++) {
            gridContainer.setItem(i, presetGrid.get(i).copy());
        }
        final List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < gridW * gridH; i++) {
            slots.add(new Slot(gridContainer, i, 0, 0));
        }

        final RecipeHolder<CraftingRecipe> holder = new RecipeHolder<>(null, recipe);

        final ServerPlaceRecipe.CraftingMenuAccess<CraftingRecipe> menu = new ServerPlaceRecipe.CraftingMenuAccess<>() {
            @Override
            public void fillCraftSlotsStackedContents(final StackedItemContents stackedContents) {
                for (final Slot slot : slots) {
                    if (!slot.getItem().isEmpty()) {
                        stackedContents.accountSimpleStack(slot.getItem());
                    }
                }
            }

            @Override
            public void clearCraftingContent() {
                gridContainer.clearContent();
            }

            @Override
            public boolean recipeMatches(final RecipeHolder<CraftingRecipe> recipe) {
                return recipeMatches;
            }
        };

        final RecipeBookMenu.PostPlaceAction action = ServerPlaceRecipe.placeRecipe(
            menu, gridW, gridH, slots, slots, inv, holder, useMaxItems, true
        );

        final List<ItemStack> gridOut = new ArrayList<>();
        for (final Slot slot : slots) {
            gridOut.add(slot.getItem());
        }
        return new PlaceResult(action, gridOut, inv);
    }

    // ============================================================================================
    // Category A — regular ingredients (baseline; must always pass)
    // ============================================================================================

    @Test
    void regular_single_craftOne() {
        final PlaceResult r = autofill(List.of(regular(Items.STONE)), 1, 1, List.of(stack(Items.STONE, 64)), CRAFT_ONE);
        assertTrue(r.placed());
        assertEquals(1, r.gridCount(Items.STONE), "one placed in the grid");
        assertEquals(63, r.inventoryCount(Items.STONE), "one consumed from inventory");
    }

    @Test
    void regular_single_craftMax_fillsStack() {
        final PlaceResult r = autofill(List.of(regular(Items.STONE)), 1, 1, List.of(stack(Items.STONE, 64)), CRAFT_MAX);
        assertTrue(r.placed());
        assertEquals(64, r.gridCount(Items.STONE), "whole stack moved into the single grid slot");
        assertEquals(0, r.inventoryCount(Items.STONE));
    }

    @Test
    void regular_twoDistinctIngredients_craftMax() {
        final PlaceResult r = autofill(
            List.of(regular(Items.STONE), regular(Items.DIRT)), 2, 1,
            List.of(stack(Items.STONE, 64), stack(Items.DIRT, 64)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(64, r.gridCount(Items.STONE));
        assertEquals(64, r.gridCount(Items.DIRT));
    }

    @Test
    void regular_sharedPool_twoSlotsSameItem_craftMax_halves() {
        // 64 stone across two ingredient slots -> 32 per slot (32 crafts).
        final PlaceResult r = autofill(
            List.of(regular(Items.STONE), regular(Items.STONE)), 2, 1,
            List.of(stack(Items.STONE, 64)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(2, r.filledSlots());
        assertEquals(32, r.grid.get(0).getCount());
        assertEquals(32, r.grid.get(1).getCount());
    }

    @Test
    void regular_notEnough_ghost() {
        final PlaceResult r = autofill(
            List.of(regular(Items.STONE), regular(Items.DIRT)), 2, 1,
            List.of(stack(Items.STONE, 1)), CRAFT_ONE
        );
        assertTrue(r.ghost(), "missing dirt -> ghost recipe, nothing placed");
        assertEquals(0, r.filledSlots());
        assertEquals(1, r.inventoryCount(Items.STONE), "inventory untouched");
    }

    @Test
    void regular_nonStackableItem_craftMax_clampsToOne() {
        // Undamaged pickaxe is usable for crafting; maxStack 1 -> batch clamps to 1.
        final PlaceResult r = autofill(
            List.of(regular(Items.DIAMOND_PICKAXE)), 1, 1,
            List.of(stack(Items.DIAMOND_PICKAXE, 1), stack(Items.DIAMOND_PICKAXE, 1)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(1, r.gridCount(Items.DIAMOND_PICKAXE));
        assertEquals(1, r.inventoryCount(Items.DIAMOND_PICKAXE), "only one pickaxe consumed");
    }

    @Test
    void regular_multiStackInventory_craftMax() {
        // Stone spread across three inventory slots (64+64+32) -> up to 64 into one grid slot.
        final PlaceResult r = autofill(
            List.of(regular(Items.STONE)), 1, 1,
            List.of(stack(Items.STONE, 64), stack(Items.STONE, 64), stack(Items.STONE, 32)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(64, r.gridCount(Items.STONE), "one full stack into the slot");
        assertEquals(96, r.inventoryCount(Items.STONE), "160 - 64 remain");
    }

    @Test
    void regular_fullThreeByThree() {
        final List<Ingredient> ing = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ing.add(regular(Items.STONE));
        }
        final PlaceResult r = autofill(ing, 3, 3, List.of(stack(Items.STONE, 9)), CRAFT_ONE);
        assertTrue(r.placed());
        assertEquals(9, r.filledSlots());
        assertEquals(9, r.gridCount(Items.STONE));
    }

    @Test
    void regular_damagedItem_notUsable_ghost() {
        // Damaged items are excluded from regular recipe-book crafting.
        final PlaceResult r = autofill(
            List.of(regular(Items.DIAMOND_PICKAXE)), 1, 1,
            List.of(damaged(Items.DIAMOND_PICKAXE, 50)), CRAFT_ONE
        );
        assertTrue(r.ghost(), "a damaged pickaxe is not usable for a regular ingredient");
    }

    // ============================================================================================
    // Category B — exact ingredients (baseline; user says these already work)
    // ============================================================================================

    @Test
    void exact_matchesOnlySameComponents() {
        final ItemStack named = renamed(Items.DIAMOND, 1, "Special");
        final PlaceResult r = autofill(
            List.of(exact(named.copy())), 1, 1,
            List.of(stack(Items.DIAMOND, 5), named.copy()), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(1, r.gridCount(Items.DIAMOND), "only the named diamond matches; maxStack irrelevant here");
        // The plain diamonds must be left untouched.
        assertTrue(r.grid.get(0).has(DataComponents.CUSTOM_NAME), "the exact (named) item was placed");
    }

    @Test
    void exact_stackable_craftMax() {
        final PlaceResult r = autofill(
            List.of(exact(stack(Items.DIAMOND, 1))), 1, 1,
            List.of(stack(Items.DIAMOND, 40)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(40, r.gridCount(Items.DIAMOND));
    }

    @Test
    void exact_multipleAllowedStacks() {
        final PlaceResult r = autofill(
            List.of(exact(stack(Items.DIAMOND, 1), stack(Items.EMERALD, 1))), 1, 1,
            List.of(stack(Items.EMERALD, 3)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(3, r.gridCount(Items.EMERALD));
    }

    @Test
    void exact_nonStackable_craftMax_clampsToOne() {
        final ItemStack tool = damaged(Items.DIAMOND_PICKAXE, 7);
        final PlaceResult r = autofill(
            List.of(exact(tool.copy())), 1, 1,
            List.of(tool.copy(), tool.copy()), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(1, r.gridCount(Items.DIAMOND_PICKAXE), "non-stackable exact -> one per slot");
        assertEquals(1, r.inventoryCount(Items.DIAMOND_PICKAXE));
    }

    @Test
    void exact_twoSlotsSharedPool_craftMax_halves() {
        final PlaceResult r = autofill(
            List.of(exact(stack(Items.DIAMOND, 1)), exact(stack(Items.DIAMOND, 1))), 2, 1,
            List.of(stack(Items.DIAMOND, 10)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(5, r.grid.get(0).getCount());
        assertEquals(5, r.grid.get(1).getCount());
    }

    @Test
    void exact_notPresent_ghost() {
        final PlaceResult r = autofill(
            List.of(exact(renamed(Items.DIAMOND, 1, "Special"))), 1, 1,
            List.of(stack(Items.DIAMOND, 5)), CRAFT_ONE
        );
        assertTrue(r.ghost(), "plain diamonds do not satisfy an exact named-diamond ingredient");
    }

    // ============================================================================================
    // Category C — mixed regular + exact (baseline)
    // ============================================================================================

    @Test
    void mixed_regularAndExact_distinctItems() {
        final PlaceResult r = autofill(
            List.of(regular(Items.STICK), exact(stack(Items.DIAMOND, 1))), 2, 1,
            List.of(stack(Items.STICK, 8), stack(Items.DIAMOND, 8)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(8, r.gridCount(Items.STICK));
        assertEquals(8, r.gridCount(Items.DIAMOND));
    }

    // ============================================================================================
    // Category D — predicate ingredients (bug-hunting; document current behaviour)
    // ============================================================================================

    @Test
    void predicate_single_stackable_craftMax() {
        final PlaceResult r = autofill(
            List.of(predicate(s -> s.is(Items.STONE), stack(Items.STONE, 1))), 1, 1,
            List.of(stack(Items.STONE, 64)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(64, r.gridCount(Items.STONE));
    }

    @Test
    void predicate_single_craftOne_pullsMatchingItem() {
        final PlaceResult r = autofill(
            List.of(predicate(s -> s.is(Items.DIAMOND_PICKAXE), stack(Items.DIAMOND_PICKAXE, 1))), 1, 1,
            List.of(damaged(Items.DIAMOND_PICKAXE, 10)), CRAFT_ONE
        );
        assertTrue(r.placed(), "the damaged pickaxe satisfies the predicate even though it is not usable for regular crafting");
        assertEquals(1, r.gridCount(Items.DIAMOND_PICKAXE));
        assertEquals(0, r.inventoryCount(Items.DIAMOND_PICKAXE));
    }

    @Test
    void predicate_distinctNonStacking_craftMax_placesOne() {
        final PlaceResult r = autofill(
            List.of(predicate(s -> s.is(Items.DIAMOND_PICKAXE), stack(Items.DIAMOND_PICKAXE, 1))), 1, 1,
            List.of(damaged(Items.DIAMOND_PICKAXE, 10), damaged(Items.DIAMOND_PICKAXE, 20)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(1, r.gridCount(Items.DIAMOND_PICKAXE), "distinct non-stacking items cannot be batched");
        assertEquals(1, r.inventoryCount(Items.DIAMOND_PICKAXE), "only one consumed");
    }

    @Test
    void predicate_identicalNonStacking_craftMax_clampsToOne() {
        // Same components (same damage) -> one Exact key summing to 3, but maxStack 1 clamps the batch.
        final PlaceResult r = autofill(
            List.of(predicate(s -> s.is(Items.DIAMOND_PICKAXE), stack(Items.DIAMOND_PICKAXE, 1))), 1, 1,
            List.of(damaged(Items.DIAMOND_PICKAXE, 100), damaged(Items.DIAMOND_PICKAXE, 100), damaged(Items.DIAMOND_PICKAXE, 100)),
            CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(1, r.gridCount(Items.DIAMOND_PICKAXE), "clamped to a single non-stackable item");
        assertEquals(2, r.inventoryCount(Items.DIAMOND_PICKAXE));
    }

    @Test
    void predicate_matchesMultipleItems_placesSomethingValid() {
        // Predicate matches any of several stackable items; whichever is chosen must be placed.
        final PlaceResult r = autofill(
            List.of(predicate(s -> s.is(Items.OAK_PLANKS) || s.is(Items.BIRCH_PLANKS), stack(Items.OAK_PLANKS, 1))), 1, 1,
            List.of(stack(Items.BIRCH_PLANKS, 5)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(5, r.gridCount(Items.BIRCH_PLANKS), "the matching birch planks were placed");
    }

    @Test
    void predicate_twoSlots_sharedPool_craftMax_halves() {
        final Predicate<ItemStack> isStone = s -> s.is(Items.STONE);
        final PlaceResult r = autofill(
            List.of(predicate(isStone, stack(Items.STONE, 1)), predicate(isStone, stack(Items.STONE, 1))), 2, 1,
            List.of(stack(Items.STONE, 64)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(32, r.grid.get(0).getCount());
        assertEquals(32, r.grid.get(1).getCount());
    }

    @Test
    void predicate_notEnough_ghost() {
        final Predicate<ItemStack> isStone = s -> s.is(Items.STONE);
        final PlaceResult r = autofill(
            List.of(predicate(isStone, stack(Items.STONE, 1)), predicate(isStone, stack(Items.STONE, 1))), 2, 1,
            List.of(stack(Items.STONE, 1)), CRAFT_ONE
        );
        assertTrue(r.ghost(), "one stone cannot fill two predicate slots");
    }

    // ---- known bugs (kept disabled so the suite stays green; flip when the algorithm is fixed) --

    @Test
    void predicateAndRegular_shareItem_shouldPlace() {
        final PlaceResult r = autofill(
            List.of(predicate(s -> s.is(Items.STONE), stack(Items.STONE, 1)), regular(Items.STONE)), 2, 1,
            List.of(stack(Items.STONE, 2)), CRAFT_ONE
        );
        assertTrue(r.placed(), "2 stone should satisfy predicate-stone + regular-stone");
        assertEquals(2, r.gridCount(Items.STONE));
    }

    // ============================================================================================
    // Category E — more mixed combinations
    // ============================================================================================

    @Test
    void predicateAndExact_distinctItems() {
        final PlaceResult r = autofill(
            List.of(predicate(s -> s.is(Items.STONE), stack(Items.STONE, 1)), exact(stack(Items.DIAMOND, 1))), 2, 1,
            List.of(stack(Items.STONE, 8), stack(Items.DIAMOND, 8)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(8, r.gridCount(Items.STONE));
        assertEquals(8, r.gridCount(Items.DIAMOND));
    }

    @Test
    void predicate_ignoresNonMatchingInventory() {
        // Predicate matches only stone; unrelated dirt in the inventory must be left alone.
        final PlaceResult r = autofill(
            List.of(predicate(s -> s.is(Items.STONE), stack(Items.STONE, 1))), 1, 1,
            List.of(stack(Items.DIRT, 64), stack(Items.STONE, 3)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(3, r.gridCount(Items.STONE));
        assertEquals(64, r.inventoryCount(Items.DIRT), "non-matching dirt untouched");
    }

    @Test
    void exact_leavesNonMatchingComponentsUntouched() {
        final ItemStack special = renamed(Items.DIAMOND, 1, "Special");
        final PlaceResult r = autofill(
            List.of(exact(special.copy())), 1, 1,
            List.of(stack(Items.DIAMOND, 10), special.copy()), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(1, r.gridCount(Items.DIAMOND), "only the named diamond was placed");
        assertEquals(10, r.inventoryCount(Items.DIAMOND), "the 10 plain diamonds are untouched");
    }

    // ============================================================================================
    // Category F — edge cases
    // ============================================================================================

    @Test
    void emptyInventory_ghost() {
        final PlaceResult r = autofill(List.of(regular(Items.STONE)), 1, 1, List.of(), CRAFT_ONE);
        assertTrue(r.ghost());
        assertEquals(0, r.filledSlots());
    }

    @Test
    void fewerIngredientsThanGrid_leavesRemainderEmpty() {
        final PlaceResult r = autofill(
            List.of(regular(Items.STONE), regular(Items.DIRT)), 3, 3,
            List.of(stack(Items.STONE, 1), stack(Items.DIRT, 1)), CRAFT_ONE
        );
        assertTrue(r.placed());
        assertEquals(2, r.filledSlots(), "only two slots filled in a 3x3 grid");
    }

    @Test
    void customMaxStackSizeComponent_regular_clampsBatch() {
        // A stone whose stack was given a MAX_STACK_SIZE=16 component. As a regular (Item-key)
        // ingredient the picker uses the item's DEFAULT max stack; this test locks in current
        // behaviour so a change to that assumption is caught.
        final ItemStack capped = stack(Items.STONE, 16);
        capped.set(DataComponents.MAX_STACK_SIZE, 16);
        final PlaceResult r = autofill(List.of(regular(Items.STONE)), 1, 1, List.of(capped), CRAFT_MAX);
        assertTrue(r.placed());
        assertEquals(16, r.gridCount(Items.STONE), "all 16 available were placed");
    }

    // ============================================================================================
    // Category G — shaped-recipe centering (exercises the ShapedRecipe branch of PlaceRecipeHelper)
    // ============================================================================================

    @Test
    void shaped_twoWide_centeredInThreeByThree() {
        // A 2x1 recipe placed into a 3x3 grid: PlaceRecipeHelper centers it vertically (row 1).
        final PlaceResult r = autofillShaped(
            List.of(regular(Items.STONE), regular(Items.DIRT)), 2, 1, 3, 3,
            List.of(stack(Items.STONE, 1), stack(Items.DIRT, 1)), CRAFT_ONE
        );
        assertTrue(r.placed());
        assertEquals(2, r.filledSlots());
        assertEquals(1, r.gridCount(Items.STONE));
        assertEquals(1, r.gridCount(Items.DIRT));
        // Centered vertically onto the middle row (slots 3 & 4), not the top-left.
        assertTrue(r.grid.get(0).isEmpty(), "top-left slot should be empty after centering");
        assertSame(Items.STONE, r.grid.get(3).getItem());
        assertSame(Items.DIRT, r.grid.get(4).getItem());
    }

    @Test
    void shaped_fullThreeByThree_noCentering() {
        final List<Ingredient> ing = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            ing.add(regular(Items.STONE));
        }
        final PlaceResult r = autofillShaped(ing, 3, 3, 3, 3, List.of(stack(Items.STONE, 9)), CRAFT_ONE);
        assertTrue(r.placed());
        assertEquals(9, r.filledSlots());
    }

    // ============================================================================================
    // Category H — top-up path (recipe already placed in the grid, recipeMatches == true)
    // ============================================================================================

    @Test
    void topUp_existingGrid_addsOneMore() {
        // Grid already holds 1 stone in each of two slots; a matching craft-one should top each to 2.
        final CraftingRecipe recipe = mock(CraftingRecipe.class);
        when(recipe.placementInfo()).thenReturn(PlacementInfo.create(List.of(regular(Items.STONE), regular(Items.STONE))));
        final PlaceResult r = place(
            recipe, 2, 1, List.of(stack(Items.STONE, 64)), CRAFT_ONE,
            List.of(stack(Items.STONE, 1), stack(Items.STONE, 1)), true
        );
        assertTrue(r.placed());
        assertEquals(2, r.grid.get(0).getCount(), "topped up to 2");
        assertEquals(2, r.grid.get(1).getCount());
    }

    // ============================================================================================
    // Category I — shaped recipes WITH gaps (L-shapes, tool shapes, ...)
    // ============================================================================================

    @Test
    void shapedWithGap_placesIntoCorrectCells_leavesGapEmpty() {
        // 2x2 recipe box, bottom-right cell is a gap:  [A B / C .]
        final List<Optional<Ingredient>> pattern = List.of(
            Optional.of(regular(Items.STONE)),   // (0,0)
            Optional.of(regular(Items.DIRT)),    // (1,0)
            Optional.of(regular(Items.DIAMOND)), // (0,1)
            Optional.empty()                     // (1,1) gap
        );
        final PlaceResult r = autofillPattern(
            pattern, 2, 2, 3, 3,
            List.of(stack(Items.STONE, 1), stack(Items.DIRT, 1), stack(Items.DIAMOND, 1)), CRAFT_ONE
        );
        assertTrue(r.placed());
        assertEquals(3, r.filledSlots(), "only the three non-gap cells are filled");
        // 2x2 box is not centered in 3x3, so it maps to grid slots 0,1 (top row) and 3,4 (next row).
        assertSame(Items.STONE, r.grid.get(0).getItem());
        assertSame(Items.DIRT, r.grid.get(1).getItem());
        assertSame(Items.DIAMOND, r.grid.get(3).getItem());
        assertTrue(r.grid.get(4).isEmpty(), "the gap cell (slot 4) stays empty");
    }

    @Test
    void shapedWithGap_notEnoughForOneIngredient_ghost() {
        final List<Optional<Ingredient>> pattern = List.of(
            Optional.of(regular(Items.STONE)),
            Optional.empty(),
            Optional.of(regular(Items.DIRT))
        );
        // 3x1 box [stone, gap, dirt] but no dirt in inventory.
        final PlaceResult r = autofillPattern(pattern, 3, 1, 3, 3, List.of(stack(Items.STONE, 5)), CRAFT_ONE);
        assertTrue(r.ghost());
    }

    // ============================================================================================
    // Category J — furnace / smoker / blast-furnace recipe book (AbstractFurnaceMenu)
    // ============================================================================================

    @Test
    void furnace_regular_craftOne() {
        final PlaceResult r = autofillFurnace(regular(Items.RAW_IRON), List.of(stack(Items.RAW_IRON, 10)), CRAFT_ONE);
        assertTrue(r.placed());
        assertEquals(1, r.gridCount(Items.RAW_IRON), "one raw iron placed into the input slot");
        assertEquals(9, r.inventoryCount(Items.RAW_IRON));
    }

    @Test
    void furnace_regular_craftMax_fillsInputSlot() {
        final PlaceResult r = autofillFurnace(regular(Items.RAW_IRON), List.of(stack(Items.RAW_IRON, 64)), CRAFT_MAX);
        assertTrue(r.placed());
        assertEquals(64, r.gridCount(Items.RAW_IRON), "whole stack moved into the furnace input");
    }

    @Test
    void furnace_exactIngredient() {
        final ItemStack special = renamed(Items.RAW_IRON, 1, "Ore");
        final PlaceResult r = autofillFurnace(exact(special.copy()), List.of(stack(Items.RAW_IRON, 5), special.copy()), CRAFT_MAX);
        assertTrue(r.placed());
        assertEquals(1, r.gridCount(Items.RAW_IRON), "only the exact named ore matches");
        assertTrue(r.grid.get(0).has(DataComponents.CUSTOM_NAME));
    }

    @Test
    void furnace_predicateIngredient() {
        final PlaceResult r = autofillFurnace(
            predicate(s -> s.is(Items.RAW_IRON), stack(Items.RAW_IRON, 1)),
            List.of(stack(Items.RAW_IRON, 3)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(3, r.gridCount(Items.RAW_IRON));
    }

    @Test
    void furnace_notEnough_ghost() {
        final PlaceResult r = autofillFurnace(regular(Items.RAW_IRON), List.of(), CRAFT_ONE);
        assertTrue(r.ghost());
        assertEquals(0, r.filledSlots());
    }

    // ============================================================================================
    // Category K — predicate/exact combined with every other choice type (regression for the fix)
    // ============================================================================================

    @Test
    void exactAndRegular_shareItem_shouldPlace() {
        // The exact-choice analog of the predicate bug: a plain item routed to the Exact pool
        // (because it matches an exact ingredient) must still satisfy a regular ingredient too.
        final PlaceResult r = autofill(
            List.of(exact(stack(Items.STONE, 1)), regular(Items.STONE)), 2, 1,
            List.of(stack(Items.STONE, 2)), CRAFT_ONE
        );
        assertTrue(r.placed(), "2 stone should satisfy exact-stone + regular-stone");
        assertEquals(2, r.gridCount(Items.STONE));
    }

    @Test
    void predicateAndRegular_shareItem_craftMax() {
        final PlaceResult r = autofill(
            List.of(predicate(s -> s.is(Items.STONE), stack(Items.STONE, 1)), regular(Items.STONE)), 2, 1,
            List.of(stack(Items.STONE, 64)), CRAFT_MAX
        );
        assertTrue(r.placed());
        assertEquals(32, r.grid.get(0).getCount(), "64 stone / 2 slots -> 32 per slot");
        assertEquals(32, r.grid.get(1).getCount());
    }

    @Test
    void predicateAndExact_overlappingItem() {
        // A plain diamond matches both the predicate and the exact ingredient; it is pooled once
        // (Exact) and shared by both slots. Two diamonds -> one craft.
        final PlaceResult r = autofill(
            List.of(predicate(s -> s.is(Items.DIAMOND), stack(Items.DIAMOND, 1)), exact(stack(Items.DIAMOND, 1))), 2, 1,
            List.of(stack(Items.DIAMOND, 2)), CRAFT_ONE
        );
        assertTrue(r.placed());
        assertEquals(2, r.gridCount(Items.DIAMOND));
    }

    @Test
    void predicateAndExact_overlappingItem_onlyOne_ghost() {
        final PlaceResult r = autofill(
            List.of(predicate(s -> s.is(Items.DIAMOND), stack(Items.DIAMOND, 1)), exact(stack(Items.DIAMOND, 1))), 2, 1,
            List.of(stack(Items.DIAMOND, 1)), CRAFT_ONE
        );
        assertTrue(r.ghost(), "one diamond cannot fill both overlapping slots");
    }

    @Test
    void predicateAndRegular_renamedItem_regularCannotUseIt() {
        // A renamed stone matches the predicate but is NOT usable for the regular ingredient. With a
        // plain stone available for the regular slot, the recipe still crafts (predicate<-renamed,
        // regular<-plain).
        final PlaceResult r = autofill(
            List.of(predicate(s -> s.is(Items.STONE), stack(Items.STONE, 1)), regular(Items.STONE)), 2, 1,
            List.of(renamed(Items.STONE, 1, "Fancy"), stack(Items.STONE, 1)), CRAFT_ONE
        );
        assertTrue(r.placed());
        assertEquals(2, r.filledSlots());
        assertEquals(2, r.gridCount(Items.STONE));
    }

    @Test
    void predicateAndRegular_onlyRenamedItem_ghost() {
        // Only a renamed stone (matches predicate but not usable for the regular slot) -> cannot fill
        // both slots.
        final PlaceResult r = autofill(
            List.of(predicate(s -> s.is(Items.STONE), stack(Items.STONE, 1)), regular(Items.STONE)), 2, 1,
            List.of(renamed(Items.STONE, 1, "Fancy")), CRAFT_ONE
        );
        assertTrue(r.ghost(), "a renamed stone cannot satisfy the regular slot");
    }

    @Test
    void threeWayMix_predicateExactRegular() {
        final ItemStack named = renamed(Items.DIAMOND, 1, "Gem");
        final PlaceResult r = autofill(
            List.of(
                predicate(s -> s.is(Items.STONE), stack(Items.STONE, 1)),
                exact(named.copy()),
                regular(Items.STICK)
            ), 3, 1,
            List.of(stack(Items.STONE, 1), named.copy(), stack(Items.STICK, 1)), CRAFT_ONE
        );
        assertTrue(r.placed());
        assertEquals(1, r.gridCount(Items.STONE));
        assertEquals(1, r.gridCount(Items.DIAMOND));
        assertEquals(1, r.gridCount(Items.STICK));
    }
}
