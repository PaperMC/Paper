package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ArrowItem;
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

        getOrEmpty(tag, CraftMetaCrossbow.CHARGED_PROJECTILES).ifPresent((p) -> {
            List<net.minecraft.world.item.ItemStack> list = p.getItems();

            if (list != null && !list.isEmpty()) {
                this.chargedProjectiles = new ArrayList<>();

                for (int i = 0; i < list.size(); i++) {
                    net.minecraft.world.item.ItemStack nbttagcompound1 = list.get(i);

                    this.chargedProjectiles.add(CraftItemStack.asCraftMirror(nbttagcompound1));
                }
            }
        });
    }

    CraftMetaCrossbow(Map<String, Object> map) {
        super(map);

        Iterable<?> projectiles = SerializableMeta.getObject(Iterable.class, map, CraftMetaCrossbow.CHARGED_PROJECTILES.BUKKIT, true);
        if (projectiles != null) {
            for (Object stack : projectiles) {
                if (stack instanceof ItemStack) {
                    this.addChargedProjectile((ItemStack) stack);
                }
            }
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.hasChargedProjectiles()) {
            List<net.minecraft.world.item.ItemStack> list = new ArrayList<>();

            for (ItemStack item : this.chargedProjectiles) {
                list.add(CraftItemStack.asNMSCopy(item));
            }

            tag.put(CraftMetaCrossbow.CHARGED_PROJECTILES, ChargedProjectiles.of(list));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isCrossbowEmpty();
    }

    boolean isCrossbowEmpty() {
        return !(this.hasChargedProjectiles());
    }

    @Override
    public boolean hasChargedProjectiles() {
        return this.chargedProjectiles != null;
    }

    @Override
    public List<ItemStack> getChargedProjectiles() {
        return (this.chargedProjectiles == null) ? ImmutableList.of() : ImmutableList.copyOf(this.chargedProjectiles);
    }

    @Override
    public void setChargedProjectiles(List<ItemStack> projectiles) {
        this.chargedProjectiles = null;

        if (projectiles == null) {
            return;
        }

        for (ItemStack i : projectiles) {
            this.addChargedProjectile(i);
        }
    }

    @Override
    public void addChargedProjectile(ItemStack item) {
        Preconditions.checkArgument(item != null, "item");
        Preconditions.checkArgument(item.getType() == Material.FIREWORK_ROCKET || CraftItemType.bukkitToMinecraft(item.getType()) instanceof ArrowItem, "Item %s is not an arrow or firework rocket", item);

        if (this.chargedProjectiles == null) {
            this.chargedProjectiles = new ArrayList<>();
        }

        this.chargedProjectiles.add(item);
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaCrossbow) {
            CraftMetaCrossbow that = (CraftMetaCrossbow) meta;

            return (this.hasChargedProjectiles() ? that.hasChargedProjectiles() && this.chargedProjectiles.equals(that.chargedProjectiles) : !that.hasChargedProjectiles());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaCrossbow || this.isCrossbowEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (this.hasChargedProjectiles()) {
            hash = 61 * hash + this.chargedProjectiles.hashCode();
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

        if (this.hasChargedProjectiles()) {
            builder.put(CraftMetaCrossbow.CHARGED_PROJECTILES.BUKKIT, this.chargedProjectiles);
        }

        return builder;
    }
}
