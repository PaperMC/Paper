package io.papermc.paper.item;

import com.destroystokyo.paper.profile.CraftPlayerProfile;
import com.destroystokyo.paper.profile.PlayerProfile;
import java.util.UUID;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.inventory.CraftItemFactory;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.support.environment.AllFeatures;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

// TODO: This should technically be used to compare legacy meta vs the newly implemented
@AllFeatures
public class MetaComparisonTest {

    private static final ItemFactory FACTORY = CraftItemFactory.instance();

    @Test
    public void testMetaApplication() {
        ItemStack itemStack = new ItemStack(Material.STONE);

        ItemMeta meta = itemStack.getItemMeta();
        meta.setCustomModelData(1);

        ItemMeta converted = FACTORY.asMetaFor(meta, Material.GOLD_INGOT);
        Assertions.assertEquals(converted.getCustomModelData(), meta.getCustomModelData());

        ItemMeta convertedAdvanced = FACTORY.asMetaFor(meta, Material.PLAYER_HEAD);
        Assertions.assertEquals(convertedAdvanced.getCustomModelData(), meta.getCustomModelData());
    }

    @Test
    public void testMetaApplicationDowngrading() {
        ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
        PlayerProfile profile = Bukkit.createProfile("Owen1212055");

        SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
        meta.setPlayerProfile(profile);

        SkullMeta converted = (SkullMeta) FACTORY.asMetaFor(meta, Material.PLAYER_HEAD);
        Assertions.assertEquals(converted.getPlayerProfile(), meta.getPlayerProfile());

        SkullMeta downgraded = (SkullMeta) FACTORY.asMetaFor(FACTORY.asMetaFor(meta, Material.STONE), Material.PLAYER_HEAD);
        Assertions.assertNull(downgraded.getPlayerProfile());
    }

    @Test
    public void testMetaApplicationDowngradingPotion() {
        ItemStack itemStack = new ItemStack(Material.POTION);
        Color color = Color.BLUE;

        PotionMeta meta = (PotionMeta) itemStack.getItemMeta();
        meta.setColor(color);

        PotionMeta converted = (PotionMeta) FACTORY.asMetaFor(meta, Material.POTION);
        Assertions.assertEquals(converted.getColor(), color);

        PotionMeta downgraded = (PotionMeta) FACTORY.asMetaFor(FACTORY.asMetaFor(meta, Material.STONE), Material.POTION);
        Assertions.assertNull(downgraded.getColor());
    }

    @Test
    public void testNullMeta() {
        ItemStack itemStack = new ItemStack(Material.AIR);

        Assertions.assertFalse(itemStack.hasItemMeta());
        Assertions.assertNull(itemStack.getItemMeta());
    }

    @Test
    public void testPotionMeta() {
        PotionEffect potionEffect = new PotionEffect(PotionEffectType.SPEED, 10, 10, false);
        ItemStack nmsItemStack = new ItemStack(Material.POTION, 1);

        testSetAndGet(nmsItemStack,
            (meta) -> ((PotionMeta) meta).addCustomEffect(potionEffect, true),
            (meta) -> Assertions.assertEquals(potionEffect, ((PotionMeta) meta).getCustomEffects().getFirst())
        );
    }

    @Test
    public void testEnchantment() {
        ItemStack stack = new ItemStack(Material.STICK, 1);

        testSetAndGet(stack,
            (meta) -> Assertions.assertTrue(meta.addEnchant(Enchantment.SHARPNESS, 1, true)),
            (meta) -> Assertions.assertEquals(1, meta.getEnchantLevel(Enchantment.SHARPNESS))
        );
    }

    @Test
    @Disabled
    public void testPlayerHead() {
        PlayerProfile profile = new CraftPlayerProfile(UUID.randomUUID(), "Owen1212055");
        ItemStack stack = new ItemStack(Material.PLAYER_HEAD, 1);

        testSetAndGet(stack,
            (meta) -> ((SkullMeta) meta).setPlayerProfile(profile),
            (meta) -> {
                Assertions.assertTrue(((SkullMeta) meta).hasOwner());
                Assertions.assertEquals(profile, ((SkullMeta) meta).getPlayerProfile());
            }
        );

        testSetAndGet(stack,
            (meta) -> ((SkullMeta) meta).setOwner("Owen1212055"),
            (meta) -> {
                Assertions.assertTrue(((SkullMeta) meta).hasOwner());
                Assertions.assertEquals("Owen1212055", ((SkullMeta) meta).getOwner());
            }
        );
    }

