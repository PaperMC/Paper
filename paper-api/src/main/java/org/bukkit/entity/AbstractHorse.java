package org.bukkit.entity;

import org.bukkit.inventory.AbstractHorseInventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/// Represents a Horse-like creature.
public interface AbstractHorse extends Vehicle, InventoryHolder, Tameable {

    /// Gets the horse's variant.
    ///
    /// A horse's variant defines its physical appearance and capabilities.
    /// Whether a horse is a regular horse, donkey, mule, or other kind of horse
    /// is determined using the variant.
    ///
    /// @return a [Horse.Variant] representing the horse's variant
    /// @deprecated different variants are different classes
    @Deprecated(since = "1.11", forRemoval = true)
    @NotNull
    public Horse.Variant getVariant();

    /// @param variant variant
    /// @deprecated you are required to spawn a different entity
    @Deprecated(since = "1.11", forRemoval = true)
    @Contract("_ -> fail")
    public void setVariant(Horse.Variant variant);

    /// Gets the domestication level of this horse.
    ///
    /// A higher domestication level indicates that the horse is closer to
    /// becoming tame. As the domestication level gets closer to the max
    /// domestication level, the chance of the horse becoming tame increases.
    ///
    /// @return domestication level
    public int getDomestication();

    /// Sets the domestication level of this horse.
    ///
    /// Setting the domestication level to a high value will increase the
    /// horse's chances of becoming tame.
    ///
    /// Domestication level must be greater than zero and no greater than
    /// the max domestication level of the horse, determined with
    /// [#getMaxDomestication()]
    ///
    /// @param level domestication level
    public void setDomestication(int level);

    /// Gets the maximum domestication level of this horse.
    ///
    /// The higher this level is, the longer it will likely take
    /// for the horse to be tamed.
    ///
    /// @return the max domestication level
    public int getMaxDomestication();

    /// Sets the maximum domestication level of this horse.
    ///
    /// Setting a higher max domestication will increase the amount of
    /// domesticating (feeding, riding, etc.) necessary in order to tame it,
    /// while setting a lower max value will have the opposite effect.
    ///
    /// Maximum domestication must be greater than zero.
    ///
    /// @param level the max domestication level
    public void setMaxDomestication(int level);

    /// Gets the jump strength of this horse.
    ///
    /// Jump strength defines how high the horse can jump. A higher jump strength
    /// increases how high a jump will go.
    ///
    /// @return the horse's jump strength
    public double getJumpStrength();

    /// Sets the jump strength of this horse.
    ///
    /// A higher jump strength increases how high a jump will go.
    /// Setting a jump strength to 0 will result in no jump.
    /// You cannot set a jump strength to a value below 0 or
    /// above 2.
    ///
    /// @param strength jump strength for this horse
    public void setJumpStrength(double strength);

    /// Gets whether the horse is currently grazing hay.
    ///
    /// @return true if eating hay
    /// @deprecated use [#isEatingGrass()], this name is incorrect
    @Deprecated(forRemoval = true)
    boolean isEatingHaystack();

    /// Sets whether the horse is grazing hay.
    ///
    /// @param eatingHaystack new hay grazing status
    /// @deprecated use [#setEatingGrass(boolean)], this name is incorrect
    @Deprecated(forRemoval = true)
    void setEatingHaystack(boolean eatingHaystack);

    @NotNull
    @Override
    public AbstractHorseInventory getInventory();

    // Paper start - Horse API
    /// Gets if a horse is in their eating grass animation.
    ///
    /// @return eating grass animation is active
    public boolean isEatingGrass();

    /// Sets if a horse is in their eating grass animation.
    ///
    /// When true, the horse will lower its neck.
    ///
    /// @param eating eating grass animation is active
    public void setEatingGrass(boolean eating);

    /// Gets if a horse is in their rearing animation.
    ///
    /// @return rearing animation is active
    public boolean isRearing();

    /// Sets if a horse is in their rearing animation.
    ///
    /// When true, the horse will stand on its hind legs.
    ///
    /// @param rearing rearing animation is active
    public void setRearing(boolean rearing);

    /// Gets if a horse is in their eating animation.
    ///
    /// @return eating animation is active
    public boolean isEating();

    /// Sets if a horse is in their eating animation.
    ///
    /// When true, the horse will bob its head.
    ///
    /// @param eating eating animation is active
    public void setEating(boolean eating);
    // Paper end - Horse API
}
