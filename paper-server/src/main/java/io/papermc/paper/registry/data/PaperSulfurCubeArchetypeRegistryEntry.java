package io.papermc.paper.registry.data;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.PaperRegistryBuilder;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.util.MCUtil;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.world.entity.SulfurCubeArchetype;
import net.minecraft.world.item.Item;
import org.bukkit.craftbukkit.attribute.CraftAttributeInstance;
import org.bukkit.craftbukkit.util.RandomSourceWrapper;
import org.bukkit.entity.SulfurCube;
import org.bukkit.inventory.ItemType;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.registry.data.util.Checks.asArgument;
import static io.papermc.paper.registry.data.util.Checks.asConfigured;

public class PaperSulfurCubeArchetypeRegistryEntry implements SulfurCubeArchetypeRegistryEntry {

    protected final Conversions conversions;
    protected @Nullable HolderSet<Item> items;
    protected List<SulfurCubeArchetype.AttributeEntry> attributeModifiers;
    protected boolean buoyant;
    protected SulfurCubeArchetype.@Nullable ExplosionData explosion;
    protected SulfurCubeArchetype.@Nullable ContactDamage contactDamage;
    protected SulfurCubeArchetype.@Nullable KnockbackModifiers knockbackModifiers;
    protected SulfurCubeArchetype.@Nullable SoundSettings soundSettings;

    public PaperSulfurCubeArchetypeRegistryEntry(final Conversions conversions, final @Nullable SulfurCubeArchetype archetype) {
        this.conversions = conversions;
        if (archetype == null) {
            this.attributeModifiers = List.of();
            return;
        }

        this.items = archetype.items();
        this.attributeModifiers = archetype.attributeModifiers();
        this.buoyant = archetype.buoyant();
        this.explosion = archetype.explosion().orElse(null);
        this.contactDamage = archetype.contactDamage().orElse(null);
        this.knockbackModifiers = archetype.knockbackModifiers();
        this.soundSettings = archetype.soundSettings();
    }

    @Override
    public RegistryKeySet<ItemType> items() {
        return PaperRegistrySets.convertToApi(RegistryKey.ITEM, asConfigured(this.items, "items"));
    }

    @Override
    public List<AttributeEntry> attributeModifiers() {
        return MCUtil.transformUnmodifiable(this.attributeModifiers, entry -> {
            return AttributeEntry.of(
                PaperRegistries.fromNms(entry.attribute().unwrapKey().orElseThrow()),
                CraftAttributeInstance.convert(entry.modifier())
            );
        });
    }

    @Override
    public boolean buoyant() {
        return this.buoyant;
    }

    @Override
    public @Nullable ExplosionSettings explosion() {
        if (this.explosion == null) {
            return null;
        }

        return ExplosionSettings.of(
            this.explosion.power(),
            this.explosion.causesFire(),
            this.explosion.fuse()
        );
    }

    @Override
    public @Nullable ContactDamage contactDamage() {
        if (this.contactDamage == null) {
            return null;
        }

        return ContactDamage.of(
            PaperRegistries.fromNms(this.contactDamage.damageType().unwrapKey().orElseThrow()),
            this.contactDamage.amount().sample(new RandomSourceWrapper(new Random())),
            this.contactDamage.attributeToSource()
        );
    }

    @Override
    public KnockbackModifiers knockbackModifiers() {
        asConfigured(this.knockbackModifiers, "knockbackModifiers");
        return KnockbackModifiers.of(
            this.knockbackModifiers.horizontalPower(),
            this.knockbackModifiers.verticalPower()
        );
    }

    @Override
    public SoundSettings soundSettings() {
        asConfigured(this.soundSettings, "soundSettings");
        return SoundSettings.of(
            PaperRegistries.fromNms(this.soundSettings.hitSound().unwrapKey().orElseThrow()),
            PaperRegistries.fromNms(this.soundSettings.pushSound().unwrapKey().orElseThrow()),
            this.soundSettings.pushSoundImpulseThreshold(),
            this.soundSettings.pushSoundCooldown()
        );
    }

    public static final class PaperBuilder extends PaperSulfurCubeArchetypeRegistryEntry implements Builder, PaperRegistryBuilder<SulfurCubeArchetype, SulfurCube.Archetype> {

        public PaperBuilder(final Conversions conversions, final @Nullable SulfurCubeArchetype archetype) {
            super(conversions, archetype);
        }

        @Override
        public Builder items(final RegistryKeySet<ItemType> items) {
            this.items = PaperRegistrySets.convertToNms(Registries.ITEM, this.conversions.lookup(), asArgument(items, "items"));
            return this;
        }

        @Override
        public Builder attributeModifiers(final Iterable<AttributeEntry> entries) {
            this.attributeModifiers = Lists.newArrayList(Iterables.transform(asArgument(entries, "entries"), entry -> {
                return new SulfurCubeArchetype.AttributeEntry(
                    this.conversions.getReferenceHolder(PaperRegistries.toNms(entry.attribute())),
                    CraftAttributeInstance.convert(entry.modifier())
                );
            }));
            return this;
        }

        @Override
        public Builder buoyant(final boolean buoyant) {
            this.buoyant = buoyant;
            return this;
        }

        @Override
        public Builder explosion(final @Nullable ExplosionSettings settings) {
            this.explosion = settings == null ? null : new SulfurCubeArchetype.ExplosionData(
                settings.power(),
                settings.incendiary(),
                settings.fuseTicks()
            );
            return this;
        }

        @Override
        public Builder contactDamage(final @Nullable ContactDamage damage) {
            this.contactDamage = damage == null ? null : new SulfurCubeArchetype.ContactDamage(
                this.conversions.getReferenceHolder(PaperRegistries.toNms(damage.damageType())),
                ConstantFloat.of(damage.amount()),
                damage.attributeToSource()
            );
            return this;
        }

        @Override
        public Builder knockbackModifiers(final KnockbackModifiers modifiers) {
            this.knockbackModifiers = new SulfurCubeArchetype.KnockbackModifiers(
                modifiers.horizontalPower(),
                modifiers.verticalPower()
            );
            return this;
        }

        @Override
        public Builder soundSettings(final SoundSettings settings) {
            this.soundSettings = new SulfurCubeArchetype.SoundSettings(
                this.conversions.getReferenceHolder(PaperRegistries.toNms(settings.hitSound())),
                this.conversions.getReferenceHolder(PaperRegistries.toNms(settings.pushSound())),
                settings.pushSoundImpulseThreshold(),
                settings.pushSoundCooldown()
            );
            return this;
        }

        @Override
        public SulfurCubeArchetype build() {
            return new SulfurCubeArchetype(
                asConfigured(this.items, "items"),
                Collections.unmodifiableList(this.attributeModifiers),
                this.buoyant,
                Optional.ofNullable(this.explosion),
                Optional.ofNullable(this.contactDamage),
                asConfigured(this.knockbackModifiers, "knockbackModifiers"),
                asConfigured(this.soundSettings, "soundSettings")
            );
        }
    }
}
