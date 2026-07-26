package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.util.MCUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import net.minecraft.world.effect.MobEffectInstance;
import org.bukkit.Color;
import org.bukkit.craftbukkit.potion.CraftPotionType;
import org.bukkit.craftbukkit.potion.CraftPotionUtil;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

public record PaperPotionContents(
    net.minecraft.world.item.alchemy.PotionContents impl
) implements PotionContents, Handleable<net.minecraft.world.item.alchemy.PotionContents> {

    @Override
    public net.minecraft.world.item.alchemy.PotionContents getHandle() {
        return this.impl;
    }

    @Override
    public @Unmodifiable List<PotionEffect> customEffects() {
        return MCUtil.transformUnmodifiable(this.impl.customEffects(), CraftPotionUtil::toBukkit);
    }

    @Override
    public @Nullable PotionType potion() {
        return this.impl.potion()
            .map(CraftPotionType::minecraftHolderToBukkit)
            .orElse(null);
    }

    @Override
    public @Nullable Color customColor() {
        return this.impl.customColor()
            .map(Color::fromARGB) // alpha channel is supported for tipped arrows, so let's just leave it in
            .orElse(null);
    }

    @Override
    public @Nullable String customName() {
        return this.impl.customName().orElse(null);
    }

    @Override
    public @Unmodifiable List<PotionEffect> allEffects() {
        //noinspection SimplifyStreamApiCallChains - explicitly want it unmodifiable, as toList() api doesnt guarantee this.
        return StreamSupport.stream(this.impl.getAllEffects().spliterator(), false)
            .map(CraftPotionUtil::toBukkit)
            .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Color computeEffectiveColor() {
        return Color.fromARGB(this.impl.getColor());
    }

    static final class BuilderImpl implements PotionContents.Builder {

        private final List<MobEffectInstance> customEffects = new ObjectArrayList<>();
        private @Nullable PotionType type;
        private @Nullable Color color;
        private @Nullable String customName;

        @Override
        public PotionContents.Builder potion(final @Nullable PotionType type) {
            this.type = type;
            return this;
        }

        @Override
        public PotionContents.Builder customColor(final @Nullable Color color) {
            this.color = color;
            return this;
        }

        @Override
        public Builder customName(final @Nullable String name) {
            Preconditions.checkArgument(name == null || name.length() <= Short.MAX_VALUE, "Custom name is longer than %s characters", Short.MAX_VALUE);
            this.customName = name;
            return this;
        }

        @Override
        public PotionContents.Builder addCustomEffect(final PotionEffect effect) {
            this.customEffects.add(CraftPotionUtil.fromBukkit(effect));
            return this;
        }

        @Override
        public PotionContents.Builder addCustomEffects(final List<PotionEffect> effects) {
            effects.forEach(this::addCustomEffect);
            return this;
        }

        @Override
        public PotionContents build() {
            if (this.type == null && this.color == null && this.customEffects.isEmpty() && this.customName == null) {
                return new PaperPotionContents(net.minecraft.world.item.alchemy.PotionContents.EMPTY);
            }

            return new PaperPotionContents(new net.minecraft.world.item.alchemy.PotionContents(
                Optional.ofNullable(this.type).map(CraftPotionType::bukkitToMinecraftHolder),
                Optional.ofNullable(this.color).map(Color::asARGB),
                new ObjectArrayList<>(this.customEffects),
                Optional.ofNullable(this.customName)
            ));
        }
    }
}
