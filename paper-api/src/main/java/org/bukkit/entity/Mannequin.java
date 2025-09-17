package org.bukkit.entity;

import com.destroystokyo.paper.SkinParts;
import io.papermc.paper.InternalAPIBridge;
import io.papermc.paper.datacomponent.item.ResolvableProfile;
import org.bukkit.inventory.EntityEquipment;
import org.jetbrains.annotations.Contract;
import org.jspecify.annotations.NullMarked;

@NullMarked
public interface Mannequin extends LivingEntity {

    /**
     * Creates a default mannequin profile.
     *
     * @return a new default mannequin profile
     */
    @Contract(value = "-> new", pure = true)
    static ResolvableProfile createDefaultProfile() {
        return InternalAPIBridge.get().defaultMannequinProfile();
    }

    /**
     * Gets the resolvable profile for this mannequin.
     *
     * @return the resolvable profile
     */
    ResolvableProfile getResolvableProfile();

    /**
     * Sets the resolvable profile for this mannequin.
     *
     * @param profile the new resolvable profile
     */
    void setResolvableProfile(ResolvableProfile profile);

    /**
     * Gets a copy of the current skin part options for this mannequin.
     *
     * @return a copy of the current skin part options
     */
    SkinParts.Mutable getSkinParts();

    /**
     * Sets the skin part options for this mannequin.
     *
     * @param parts the new skin part options
     */
    void setSkinParts(SkinParts parts);

    @Override
    EntityEquipment getEquipment();
}
