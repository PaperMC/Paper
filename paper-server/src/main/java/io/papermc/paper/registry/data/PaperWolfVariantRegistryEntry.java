package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientAsset;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Wolf;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperWolfVariantRegistryEntry implements WolfVariantRegistryEntry {

    protected net.minecraft.core.@Nullable ClientAsset angryClientAsset;
    protected net.minecraft.core.@Nullable ClientAsset wildClientAsset;
    protected net.minecraft.core.@Nullable ClientAsset tameClientAsset;
    protected SpawnPrioritySelectors spawnConditions;

    protected final Conversions conversions;

    public PaperWolfVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable WolfVariant internal
    ) {
        this.conversions = conversions;
        if (internal == null) {
            this.spawnConditions = SpawnPrioritySelectors.EMPTY;
            return;
        }

        this.angryClientAsset = internal.assetInfo().angry();
        this.wildClientAsset = internal.assetInfo().wild();
        this.tameClientAsset = internal.assetInfo().tame();
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public ClientAsset angryClientAsset() {
        return this.conversions.asBukkit(asConfigured(this.angryClientAsset, "angryClientAsset"));
    }

    @Override
    public ClientAsset wildClientAsset() {
        return this.conversions.asBukkit(asConfigured(this.wildClientAsset, "wildClientAsset"));
    }

    @Override
    public ClientAsset tameClientAsset() {
        return this.conversions.asBukkit(asConfigured(this.tameClientAsset, "tameClientAsset"));
    }

    public static final class PaperBuilder extends PaperWolfVariantRegistryEntry implements Builder, PaperRegistryBuilder<WolfVariant, Wolf.Variant> {

        public PaperBuilder(final Conversions conversions, final @Nullable WolfVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder angryClientAsset(final ClientAsset angryClientAsset) {
            this.angryClientAsset = this.conversions.asVanilla(asArgument(angryClientAsset, "angryClientAsset"));
            return this;
        }

        @Override
        public Builder wildClientAsset(final ClientAsset wildClientAsset) {
            this.wildClientAsset = this.conversions.asVanilla(asArgument(wildClientAsset, "wildClientAsset"));
            return this;
        }

        @Override
        public Builder tameClientAsset(final ClientAsset tameClientAsset) {
            this.tameClientAsset = this.conversions.asVanilla(asArgument(tameClientAsset, "tameClientAsset"));
            return this;
        }

        @Override
        public WolfVariant build() {
            return new WolfVariant(
                new WolfVariant.AssetInfo(
                    asConfigured(this.wildClientAsset, "wildClientAsset"),
                    asConfigured(this.tameClientAsset, "tameClientAsset"),
                    asConfigured(this.angryClientAsset, "angryClientAsset")
                ),
                asConfigured(this.spawnConditions, "spawnConditions")
            );
        }
    }
}