    @Test
    public void testBookMetaAuthor() {
        ItemStack stack = new ItemStack(Material.WRITTEN_BOOK, 1);

        // Legacy string
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).setAuthor("Owen1212055"),
            (meta) -> Assertions.assertEquals("Owen1212055", ((BookMeta) meta).getAuthor())
        );

        // Component Colored
        Component coloredName = Component.text("Owen1212055", NamedTextColor.DARK_GRAY);
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).author(coloredName),
            (meta) -> Assertions.assertEquals(coloredName, ((BookMeta) meta).author())
        );

        // Simple text
        Component name = Component.text("Owen1212055");
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).author(name),
            (meta) -> Assertions.assertEquals(name, ((BookMeta) meta).author())
        );
    }

    @Test
    public void testBookMetaTitle() {
        ItemStack stack = new ItemStack(Material.WRITTEN_BOOK, 1);

        // Legacy string
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).setTitle("Owen1212055"),
            (meta) -> Assertions.assertEquals("Owen1212055", ((BookMeta) meta).getTitle())
        );

        // Component Colored
        Component coloredName = Component.text("Owen1212055", NamedTextColor.DARK_GRAY);
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).title(coloredName),
            (meta) -> Assertions.assertEquals(coloredName, ((BookMeta) meta).title())
        );

        // Simple text
        Component name = Component.text("Owen1212055");
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).title(name),
            (meta) -> Assertions.assertEquals(name, ((BookMeta) meta).title())
        );
    }


    @Test
    public void testWriteableBookPages() {
        ItemStack stack = new ItemStack(Material.WRITABLE_BOOK, 1);

        // Writeable books are serialized as plain text, but has weird legacy color support.
        // So, we need to test to make sure that all works here.

        // Legacy string
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).addPage("Owen1212055"),
            (meta) -> Assertions.assertEquals("Owen1212055", ((BookMeta) meta).getPage(1))
        );

        // Legacy string colored
        String translatedLegacy = ChatColor.translateAlternateColorCodes('&', "&7Owen1212055");
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).addPage(translatedLegacy),
            (meta) -> Assertions.assertEquals(translatedLegacy, ((BookMeta) meta).getPage(1))
        );

        // Component Colored
        Component coloredName = Component.text("Owen1212055", NamedTextColor.DARK_GRAY);
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).addPages(coloredName),
            (meta) -> Assertions.assertEquals(coloredName, ((BookMeta) meta).page(1))
        );

        // Simple text
        Component name = Component.text("Owen1212055");
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).addPages(name),
            (meta) -> Assertions.assertEquals(name, ((BookMeta) meta).page(1))
        );

        // Simple text + hover... should NOT be saved
        // As this is plain text
        Component nameWithHover = Component.text("Owen1212055")
            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Hover")));
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).addPages(nameWithHover),
            (meta) -> Assertions.assertEquals(name, ((BookMeta) meta).page(1))
        );
    }

    @Test
    public void testWrittenBookPages() {
        ItemStack stack = new ItemStack(Material.WRITTEN_BOOK, 1);

        // Writeable books are serialized as plain text, but has weird legacy color support.
        // So, we need to test to make sure that all works here.

        // Legacy string
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).addPage("Owen1212055"),
            (meta) -> Assertions.assertEquals("Owen1212055", ((BookMeta) meta).getPage(1))
        );

        // Legacy string colored
        String translatedLegacy = ChatColor.translateAlternateColorCodes('&', "&7Owen1212055");
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).addPage(translatedLegacy),
            (meta) -> Assertions.assertEquals(translatedLegacy, ((BookMeta) meta).getPage(1))
        );

        // Component Colored
        Component coloredName = Component.text("Owen1212055", NamedTextColor.DARK_GRAY);
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).addPages(coloredName),
            (meta) -> Assertions.assertEquals(coloredName, ((BookMeta) meta).page(1))
        );

        // Simple text
        Component name = Component.text("Owen1212055");
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).addPages(name),
            (meta) -> Assertions.assertEquals(name, ((BookMeta) meta).page(1))
        );

        // Simple text + hover... should be saved
        Component nameWithHover = Component.text("Owen1212055")
            .hoverEvent(HoverEvent.hoverEvent(HoverEvent.Action.SHOW_TEXT, Component.text("Hover")));
        testSetAndGet(stack,
            (meta) -> ((BookMeta) meta).addPages(nameWithHover),
            (meta) -> Assertions.assertEquals(nameWithHover, ((BookMeta) meta).page(1))
        );
    }

    private void testSetAndGet(ItemStack itemStack, Consumer<ItemMeta> set, Consumer<ItemMeta> get) {
        ItemMeta craftMeta = CraftItemStack.getItemMeta(CraftItemStack.asNMSCopy(itemStack)); // TODO: This should be converted to use the old meta when this is added.
        ItemMeta paperMeta = CraftItemStack.getItemMeta(CraftItemStack.asNMSCopy(itemStack));
        // Test craft meta
        set.accept(craftMeta);
        get.accept(craftMeta);

        // Test paper meta
        set.accept(paperMeta);
        get.accept(paperMeta);
    }
}
