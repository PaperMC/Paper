package org.bukkit.craftbukkit.inventory.view;

import com.google.common.base.Preconditions;
import net.minecraft.world.inventory.ContainerLectern;
import net.minecraft.world.level.block.entity.TileEntityLectern;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.LecternInventory;
import org.bukkit.inventory.view.LecternView;

public class CraftLecternView extends CraftInventoryView<ContainerLectern, LecternInventory> implements LecternView {

    public CraftLecternView(final HumanEntity player, final LecternInventory viewing, final ContainerLectern container) {
        super(player, viewing, container);
    }

    @Override
    public int getPage() {
        return container.getPage();
    }

    @Override
    public void setPage(final int page) {
        Preconditions.checkArgument(page >= 0, "The minimum page is 0");
        container.setData(TileEntityLectern.DATA_PAGE, page);
    }
}
