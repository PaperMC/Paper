package io.papermc.paper.registry.data;

import io.papermc.paper.registry.RegistryBuilderFactory;
import io.papermc.paper.registry.data.util.Conversions;
import java.util.function.Consumer;
import net.minecraft.core.registries.Registries;
import org.bukkit.Art;

public final class InlinedRegistryBuilderProviderImpl implements InlinedRegistryBuilderProvider {

    @Override
    public Art createPaintingVariant(final Consumer<RegistryBuilderFactory<Art, ? extends PaintingVariantRegistryEntry.Builder>> value) {
        return Conversions.global().createApiInstanceFromBuilder(Registries.PAINTING_VARIANT, value);
    }
}
