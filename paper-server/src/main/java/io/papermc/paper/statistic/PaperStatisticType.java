package io.papermc.paper.statistic;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import io.papermc.paper.registry.HolderableBase;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.RegistryKey;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.entity.CraftEntityType;
import org.bukkit.entity.EntityType;

public class PaperStatisticType<S extends Keyed, M> extends HolderableBase<StatType<M>> implements StatisticType<S> {

    private final Supplier<RegistryKey<S>> registryKey;
    private final Map<S, Statistic<S>> statCacheMap;
    private final Predicate<S> typeCheck;
    private final Function<S, M> bukkitToMinecraft;
    private final BiFunction<M, ResourceKey<? extends Registry<M>>, S> minecraftToBukkit;

    private PaperStatisticType(final Holder<StatType<M>> holder) {
        this(holder, $ -> true, CraftRegistry::bukkitToMinecraft, CraftRegistry::minecraftToBukkit);
    }

    private PaperStatisticType(final Holder<StatType<M>> holder, final Predicate<S> typeCheck, final Function<S, M> bukkitToMinecraft, final BiFunction<M, ResourceKey<? extends Registry<M>>, S> minecraftToBukkit) {
        super(holder);
        this.bukkitToMinecraft = bukkitToMinecraft;
        this.minecraftToBukkit = minecraftToBukkit;
        this.registryKey = Suppliers.memoize(() -> PaperRegistries.registryFromNms(this.getHandle().getRegistry().key()));
        this.statCacheMap = new IdentityHashMap<>(); // identity cause keys are registry objects
        this.typeCheck = typeCheck;
    }

    @SuppressWarnings("unchecked")
    public static <M> StatisticType<?> create(final Holder<?> holder) {
        // don't call .value() here, it's unbound
        if (holder.is(Identifier.withDefaultNamespace("killed")) || holder.is(Identifier.withDefaultNamespace("killed_by"))) {
            return new PaperStatisticType<>(
                (Holder<StatType<net.minecraft.world.entity.EntityType<?>>>) holder,
                t -> t != EntityType.UNKNOWN,
                CraftEntityType::bukkitToMinecraft,
                (entityType, $) -> CraftEntityType.minecraftToBukkit(entityType)
            );
        } else {
            return new PaperStatisticType<>((Holder<StatType<M>>) holder);
        }
    }

    public static <S extends Keyed, M> PaperStatisticType<S, M> minecraftToBukkit(final StatType<M> minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.STAT_TYPE);
    }

    public static <S, M> StatType<M> bukkitToMinecraft(final StatisticType<S> bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public Statistic<S> convertStat(final Stat<M> stat) {
        return this.forValue(this.minecraftToBukkit.apply(stat.getValue(), this.getHandle().getRegistry().key()));
    }

    @Override
    public Statistic<S> forValue(final S value) {
        if (!this.typeCheck.test(value)) {
            throw new IllegalArgumentException(value + " is not valid for stat type " + this.getKey());
        }
        return this.statCacheMap.computeIfAbsent(value, newValue -> {
            final M nmsValue = this.bukkitToMinecraft.apply(newValue);
            final Stat<M> stat = this.getHandle().get(nmsValue);
            return new PaperStatistic<>(stat, newValue, this);
        });
    }

    @Override
    public RegistryKey<S> registryKey() {
        return this.registryKey.get();
    }

    @Override
    public String translationKey() {
        Preconditions.checkArgument(this != StatisticTypes.CUSTOM, this.getKey() + " does not have a translation key, see CustomStatistic#translationKey()");
        return "stat_type." + this.getKey().toString().replace(':', '.');
    }
}
