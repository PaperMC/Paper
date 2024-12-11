package org.bukkit.craftbukkit.potion;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.jetbrains.annotations.NotNull;

public class CraftPotionEffectType extends PotionEffectType implements Handleable<MobEffect> {

    public static PotionEffectType minecraftHolderToBukkit(Holder<MobEffect> minecraft) {
        return CraftPotionEffectType.minecraftToBukkit(minecraft.value());
    }

    public static PotionEffectType minecraftToBukkit(MobEffect minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.MOB_EFFECT, Registry.EFFECT);
    }

    public static MobEffect bukkitToMinecraft(PotionEffectType bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<MobEffect> bukkitToMinecraftHolder(PotionEffectType bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit, Registries.MOB_EFFECT);
    }

    private final NamespacedKey key;
    private final MobEffect handle;
    private final int id;

    public CraftPotionEffectType(NamespacedKey key, MobEffect handle) {
        this.key = key;
        this.handle = handle;
        this.id = CraftRegistry.getMinecraftRegistry(Registries.MOB_EFFECT).getId(handle) + 1;
    }

    @Override
    public MobEffect getHandle() {
        return this.handle;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return this.key;
    }

    @Override
    public double getDurationModifier() {
        return 1.0D;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public String getName() {
        return switch (this.getId()) {
            case 1 -> "SPEED";
            case 2 -> "SLOW";
            case 3 -> "FAST_DIGGING";
            case 4 -> "SLOW_DIGGING";
            case 5 -> "INCREASE_DAMAGE";
            case 6 -> "HEAL";
            case 7 -> "HARM";
            case 8 -> "JUMP";
            case 9 -> "CONFUSION";
            case 10 -> "REGENERATION";
            case 11 -> "DAMAGE_RESISTANCE";
            case 12 -> "FIRE_RESISTANCE";
            case 13 -> "WATER_BREATHING";
            case 14 -> "INVISIBILITY";
            case 15 -> "BLINDNESS";
            case 16 -> "NIGHT_VISION";
            case 17 -> "HUNGER";
            case 18 -> "WEAKNESS";
            case 19 -> "POISON";
            case 20 -> "WITHER";
            case 21 -> "HEALTH_BOOST";
            case 22 -> "ABSORPTION";
            case 23 -> "SATURATION";
            case 24 -> "GLOWING";
            case 25 -> "LEVITATION";
            case 26 -> "LUCK";
            case 27 -> "UNLUCK";
            case 28 -> "SLOW_FALLING";
            case 29 -> "CONDUIT_POWER";
            case 30 -> "DOLPHINS_GRACE";
            case 31 -> "BAD_OMEN";
            case 32 -> "HERO_OF_THE_VILLAGE";
            case 33 -> "DARKNESS";
            default -> this.getKey().toString();
        };
    }

    @NotNull
    @Override
    public PotionEffect createEffect(int duration, int amplifier) {
        return new PotionEffect(this, this.isInstant() ? 1 : (int) (duration * this.getDurationModifier()), amplifier);
    }

    @Override
    public boolean isInstant() {
        return this.handle.isInstantenous();
    }

    @Override
    public PotionEffectTypeCategory getCategory() {
        return CraftPotionEffectTypeCategory.minecraftToBukkit(this.handle.getCategory());
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(this.handle.getColor());
    }

    @NotNull
    @Override
    public String getTranslationKey() {
        return this.handle.getDescriptionId();
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof PotionEffectType)) {
            return false;
        }

        return this.getKey().equals(((PotionEffectType) other).getKey());
    }

    @Override
    public int hashCode() {
        return this.getKey().hashCode();
    }

    @Override
    public String toString() {
        return "CraftPotionEffectType[" + this.getKey() + "]";
    }
}
