package io.papermc.paper.statistic;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.Holderable;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.stats.Stats;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.entity.EntityType;
import org.jspecify.annotations.NonNull;

public class PaperStatisticType<S extends @NonNull Keyed, M> extends HolderableBase<StatType<M>> implements StatisticType<S> {

    private final Supplier<RegistryKey<S>> registryKey;
    private final Map<S, Statistic<S>> statCacheMap;
    private final Predicate<S> typeCheck;

    private PaperStatisticType(final Holder<StatType<M>> holder) {
        this(holder, s -> true);
    }

    private PaperStatisticType(final Holder<StatType<M>> holder, final Predicate<S> typeCheck) {
        super(holder);
        this.registryKey = Suppliers.memoize(() -> PaperRegistries.registryFromNms(this.getHandle().getRegistry().key()));
        this.statCacheMap = new IdentityHashMap<>(); // identity cause keys are registry objects
        this.typeCheck = typeCheck;
    }

    @SuppressWarnings("unchecked")
    public static <M> StatisticType<?> create(final Holder<StatType<?>> holder) {
        // don't call .value() here, its unbound
        if (holder.is(ResourceLocation.withDefaultNamespace("killed")) || holder.is(ResourceLocation.withDefaultNamespace("killed_by"))) {
            return new PaperStatisticType<>((Holder<StatType<net.minecraft.world.entity.EntityType<?>>>) (Holder<?>) holder, t -> t != EntityType.UNKNOWN);
        } else {
            return new PaperStatisticType<>((Holder<StatType<M>>) (Holder<?>) holder);
        }
    }

    public static StatisticType<?> minecraftToBukkit(final StatType<?> minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.STAT_TYPE);
    }

    public static StatType<?> bukkitToMinecraft(final StatisticType<?> bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public Statistic<S> convertStat(final Stat<? extends M> nmsStat) {
        return this.of(CraftRegistry.minecraftToBukkit(nmsStat.getValue(), this.getHandle().getRegistry().key()));
    }

    @Override
    public Statistic<S> of(final S value) {
        if (!this.typeCheck.test(value)) {
            throw new IllegalArgumentException(value + " is not valid for stat type " + this.getKey());
        }
        return this.statCacheMap.computeIfAbsent(
            value, newValue -> {
                final M nmsValue = CraftRegistry.bukkitToMinecraft(value);
                final Stat<M> nmsStat = this.getHandle().get(nmsValue);
                return new PaperStatistic<>(nmsStat, value, nmsValue, this);
            }
        );
    }

    @Override
    public RegistryKey<S> registryKey() {
        return this.registryKey.get();
    }

    @Override
    public String translationKey() {
        Preconditions.checkArgument(this != StatisticType.CUSTOM, this.key() + " does not have a translation key, see CustomStatistic#translationKey()");
        return "stat_type." + this.getKey().toString().replace(':', '.');
    }
}
