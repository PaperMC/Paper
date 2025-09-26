package io.papermc.paper.datacomponent.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import com.google.common.base.Preconditions;
import io.papermc.paper.registry.PaperRegistries;
import io.papermc.paper.registry.data.util.Conversions;
import io.papermc.paper.registry.set.PaperRegistrySets;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.text.Filtered;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.TriState;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.OminousBottleAmplifier;
import org.bukkit.JukeboxSong;
import org.bukkit.block.BlockType;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.damage.DamageType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.map.MapCursor;
import org.jspecify.annotations.Nullable;

public final class ItemComponentTypesBridgesImpl implements ItemComponentTypesBridge {

    @Override
    public ChargedProjectiles.Builder chargedProjectiles() {
        return new PaperChargedProjectiles.BuilderImpl();
    }

    @Override
    public PotDecorations.Builder potDecorations() {
        return new PaperPotDecorations.BuilderImpl();
    }

    @Override
    public ItemLore.Builder lore() {
        return new PaperItemLore.BuilderImpl();
    }

    @Override
    public ItemEnchantments.Builder enchantments() {
        return new PaperItemEnchantments.BuilderImpl();
    }

    @Override
    public ItemAttributeModifiers.Builder modifiers() {
        return new PaperItemAttributeModifiers.BuilderImpl();
    }

    @Override
    public FoodProperties.Builder food() {
        return new PaperFoodProperties.BuilderImpl();
    }

    @Override
    public DyedItemColor.Builder dyedItemColor() {
        return new PaperDyedItemColor.BuilderImpl();
    }

    @Override
    public PotionContents.Builder potionContents() {
        return new PaperPotionContents.BuilderImpl();
    }

    @Override
    public BundleContents.Builder bundleContents() {
        return new PaperBundleContents.BuilderImpl();
    }

    @Override
    public SuspiciousStewEffects.Builder suspiciousStewEffects() {
        return new PaperSuspiciousStewEffects.BuilderImpl();
    }

    @Override
    public MapItemColor.Builder mapItemColor() {
        return new PaperMapItemColor.BuilderImpl();
    }

    @Override
    public MapDecorations.Builder mapDecorations() {
        return new PaperMapDecorations.BuilderImpl();
    }

    @Override
    public MapDecorations.DecorationEntry decorationEntry(final MapCursor.Type type, final double x, final double z, final float rotation) {
        return PaperMapDecorations.PaperDecorationEntry.toApi(type, x, z, rotation);
    }

    @Override
    public SeededContainerLoot.Builder seededContainerLoot(final Key lootTableKey) {
        return new PaperSeededContainerLoot.BuilderImpl(lootTableKey);
    }

    @Override
    public ItemContainerContents.Builder itemContainerContents() {
        return new PaperItemContainerContents.BuilderImpl();
    }

    @Override
    public JukeboxPlayable.Builder jukeboxPlayable(final JukeboxSong song) {
        return new PaperJukeboxPlayable.BuilderImpl(song);
    }

    @Override
    public Tool.Builder tool() {
        return new PaperItemTool.BuilderImpl();
    }

    @Override
    public Tool.Rule rule(final RegistryKeySet<BlockType> blocks, final @Nullable Float speed, final TriState correctForDrops) {
        return PaperItemTool.PaperRule.fromUnsafe(blocks, speed, correctForDrops);
    }

    @Override
    public ItemAdventurePredicate.Builder itemAdventurePredicate() {
        return new PaperItemAdventurePredicate.BuilderImpl();
    }

    @Override
    public WrittenBookContent.Builder writtenBookContent(final Filtered<String> title, final String author) {
        return new PaperWrittenBookContent.BuilderImpl(title, author);
    }

    @Override
    public WritableBookContent.Builder writeableBookContent() {
        return new PaperWritableBookContent.BuilderImpl();
    }

    @Override
    public ItemArmorTrim.Builder itemArmorTrim(final ArmorTrim armorTrim) {
        return new PaperItemArmorTrim.BuilderImpl(armorTrim);
    }

    @Override
    public LodestoneTracker.Builder lodestoneTracker() {
        return new PaperLodestoneTracker.BuilderImpl();
    }

