package org.bukkit.inventory.view;

import org.bukkit.inventory.BeaconInventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/// An instance of [InventoryView] which provides extra methods related to
/// beacon view data.
public interface BeaconView extends InventoryView {

    @NotNull
    @Override
    BeaconInventory getTopInventory();

    /// Gets the tier of the beacon
    ///
    /// Beacon tier is deduced by the height of the pyramid the beacon is
    /// standing on. The level of the beacon is 0 unless the beacon is activated.
    ///
    /// @return The tier of the beacon
    int getTier();

    /// Gets the primary effect of the beacon.
    ///
    /// If the beacon level is high enough where the primary effect can be
    /// upgraded to level two, e.g. Speed 2. Instead of
    /// [#getSecondaryEffect()] being null it [#getSecondaryEffect()]
    /// returns the same [PotionEffectType] as this method.
    ///
    /// @return The primary effect enabled on the beacon
    @Nullable
    PotionEffectType getPrimaryEffect();

    /// Gets the secondary effect of the beacon.
    ///
    /// If the beacon level is high enough where the primary effect can be
    /// upgraded to level two, e.g. Speed 2. The secondary effect will return the
    /// same effect as [#getPrimaryEffect()].
    ///
    /// @return The secondary effect enabled on the beacon
    @Nullable
    PotionEffectType getSecondaryEffect();

    /// Sets the primary effect of the beacon, or null to clear
    ///
    /// The [PotionEffectType] provided must be one that is already within
    /// the beacon as a valid option.
    ///
    ///   1. [PotionEffectType#SPEED]
    ///   2. [PotionEffectType#HASTE]
    ///   3. [PotionEffectType#RESISTANCE]
    ///   4. [PotionEffectType#JUMP_BOOST]
    ///   5. [PotionEffectType#STRENGTH]
    ///   6. [PotionEffectType#REGENERATION]
    ///
    /// @param effect desired primary effect
    void setPrimaryEffect(@Nullable final PotionEffectType effect);

    /// Sets the secondary effect on this beacon, or null to clear. Note that
    /// tier must be >= 4 and a primary effect must be set in order for this
    /// effect to be active.
    ///
    /// The [PotionEffectType] provided must be one that is already within
    /// the beacon as a valid option.
    ///
    ///   1. [PotionEffectType#SPEED]
    ///   2. [PotionEffectType#HASTE]
    ///   3. [PotionEffectType#RESISTANCE]
    ///   4. [PotionEffectType#JUMP_BOOST]
    ///   5. [PotionEffectType#STRENGTH]
    ///   6. [PotionEffectType#REGENERATION]
    ///
    /// @param effect the desired secondary effect
    void setSecondaryEffect(@Nullable final PotionEffectType effect);
}
