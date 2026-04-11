package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Wolf;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperWolfVariantRegistryEntry implements WolfVariantRegistryEntry {

    protected ClientAsset.@Nullable ResourceTexture angryClientTextureAsset;
    protected ClientAsset.@Nullable ResourceTexture wildClientTextureAsset;
    protected ClientAsset.@Nullable ResourceTexture tameClientTextureAsset;
    protected ClientAsset.@Nullable ResourceTexture babyAngryClientTextureAsset;
    protected ClientAsset.@Nullable ResourceTexture babyWildClientTextureAsset;
    protected ClientAsset.@Nullable ResourceTexture babyTameClientTextureAsset;
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

        this.angryClientTextureAsset = internal.adultInfo().angry();
        this.wildClientTextureAsset = internal.adultInfo().wild();
        this.tameClientTextureAsset = internal.adultInfo().tame();
        this.babyAngryClientTextureAsset = internal.babyInfo().angry();
        this.babyWildClientTextureAsset = internal.babyInfo().wild();
        this.babyTameClientTextureAsset = internal.babyInfo().tame();
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public ClientTextureAsset angryClientTextureAsset() {
        return this.conversions.asBukkit(asConfigured(this.angryClientTextureAsset, "angryClientTextureAsset"));
    }

    @Override
    public ClientTextureAsset wildClientTextureAsset() {
        return this.conversions.asBukkit(asConfigured(this.wildClientTextureAsset, "wildClientTextureAsset"));
    }

    @Override
    public ClientTextureAsset tameClientTextureAsset() {
        return this.conversions.asBukkit(asConfigured(this.tameClientTextureAsset, "tameClientTextureAsset"));
    }

    @Override
    public ClientTextureAsset babyAngryClientTextureAsset() {
        return this.conversions.asBukkit(asConfigured(this.babyAngryClientTextureAsset, "babyAngryClientTextureAsset"));
    }

    @Override
    public ClientTextureAsset babyWildClientTextureAsset() {
        return this.conversions.asBukkit(asConfigured(this.babyWildClientTextureAsset, "babyWildClientTextureAsset"));
    }

    @Override
    public ClientTextureAsset babyTameClientTextureAsset() {
        return this.conversions.asBukkit(asConfigured(this.babyTameClientTextureAsset, "babyTameClientTextureAsset"));
    }

    public static final class PaperBuilder extends PaperWolfVariantRegistryEntry implements Builder, PaperRegistryBuilder<WolfVariant, Wolf.Variant> {

        public PaperBuilder(final Conversions conversions, final @Nullable WolfVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder angryClientTextureAsset(final ClientTextureAsset angryClientTextureAsset) {
            this.angryClientTextureAsset = this.conversions.asVanilla(asArgument(angryClientTextureAsset, "angryClientTextureAsset"));
            return this;
        }

        @Override
        public Builder wildClientTextureAsset(final ClientTextureAsset wildClientTextureAsset) {
            this.wildClientTextureAsset = this.conversions.asVanilla(asArgument(wildClientTextureAsset, "wildClientTextureAsset"));
            return this;
        }

        @Override
        public Builder tameClientTextureAsset(final ClientTextureAsset tameClientTextureAsset) {
            this.tameClientTextureAsset = this.conversions.asVanilla(asArgument(tameClientTextureAsset, "tameClientTextureAsset"));
            return this;
        }

        @Override
        public Builder babyAngryClientTextureAsset(final ClientTextureAsset babyAngryClientTextureAsset) {
            this.babyAngryClientTextureAsset = this.conversions.asVanilla(asArgument(babyAngryClientTextureAsset, "babyAngryClientTextureAsset"));
            return this;
        }

        @Override
        public Builder babyWildClientTextureAsset(final ClientTextureAsset babyWildClientTextureAsset) {
            this.babyWildClientTextureAsset = this.conversions.asVanilla(asArgument(babyWildClientTextureAsset, "babyWildClientTextureAsset"));
            return this;
        }

        @Override
        public Builder babyTameClientTextureAsset(final ClientTextureAsset babyTameClientTextureAsset) {
            this.babyTameClientTextureAsset = this.conversions.asVanilla(asArgument(babyTameClientTextureAsset, "babyTameClientTextureAsset"));
            return this;
        }

        @Override
        public WolfVariant build() {
            return new WolfVariant(
                new WolfVariant.AssetInfo(
                    asConfigured(this.wildClientTextureAsset, "wildClientTextureAsset"),
                    asConfigured(this.tameClientTextureAsset, "tameClientTextureAsset"),
                    asConfigured(this.angryClientTextureAsset, "angryClientTextureAsset")
                ),
                new WolfVariant.AssetInfo(
                    asConfigured(this.babyWildClientTextureAsset, "babyWildClientTextureAsset"),
                    asConfigured(this.babyTameClientTextureAsset, "babyTameClientTextureAsset"),
                    asConfigured(this.babyAngryClientTextureAsset, "babyAngryClientTextureAsset")
                ),
                asConfigured(this.spawnConditions, "spawnConditions")
            );
        }
    }
}
