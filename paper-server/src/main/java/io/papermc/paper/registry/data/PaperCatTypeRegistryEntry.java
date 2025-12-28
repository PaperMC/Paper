package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.data.variant.PaperSpawnConditions;
import io.papermc.paper.registry.data.variant.SpawnConditionPriority;
import io.papermc.paper.util.MCUtil;
import java.util.List;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.feline.CatVariant;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.Cat;
import org.jetbrains.annotations.Unmodifiable;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperCatTypeRegistryEntry implements CatTypeRegistryEntry {

    protected final Conversions conversions;
    protected ClientAsset.@Nullable ResourceTexture clientTextureAsset;
    protected SpawnPrioritySelectors spawnConditions;

    public PaperCatTypeRegistryEntry(
        final Conversions conversions,
        final @Nullable CatVariant internal
    ) {
        this.conversions = conversions;
        if (internal == null) {
            this.spawnConditions = SpawnPrioritySelectors.EMPTY;
            return;
        }

        this.clientTextureAsset = internal.assetInfo();
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public ClientTextureAsset clientTextureAsset() {
        return MCUtil.toTextureAsset(asConfigured(this.clientTextureAsset, "clientTextureAsset"));
    }

    @Override
    public @Unmodifiable List<SpawnConditionPriority> spawnConditions() {
        return PaperSpawnConditions.fromNms(this.spawnConditions);
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
        public Builder spawnConditions(final List<SpawnConditionPriority> spawnConditions) {
            this.spawnConditions = PaperSpawnConditions.fromApi(asArgument(spawnConditions, "spawnConditions"), this.conversions);
            return this;
        }

        @Override
        public CatVariant build() {
            return new CatVariant(
                asConfigured(this.clientTextureAsset, "clientTextureAsset"),
                asConfigured(this.spawnConditions, "spawnConditions")
            );
        }
    }
}
