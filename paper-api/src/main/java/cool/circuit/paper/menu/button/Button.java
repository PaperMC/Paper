package cool.circuit.paper.menu.button;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public final class Button {
    private final String displayName;
    private final List<String> lore;
    private final Material material;
    private final boolean glowing;
    private final int slot;

    /**
     * Constructs a new Button.
     *
     * @param displayName The name displayed on the button.
     * @param lore The lore text displayed below the button.
     * @param material The material representing the button's item.
     * @param glowing If true, the button will have an enchantment glint (glow effect).
     * @param slot The slot position of the button in the menu.
     */
    public Button(@NotNull final String displayName, @NotNull final List<String> lore, @NotNull final Material material, final boolean glowing, final int slot) {
        this.displayName = displayName;
        this.lore = lore;
        this.material = material;
        this.glowing = glowing;
        this.slot = slot;
    }

    /**
     * Gets the display name of the button.
     *
     * @return The display name of the button.
     */
    public @NotNull String getDisplayName() {
        return displayName;
    }

    /**
     * Gets the lore of the button.
     *
     * @return A list of strings representing the lore text of the button.
     */
    public @NotNull List<String> getLore() {
        return lore;
    }

    /**
     * Gets the material of the button.
     *
     * @return The Material representing the button's item.
     */
    public @NotNull Material getMaterial() {
        return material;
    }

    /**
     * Checks if the button should have a glowing effect (enchantment glint).
     *
     * @return True if the button should have an enchantment glint; otherwise, false.
     */
    public boolean isGlowing() {
        return glowing;
    }

    /**
     * Gets the ItemStack representing the button.
     *
     * @return The ItemStack representing the button, including the material, display name, lore, and glowing effect.
     */
    public @NotNull ItemStack getItem() {
        final ItemStack item = new ItemStack(material);

        final ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(displayName);
        meta.setLore(lore);
        meta.setEnchantmentGlintOverride(glowing);

        item.setItemMeta(meta);
        return item;
    }

    /**
     * Gets the slot position of the button in the menu.
     *
     * @return The slot position where the button should be placed in the menu.
     */
    public int getSlot() {
        return slot;
    }
}
