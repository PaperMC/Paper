package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Holder;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.enchantments.Enchantment;

public record PaperItemEnchantments(
    net.minecraft.world.item.enchantment.ItemEnchantments impl,
    Map<Enchantment, Integer> enchantments // API values are stored externally as the concept of a lazy key transformer map does not make much sense
) implements ItemEnchantments, Handleable<net.minecraft.world.item.enchantment.ItemEnchantments> {

    public PaperItemEnchantments(final net.minecraft.world.item.enchantment.ItemEnchantments itemEnchantments) {
        this(itemEnchantments, convert(itemEnchantments));
    }

    private static Map<Enchantment, Integer> convert(final net.minecraft.world.item.enchantment.ItemEnchantments itemEnchantments) {
        if (itemEnchantments.isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<Enchantment, Integer> map = new HashMap<>(itemEnchantments.size());
        for (final Object2IntMap.Entry<Holder<net.minecraft.world.item.enchantment.Enchantment>> entry : itemEnchantments.entrySet()) {
            map.put(CraftEnchantment.minecraftHolderToBukkit(entry.getKey()), entry.getIntValue());
        }
        return Collections.unmodifiableMap(map); // TODO look into making a "transforming" map maybe?
    }

    @Override
    public net.minecraft.world.item.enchantment.ItemEnchantments getHandle() {
        return this.impl;
    }

    static final class BuilderImpl implements ItemEnchantments.Builder {

        private final Map<Enchantment, Integer> enchantments = new Object2ObjectOpenHashMap<>();

        @Override
        public ItemEnchantments.Builder add(final Enchantment enchantment, final int level) {
            Preconditions.checkArgument(
                level >= 1 && level <= net.minecraft.world.item.enchantment.Enchantment.MAX_LEVEL,
                "level must be between %s and %s, was %s",
                1, net.minecraft.world.item.enchantment.Enchantment.MAX_LEVEL,
                level
            );
            this.enchantments.put(enchantment, level);
            return this;
        }

        @Override
        public ItemEnchantments.Builder addAll(final Map<Enchantment, Integer> enchantments) {
            enchantments.forEach(this::add);
            return this;
        }

        @Override
        public ItemEnchantments build() {
            final net.minecraft.world.item.enchantment.ItemEnchantments initialEnchantments = net.minecraft.world.item.enchantment.ItemEnchantments.EMPTY;
            if (this.enchantments.isEmpty()) {
                return new PaperItemEnchantments(initialEnchantments);
            }

            final net.minecraft.world.item.enchantment.ItemEnchantments.Mutable mutable = new net.minecraft.world.item.enchantment.ItemEnchantments.Mutable(initialEnchantments);
            this.enchantments.forEach((enchantment, level) ->
                mutable.set(CraftEnchantment.bukkitToMinecraftHolder(enchantment), level)
            );
            return new PaperItemEnchantments(mutable.toImmutable());
        }
    }
}
