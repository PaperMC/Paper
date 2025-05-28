package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.Optional;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftEntity;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.craftbukkit.inventory.components.CraftCustomModelDataComponent;
import org.bukkit.craftbukkit.inventory.components.CraftEquippableComponent;
import org.bukkit.craftbukkit.inventory.components.CraftFoodComponent;
import org.bukkit.craftbukkit.inventory.components.CraftJukeboxComponent;
import org.bukkit.craftbukkit.inventory.components.CraftToolComponent;
import org.bukkit.craftbukkit.inventory.components.CraftUseCooldownComponent;
import org.bukkit.craftbukkit.util.CraftLegacy;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemFactory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public final class CraftItemFactory implements ItemFactory {
    static final Color DEFAULT_LEATHER_COLOR = Color.fromRGB(0xA06540);
    private static final CraftItemFactory instance;
    private static final RandomSource randomSource = RandomSource.create();

    static {
        instance = new CraftItemFactory();
        ConfigurationSerialization.registerClass(SerializableMeta.class);
        ConfigurationSerialization.registerClass(CraftCustomModelDataComponent.class);
        ConfigurationSerialization.registerClass(CraftEquippableComponent.class);
        ConfigurationSerialization.registerClass(CraftFoodComponent.class);
        ConfigurationSerialization.registerClass(CraftToolComponent.class);
        ConfigurationSerialization.registerClass(CraftToolComponent.CraftToolRule.class);
        ConfigurationSerialization.registerClass(CraftJukeboxComponent.class);
        ConfigurationSerialization.registerClass(CraftUseCooldownComponent.class);
    }

    private CraftItemFactory() {
    }

    @Override
    public boolean isApplicable(ItemMeta meta, ItemStack itemstack) {
        if (itemstack == null) {
            return false;
        }
        return this.isApplicable(meta, itemstack.getType());
    }

    @Override
    public boolean isApplicable(ItemMeta meta, Material type) {
        type = CraftLegacy.fromLegacy(type); // This may be called from legacy item stacks, try to get the right material
        if (type == null || meta == null) {
            return false;
        }

        Preconditions.checkArgument(meta instanceof CraftMetaItem, "Meta of %s not created by %s", meta.getClass().toString(), CraftItemFactory.class.getName());

        return ((CraftMetaItem) meta).applicableTo(type);
    }

    @Override
    public ItemMeta getItemMeta(Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        return this.getItemMeta(material, null);
    }

    private ItemMeta getItemMeta(Material material, CraftMetaItem meta) {
        material = CraftLegacy.fromLegacy(material); // This may be called from legacy item stacks, try to get the right material

        if (!material.isItem()) {
            // default behavior for none items is a new CraftMetaItem
            return new CraftMetaItem(meta);
        }

        return ((CraftItemType<?>) material.asItemType()).getItemMeta(meta);
    }

    @Override
    public boolean equals(ItemMeta meta1, ItemMeta meta2) {
        if (meta1 == meta2) {
            return true;
        }

        if (meta1 != null) {
            Preconditions.checkArgument(meta1 instanceof CraftMetaItem, "First meta of %s does not belong to %s", meta1.getClass().getName(), CraftItemFactory.class.getName());
        } else {
            return ((CraftMetaItem) meta2).isEmpty();
        }
        if (meta2 != null) {
            Preconditions.checkArgument(meta2 instanceof CraftMetaItem, "Second meta of %s does not belong to %s", meta2.getClass().getName(), CraftItemFactory.class.getName());
        } else {
            return ((CraftMetaItem) meta1).isEmpty();
        }

        return this.equals((CraftMetaItem) meta1, (CraftMetaItem) meta2);
    }

    boolean equals(CraftMetaItem meta1, CraftMetaItem meta2) {
        /*
         * This couldn't be done inside of the objects themselves, else force recursion.
         * This is a fairly clean way of implementing it, by dividing the methods into purposes and letting each method perform its own function.
         *
         * The common and uncommon were split, as both could have variables not applicable to the other, like a skull and book.
         * Each object needs its chance to say "hey wait a minute, we're not equal," but without the redundancy of using the 1.equals(2) && 2.equals(1) checking the 'commons' twice.
         *
         * Doing it this way fills all conditions of the .equals() method.
         */
        return meta1.equalsCommon(meta2) && meta1.notUncommon(meta2) && meta2.notUncommon(meta1);
    }

    public static CraftItemFactory instance() {
        return CraftItemFactory.instance;
    }

    @Override
    public ItemMeta asMetaFor(ItemMeta meta, ItemStack stack) {
        Preconditions.checkArgument(stack != null, "ItemStack stack cannot be null");
        return this.asMetaFor(meta, stack.getType());
    }

    @Override
    public ItemMeta asMetaFor(ItemMeta meta, Material material) {
        Preconditions.checkArgument(material != null, "Material cannot be null");
        Preconditions.checkArgument(meta instanceof CraftMetaItem, "ItemMeta of %s not created by %s", (meta != null ? meta.getClass().toString() : "null"), CraftItemFactory.class.getName());
        return this.getItemMeta(material, (CraftMetaItem) meta);
    }

    @Override
    public Color getDefaultLeatherColor() {
        return CraftItemFactory.DEFAULT_LEATHER_COLOR;
    }

    @Override
    public ItemStack createItemStack(String input) throws IllegalArgumentException {
        try {
            StringReader reader = new StringReader(input);
            ItemParser.ItemResult arg = new ItemParser(CraftRegistry.getMinecraftRegistry()).parse(reader);
            Preconditions.checkArgument(!reader.canRead(), "Trailing input found when parsing ItemStack: %s", input);

            Item item = arg.item().value();
            net.minecraft.world.item.ItemStack nmsItemStack = new net.minecraft.world.item.ItemStack(item);

            DataComponentPatch nbt = arg.components();
            if (nbt != null) {
                nmsItemStack.applyComponents(nbt);
            }

            return CraftItemStack.asCraftMirror(nmsItemStack);
        } catch (CommandSyntaxException ex) {
            throw new IllegalArgumentException("Could not parse ItemStack: " + input, ex);
        }
    }

    @Override
    public Material getSpawnEgg(EntityType type) {
        if (type == EntityType.UNKNOWN) {
            return null;
        }
        net.minecraft.world.entity.EntityType<?> nmsType = CraftEntityType.bukkitToMinecraft(type);
        Item nmsItem = SpawnEggItem.byId(nmsType);

        if (nmsItem == null) {
            return null;
        }

        return CraftItemType.minecraftToBukkit(nmsItem);
    }

    @Override
    public ItemStack enchantItem(Entity entity, ItemStack itemStack, int level, boolean allowTreasures) {
        Preconditions.checkArgument(entity != null, "The entity must not be null");

        return CraftItemFactory.enchantItem(((CraftEntity) entity).getHandle().random, itemStack, level, allowTreasures);
    }

    @Override
    public ItemStack enchantItem(final World world, final ItemStack itemStack, final int level, final boolean allowTreasures) {
        Preconditions.checkArgument(world != null, "The world must not be null");

        return CraftItemFactory.enchantItem(((CraftWorld) world).getHandle().random, itemStack, level, allowTreasures);
    }

    @Override
    public ItemStack enchantItem(final ItemStack itemStack, final int level, final boolean allowTreasures) {
        return CraftItemFactory.enchantItem(CraftItemFactory.randomSource, itemStack, level, allowTreasures);
    }

    private static ItemStack enchantItem(RandomSource source, ItemStack itemStack, int level, boolean allowTreasures) {
        Preconditions.checkArgument(itemStack != null, "ItemStack must not be null");
        Preconditions.checkArgument(!itemStack.getType().isAir(), "ItemStack must not be air");
        itemStack = CraftItemStack.asCraftCopy(itemStack);
        CraftItemStack craft = (CraftItemStack) itemStack;
        RegistryAccess registry = CraftRegistry.getMinecraftRegistry();
        Optional<HolderSet.Named<Enchantment>> optional = (allowTreasures) ? Optional.empty() : registry.lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.IN_ENCHANTING_TABLE);
        return CraftItemStack.asCraftMirror(EnchantmentHelper.enchantItem(source, craft.handle, level, registry, optional));
    }

    @Override
    public net.kyori.adventure.text.event.HoverEvent<net.kyori.adventure.text.event.HoverEvent.ShowItem> asHoverEvent(final ItemStack item, final java.util.function.UnaryOperator<net.kyori.adventure.text.event.HoverEvent.ShowItem> op) {
        Preconditions.checkArgument(item.getAmount() > 0 && item.getAmount() <= Item.ABSOLUTE_MAX_STACK_SIZE, "ItemStack amount must be between 1 and %s but was %s", Item.ABSOLUTE_MAX_STACK_SIZE, item.getAmount());
        return net.kyori.adventure.text.event.HoverEvent.showItem(op.apply(
            net.kyori.adventure.text.event.HoverEvent.ShowItem.showItem(
                item.getType().getKey(),
                item.getAmount(),
                io.papermc.paper.adventure.PaperAdventure.asAdventure(CraftItemStack.unwrap(item).getComponentsPatch())) // unwrap is fine here because the components patch will be safely copied
        ));
    }

    @Override
    public net.kyori.adventure.text.@org.jetbrains.annotations.NotNull Component displayName(@org.jetbrains.annotations.NotNull ItemStack itemStack) {
        return io.papermc.paper.adventure.PaperAdventure.asAdventure(CraftItemStack.asNMSCopy(itemStack).getDisplayName());
    }

    // Paper start - ensure server conversions API
    // TODO: DO WE NEED THIS?
    @Override
    public ItemStack ensureServerConversions(ItemStack item) {
        return CraftItemStack.asCraftMirror(CraftItemStack.asNMSCopy(item));
    }
    // Paper end - ensure server conversions API

    // Paper start - add getI18NDisplayName
    @Override
    public String getI18NDisplayName(ItemStack item) {
        net.minecraft.world.item.ItemStack nms = null;
        if (item instanceof CraftItemStack) {
            nms = ((CraftItemStack) item).handle;
        }
        if (nms == null) {
            nms = CraftItemStack.asNMSCopy(item);
        }

        return nms != null ? nms.getItem().getName(nms).getString() : null;
    }
    // Paper end - add getI18NDisplayName

    // Paper start - bungee hover events
    @Override
    public net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(ItemStack itemStack) {
        throw new UnsupportedOperationException("BungeeCord Chat API does not support data components");
        /*
        net.md_5.bungee.api.chat.ItemTag itemTag = net.md_5.bungee.api.chat.ItemTag.ofNbt(CraftItemStack.asNMSCopy(itemStack).getOrCreateTag().toString());
        return new net.md_5.bungee.api.chat.hover.content.Item(
            itemStack.getType().getKey().toString(),
            itemStack.getAmount(),
            itemTag);
         */
    }

    @Override
    public net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(org.bukkit.entity.Entity entity) {
        return hoverContentOf(entity, org.apache.commons.lang3.StringUtils.isBlank(entity.getCustomName()) ? null : new net.md_5.bungee.api.chat.TextComponent(entity.getCustomName()));
    }

    @Override
    public net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(org.bukkit.entity.Entity entity, String customName) {
        return hoverContentOf(entity, org.apache.commons.lang3.StringUtils.isBlank(customName) ? null : new net.md_5.bungee.api.chat.TextComponent(customName));
    }

    @Override
    public net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(org.bukkit.entity.Entity entity, net.md_5.bungee.api.chat.BaseComponent customName) {
        return new net.md_5.bungee.api.chat.hover.content.Entity(
            entity.getType().getKey().toString(),
            entity.getUniqueId().toString(),
            customName);
    }

    @Override
    public net.md_5.bungee.api.chat.hover.content.Content hoverContentOf(org.bukkit.entity.Entity entity, net.md_5.bungee.api.chat.BaseComponent[] customName) {
        return new net.md_5.bungee.api.chat.hover.content.Entity(
            entity.getType().getKey().toString(),
            entity.getUniqueId().toString(),
            new net.md_5.bungee.api.chat.TextComponent(customName));
    }
    // Paper end - bungee hover events

    // Paper start - old getSpawnEgg API
    // @Override // used to override, upstream added conflicting method, is called via Commodore now
    @Deprecated
    public ItemStack getSpawnEgg0(org.bukkit.entity.EntityType type) {
        if (type == null) {
            return null;
        }
        String typeId = type.getKey().toString();
        net.minecraft.resources.ResourceLocation typeKey = ResourceLocation.parse(typeId);
        net.minecraft.world.entity.EntityType<?> nmsType = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getValue(typeKey);
        net.minecraft.world.item.SpawnEggItem eggItem = net.minecraft.world.item.SpawnEggItem.byId(nmsType);
        return eggItem == null ? null : new net.minecraft.world.item.ItemStack(eggItem).asBukkitMirror();
    }
    // Paper end - old getSpawnEgg API
    // Paper start - enchantWithLevels API
    @Override
    public ItemStack enchantWithLevels(ItemStack itemStack, int levels, boolean allowTreasure, java.util.Random random) {
        return enchantWithLevels(
            itemStack,
            levels,
            allowTreasure
                ? Optional.empty()
                // While IN_ENCHANTING_TABLE is not logically the same as all but TREASURE, the tag is defined as
                // NON_TREASURE, which does contain all enchantments not in the treasure tag.
                // Additionally, the allowTreasure boolean is more intended to configure this method to behave like
                // an enchanting table.
                : net.minecraft.server.MinecraftServer.getServer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).get(EnchantmentTags.IN_ENCHANTING_TABLE),
            random
        );
    }

    @Override
    public ItemStack enchantWithLevels(ItemStack itemStack, int levels, io.papermc.paper.registry.set.RegistryKeySet<org.bukkit.enchantments.Enchantment> keySet, java.util.Random random) {
        return enchantWithLevels(
            itemStack,
            levels,
            Optional.of(
                io.papermc.paper.registry.set.PaperRegistrySets.convertToNms(
                    Registries.ENCHANTMENT,
                    Conversions.global().lookup(),
                    keySet
                )
            ),
            random
        );
    }

    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private ItemStack enchantWithLevels(
        ItemStack itemStack,
        int levels,
        Optional<? extends net.minecraft.core.HolderSet<net.minecraft.world.item.enchantment.Enchantment>> possibleEnchantments,
        java.util.Random random
    ) {
        Preconditions.checkArgument(itemStack != null, "Argument 'itemStack' must not be null");
        Preconditions.checkArgument(!itemStack.isEmpty(), "Argument 'itemStack' cannot be empty");
        Preconditions.checkArgument(random != null, "Argument 'random' must not be null");
        final net.minecraft.world.item.ItemStack internalStack = CraftItemStack.asNMSCopy(itemStack);
        if (internalStack.isEnchanted()) {
            internalStack.set(net.minecraft.core.component.DataComponents.ENCHANTMENTS, net.minecraft.world.item.enchantment.ItemEnchantments.EMPTY);
        }
        final net.minecraft.core.RegistryAccess registryAccess = net.minecraft.server.MinecraftServer.getServer().registryAccess();
        final net.minecraft.world.item.ItemStack enchanted = net.minecraft.world.item.enchantment.EnchantmentHelper.enchantItem(
            new org.bukkit.craftbukkit.util.RandomSourceWrapper(random),
            internalStack,
            levels,
            registryAccess,
            possibleEnchantments
        );
        return CraftItemStack.asCraftMirror(enchanted);
    }
    // Paper end - enchantWithLevels API
}
