package io.papermc.paper.datacomponent.item;

import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.TypedKey;
import io.papermc.paper.registry.data.util.Conversions;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Holder;
import org.bukkit.craftbukkit.enchantments.CraftEnchantment;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.enchantments.Enchantment;

import static io.papermc.paper.util.BoundChecker.requireRange;

public record PaperItemEnchantments(
    net.minecraft.world.item.enchantment.ItemEnchantments impl
) implements ItemEnchantments, Handleable<net.minecraft.world.item.enchantment.ItemEnchantments> {

    // API values are converted when requested, as the enchantment registry might not be loaded yet
    private static <K> Map<K, Integer> convert(final net.minecraft.world.item.enchantment.ItemEnchantments itemEnchantments, final Function<Holder<net.minecraft.world.item.enchantment.Enchantment>, K> keyConverter) {
        if (itemEnchantments.isEmpty()) {
            return Collections.emptyMap();
        }
        final Map<K, Integer> map = new HashMap<>(itemEnchantments.size());
        for (final Object2IntMap.Entry<Holder<net.minecraft.world.item.enchantment.Enchantment>> entry : itemEnchantments.entrySet()) {
            map.put(keyConverter.apply(entry.getKey()), entry.getIntValue());
        }
        return Collections.unmodifiableMap(map); // TODO look into making a "transforming" map maybe?
    }

    @Override
    public Map<Enchantment, Integer> enchantments() {
        return convert(this.impl, CraftEnchantment::minecraftHolderToBukkit);
    }

    @Override
    public Map<TypedKey<Enchantment>, Integer> enchantmentKeys() {
        // enchantments on items can only be references to registered enchantments
        return convert(this.impl, holder -> PaperRegistries.fromNms(holder.unwrapKey().orElseThrow()));
    }

    @Override
    public net.minecraft.world.item.enchantment.ItemEnchantments getHandle() {
        return this.impl;
    }

    static final class BuilderImpl implements ItemEnchantments.Builder {

        private final Object2IntMap<Holder<net.minecraft.world.item.enchantment.Enchantment>> enchantments = new Object2IntOpenHashMap<>();

        @Override
        public ItemEnchantments.Builder add(final Enchantment enchantment, final int level) {
            return this.add(CraftEnchantment.bukkitToMinecraftHolder(enchantment), level);
        }

        @Override
        public ItemEnchantments.Builder add(final TypedKey<Enchantment> enchantment, final int level) {
            return this.add(Conversions.current().getReferenceHolder(enchantment), level);
        }

        private ItemEnchantments.Builder add(final Holder<net.minecraft.world.item.enchantment.Enchantment> enchantment, final int level) {
            this.enchantments.put(enchantment, requireRange(level, "level", 1, net.minecraft.world.item.enchantment.Enchantment.MAX_LEVEL));
            return this;
        }

        @Override
        public ItemEnchantments.Builder addAll(final Map<Enchantment, Integer> enchantments) {
            enchantments.forEach(this::add);
            return this;
        }

        @Override
        public ItemEnchantments.Builder addAllKeys(final Map<TypedKey<Enchantment>, Integer> enchantments) {
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
            this.enchantments.forEach(mutable::set);
            return new PaperItemEnchantments(mutable.toImmutable());
        }
    }
}
