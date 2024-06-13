package org.bukkit.attribute;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Keyed;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.EquipmentSlotGroup;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Concrete implementation of an attribute modifier.
 */
public class AttributeModifier implements ConfigurationSerializable, Keyed {

    private final NamespacedKey key;
    private final double amount;
    private final Operation operation;
    private final EquipmentSlotGroup slot;

    @Deprecated
    public AttributeModifier(@NotNull String name, double amount, @NotNull Operation operation) {
        this(UUID.randomUUID(), name, amount, operation);
    }

    @Deprecated
    public AttributeModifier(@NotNull UUID uuid, @NotNull String name, double amount, @NotNull Operation operation) {
        this(uuid, name, amount, operation, (EquipmentSlot) null);
    }

    @Deprecated
    public AttributeModifier(@NotNull UUID uuid, @NotNull String name, double amount, @NotNull Operation operation, @Nullable EquipmentSlot slot) {
        this(uuid, name, amount, operation, (slot) == null ? EquipmentSlotGroup.ANY : slot.getGroup());
    }

    @Deprecated
    public AttributeModifier(@NotNull UUID uuid, @NotNull String name, double amount, @NotNull Operation operation, @NotNull EquipmentSlotGroup slot) {
        this(NamespacedKey.fromString(uuid.toString()), amount, operation, slot);
    }

    public AttributeModifier(@NotNull NamespacedKey key, double amount, @NotNull Operation operation, @NotNull EquipmentSlotGroup slot) {
        Preconditions.checkArgument(key != null, "Key cannot be null");
        Preconditions.checkArgument(operation != null, "Operation cannot be null");
        Preconditions.checkArgument(slot != null, "EquipmentSlotGroup cannot be null");
        this.key = key;
        this.amount = amount;
        this.operation = operation;
        this.slot = slot;
    }

    /**
     * Get the unique ID for this modifier.
     *
     * @return unique id
     * @see #getKey()
     * @deprecated attributes are now identified by keys
     */
    @NotNull
    @Deprecated
    public UUID getUniqueId() {
        return UUID.fromString(getKey().toString());
    }

    @NotNull
    @Override
    public NamespacedKey getKey() {
        return key;
    }

    /**
     * Get the name of this modifier.
     *
     * @return name
     */
    @NotNull
    public String getName() {
        return key.getKey();
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
        data.put("key", key.toString());
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
        return this.key.equals(mod.key) && this.amount == mod.amount && this.operation == mod.operation && slots;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + Objects.hashCode(this.key);
        hash = 17 * hash + (int) (Double.doubleToLongBits(this.amount) ^ (Double.doubleToLongBits(this.amount) >>> 32));
        hash = 17 * hash + Objects.hashCode(this.operation);
        hash = 17 * hash + Objects.hashCode(this.slot);
        return hash;
    }

    @Override
    public String toString() {
        return "AttributeModifier{"
                + "key=" + this.key.toString()
                + ", operation=" + this.operation.name()
                + ", amount=" + this.amount
                + ", slot=" + (this.slot != null ? this.slot.toString() : "")
                + "}";
    }

    @NotNull
    public static AttributeModifier deserialize(@NotNull Map<String, Object> args) {
        NamespacedKey key;
        if (args.containsKey("uuid")) {
            key = NamespacedKey.fromString((String) args.get("uuid"));
        } else {
            key = NamespacedKey.fromString((String) args.get("key"));
        }
        if (args.containsKey("slot")) {
            EquipmentSlotGroup slotGroup = EquipmentSlotGroup.getByName(args.get("slot").toString().toLowerCase(Locale.ROOT));
            if (slotGroup == null) {
                slotGroup = EquipmentSlotGroup.ANY;

                EquipmentSlot slot = EquipmentSlot.valueOf((args.get("slot").toString().toUpperCase(Locale.ROOT)));
                if (slot != null) {
                    slotGroup = slot.getGroup();
                }
            }

            return new AttributeModifier(key, NumberConversions.toDouble(args.get("amount")), Operation.values()[NumberConversions.toInt(args.get("operation"))], slotGroup);
        }
        return new AttributeModifier(key, NumberConversions.toDouble(args.get("amount")), Operation.values()[NumberConversions.toInt(args.get("operation"))], EquipmentSlotGroup.ANY);
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
