package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.util.MCUtil;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Cat;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperCatTypeRegistryEntry implements CatTypeRegistryEntry {

    protected ClientAsset.@Nullable ResourceTexture clientTextureAsset;
    protected ClientAsset.@Nullable ResourceTexture babyClientTextureAsset;
    protected SpawnPrioritySelectors spawnConditions;

    public PaperCatTypeRegistryEntry(
        final Conversions ignoredConversions,
        final @Nullable CatVariant internal
    ) {
        if (internal == null) {
            this.spawnConditions = SpawnPrioritySelectors.EMPTY;
            return;
        }

        this.clientTextureAsset = internal.assetInfo(false);
        this.babyClientTextureAsset = internal.assetInfo(true);
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public ClientTextureAsset clientTextureAsset() {
        return MCUtil.toTextureAsset(asConfigured(this.clientTextureAsset, "clientTextureAsset"));
    }

    @Override
    public ClientTextureAsset babyClientTextureAsset() {
        return MCUtil.toTextureAsset(asConfigured(this.babyClientTextureAsset, "babyClientTextureAsset"));
    }

    public static final class PaperBuilder extends PaperCatTypeRegistryEntry implements Builder, PaperRegistryBuilder<CatVariant, Cat.Type> {

        public PaperBuilder(final Conversions conversions, final @Nullable CatVariant internal) {
            super(conversions, internal);
        }

        @Override
        public Builder clientTextureAsset(final ClientTextureAsset clientTextureAsset) {
            this.clientTextureAsset = MCUtil.toResourceTexture(asArgument(clientTextureAsset, "clientTextureAsset"));
            return this;
        }

        @Override
        public Builder babyClientTextureAsset(final ClientTextureAsset babyClientTextureAsset) {
            this.babyClientTextureAsset = MCUtil.toResourceTexture(asArgument(babyClientTextureAsset, "babyClientTextureAsset"));
            return this;
        }

        @Override
        public CatVariant build() {
            return new CatVariant(
                asConfigured(this.clientTextureAsset, "clientTextureAsset"),
                asConfigured(this.babyClientTextureAsset, "babyClientTextureAsset"),
                asConfigured(this.spawnConditions, "spawnConditions")
            );
        }
    }
}
