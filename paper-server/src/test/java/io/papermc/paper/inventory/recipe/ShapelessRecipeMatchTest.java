package io.papermc.paper.inventory.recipe;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.bukkit.support.environment.VanillaFeature;
import org.junit.jupiter.api.Test;

/**
 * Tests the <b>actual shapeless crafting</b> path: {@link ShapelessRecipe#matches} over a real
 * {@link ShapelessRecipe}. This exercises a different accounting route than the recipe-book placer
 * ({@code CraftingInput} ctor + {@code StackedContentsExtrasMap#accountInput}/{@code regularRemoved}),
 * plus the single-ingredient {@code Ingredient#test} shortcut. The same {@link ShapelessRecipe#matches}
 * is what the Crafter block and normal crafting menus use to decide whether a grid produces a result.
 *
 * <p>Regular/exact ingredients are the must-pass baseline; predicate + combination cases guard the
 * predicate integration fix.
 */
@VanillaFeature
class ShapelessRecipeMatchTest {

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

    private static ItemStack renamed(final Item item, final String name) {
        final ItemStack s = new ItemStack(item);
        s.set(DataComponents.CUSTOM_NAME, net.minecraft.network.chat.Component.literal(name));
        return s;
    }

    private static ShapelessRecipe shapeless(final Ingredient... ingredients) {
        return new ShapelessRecipe(
            new Recipe.CommonInfo(true),
            new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, ""),
            new ItemStackTemplate(Items.DIAMOND), // result is irrelevant to matches()
            List.of(ingredients)
        );
    }

    /** Runs the real matches() over a grid sized to hold exactly the given non-empty items. */
    private static boolean matches(final ShapelessRecipe recipe, final ItemStack... grid) {
        final CraftingInput input = CraftingInput.of(grid.length, 1, List.of(grid));
        return recipe.matches(input, null); // shapeless matching ignores the level
    }

    // ---- regular ingredients (baseline) --------------------------------------------------------

    @Test
    void regular_exactMatch() {
        assertTrue(matches(shapeless(regular(Items.STONE), regular(Items.DIRT)),
            new ItemStack(Items.STONE), new ItemStack(Items.DIRT)));
    }

    @Test
    void regular_orderIndependent() {
        // Shapeless: item order in the grid must not matter.
        assertTrue(matches(shapeless(regular(Items.STONE), regular(Items.DIRT)),
            new ItemStack(Items.DIRT), new ItemStack(Items.STONE)));
    }

    @Test
    void regular_wrongItem_noMatch() {
        assertFalse(matches(shapeless(regular(Items.STONE), regular(Items.DIRT)),
            new ItemStack(Items.STONE), new ItemStack(Items.COBBLESTONE)));
    }

    @Test
    void regular_missingItem_noMatch() {
        assertFalse(matches(shapeless(regular(Items.STONE), regular(Items.DIRT)),
            new ItemStack(Items.STONE), new ItemStack(Items.STONE)));
    }

    @Test
    void regular_extraItem_noMatch() {
        // Three grid items for a two-ingredient recipe -> ingredientCount mismatch.
        assertFalse(matches(shapeless(regular(Items.STONE), regular(Items.DIRT)),
            new ItemStack(Items.STONE), new ItemStack(Items.DIRT), new ItemStack(Items.DIAMOND)));
    }

    @Test
    void regular_duplicateIngredient() {
        assertTrue(matches(shapeless(regular(Items.STONE), regular(Items.STONE)),
            new ItemStack(Items.STONE), new ItemStack(Items.STONE)));
        assertFalse(matches(shapeless(regular(Items.STONE), regular(Items.STONE)),
            new ItemStack(Items.STONE), new ItemStack(Items.DIRT)));
    }

    @Test
    void regular_singleIngredientShortcut() {
        assertTrue(matches(shapeless(regular(Items.STONE)), new ItemStack(Items.STONE)));
        assertFalse(matches(shapeless(regular(Items.STONE)), new ItemStack(Items.DIRT)));
    }

    // ---- exact ingredients ---------------------------------------------------------------------

    @Test
    void exact_matchesOnlySameComponents() {
        assertTrue(matches(shapeless(exact(renamed(Items.DIAMOND, "Special"))), renamed(Items.DIAMOND, "Special")));
        assertFalse(matches(shapeless(exact(renamed(Items.DIAMOND, "Special"))), new ItemStack(Items.DIAMOND)));
    }

    @Test
    void exact_plusRegular() {
        assertTrue(matches(shapeless(exact(renamed(Items.DIAMOND, "Special")), regular(Items.STONE)),
            renamed(Items.DIAMOND, "Special"), new ItemStack(Items.STONE)));
    }

    // ---- predicate ingredients -----------------------------------------------------------------

    @Test
    void predicate_singleShortcut() {
        assertTrue(matches(shapeless(predicate(s -> s.is(Items.STONE), new ItemStack(Items.STONE))), new ItemStack(Items.STONE)));
        assertFalse(matches(shapeless(predicate(s -> s.is(Items.STONE), new ItemStack(Items.STONE))), new ItemStack(Items.DIRT)));
    }

    @Test
    void predicate_twoPredicates() {
        final Predicate<ItemStack> isStone = s -> s.is(Items.STONE);
        assertTrue(matches(shapeless(predicate(isStone, new ItemStack(Items.STONE)), predicate(isStone, new ItemStack(Items.STONE))),
            new ItemStack(Items.STONE), new ItemStack(Items.STONE)));
    }

    @Test
    void predicate_plusRegular_shareItem() {
        // The fix: grid accounting routes both stone to the Exact pool; the regular stone ingredient
        // must still be satisfiable. (This is the actual-crafting analog of the recipe-book bug.)
        assertTrue(matches(shapeless(predicate(s -> s.is(Items.STONE), new ItemStack(Items.STONE)), regular(Items.STONE)),
            new ItemStack(Items.STONE), new ItemStack(Items.STONE)));
    }

    @Test
    void predicate_plusRegular_distinctItems_orderIndependent() {
        final ShapelessRecipe recipe = shapeless(predicate(s -> s.is(Items.DIAMOND), new ItemStack(Items.DIAMOND)), regular(Items.STONE));
        assertTrue(matches(recipe, new ItemStack(Items.DIAMOND), new ItemStack(Items.STONE)));
        assertTrue(matches(recipe, new ItemStack(Items.STONE), new ItemStack(Items.DIAMOND)));
    }

    @Test
    void predicate_plusRegular_noMatchingRegularItem() {
        // grid stone + dirt: dirt matches neither ingredient -> no match.
        assertFalse(matches(shapeless(predicate(s -> s.is(Items.STONE), new ItemStack(Items.STONE)), regular(Items.STONE)),
            new ItemStack(Items.STONE), new ItemStack(Items.DIRT)));
    }

    @Test
    void predicate_plusExact() {
        assertTrue(matches(shapeless(predicate(s -> s.is(Items.DIAMOND), new ItemStack(Items.DIAMOND)), exact(renamed(Items.EMERALD, "Gem"))),
            new ItemStack(Items.DIAMOND), renamed(Items.EMERALD, "Gem")));
    }

    @Test
    void predicate_plusRegular_renamedSatisfiesPredicateOnly() {
        // renamed stone -> predicate slot; plain stone -> regular slot. Order-independent.
        final ShapelessRecipe recipe = shapeless(predicate(s -> s.is(Items.STONE), new ItemStack(Items.STONE)), regular(Items.STONE));
        assertTrue(matches(recipe, renamed(Items.STONE, "Fancy"), new ItemStack(Items.STONE)));
        // Two renamed stones: the regular slot cannot use a renamed (non-usable) stone -> no match.
        assertFalse(matches(recipe, renamed(Items.STONE, "Fancy"), renamed(Items.STONE, "Fancy2")));
    }

    @Test
    void threeWayMix_predicateExactRegular() {
        final ShapelessRecipe recipe = shapeless(
            predicate(s -> s.is(Items.STONE), new ItemStack(Items.STONE)),
            exact(renamed(Items.DIAMOND, "Gem")),
            regular(Items.STICK)
        );
        assertTrue(matches(recipe, new ItemStack(Items.STICK), renamed(Items.DIAMOND, "Gem"), new ItemStack(Items.STONE)));
        assertFalse(matches(recipe, new ItemStack(Items.STICK), new ItemStack(Items.DIAMOND), new ItemStack(Items.STONE)));
    }
}
