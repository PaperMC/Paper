package io.papermc.paper.registry.data;

import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.data.client.ClientTextureAsset;
import io.papermc.paper.registry.data.util.Conversions;
import net.minecraft.core.ClientAsset;
import net.minecraft.world.entity.animal.nautilus.ZombieNautilusVariant;
import net.minecraft.world.entity.variant.ModelAndTexture;
import net.minecraft.world.entity.variant.SpawnPrioritySelectors;
import org.bukkit.entity.ZombieNautilus;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperZombieNautilusVariantRegistryEntry implements ZombieNautilusVariantRegistryEntry {

    protected ZombieNautilusVariant.@Nullable ModelType model;
    protected ClientAsset.@Nullable ResourceTexture clientTextureAsset;
    protected SpawnPrioritySelectors spawnConditions;

    protected final Conversions conversions;

    public PaperZombieNautilusVariantRegistryEntry(
        final Conversions conversions,
        final @Nullable ZombieNautilusVariant internal
    ) {
        this.conversions = conversions;
        if (internal == null) {
            this.spawnConditions = SpawnPrioritySelectors.EMPTY;
            return;
        }

        this.clientTextureAsset = internal.modelAndTexture().asset();
        this.model = internal.modelAndTexture().model();
        this.spawnConditions = internal.spawnConditions();
    }

    @Override
    public ClientTextureAsset clientTextureAsset() {
        return this.conversions.asBukkit(asConfigured(this.clientTextureAsset, "clientTextureAsset"));
    }

    @Override
    public ZombieNautilusVariantRegistryEntry.Model model() {
        return switch (asConfigured(this.model, "model")) {
            case NORMAL -> ZombieNautilusVariantRegistryEntry.Model.NORMAL;
            case WARM -> ZombieNautilusVariantRegistryEntry.Model.WARM;
        };
    }

    public static final class PaperBuilder extends PaperZombieNautilusVariantRegistryEntry implements Builder, PaperRegistryBuilder<ZombieNautilusVariant, ZombieNautilus.Variant> {

        public PaperBuilder(final Conversions conversions, final @Nullable ZombieNautilusVariant internal) {
            super(conversions, internal);
        }

        @Override
        public ZombieNautilusVariantRegistryEntry.Builder clientTextureAsset(final ClientTextureAsset clientTextureAsset) {
            this.clientTextureAsset = this.conversions.asVanilla(asArgument(clientTextureAsset, "clientTextureAsset"));
            return this;
        }

        @Override
        public ZombieNautilusVariantRegistryEntry.Builder model(final ZombieNautilusVariantRegistryEntry.Model model) {
            this.model = switch (asArgument(model, "model")) {
                case NORMAL -> ZombieNautilusVariant.ModelType.NORMAL;
                case WARM -> ZombieNautilusVariant.ModelType.WARM;
            };
            return this;
        }

        @Override
        public ZombieNautilusVariant build() {
            return new ZombieNautilusVariant(
                new ModelAndTexture<>(asConfigured(this.model, "model"), asConfigured(this.clientTextureAsset, "clientTextureAsset")),
                asConfigured(this.spawnConditions, "spawnConditions")
            );
        }
    }
}
