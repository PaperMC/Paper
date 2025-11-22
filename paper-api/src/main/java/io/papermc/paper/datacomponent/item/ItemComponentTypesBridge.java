package io.papermc.paper.datacomponent.item;

import com.destroystokyo.paper.profile.PlayerProfile;
import io.papermc.paper.registry.set.RegistryKeySet;
import io.papermc.paper.registry.tag.TagKey;
import io.papermc.paper.text.Filtered;
import java.util.Optional;
import java.util.ServiceLoader;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.util.TriState;
import org.bukkit.JukeboxSong;
import org.bukkit.block.BlockType;
import org.bukkit.damage.DamageType;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ItemType;
import org.bukkit.inventory.meta.trim.ArmorTrim;
import org.bukkit.map.MapCursor;
import org.jetbrains.annotations.ApiStatus;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
@ApiStatus.Internal
interface ItemComponentTypesBridge {

    Optional<ItemComponentTypesBridge> BRIDGE = ServiceLoader.load(ItemComponentTypesBridge.class, ItemComponentTypesBridge.class.getClassLoader()).findFirst();

    static ItemComponentTypesBridge bridge() {
        return BRIDGE.orElseThrow();
    }

    ChargedProjectiles.Builder chargedProjectiles();

    PotDecorations.Builder potDecorations();

    ItemLore.Builder lore();

    ItemEnchantments.Builder enchantments();

    ItemAttributeModifiers.Builder modifiers();

    FoodProperties.Builder food();

    DyedItemColor.Builder dyedItemColor();

    PotionContents.Builder potionContents();

    BundleContents.Builder bundleContents();

    SuspiciousStewEffects.Builder suspiciousStewEffects();

    MapItemColor.Builder mapItemColor();

    MapDecorations.Builder mapDecorations();

    MapDecorations.DecorationEntry decorationEntry(MapCursor.Type type, double x, double z, float rotation);

    SeededContainerLoot.Builder seededContainerLoot(Key lootTableKey);

    WrittenBookContent.Builder writtenBookContent(Filtered<String> title, String author);

    WritableBookContent.Builder writeableBookContent();

    ItemArmorTrim.Builder itemArmorTrim(ArmorTrim armorTrim);

    LodestoneTracker.Builder lodestoneTracker();

    Fireworks.Builder fireworks();

    ResolvableProfile.Builder resolvableProfile();

    ResolvableProfile.SkinPatchBuilder skinPatch();

    ResolvableProfile.SkinPatch emptySkinPatch();

    ResolvableProfile resolvableProfile(PlayerProfile profile);

    BannerPatternLayers.Builder bannerPatternLayers();

    BlockItemDataProperties.Builder blockItemStateProperties();

    ItemContainerContents.Builder itemContainerContents();

    JukeboxPlayable.Builder jukeboxPlayable(JukeboxSong song);

    Tool.Builder tool();

    Tool.Rule rule(RegistryKeySet<BlockType> blocks, @Nullable Float speed, TriState correctForDrops);

    ItemAdventurePredicate.Builder itemAdventurePredicate();

    CustomModelData.Builder customModelData();

    MapId mapId(int id);

    UseRemainder useRemainder(ItemStack stack);

    Consumable.Builder consumable();

    UseCooldown.Builder useCooldown(final float seconds);

    DamageResistant damageResistant(TagKey<DamageType> types);

    Enchantable enchantable(int level);

    Repairable repairable(RegistryKeySet<ItemType> types);

    Equippable.Builder equippable(EquipmentSlot slot);

    DeathProtection.Builder deathProtection();

    OminousBottleAmplifier ominousBottleAmplifier(int amplifier);

    BlocksAttacks.Builder blocksAttacks();

    TooltipDisplay.Builder tooltipDisplay();

    Weapon.Builder weapon();
}
