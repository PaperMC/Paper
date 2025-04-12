package io.papermc.paper.util.sanitizer;

import io.papermc.paper.configuration.GlobalConfiguration;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.UnaryOperator;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Required;

/**
 * The item obfuscation binding is a state bound by the configured item obfuscation.
 * It only hosts the bound and computed data from the global configuration.
 */
@NullMarked
public final class ItemObfuscationBinding {

    public final ItemObfuscationSession.ObfuscationLevel level;
    private final BoundObfuscationConfiguration base;
    private final Map<ResourceLocation, BoundObfuscationConfiguration> overrides;

    public ItemObfuscationBinding(final GlobalConfiguration.Anticheat.Obfuscation.Items items) {
        this.level = items.enableItemObfuscation ? ItemObfuscationSession.ObfuscationLevel.ALL : ItemObfuscationSession.ObfuscationLevel.OVERSIZED;
        this.base = bind(items.allModels);
        final Map<ResourceLocation, BoundObfuscationConfiguration> overrides = new HashMap<>();
        for (final Map.Entry<ResourceLocation, AssetObfuscationConfiguration> entry : items.modelOverrides.entrySet()) {
            overrides.put(entry.getKey(), bind(entry.getValue()));
        }
        this.overrides = Collections.unmodifiableMap(overrides);
    }

    public record BoundObfuscationConfiguration(boolean sanitizeCount,
                                                Map<DataComponentType<?>, MutationType> patchStrategy) {

        sealed interface MutationType permits MutationType.Drop, MutationType.Sanitize {
            enum Drop implements MutationType {
                INSTANCE
            }

            record Sanitize(UnaryOperator sanitizer) implements MutationType {

            }
        }
    }

    @ConfigSerializable
    public record AssetObfuscationConfiguration(@Required boolean sanitizeCount,
                                                Set<DataComponentType<?>> dontObfuscate,
                                                Set<DataComponentType<?>> alsoObfuscate) {

    }

    private static BoundObfuscationConfiguration bind(final AssetObfuscationConfiguration config) {
        final Set<DataComponentType<?>> base = new HashSet<>(BASE_OVERRIDERS);
        base.addAll(config.alsoObfuscate());
        base.removeAll(config.dontObfuscate());

        final Map<DataComponentType<?>, BoundObfuscationConfiguration.MutationType> finalStrategy = new HashMap<>();
        // Configure what path the data component should go through, should it be dropped, or should it be sanitized?
        for (final DataComponentType<?> type : base) {
            // We require some special logic, sanitize it rather than dropping it.
            final UnaryOperator<?> sanitizationOverride = ItemComponentSanitizer.SANITIZATION_OVERRIDES.get(type);
            if (sanitizationOverride != null) {
                finalStrategy.put(type, new BoundObfuscationConfiguration.MutationType.Sanitize(sanitizationOverride));
            } else {
                finalStrategy.put(type, BoundObfuscationConfiguration.MutationType.Drop.INSTANCE);
            }
        }

        return new BoundObfuscationConfiguration(config.sanitizeCount(), finalStrategy);
    }

    public BoundObfuscationConfiguration getAssetObfuscation(final ItemStack itemStack) {
        if (this.overrides.isEmpty()) {
            return this.base;
        }
        return this.overrides.getOrDefault(itemStack.get(DataComponents.ITEM_MODEL), this.base);
    }

    static final Set<DataComponentType<?>> BASE_OVERRIDERS = Set.of(
        DataComponents.MAX_STACK_SIZE,
        DataComponents.MAX_DAMAGE,
        DataComponents.DAMAGE,
        DataComponents.UNBREAKABLE,
        DataComponents.CUSTOM_NAME,
        DataComponents.ITEM_NAME,
        DataComponents.LORE,
        DataComponents.RARITY,
        DataComponents.ENCHANTMENTS,
        DataComponents.CAN_PLACE_ON,
        DataComponents.CAN_BREAK,
        DataComponents.ATTRIBUTE_MODIFIERS,
        DataComponents.TOOLTIP_DISPLAY,
        DataComponents.REPAIR_COST,
        DataComponents.USE_REMAINDER,
        DataComponents.FOOD,
        DataComponents.DAMAGE_RESISTANT,
        // Not important on the player
        DataComponents.TOOL,
        DataComponents.ENCHANTABLE,
        DataComponents.REPAIRABLE,
        DataComponents.GLIDER,
        DataComponents.TOOLTIP_STYLE,
        DataComponents.DEATH_PROTECTION,
        DataComponents.STORED_ENCHANTMENTS,
        DataComponents.MAP_ID,
        DataComponents.POTION_CONTENTS,
        DataComponents.SUSPICIOUS_STEW_EFFECTS,
        DataComponents.WRITABLE_BOOK_CONTENT,
        DataComponents.WRITTEN_BOOK_CONTENT,
        DataComponents.CUSTOM_DATA,
        DataComponents.ENTITY_DATA,
        DataComponents.BUCKET_ENTITY_DATA,
        DataComponents.BLOCK_ENTITY_DATA,
        DataComponents.INSTRUMENT,
        DataComponents.OMINOUS_BOTTLE_AMPLIFIER,
        DataComponents.JUKEBOX_PLAYABLE,
        DataComponents.LODESTONE_TRACKER,
        DataComponents.FIREWORKS,
        DataComponents.NOTE_BLOCK_SOUND,
        DataComponents.BEES,
        DataComponents.LOCK,
        DataComponents.CONTAINER_LOOT
    );
}
