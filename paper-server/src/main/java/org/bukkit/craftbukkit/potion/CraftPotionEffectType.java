package org.bukkit.craftbukkit.potion;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffectList;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

public class CraftPotionEffectType extends PotionEffectType implements Handleable<MobEffectList> {

    public static PotionEffectType minecraftToBukkit(MobEffectList minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.MOB_EFFECT, Registry.EFFECT);
    }

    public static MobEffectList bukkitToMinecraft(PotionEffectType bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    private final NamespacedKey key;
    private final MobEffectList handle;
    private final int id;

    public CraftPotionEffectType(NamespacedKey key, MobEffectList handle) {
        this.key = key;
        this.handle = handle;
        this.id = CraftRegistry.getMinecraftRegistry(Registries.MOB_EFFECT).getId(handle) + 1;
    }

    @Override
    public MobEffectList getHandle() {
        return handle;
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    @Override
    public double getDurationModifier() {
        return 1.0D;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return switch (getId()) {
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
            default -> getKey().toString();
        };
    }

    @NotNull
    @Override
    public PotionEffect createEffect(int duration, int amplifier) {
        return new PotionEffect(this, isInstant() ? 1 : (int) (duration * getDurationModifier()), amplifier);
    }

    @Override
    public boolean isInstant() {
        return handle.isInstantenous();
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(handle.getColor());
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }

        if (!(other instanceof PotionEffectType)) {
            return false;
        }

        return getKey().equals(((PotionEffectType) other).getKey());
    }

    @Override
    public int hashCode() {
        return getKey().hashCode();
    }

    @Override
    public String toString() {
        return "CraftPotionEffectType[" + getKey() + "]";
    }
}
