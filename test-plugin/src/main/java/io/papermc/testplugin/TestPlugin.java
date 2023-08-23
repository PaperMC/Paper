package io.papermc.testplugin;

import io.papermc.paper.event.player.ChatEvent;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.CookingRecipe;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.format.NamedTextColor.GOLD;
import static net.kyori.adventure.text.format.Style.style;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

public final class TestPlugin extends JavaPlugin implements Listener {

    static final NamespacedKey FURNACE_RECIPE_KEY = new NamespacedKey("test", "recipe");
    static final NamespacedKey SHAPED_RECIPE_KEY = new NamespacedKey("test", "shaped_recipe");
    static final NamespacedKey SHAPELESS_RECIPE_KEY = new NamespacedKey("test", "shapless_recipe");

    static final NamespacedKey PDC_KEY = new NamespacedKey("test", "pdc_key");

    static final ItemStack SOME_INGREDIENT = new ItemStack(Material.EMERALD);
    static {
        SOME_INGREDIENT.editMeta(meta -> {
            meta.getPersistentDataContainer().set(PDC_KEY, PersistentDataType.BOOLEAN, true);
            meta.lore(List.of(text("SPECIAL EMERALD", style(ITALIC.withState(false), GOLD))));
        });
    }

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        final ItemStack furnaceInput = new ItemStack(Material.STICK);
        furnaceInput.editMeta(meta -> {
            meta.displayName(text("HELLO"));
        });
        final CookingRecipe<?> recipe = new FurnaceRecipe(FURNACE_RECIPE_KEY, new ItemStack(Material.DIAMOND), new RecipeChoice.ExactChoice(furnaceInput), 0, 20);
        this.getServer().addRecipe(recipe);

        final ShapedRecipe shapedRecipe = new ShapedRecipe(SHAPED_RECIPE_KEY, new ItemStack(Material.NETHER_BRICK));
        shapedRecipe.shape(
            "#$%",
            " @ ",
            " $ "
        );
        shapedRecipe.setIngredient('$', new RecipeChoice.ExactChoice(SOME_INGREDIENT));
        shapedRecipe.setIngredient('#', new RecipeChoice.MaterialChoice(Material.OAK_PLANKS));
        shapedRecipe.setIngredient('%', new RecipeChoice.ExactChoice(new ItemStack(SOME_INGREDIENT.getType())));
        shapedRecipe.setIngredient('@', new RecipeChoice.MaterialChoice(SOME_INGREDIENT.getType()));
        this.getServer().addRecipe(shapedRecipe);

        final ShapelessRecipe shapelessRecipe = new ShapelessRecipe(SHAPELESS_RECIPE_KEY, new ItemStack(Material.DEEPSLATE));
        for (int i = 0; i < 4; i++) {
            shapelessRecipe.addIngredient(new RecipeChoice.ExactChoice(SOME_INGREDIENT));
        }
        shapelessRecipe.addIngredient(new RecipeChoice.MaterialChoice(Material.ICE));
        shapelessRecipe.addIngredient(new RecipeChoice.MaterialChoice(SOME_INGREDIENT.getType()));
        this.getServer().addRecipe(shapelessRecipe);
    }

    @EventHandler
    public void onEvent(PlayerJoinEvent event) {
        event.getPlayer().discoverRecipe(FURNACE_RECIPE_KEY);
        event.getPlayer().discoverRecipe(SHAPED_RECIPE_KEY);
        event.getPlayer().discoverRecipe(SHAPELESS_RECIPE_KEY);
    }

    @EventHandler
    public void onEvent(ChatEvent event) {
        final ItemStack clone = SOME_INGREDIENT.clone();
        clone.setAmount(64);
        event.getPlayer().getInventory().addItem(clone);
    }
}
