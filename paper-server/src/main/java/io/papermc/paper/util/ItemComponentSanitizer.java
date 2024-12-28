package io.papermc.paper.util;

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

final class ItemComponentSanitizer {

    /*
     * This returns for types, that when configured to be serialized, should instead return these objects.
     * This is possibly because dropping the patched type may introduce visual changes.
     */
    static final Map<DataComponentType<?>, UnaryOperator<?>> SANITIZATION_OVERRIDES = Util.make(ImmutableMap.<DataComponentType<?>, UnaryOperator<?>>builder(), (map) -> {
            put(map, DataComponents.LODESTONE_TRACKER, empty(new LodestoneTracker(Optional.empty(), false))); // We need it to be present to keep the glint
            put(map, DataComponents.ENCHANTMENTS, empty(dummyEnchantments())); // We need to keep it present to keep the glint
            put(map, DataComponents.STORED_ENCHANTMENTS, empty(dummyEnchantments())); // We need to keep it present to keep the glint
            put(map, DataComponents.POTION_CONTENTS, ItemComponentSanitizer::sanitizePotionContents); // Custom situational serialization
        }
    ).build();

    private static <T> void put(ImmutableMap.Builder map, DataComponentType<T> type, UnaryOperator<T> object) {
        map.put(type, object);
    }

    private static <T> UnaryOperator<T> empty(T object) {
        return (unused) -> object;
    }

    // <editor-fold desc="Component Sanitizers" defaultstate="collapsed">
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
        ItemEnchantments.Mutable obj = new ItemEnchantments.Mutable(ItemEnchantments.EMPTY);
        obj.set(MinecraftServer.getServer().registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getRandom(RandomSource.create()).orElseThrow(), 1);
        return obj.toImmutable();
    }

    public static int sanitizeCount(ItemStack itemStack, int count) {
        if (DataSanitizationUtil.DATA_SANITIZER.get().isNotSanitizing()) {
            return count;
        }

        GlobalConfiguration.Anticheat.Obfuscation.Items items = GlobalConfiguration.get().anticheat.obfuscation.items;
        if (items.enableItemObfuscation && DataSanitizationUtil.getAssetObfuscation(itemStack).sanitizeCount()) {
            return 1;
        } else {
            return count;
        }
    }

    public static boolean shouldDrop(DataComponentType<?> key) {
        if (DataSanitizationUtil.DATA_SANITIZER.get().isNotSanitizing()) {
            return false;
        }

        DataSanitizationUtil.ContentScope scope = DataSanitizationUtil.DATA_SANITIZER.get().scope().get();
        ItemStack targetItemstack = scope.itemStack();

        // Only drop if configured to do so.
        return DataSanitizationUtil.getAssetObfuscation(targetItemstack).patchStrategy().get(key) == DataSanitizationUtil.BoundObfuscationConfiguration.MutationType.Drop.INSTANCE;
    }

    public static Optional<?> override(DataComponentType<?> key, Optional<?> value) {
        if (DataSanitizationUtil.DATA_SANITIZER.get().isNotSanitizing()) {
            return value;
        }
        // Ignore removed values
        if (value.isEmpty()) {
            return value;
        }

        DataSanitizationUtil.ContentScope scope = DataSanitizationUtil.DATA_SANITIZER.get().scope().get();
        ItemStack targetItemstack = scope.itemStack();

        return switch (DataSanitizationUtil.getAssetObfuscation(targetItemstack).patchStrategy().get(key)) {
            case DataSanitizationUtil.BoundObfuscationConfiguration.MutationType.Drop unused -> Optional.empty();
            case DataSanitizationUtil.BoundObfuscationConfiguration.MutationType.Sanitize sanitize -> Optional.of(sanitize.sanitizer().apply(value.get()));
            case null -> value;
        };
    }
}
