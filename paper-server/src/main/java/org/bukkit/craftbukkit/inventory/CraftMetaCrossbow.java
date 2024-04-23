package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemArrow;
import net.minecraft.world.item.component.ChargedProjectiles;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CrossbowMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaCrossbow extends CraftMetaItem implements CrossbowMeta {

    static final ItemMetaKey CHARGED = new ItemMetaKey("charged");
    static final ItemMetaKeyType<ChargedProjectiles> CHARGED_PROJECTILES = new ItemMetaKeyType<>(DataComponents.CHARGED_PROJECTILES, "charged-projectiles");
    //
    private List<ItemStack> chargedProjectiles;

    CraftMetaCrossbow(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaCrossbow)) {
            return;
        }

        CraftMetaCrossbow crossbow = (CraftMetaCrossbow) meta;
        if (crossbow.hasChargedProjectiles()) {
            this.chargedProjectiles = new ArrayList<>(crossbow.chargedProjectiles);
        }
    }

    CraftMetaCrossbow(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, CHARGED_PROJECTILES).ifPresent((p) -> {
            List<net.minecraft.world.item.ItemStack> list = p.getItems();

            if (list != null && !list.isEmpty()) {
                chargedProjectiles = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    net.minecraft.world.item.ItemStack nbttagcompound1 = list.get(i);

                    chargedProjectiles.add(CraftItemStack.asCraftMirror(nbttagcompound1));
                }
            }
        });
    }

    CraftMetaCrossbow(Map<String, Object> map) {
        super(map);

        Iterable<?> projectiles = SerializableMeta.getObject(Iterable.class, map, CHARGED_PROJECTILES.BUKKIT, true);
        if (projectiles != null) {
            for (Object stack : projectiles) {
                if (stack instanceof ItemStack) {
                    addChargedProjectile((ItemStack) stack);
                }
            }
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (hasChargedProjectiles()) {
            List<net.minecraft.world.item.ItemStack> list = new ArrayList<>();

            for (ItemStack item : chargedProjectiles) {
                list.add(CraftItemStack.asNMSCopy(item));
            }

            tag.put(CHARGED_PROJECTILES, ChargedProjectiles.of(list));
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.CROSSBOW;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isCrossbowEmpty();
    }

    boolean isCrossbowEmpty() {
        return !(hasChargedProjectiles());
    }

    @Override
    public boolean hasChargedProjectiles() {
        return chargedProjectiles != null;
    }

    @Override
    public List<ItemStack> getChargedProjectiles() {
        return (chargedProjectiles == null) ? ImmutableList.of() : ImmutableList.copyOf(chargedProjectiles);
    }

    @Override
    public void setChargedProjectiles(List<ItemStack> projectiles) {
        chargedProjectiles = null;

        if (projectiles == null) {
            return;
        }

        for (ItemStack i : projectiles) {
            addChargedProjectile(i);
        }
    }

    @Override
    public void addChargedProjectile(ItemStack item) {
        Preconditions.checkArgument(item != null, "item");
        Preconditions.checkArgument(item.getType() == Material.FIREWORK_ROCKET || CraftItemType.bukkitToMinecraft(item.getType()) instanceof ItemArrow, "Item %s is not an arrow or firework rocket", item);

        if (chargedProjectiles == null) {
            chargedProjectiles = new ArrayList<>();
        }

        chargedProjectiles.add(item);
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaCrossbow) {
            CraftMetaCrossbow that = (CraftMetaCrossbow) meta;

            return (hasChargedProjectiles() ? that.hasChargedProjectiles() && this.chargedProjectiles.equals(that.chargedProjectiles) : !that.hasChargedProjectiles());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaCrossbow || isCrossbowEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (hasChargedProjectiles()) {
            hash = 61 * hash + chargedProjectiles.hashCode();
        }

        return original != hash ? CraftMetaCrossbow.class.hashCode() ^ hash : hash;
    }

    @Override
    public CraftMetaCrossbow clone() {
        return (CraftMetaCrossbow) super.clone();
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasChargedProjectiles()) {
            builder.put(CHARGED_PROJECTILES.BUKKIT, chargedProjectiles);
        }

        return builder;
    }
}
