package org.bukkit.craftbukkit.potion;

import com.google.common.base.Preconditions;
import com.google.common.base.Suppliers;
import io.papermc.paper.util.Holderable;
import io.papermc.paper.world.flag.PaperFeatureDependent;
import java.util.Map;
import java.util.function.Supplier;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.Attribute;
import org.bukkit.Color;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.attribute.CraftAttribute;
import org.bukkit.craftbukkit.attribute.CraftAttributeInstance;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.potion.PotionEffectTypeCategory;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class CraftPotionEffectType extends PotionEffectType implements Holderable<MobEffect>, io.papermc.paper.world.flag.PaperFeatureDependent<MobEffect> {

    public static PotionEffectType minecraftHolderToBukkit(Holder<MobEffect> minecraft) {
        return CraftPotionEffectType.minecraftToBukkit(minecraft.value());
    }

    public static PotionEffectType minecraftToBukkit(MobEffect minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.MOB_EFFECT);
    }

    public static MobEffect bukkitToMinecraft(PotionEffectType bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<MobEffect> bukkitToMinecraftHolder(PotionEffectType bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    private final Holder<MobEffect> holder;
    private final Supplier<Integer> id;

    public CraftPotionEffectType(final Holder<MobEffect> holder) {
        this.holder = holder;
        this.id = Suppliers.memoize(() -> CraftRegistry.getMinecraftRegistry(Registries.MOB_EFFECT).getId(this.getHandle()) + 1);
    }

    @Override
    public Holder<MobEffect> getHolder() {
        return this.holder;
    }

    @Override
    public NamespacedKey getKey() {
        return PaperFeatureDependent.super.getKey();
    }

    @Override
    public double getDurationModifier() {
        return 1.0D;
    }

    @Override
    public int getId() {
        return this.id.get();
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

    @Override
    public PotionEffect createEffect(int duration, int amplifier) {
        return new PotionEffect(this, this.isInstant() ? 1 : (int) (duration * this.getDurationModifier()), amplifier);
    }

    @Override
    public boolean isInstant() {
        return this.getHandle().isInstantenous();
    }

    @Override
    public PotionEffectTypeCategory getCategory() {
        return CraftPotionEffectTypeCategory.minecraftToBukkit(this.getHandle().getCategory());
    }

    @Override
    public Color getColor() {
        return Color.fromRGB(this.getHandle().getColor());
    }

    @Override
    public String getTranslationKey() {
        return this.getHandle().getDescriptionId();
    }

    @Override
    public Map<org.bukkit.attribute.Attribute, AttributeModifier> getEffectAttributes() {
        // re-create map each time because a nms MobEffect can have its attributes modified
        final Map<org.bukkit.attribute.Attribute, AttributeModifier> attributeMap = new java.util.HashMap<>();
        this.getHandle().attributeModifiers.forEach((attribute, attributeModifier) -> {
            attributeMap.put(
                CraftAttribute.minecraftHolderToBukkit(attribute),
                // use zero as amplifier to get the base amount, as it is amount = base * (amplifier + 1)
                CraftAttributeInstance.convert(attributeModifier.create(0))
            );
        });
        return Map.copyOf(attributeMap);
    }

    @Override
    public double getAttributeModifierAmount(org.bukkit.attribute.Attribute attribute, int effectAmplifier) {
        Preconditions.checkArgument(effectAmplifier >= 0, "effectAmplifier must be greater than or equal to 0");
        Holder<Attribute> nmsAttribute = CraftAttribute.bukkitToMinecraftHolder(attribute);
        Preconditions.checkArgument(this.getHandle().attributeModifiers.containsKey(nmsAttribute), attribute + " is not present on " + this.getKey());
        return this.getHandle().attributeModifiers.get(nmsAttribute).create(effectAmplifier).amount();
    }

    @Override
    public PotionEffectType.Category getEffectCategory() {
        return fromNMS(this.getHandle().getCategory());
    }

    @Override
    public String translationKey() {
        return this.getHandle().getDescriptionId();
    }

    public static PotionEffectType.Category fromNMS(MobEffectCategory mobEffectInfo) {
        return switch (mobEffectInfo) {
            case BENEFICIAL -> PotionEffectType.Category.BENEFICIAL;
            case HARMFUL -> PotionEffectType.Category.HARMFUL;
            case NEUTRAL -> PotionEffectType.Category.NEUTRAL;
        };
    }

    @Override
    public boolean equals(Object other) {
        return PaperFeatureDependent.super.implEquals(other);
    }

    @Override
    public int hashCode() {
        return PaperFeatureDependent.super.implHashCode();
    }

    @Override
    public String toString() {
        return PaperFeatureDependent.super.implToString();
    }
}
