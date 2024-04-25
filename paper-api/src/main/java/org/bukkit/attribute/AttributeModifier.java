package org.bukkit.attribute;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Concrete implementation of an attribute modifier.
 */
public class AttributeModifier implements ConfigurationSerializable {

    private final UUID uuid;
    private final String name;
    private final double amount;
    private final Operation operation;
    private final EquipmentSlotGroup slot;

    public AttributeModifier(@NotNull String name, double amount, @NotNull Operation operation) {
        this(UUID.randomUUID(), name, amount, operation);
    }

    public AttributeModifier(@NotNull UUID uuid, @NotNull String name, double amount, @NotNull Operation operation) {
        this(uuid, name, amount, operation, (EquipmentSlot) null);
    }

    public AttributeModifier(@NotNull UUID uuid, @NotNull String name, double amount, @NotNull Operation operation, @Nullable EquipmentSlot slot) {
        this(uuid, name, amount, operation, (slot) == null ? EquipmentSlotGroup.ANY : slot.getGroup());
    }

    public AttributeModifier(@NotNull UUID uuid, @NotNull String name, double amount, @NotNull Operation operation, @NotNull EquipmentSlotGroup slot) {
        Preconditions.checkArgument(uuid != null, "UUID cannot be null");
        Preconditions.checkArgument(name != null, "Name cannot be null");
        Preconditions.checkArgument(operation != null, "Operation cannot be null");
        Preconditions.checkArgument(slot != null, "EquipmentSlotGroup cannot be null");
        this.uuid = uuid;
        this.name = name;
        this.amount = amount;
        this.operation = operation;
        this.slot = slot;
    }

    /**
     * Get the unique ID for this modifier.
     *
     * @return unique id
     */
    @NotNull
    public UUID getUniqueId() {
        return uuid;
    }

    /**
     * Get the name of this modifier.
     *
     * @return name
     */
    @NotNull
    public String getName() {
        return name;
    }

    /**
     * Get the amount by which this modifier will apply its {@link Operation}.
     *
     * @return modification amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Get the operation this modifier will apply.
     *
     * @return operation
     */
    @NotNull
    public Operation getOperation() {
        return operation;
    }

    /**
     * Get the {@link EquipmentSlot} this AttributeModifier is active on,
     * or null if this modifier is applicable for any slot.
     *
     * @return the slot
     * @deprecated use {@link #getSlotGroup()}
     */
    @Nullable
    @Deprecated
    public EquipmentSlot getSlot() {
        return slot == EquipmentSlotGroup.ANY ? null : slot.getExample();
    }

    /**
     * Get the {@link EquipmentSlot} this AttributeModifier is active on,
     * or null if this modifier is applicable for any slot.
     *
     * @return the slot
     */
    @NotNull
    public EquipmentSlotGroup getSlotGroup() {
        return slot;
    }

    @NotNull
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("uuid", uuid.toString());
        data.put("name", name);
        data.put("operation", operation.ordinal());
        data.put("amount", amount);
        if (slot != null && slot != EquipmentSlotGroup.ANY) {
            data.put("slot", slot.toString());
        }
        return data;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AttributeModifier)) {
            return false;
        }
        AttributeModifier mod = (AttributeModifier) other;
        boolean slots = (this.slot != null ? (this.slot == mod.slot) : mod.slot == null);
        return this.uuid.equals(mod.uuid) && this.name.equals(mod.name) && this.amount == mod.amount && this.operation == mod.operation && slots;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.uuid);
        hash = 17 * hash + Objects.hashCode(this.name);
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.amount) ^ (Double.doubleToLongBits(this.amount) >>> 32));
        hash = 17 * hash + Objects.hashCode(this.operation);
        hash = 17 * hash + Objects.hashCode(this.slot);
        return hash;
    }

    @Override
    public String toString() {
        return "AttributeModifier{"
                + "uuid=" + this.uuid.toString()
                + ", name=" + this.name
                + ", operation=" + this.operation.name()
                + ", amount=" + this.amount
                + ", slot=" + (this.slot != null ? this.slot.toString() : "")
                + "}";
    }

    @NotNull
    public static AttributeModifier deserialize(@NotNull Map<String, Object> args) {
        if (args.containsKey("slot")) {
            EquipmentSlotGroup slotGroup = EquipmentSlotGroup.getByName(args.get("slot").toString().toLowerCase());
            if (slotGroup == null) {
                slotGroup = EquipmentSlotGroup.ANY;

                EquipmentSlot slot = EquipmentSlot.valueOf((args.get("slot").toString().toUpperCase()));
                if (slot != null) {
                    slotGroup = slot.getGroup();
                }
            }

            return new AttributeModifier(UUID.fromString((String) args.get("uuid")), (String) args.get("name"), NumberConversions.toDouble(args.get("amount")), Operation.values()[NumberConversions.toInt(args.get("operation"))], slotGroup);
        }
        return new AttributeModifier(UUID.fromString((String) args.get("uuid")), (String) args.get("name"), NumberConversions.toDouble(args.get("amount")), Operation.values()[NumberConversions.toInt(args.get("operation"))]);
    }

    /**
     * Enumerable operation to be applied.
     */
    public enum Operation {

        /**
         * Adds (or subtracts) the specified amount to the base value.
         */
        ADD_NUMBER,
        /**
         * Adds this scalar of amount to the base value.
         */
        ADD_SCALAR,
        /**
         * Multiply amount by this value, after adding 1 to it.
         */
        MULTIPLY_SCALAR_1;
    }
}
