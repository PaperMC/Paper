package io.papermc.paper.inventory.recipe;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.entity.player.StackedItemContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

@VanillaFeature
class PredicateChoiceMatchingTest {

    private static Ingredient regular(final net.minecraft.world.item.Item item) {
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

    private static ItemStack damaged(final net.minecraft.world.item.Item item, final int damage) {
        final ItemStack stack = new ItemStack(item);
        stack.setDamageValue(damage);
        return stack;
    }

    private record Outcome(boolean canCraftOne, int biggestStack, int effectiveBatch, List<ItemOrExact> itemsUsed) {}

    private static Outcome run(final List<Ingredient> ingredients, final List<ItemStack> inventory) {
        final StackedItemContents contents = new StackedItemContents();
        final PlacementInfo placementInfo = PlacementInfo.create(ingredients);
        final Recipe<?> recipe = mock(Recipe.class);
        when(recipe.placementInfo()).thenReturn(placementInfo);

        contents.initializeExtras(recipe, null);
        for (final ItemStack stack : inventory) {
            contents.accountSimpleStack(stack);
        }

        final boolean canCraftOne = contents.canCraft(recipe, null);
        final int biggest = contents.getBiggestCraftableStack(recipe, null);

        final List<ItemOrExact> itemsUsed = new ArrayList<>();
        int effectiveBatch = 0;
        if (biggest > 0) {
            contents.canCraft(recipe, biggest, itemsUsed::add);
            effectiveBatch = biggest;
            for (final ItemOrExact used : itemsUsed) {
                effectiveBatch = Math.min(effectiveBatch, used.getMaxStackSize());
            }
        }
        return new Outcome(canCraftOne, biggest, effectiveBatch, itemsUsed);
    }

    @Test
    void regularIngredient_countsNormally() {
        final Outcome o = run(List.of(regular(Items.STONE)), List.of(new ItemStack(Items.STONE, 64)));
        assertTrue(o.canCraftOne(), "one stone should satisfy a stone ingredient");
        assertEquals(64, o.biggestStack(), "64 stone -> 64 craftable");
    }

    @Test
    void exactIngredient_matchesByComponents() {
        final ItemStack named = new ItemStack(Items.STONE);
        named.setDamageValue(0);
        final Outcome o = run(List.of(exact(new ItemStack(Items.DIAMOND, 1))), List.of(new ItemStack(Items.DIAMOND, 5)));
        assertTrue(o.canCraftOne());
        assertEquals(5, o.biggestStack());
    }

    @Test
    void predicate_singleIngredient_stackableItems() {
        final Outcome o = run(
            List.of(predicate(s -> s.is(Items.STONE), new ItemStack(Items.STONE))),
            List.of(new ItemStack(Items.STONE, 64))
        );
        assertTrue(o.canCraftOne(), "stone matches the predicate");
        assertEquals(64, o.biggestStack(), "64 stackable matching items -> 64 craftable");
        assertEquals(64, o.effectiveBatch());
    }

    @Test
    void predicate_distinctNonStackingItems() {
        // Two DIFFERENT damaged pickaxes (different components -> distinct Exact keys, maxStack 1).
        final Outcome o = run(
            List.of(predicate(s -> s.is(Items.DIAMOND_PICKAXE), new ItemStack(Items.DIAMOND_PICKAXE))),
            List.of(damaged(Items.DIAMOND_PICKAXE, 10), damaged(Items.DIAMOND_PICKAXE, 20))
        );
        assertTrue(o.canCraftOne(), "either pickaxe can satisfy the single ingredient");
        // Two separate single crafts are possible, but never as one stacked batch (maxStack 1).
        assertEquals(1, o.effectiveBatch(), "distinct non-stacking items cannot be crafted as a batch");
    }

    @Test
    void predicate_identicalNonStackingItems_summing() {
        // Three IDENTICAL damaged pickaxes: same components -> ONE Exact key with count 3,
        // even though they are physically non-stackable (maxStack 1). This is the "it thinks it
        // can stack all the matching items together" case.
        final Outcome o = run(
            List.of(predicate(s -> s.is(Items.DIAMOND_PICKAXE), new ItemStack(Items.DIAMOND_PICKAXE))),
            List.of(damaged(Items.DIAMOND_PICKAXE, 100), damaged(Items.DIAMOND_PICKAXE, 100), damaged(Items.DIAMOND_PICKAXE, 100))
        );
        assertTrue(o.canCraftOne());
        // getBiggestCraftableStack ignores per-item max stack size, so it reports 3 ...
        assertEquals(3, o.biggestStack(), "raw picker sums the identical items");
        // ... but the effective (placeable) batch must clamp to the real max stack size of 1.
        assertEquals(1, o.effectiveBatch(), "cannot actually place a batch bigger than maxStackSize");
    }

    @Test
    void predicateAndRegular_shareItem_shouldStillCraft() {
        // Shapeless recipe: [ any-stone predicate , stone ]. Two stone in inventory is enough:
        // one satisfies the predicate slot, one satisfies the regular slot.
        final Outcome o = run(
            List.of(predicate(s -> s.is(Items.STONE), new ItemStack(Items.STONE)), regular(Items.STONE)),
            List.of(new ItemStack(Items.STONE, 2))
        );
        assertTrue(o.canCraftOne(), "2 stone should satisfy a predicate-stone + regular-stone recipe");
    }

    @Test
    void predicateAndRegular_singleItem_correctlyUncraftable() {
        final Outcome o = run(
            List.of(predicate(s -> s.is(Items.STONE), new ItemStack(Items.STONE)), regular(Items.STONE)),
            List.of(new ItemStack(Items.STONE, 1))
        );
        assertFalse(o.canCraftOne(), "one stone cannot fill both a predicate and a regular slot");
    }

    @Test
    void twoPredicates_sharedPool_halveCorrectly() {
        final Predicate<ItemStack> isStone = s -> s.is(Items.STONE);
        final Outcome o = run(
            List.of(predicate(isStone, new ItemStack(Items.STONE)), predicate(isStone, new ItemStack(Items.STONE))),
            List.of(new ItemStack(Items.STONE, 64))
        );
        assertTrue(o.canCraftOne());
        assertEquals(32, o.biggestStack(), "64 stone across two ingredient slots -> 32 crafts");
    }

    @Test
    void initialize_clearsStalePredicateIngredients() {
        // initialize() rebuilds the extras from a recipe. It clears exactIngredients but must ALSO
        // clear predicateIngredients, otherwise a reused extras map (e.g. a cached
        // CraftingInput#stackedContents whose resetExtras was skipped because amounts was empty)
        // carries a previous recipe's predicates into the next recipe's matching.
        final StackedContents<ItemOrExact> raw = new StackedContents<>();
        final StackedContentsExtrasMap extras = new StackedContentsExtrasMap(raw);

        final Recipe<?> withPredicate = mock(Recipe.class);
        when(withPredicate.placementInfo()).thenReturn(PlacementInfo.create(List.of(
            predicate(s -> s.is(Items.STONE), new ItemStack(Items.STONE))
        )));
        extras.initialize(withPredicate);
        assertEquals(1, extras.predicateIngredients.size(), "the predicate was registered");

        // Re-initialize for a predicate-free recipe, mirroring a reuse where resetExtras did not run.
        final Recipe<?> noPredicate = mock(Recipe.class);
        when(noPredicate.placementInfo()).thenReturn(PlacementInfo.create(List.of(regular(Items.DIRT))));
        extras.initialize(noPredicate);
        assertEquals(0, extras.predicateIngredients.size(), "initialize() must drop the stale predicate");
    }

    @Test
    void twoPredicates_notEnough() {
        final Predicate<ItemStack> isStone = s -> s.is(Items.STONE);
        final Outcome o = run(
            List.of(predicate(isStone, new ItemStack(Items.STONE)), predicate(isStone, new ItemStack(Items.STONE))),
            List.of(new ItemStack(Items.STONE, 1))
        );
        assertFalse(o.canCraftOne(), "one stone cannot fill two predicate slots");
    }

    // ---- lifecycle / leak ----------------------------------------------------------------------

    @Test
    void predicateIngredients_doNotLeakAcrossReuse() {
        // Reuse ONE StackedItemContents across two recipe checks, mirroring how a cached
        // CraftingInput#stackedContents() is reused. initialize() clears exactIngredients but not
        // predicateIngredients, and resetExtras() is skipped when amounts is empty.
        final StackedItemContents contents = new StackedItemContents();

        // Recipe 1: predicate matching DIAMOND. No matching items accounted -> amounts stays empty.
        final Recipe<?> recipe1 = mock(Recipe.class);
        when(recipe1.placementInfo()).thenReturn(PlacementInfo.create(List.of(
            predicate(s -> s.is(Items.DIAMOND), new ItemStack(Items.DIAMOND))
        )));
        contents.initializeExtras(recipe1, null);
        contents.resetExtras(); // amounts empty -> this is a no-op, predicate list NOT cleared
        contents.clear();

        // Recipe 2: a plain regular DIRT recipe, no predicates at all.
        final Recipe<?> recipe2 = mock(Recipe.class);
        when(recipe2.placementInfo()).thenReturn(PlacementInfo.create(List.of(regular(Items.DIRT))));
        contents.initializeExtras(recipe2, null);

        // Account a DIAMOND. For recipe2 it should be irrelevant regular content. If the stale
        // DIAMOND predicate leaked, the diamond gets mis-routed into an Exact key.
        contents.accountSimpleStack(new ItemStack(Items.DIAMOND, 1));
        // Recipe2 needs DIRT which we do not have -> must be uncraftable regardless.
        assertFalse(contents.canCraft(recipe2, null), "a diamond must not craft a dirt recipe");
    }
}
