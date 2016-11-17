package org.bukkit.craftbukkit.entity;

import net.minecraft.server.EntityHorse;
import org.apache.commons.lang.Validate;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftInventoryHorse;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Horse;
import org.bukkit.inventory.HorseInventory;

public class CraftHorse extends CraftAbstractHorse implements Horse {

    public CraftHorse(CraftServer server, EntityHorse entity) {
        super(server, entity);
    }

    @Override
    public EntityHorse getHandle() {
        return (EntityHorse) super.getHandle();
    }

    @Override
    public Variant getVariant() {
        return Variant.HORSE;
    }

    @Override
    public Color getColor() {
        return Color.values()[getHandle().getVariant() & 0xFF];
    }

    @Override
    public void setColor(Color color) {
        Validate.notNull(color, "Color cannot be null");
        getHandle().setVariant(color.ordinal() & 0xFF | getStyle().ordinal() << 8);
    }

    @Override
    public Style getStyle() {
        return Style.values()[getHandle().getVariant() >>> 8];
    }

    @Override
    public void setStyle(Style style) {
        Validate.notNull(style, "Style cannot be null");
        getHandle().setVariant(getColor().ordinal() & 0xFF | style.ordinal() << 8);
    }

    @Override
    public boolean isCarryingChest() {
        return false;
    }

    @Override
    public void setCarryingChest(boolean chest) {
        throw new UnsupportedOperationException("Not supported.");
    }

    @Override
    public HorseInventory getInventory() {
        return new CraftInventoryHorse(getHandle().inventoryChest);
    }

    @Override
    public String toString() {
        return "CraftHorse{variant=" + getVariant() + ", owner=" + getOwner() + '}';
    }

    @Override
    public EntityType getType() {
        return EntityType.HORSE;
    }
}
