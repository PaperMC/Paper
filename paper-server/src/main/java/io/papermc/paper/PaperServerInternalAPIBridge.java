package io.papermc.paper;

import io.papermc.paper.block.property.EnumBlockProperty;
import io.papermc.paper.block.property.PaperBlockProperties;
import java.util.Objects;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import org.bukkit.craftbukkit.block.data.CraftBlockData;
import org.bukkit.craftbukkit.damage.CraftDamageEffect;
import org.bukkit.damage.DamageEffect;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class PaperServerInternalAPIBridge implements InternalAPIBridge {
    public static final PaperServerInternalAPIBridge INSTANCE = new PaperServerInternalAPIBridge();

    @Override
    public DamageEffect getDamageEffect(final String key) {
        return Objects.requireNonNull(CraftDamageEffect.getById(key), "Unknown damage effect key: " + key);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public <B extends Enum<B>> String getPropertyEnumName(final EnumBlockProperty<B> enumProperty, final B bukkitEnum) {
        final Property<?> nmsProperty = PaperBlockProperties.convertToNmsProperty(enumProperty);
        if (!(nmsProperty instanceof final EnumProperty nmsEnumProperty)) {
            throw new IllegalArgumentException("Could not convert " + enumProperty + " to an nms EnumProperty");
        }
        return nmsEnumProperty.getName(CraftBlockData.toNMS(bukkitEnum, nmsEnumProperty.getValueClass()));
    }
}
