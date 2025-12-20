package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.data.variant.PaperSpawnConditions;
import io.papermc.paper.registry.data.variant.SpawnConditionPriority;
import io.papermc.paper.util.MCUtil;
import java.util.List;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.wolf.WolfVariant;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Wolf;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperWolfVariantRegistryEntry implements WolfVariantRegistryEntry {

    protected final Conversions conversions;
    protected ClientAsset.@Nullable ResourceTexture angryClientTextureAsset;
    protected ClientAsset.@Nullable ResourceTexture wildClientTextureAsset;
    protected ClientAsset.@Nullable ResourceTexture tameClientTextureAsset;
    protected SpawnPrioritySelectors spawnConditions;

    public PaperWolfVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable WolfVariant internal
    ) {
        this.conversions = conversions;
        if (internal == null) {
            this.spawnConditions = SpawnPrioritySelectors.EMPTY;
            return;
        }

        this.angryClientTextureAsset = internal.assetInfo().angry();
        this.wildClientTextureAsset = internal.assetInfo().wild();
        this.tameClientTextureAsset = internal.assetInfo().tame();
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public ClientTextureAsset angryClientTextureAsset() {
        return MCUtil.toTextureAsset(asConfigured(this.angryClientTextureAsset, "angryClientTextureAsset"));
    }

    @Override
    public ClientTextureAsset wildClientTextureAsset() {
        return MCUtil.toTextureAsset(asConfigured(this.wildClientTextureAsset, "wildClientTextureAsset"));
    }

    @Override
    public ClientTextureAsset tameClientTextureAsset() {
        return MCUtil.toTextureAsset(asConfigured(this.tameClientTextureAsset, "tameClientTextureAsset"));
    }

    @Override
    public @Unmodifiable List<SpawnConditionPriority> spawnConditions() {
        return PaperSpawnConditions.fromNms(this.spawnConditions);
    }

    public static final class PaperBuilder extends PaperWolfVariantRegistryEntry implements Builder, PaperRegistryBuilder<WolfVariant, Wolf.Variant> {

        public PaperBuilder(final Conversions conversions, final @Nullable WolfVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder angryClientTextureAsset(final ClientTextureAsset angryClientTextureAsset) {
            this.angryClientTextureAsset = MCUtil.toResourceTexture(asArgument(angryClientTextureAsset, "angryClientTextureAsset"));
            return this;
        }

        @Override
        public Builder wildClientTextureAsset(final ClientTextureAsset wildClientTextureAsset) {
            this.wildClientTextureAsset = MCUtil.toResourceTexture(asArgument(wildClientTextureAsset, "wildClientTextureAsset"));
            return this;
        }

        @Override
        public Builder tameClientTextureAsset(final ClientTextureAsset tameClientTextureAsset) {
            this.tameClientTextureAsset = MCUtil.toResourceTexture(asArgument(tameClientTextureAsset, "tameClientTextureAsset"));
            return this;
        }

        @Override
        public Builder spawnConditions(final List<SpawnConditionPriority> spawnConditions) {
            this.spawnConditions = PaperSpawnConditions.fromApi(asArgument(spawnConditions, "spawnConditions"), this.conversions);
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
                asConfigured(this.spawnConditions, "spawnConditions")
            );
        }
    }
}
