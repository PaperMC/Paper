package org.bukkit.inventory;

import java.util.function.Supplier;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public enum EquipmentSlot {

    HAND(() -> EquipmentSlotGroup.MAINHAND),
    OFF_HAND(() -> EquipmentSlotGroup.OFFHAND),
    FEET(() -> EquipmentSlotGroup.FEET),
    LEGS(() -> EquipmentSlotGroup.LEGS),
    CHEST(() -> EquipmentSlotGroup.CHEST),
    HEAD(() -> EquipmentSlotGroup.HEAD),
    /**
     * Only for certain entities such as horses, happy ghasts and wolves.
     */
    BODY(() -> EquipmentSlotGroup.BODY),
    /**
     * Only for certain entities such as pigs, horses, striders and copper golems.
     */
    SADDLE(() -> EquipmentSlotGroup.SADDLE);

    private final Supplier<EquipmentSlotGroup> group; // Supplier because of class loading order, since EquipmentSlot and EquipmentSlotGroup reference each other on class init

    private EquipmentSlot(/*@NotNull*/ Supplier<EquipmentSlotGroup> group) {
        this.group = group;
    }

    /**
     * Gets the {@link EquipmentSlotGroup} corresponding to this slot.
     *
     * @return corresponding {@link EquipmentSlotGroup}
     */
    @NotNull
    @ApiStatus.Internal
    public EquipmentSlotGroup getGroup() {
        return group.get();
    }

    /**
     * Checks whether this equipment slot is a hand:
     * either {@link #HAND} or {@link #OFF_HAND}
     *
     * @return whether this is a hand slot
     */
    public boolean isHand() {
        return this == HAND || this == OFF_HAND;
    }

    /**
     * Gets the opposite hand
     *
     * @return the opposite hand
     * @throws IllegalArgumentException if this equipment slot is not a hand
     * @see #isHand()
     */
    public @NotNull EquipmentSlot getOppositeHand() {
        return switch (this) {
            case HAND -> OFF_HAND;
            case OFF_HAND -> HAND;
            default -> throw new IllegalArgumentException("Unable to determine an opposite hand for equipment slot: " + name());
        };
    }

    /**
     * Checks whether this equipment slot
     * is one of the armor slots:
     * {@link #HEAD}, {@link #CHEST},
     * {@link #LEGS}, {@link #FEET}, or {@link #BODY}
     *
     * @return whether this is an armor slot
     */
    public boolean isArmor() {
        return this == HEAD || this == CHEST || this == LEGS || this == FEET || this == BODY;
    }
}
