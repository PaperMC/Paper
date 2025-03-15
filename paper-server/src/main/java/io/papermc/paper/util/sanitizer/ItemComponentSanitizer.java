package io.papermc.paper.util.sanitizer;

import com.google.common.collect.ImmutableMap;
import io.papermc.paper.configuration.GlobalConfiguration;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.UnaryOperator;
import net.minecraft.Util;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.component.LodestoneTracker;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.jspecify.annotations.NullMarked;

@NullMarked
public final class ItemComponentSanitizer {

    /*
     * This returns for types, that when configured to be serialized, should instead return these objects.
     * This is possibly because dropping the patched type may introduce visual changes.
     */
    static final Map<DataComponentType<?>, UnaryOperator<?>> SANITIZATION_OVERRIDES = Util.make(ImmutableMap.<DataComponentType<?>, UnaryOperator<?>>builder(), (map) -> {
            put(map, DataComponents.LODESTONE_TRACKER, empty(new LodestoneTracker(Optional.empty(), false))); // We need it to be present to keep the glint
            put(map, DataComponents.POTION_CONTENTS, ItemComponentSanitizer::sanitizePotionContents); // Custom situational serialization

            if (MinecraftServer.getServer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).size() > 0) {
                put(map, DataComponents.ENCHANTMENTS, empty(dummyEnchantments())); // We need to keep it present to keep the glint
                put(map, DataComponents.STORED_ENCHANTMENTS, empty(dummyEnchantments())); // We need to keep it present to keep the glint
            }
        }
    ).build();

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static <T> void put(final ImmutableMap.Builder map, final DataComponentType<T> type, final UnaryOperator<T> object) {
        map.put(type, object);
    }

    private static <T> UnaryOperator<T> empty(final T object) {
        return (unused) -> object;
    }

    private static PotionContents sanitizePotionContents(final PotionContents potionContents) {
        // We have a custom color! We can hide everything!
        if (potionContents.customColor().isPresent()) {
            return new PotionContents(Optional.empty(), potionContents.customColor(), List.of(), Optional.empty());
        }

        // WE cannot hide anything really, as the color is a mix of potion/potion contents, which can
        // possibly be reversed.
        return potionContents;
    }

    // We cant use the empty map from enchantments because we want to keep the glow
    private static ItemEnchantments dummyEnchantments() {
        final ItemEnchantments.Mutable obj = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        obj.set(MinecraftServer.getServer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getRandom(RandomSource.create()).orElseThrow(), 1);
        return obj.toImmutable();
    }

    public static int sanitizeCount(final ItemObfuscationSession obfuscationSession, final ItemStack itemStack, final int count) {
        if (obfuscationSession.obfuscationLevel() != ItemObfuscationSession.ObfuscationLevel.ALL) return count; // Ignore if we are not obfuscating

        if (GlobalConfiguration.get().anticheat.obfuscation.items.binding.getAssetObfuscation(itemStack).sanitizeCount()) {
            return 1;
        } else {
            return count;
        }
    }

    public static boolean shouldDrop(final ItemObfuscationSession obfuscationSession, final DataComponentType<?> key) {
        if (obfuscationSession.obfuscationLevel() != ItemObfuscationSession.ObfuscationLevel.ALL) return false; // Ignore if we are not obfuscating

        final ItemStack targetItemstack = obfuscationSession.context().itemStack();

        // Only drop if configured to do so.
        return GlobalConfiguration.get().anticheat.obfuscation.items.binding.getAssetObfuscation(targetItemstack).patchStrategy().get(key) == ItemObfuscationBinding.BoundObfuscationConfiguration.MutationType.Drop.INSTANCE;
    }

    public static Optional<?> override(final ItemObfuscationSession obfuscationSession, final DataComponentType<?> key, final Optional<?> value) {
        if (obfuscationSession.obfuscationLevel() != ItemObfuscationSession.ObfuscationLevel.ALL) return value; // Ignore if we are not obfuscating

        // Ignore removed values
        if (value.isEmpty()) {
            return value;
        }

        final ItemStack targetItemstack = obfuscationSession.context().itemStack();

        return switch (GlobalConfiguration.get().anticheat.obfuscation.items.binding.getAssetObfuscation(targetItemstack).patchStrategy().get(key)) {
            case final ItemObfuscationBinding.BoundObfuscationConfiguration.MutationType.Drop ignored -> Optional.empty();
            case final ItemObfuscationBinding.BoundObfuscationConfiguration.MutationType.Sanitize sanitize -> Optional.of(sanitize.sanitizer().apply(value.get()));
            case null -> value;
        };
    }
}
