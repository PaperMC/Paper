package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientAsset;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.world.entity.animal.frog.FrogVariant;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Frog;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperFrogVariantRegistryEntry implements FrogVariantRegistryEntry {

    protected net.minecraft.core.@Nullable ClientAsset clientAsset;
    protected SpawnPrioritySelectors spawnConditions;

    protected final Conversions conversions;

    public PaperFrogVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable FrogVariant internal
    ) {
        this.conversions = conversions;
        if (internal == null) {
            spawnConditions = SpawnPrioritySelectors.EMPTY;
            return;
        }

        this.clientAsset = internal.assetInfo();
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public ClientAsset clientAsset() {
        return this.conversions.asBukkit(asConfigured(this.clientAsset, "clientAsset"));
    }

    public static final class PaperBuilder extends PaperFrogVariantRegistryEntry implements Builder, PaperRegistryBuilder<FrogVariant, Frog.Variant> {

        public PaperBuilder(final Conversions conversions, final @Nullable FrogVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder clientAsset(final ClientAsset clientAsset) {
            this.clientAsset = this.conversions.asVanilla(asArgument(clientAsset, "clientAsset"));
            return this;
        }

        @Override
        public FrogVariant build() {
            return new FrogVariant(
                asConfigured(this.clientAsset, "clientAsset"),
                asConfigured(this.spawnConditions, "spawnConditions")
            );
        }
    }
}
