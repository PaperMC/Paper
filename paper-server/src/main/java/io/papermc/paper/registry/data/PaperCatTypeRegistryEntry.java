package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientAsset;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.world.entity.animal.CatVariant;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Cat;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperCatTypeRegistryEntry implements CatTypeRegistryEntry {

    protected net.minecraft.core.@Nullable ClientAsset clientAsset;
    protected SpawnPrioritySelectors spawnConditions;

    protected final Conversions conversions;

    public PaperCatTypeRegistryEntry(
        final Conversions conversions,
        final @Nullable CatVariant internal
    ) {
        this.conversions = conversions;
        if (internal == null) {
            this.spawnConditions = SpawnPrioritySelectors.EMPTY;
            return;
        }

        this.clientAsset = internal.assetInfo();
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public ClientAsset clientAsset() {
        return this.conversions.asBukkit(asConfigured(this.clientAsset, "clientAsset"));
    }

    public static final class PaperBuilder extends PaperCatTypeRegistryEntry implements Builder, PaperRegistryBuilder<CatVariant, Cat.Type> {

        public PaperBuilder(final Conversions conversions, final @Nullable CatVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder clientAsset(final ClientAsset clientAsset) {
            this.clientAsset = this.conversions.asVanilla(asArgument(clientAsset, "clientAsset"));
            return this;
        }

        @Override
        public CatVariant build() {
            return new CatVariant(
                asConfigured(this.clientAsset, "clientAsset"),
                asConfigured(this.spawnConditions, "spawnConditions")
            );
        }
    }
}
