package org.bukkit.craftbukkit.potion;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.OldEnumHolderable;
import io.papermc.paper.world.flag.PaperFeatureDependent;
import java.util.List;
import java.util.Locale;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.alchemy.Potion;
import org.bukkit.NamespacedKey;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.util.ApiVersion;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftPotionType extends OldEnumHolderable<PotionType, Potion> implements PotionType, PaperFeatureDependent<Potion> {

    public static PotionType minecraftHolderToBukkit(Holder<Potion> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.POTION);
    }

    public static PotionType minecraftToBukkit(Potion minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.POTION);
    }

    public static Potion bukkitToMinecraft(PotionType bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<Potion> bukkitToMinecraftHolder(PotionType bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public static String bukkitToString(PotionType bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return bukkit.getKey().toString();
    }

    public static PotionType stringToBukkit(String string) {
        Preconditions.checkArgument(string != null);

        // We currently do not have any version-dependent remapping, so we can use current version
        // First convert from when only the names where saved
        string = FieldRename.convertPotionTypeName(ApiVersion.CURRENT, string);
        string = string.toLowerCase(Locale.ROOT);
        NamespacedKey key = NamespacedKey.fromString(string);
        if (key == null) return null; // Paper - Fixup NamespacedKey handling

        // Now also convert from when keys where saved
        return CraftRegistry.get(RegistryKey.POTION, key, ApiVersion.CURRENT);
    }

    private static int count = 0;

    public CraftPotionType(final Holder<Potion> holder) {
        super(holder, count++);
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return this.getHandle().getEffects().stream().map(CraftPotionUtil::toBukkit).toList();
    }

    @Override
    public boolean isInstant() {
        return this.getHandle().hasInstantEffects();
    }

    @Override
    public boolean isUpgradeable() {
        return BuiltInRegistries.POTION.containsKey(this.getHolder().unwrapKey().orElseThrow().identifier().withPrefix("strong_"));
    }

    @Override
    public boolean isExtendable() {
        return BuiltInRegistries.POTION.containsKey(this.getHolder().unwrapKey().orElseThrow().identifier().withPrefix("long_"));
    }

    @Override
    public int getMaxLevel() {
        return this.isUpgradeable() ? 2 : 1;
    }
}
