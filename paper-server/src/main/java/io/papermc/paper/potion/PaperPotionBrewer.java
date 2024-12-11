package io.papermc.paper.potion;

import com.google.common.base.Preconditions;
import java.util.Collection;
import net.minecraft.server.MinecraftServer;
import org.bukkit.NamespacedKey;
import org.bukkit.potion.PotionBrewer;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionType;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class PaperPotionBrewer implements PotionBrewer {

    private final MinecraftServer minecraftServer;

    public PaperPotionBrewer(final MinecraftServer minecraftServer) {
        this.minecraftServer = minecraftServer;
    }

    @Override
    @Deprecated(forRemoval = true)
    public Collection<PotionEffect> getEffects(PotionType type, boolean upgraded, boolean extended) {
        final org.bukkit.NamespacedKey key = type.getKey();

        Preconditions.checkArgument(!key.getKey().startsWith("strong_"), "Strong potion type cannot be used directly, got %s", key);
        Preconditions.checkArgument(!key.getKey().startsWith("long_"), "Extended potion type cannot be used directly, got %s", key);

        org.bukkit.NamespacedKey effectiveKey = key;
        if (upgraded) {
            effectiveKey = new org.bukkit.NamespacedKey(key.namespace(), "strong_" + key.key());
        } else if (extended) {
            effectiveKey = new org.bukkit.NamespacedKey(key.namespace(), "long_" + key.key());
        }

        final org.bukkit.potion.PotionType effectivePotionType = org.bukkit.Registry.POTION.get(effectiveKey);
        Preconditions.checkNotNull(type, "Unknown potion type from data " + effectiveKey.asMinimalString()); // Legacy error message in 1.20.4
        return effectivePotionType.getPotionEffects();
    }

    @Override
    public void addPotionMix(final PotionMix potionMix) {
        this.minecraftServer.potionBrewing().addPotionMix(potionMix);
    }

    @Override
    public void removePotionMix(final NamespacedKey key) {
        this.minecraftServer.potionBrewing.removePotionMix(key);
    }

    @Override
    public void resetPotionMixes() {
        this.minecraftServer.potionBrewing = this.minecraftServer.potionBrewing().reload(this.minecraftServer.getWorldData().enabledFeatures());
    }
}
