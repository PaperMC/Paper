package io.papermc.testplugin;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Registers a spread of recipes exercising the full {@link RecipeChoice} matrix
 * (regular / {@link RecipeChoice.MaterialChoice} / {@link RecipeChoice.ItemTypeChoice} /
 * {@link RecipeChoice.ExactChoice} / {@link RecipeChoice.PredicateChoice}) solo and combined, across
 * shapeless, shaped and furnace recipes — plus a {@code /recipeitems} command that hands you a kit
 * with everything needed to craft each one in-game.
 *
 * <p>Result items are named {@code R# ...} so you can see which recipe produced them.
 */
public final class RecipeTests {

    private RecipeTests() {
    }

    /** The exact stack used by exact-choice recipes; the /recipeitems kit hands you matching copies. */
    private static ItemStack recipeKey() {
        final ItemStack key = ItemStack.of(Material.DIAMOND);
        key.editMeta(meta -> meta.displayName(Component.text("Recipe Key", NamedTextColor.AQUA)
            .decoration(TextDecoration.ITALIC, false)));
        return key;
    }

    private static ItemStack result(final String name) {
        final ItemStack result = ItemStack.of(Material.NETHER_STAR);
        result.editMeta(meta -> meta.displayName(Component.text(name, NamedTextColor.GOLD)
            .decoration(TextDecoration.ITALIC, false)));
        return result;
    }

    public static void register(final JavaPlugin plugin) {
        registerRecipes(plugin);
        registerCommand(plugin);
    }

    private static void registerRecipes(final JavaPlugin plugin) {
        // R1 — shapeless: PredicateChoice + regular sharing an item type (the integration fix).
        //      Two diamonds: one feeds the predicate slot, one the regular slot.
        final ShapelessRecipe r1 = new ShapelessRecipe(new NamespacedKey(plugin, "r1_predicate_and_regular"),
            result("R1 Predicate+Regular"));
        r1.addIngredient(RecipeChoice.predicateChoice(stack -> stack.getType() == Material.DIAMOND, ItemStack.of(Material.DIAMOND)));
        r1.addIngredient(Material.DIAMOND);
        plugin.getServer().addRecipe(r1);

        // R2 — shapeless: PredicateChoice solo (any pickaxe).
        final ShapelessRecipe r2 = new ShapelessRecipe(new NamespacedKey(plugin, "r2_any_pickaxe"),
            result("R2 Any Pickaxe"));
        r2.addIngredient(RecipeChoice.predicateChoice(stack -> stack.getType().name().endsWith("_PICKAXE"), ItemStack.of(Material.IRON_PICKAXE)));
        plugin.getServer().addRecipe(r2);

        // R3 — shapeless: ExactChoice solo (only the named "Recipe Key" diamond matches).
        final ShapelessRecipe r3 = new ShapelessRecipe(new NamespacedKey(plugin, "r3_exact"),
            result("R3 Exact"));
        r3.addIngredient(RecipeChoice.exactChoice(recipeKey()));
        plugin.getServer().addRecipe(r3);

        // R4 — shapeless: ItemTypeChoice solo (oak or birch planks).
        final ShapelessRecipe r4 = new ShapelessRecipe(new NamespacedKey(plugin, "r4_item_type"),
            result("R4 ItemType"));
        r4.addIngredient(RecipeChoice.itemType(ItemType.OAK_PLANKS, ItemType.BIRCH_PLANKS));
        plugin.getServer().addRecipe(r4);

        // R5 — shapeless: PredicateChoice + ExactChoice (emerald via predicate, named diamond via exact).
        final ShapelessRecipe r5 = new ShapelessRecipe(new NamespacedKey(plugin, "r5_predicate_and_exact"),
            result("R5 Predicate+Exact"));
        r5.addIngredient(RecipeChoice.predicateChoice(stack -> stack.getType() == Material.EMERALD, ItemStack.of(Material.EMERALD)));
        r5.addIngredient(RecipeChoice.exactChoice(recipeKey()));
        plugin.getServer().addRecipe(r5);

        // R6 — shaped 2x2: every choice type at once.
        //      P = predicate(stone), E = exact(key), I = itemType(planks), R = regular stick.
        final ShapedRecipe r6 = new ShapedRecipe(new NamespacedKey(plugin, "r6_shaped_matrix"),
            result("R6 Shaped Matrix"));
        r6.shape("PE", "IR");
        r6.setIngredient('P', RecipeChoice.predicateChoice(stack -> stack.getType() == Material.STONE, ItemStack.of(Material.STONE)));
        r6.setIngredient('E', RecipeChoice.exactChoice(recipeKey()));
        r6.setIngredient('I', RecipeChoice.itemType(ItemType.OAK_PLANKS, ItemType.BIRCH_PLANKS));
        r6.setIngredient('R', Material.STICK);
        plugin.getServer().addRecipe(r6);

        // R7 — furnace: PredicateChoice input (any raw iron).
        final FurnaceRecipe r7 = new FurnaceRecipe(new NamespacedKey(plugin, "r7_furnace_predicate"),
            result("R7 Furnace Predicate"),
            RecipeChoice.predicateChoice(stack -> stack.getType() == Material.RAW_IRON, ItemStack.of(Material.RAW_IRON)),
            0.1f, 200);
        plugin.getServer().addRecipe(r7);

        // R8 — shapeless: obsolete MaterialChoice (cobblestone or stone) still works.
        final ShapelessRecipe r8 = new ShapelessRecipe(new NamespacedKey(plugin, "r8_material_choice"),
            result("R8 MaterialChoice"));
        r8.addIngredient(new RecipeChoice.MaterialChoice(Material.COBBLESTONE, Material.STONE));
        plugin.getServer().addRecipe(r8);

        // R9 — shapeless: two PredicateChoice slots drawing from the same pool.
        final ShapelessRecipe r9 = new ShapelessRecipe(new NamespacedKey(plugin, "r9_two_predicates"),
            result("R9 Two Predicates"));
        r9.addIngredient(RecipeChoice.predicateChoice(stack -> stack.getType() == Material.STONE, ItemStack.of(Material.STONE)));
        r9.addIngredient(RecipeChoice.predicateChoice(stack -> stack.getType() == Material.STONE, ItemStack.of(Material.STONE)));
        plugin.getServer().addRecipe(r9);
    }

    private static void registerCommand(final JavaPlugin plugin) {
        plugin.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->
            event.registrar().register(
                Commands.literal("recipeitems")
                    .executes(ctx -> {
                        if (ctx.getSource().getExecutor() instanceof final Player player) {
                            giveKit(player);
                            player.sendMessage(Component.text("Gave the recipe-test kit. Craft the R1..R9 recipes; "
                                + "results are named so you can tell which worked.", NamedTextColor.GREEN));
                        } else {
                            ctx.getSource().getSender().sendMessage(Component.text("Run this as a player.", NamedTextColor.RED));
                        }
                        return Command.SINGLE_SUCCESS;
                    })
                    .build()
            )
        );
    }

    private static void giveKit(final Player player) {
        final ItemStack keys = recipeKey();
        keys.setAmount(8); // R3, R5, R6 exact inputs (all identical "Recipe Key" diamonds)

        player.getInventory().addItem(
            ItemStack.of(Material.DIAMOND, 8),      // R1 (predicate + regular)
            ItemStack.of(Material.IRON_PICKAXE),    // R2 (any pickaxe)
            keys,                                   // R3 / R5 / R6 exact "Recipe Key"
            ItemStack.of(Material.OAK_PLANKS, 16),  // R4 / R6 itemType
            ItemStack.of(Material.EMERALD, 8),      // R5 predicate
            ItemStack.of(Material.STONE, 32),       // R6 / R8 / R9
            ItemStack.of(Material.STICK, 8),        // R6 regular
            ItemStack.of(Material.RAW_IRON, 8),     // R7 furnace input
            ItemStack.of(Material.COAL, 8),         // furnace fuel
            ItemStack.of(Material.COBBLESTONE, 8)   // R8 MaterialChoice
        );
    }
}
