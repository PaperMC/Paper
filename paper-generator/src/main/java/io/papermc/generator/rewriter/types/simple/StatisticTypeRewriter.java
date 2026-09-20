package io.papermc.generator.rewriter.types.simple;

import io.papermc.generator.registry.RegistryEntries;
import io.papermc.generator.rewriter.types.registry.RegistryFieldRewriter;
import io.papermc.generator.utils.Formatting;
import io.papermc.paper.statistic.CustomStatistic;
import io.papermc.paper.statistic.StatisticType;
import java.util.Comparator;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.stats.StatType;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import org.bukkit.block.BlockType;
import org.bukkit.inventory.ItemType;

public class StatisticTypeRewriter extends RegistryFieldRewriter<StatType<?>> {

    private static final Map<Class<?>, Class<?>> BRIDGE = Map.of(
        Item.class, ItemType.class,
        Block.class, BlockType.class,
        EntityType.class, org.bukkit.entity.EntityType.class,
        Identifier.class, CustomStatistic.class
    );

    private static final Map<ResourceKey<StatType<?>>, String> FIELD_NAMES = RegistryEntries.byRegistryKey(Registries.STAT_TYPE).getFieldNames();

    public StatisticTypeRewriter() {
        super(Registries.STAT_TYPE, "get");
    }

    @Override
    protected Comparator<? super Holder.Reference<StatType<?>>> comparator() {
        return Formatting.alphabeticKeyOrder(reference -> FIELD_NAMES.get(reference.key()));
    }

    @Override
    protected String rewriteFieldType(Holder.Reference<StatType<?>> reference) {
        final Class<?> genericType = RegistryEntries.byRegistryKey(reference.value().getRegistry().key()).elementClass();
        if (!BRIDGE.containsKey(genericType)) {
            throw new IllegalStateException("Unable to translate stat type generic " + genericType.getCanonicalName() + " into the api!");
        }

        return "%s<%s>".formatted(StatisticType.class.getSimpleName(), this.importCollector.getShortName(BRIDGE.get(genericType)));
    }

    @Override
    protected String rewriteFieldName(Holder.Reference<StatType<?>> reference) {
        return FIELD_NAMES.get(reference.key());
    }
}
