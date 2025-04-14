package io.papermc.paper.registry.data;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.PaperRegistryBuilderFactory;
import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.entry.RegistryEntryMeta;
import java.util.function.Consumer;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import org.bukkit.Art;
import org.bukkit.Keyed;
import org.bukkit.craftbukkit.CraftRegistry;

@SuppressWarnings("BoundedWildcard")
public final class InlinedRegistryBuilderProviderImpl implements InlinedRegistryBuilderProvider {

    private static <M, A extends Keyed, B extends PaperRegistryBuilder<M, A>> A create(final ResourceKey<? extends Registry<M>> registryKey, final Consumer<PaperRegistryBuilderFactory<M, A, B>> value) {
        final RegistryEntryMeta.Buildable<M, A, B> buildableMeta = PaperRegistries.getBuildableMeta(registryKey);
        Preconditions.checkArgument(buildableMeta.registryTypeMapper().supportsDirectHolders(), "Registry type mapper must support direct holders");
        final PaperRegistryBuilderFactory<M, A, B> builderFactory = new PaperRegistryBuilderFactory<>(Conversions.global(), buildableMeta.builderFiller(), CraftRegistry.getMinecraftRegistry(buildableMeta.mcKey())::getValue);
        value.accept(builderFactory);
        return buildableMeta.registryTypeMapper().createBukkit(Holder.direct(builderFactory.requireBuilder().build()));
    }

    @Override
    public Art createPaintingVariant(final Consumer<RegistryBuilderFactory<Art, ? extends PaintingVariantRegistryEntry.Builder>> value) {
        return create(Registries.PAINTING_VARIANT, value::accept);
    }
}