    @Override
    public Fireworks.Builder fireworks() {
        return new PaperFireworks.BuilderImpl();
    }

    @Override
    public ResolvableProfile.Builder resolvableProfile() {
        return new PaperResolvableProfile.BuilderImpl();
    }

    @Override
    public ResolvableProfile.SkinPatchBuilder skinPatch() {
        return new PaperResolvableProfile.SkinPatchBuilderImpl();
    }

    @Override
    public ResolvableProfile.SkinPatch emptySkinPatch() {
        return new PaperResolvableProfile.PaperSkinPatch(null, null, null, null);
    }

    @Override
    public ResolvableProfile resolvableProfile(final PlayerProfile profile) {
        return PaperResolvableProfile.toApi(profile);
    }

    @Override
    public BannerPatternLayers.Builder bannerPatternLayers() {
        return new PaperBannerPatternLayers.BuilderImpl();
    }

    @Override
    public BlockItemDataProperties.Builder blockItemStateProperties() {
        return new PaperBlockItemDataProperties.BuilderImpl();
    }

    @Override
    public MapId mapId(final int id) {
        return new PaperMapId(new net.minecraft.world.level.saveddata.maps.MapId(id));
    }

    @Override
    public UseRemainder useRemainder(final ItemStack stack) {
        Preconditions.checkArgument(stack != null, "Item cannot be null");
        Preconditions.checkArgument(!stack.isEmpty(), "Remaining item cannot be empty!");
        return new PaperUseRemainder(
            new net.minecraft.world.item.component.UseRemainder(CraftItemStack.asNMSCopy(stack))
        );
    }

    @Override
    public Consumable.Builder consumable() {
        return new PaperConsumable.BuilderImpl();
    }

    @Override
    public UseCooldown.Builder useCooldown(final float seconds) {
        Preconditions.checkArgument(seconds > 0, "seconds must be positive, was %s", seconds);
        return new PaperUseCooldown.BuilderImpl(seconds);
    }

    @Override
    public DamageResistant damageResistant(final TagKey<DamageType> types) {
        return new PaperDamageResistant(new net.minecraft.world.item.component.DamageResistant(PaperRegistries.toNms(types)));
    }

    @Override
    public Enchantable enchantable(final int level) {
        return new PaperEnchantable(new net.minecraft.world.item.enchantment.Enchantable(level));
    }

    @Override
    public Repairable repairable(final RegistryKeySet<ItemType> types) {
        return new PaperRepairable(new net.minecraft.world.item.enchantment.Repairable(
            PaperRegistrySets.convertToNms(Registries.ITEM, Conversions.global().lookup(), types)
        ));
    }

    @Override
    public Equippable.Builder equippable(EquipmentSlot slot) {
        return new PaperEquippable.BuilderImpl(slot);
    }

    @Override
    public DeathProtection.Builder deathProtection() {
        return new PaperDeathProtection.BuilderImpl();
    }

    @Override
    public CustomModelData.Builder customModelData() {
        return new PaperCustomModelData.BuilderImpl();
    }

    @Override
    public PaperOminousBottleAmplifier ominousBottleAmplifier(final int amplifier) {
        Preconditions.checkArgument(OminousBottleAmplifier.MIN_AMPLIFIER <= amplifier && amplifier <= OminousBottleAmplifier.MAX_AMPLIFIER,
            "amplifier must be between %s-%s, was %s", OminousBottleAmplifier.MIN_AMPLIFIER, OminousBottleAmplifier.MAX_AMPLIFIER, amplifier
        );
        return new PaperOminousBottleAmplifier(
            new OminousBottleAmplifier(amplifier)
        );
    }

    @Override
    public BlocksAttacks.Builder blocksAttacks() {
        return new PaperBlocksAttacks.BuilderImpl();
    }

    @Override
    public TooltipDisplay.Builder tooltipDisplay() {
        return new PaperTooltipDisplay.BuilderImpl();
    }

    @Override
    public Weapon.Builder weapon() {
        return new PaperWeapon.BuilderImpl();
    }
}
