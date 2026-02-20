package io.papermc.paper.world.attribute;

import io.papermc.paper.registry.typed.AbstractTypedDataCollector;
import io.papermc.paper.registry.typed.PaperTypedDataAdapter;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ARGB;
import net.minecraft.world.attribute.AttributeTypes;
import net.minecraft.world.attribute.EnvironmentAttribute;
import org.bukkit.Color;

class PaperEnvironmentalAttributeTypeCollector extends AbstractTypedDataCollector<EnvironmentAttribute<?>> {

    public PaperEnvironmentalAttributeTypeCollector(final Registry<EnvironmentAttribute<?>> registry, final Map<ResourceKey<EnvironmentAttribute<?>>, PaperTypedDataAdapter<?, ?>> adapters) {
        super(registry, adapters);
    }

    public <NMS> void registerIdentity(final EnvironmentAttribute<NMS> attribute) {
        super.registerIdentity(attribute, EnvironmentAttribute::valueCodec);
    }

    public void registerIntAsColor(final EnvironmentAttribute<Integer> attribute) {
        if (attribute.type() == AttributeTypes.ARGB_COLOR) {
            this.register(attribute, Color::fromARGB, Color::asARGB);
        } else if (attribute.type() == AttributeTypes.RGB_COLOR) {
            this.register(attribute, color -> Color.fromRGB(color & 0x00FFFFFF), color -> ARGB.opaque(color.asRGB()));
        } else {
            throw new IllegalArgumentException("Environment attribute value cannot be converted as a color: " + attribute.type());
        }
    }

    @Override
    public void registerUntyped(final EnvironmentAttribute<?> attribute) {
        throw new IllegalStateException("Non-typed adapter is not supported for environmental attribute types! got: " + attribute); // TODO should be restricted by API design not at runtime
    }

    // Not using @Override because of generic types
    public <NMS, API> void register(final EnvironmentAttribute<NMS> attribute, final Function<NMS, API> vanillaToApi, final Function<API, NMS> apiToVanilla) {
        super.register(attribute, vanillaToApi, apiToVanilla);
    }
}
