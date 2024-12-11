package org.bukkit.craftbukkit.inventory.view;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.LoomMenu;
import net.minecraft.world.level.block.entity.BannerPattern;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.block.banner.CraftPatternType;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.LoomInventory;
import org.bukkit.inventory.view.LoomView;

public class CraftLoomView extends CraftInventoryView<LoomMenu, LoomInventory> implements LoomView {

    public CraftLoomView(final HumanEntity player, final LoomInventory viewing, final LoomMenu container) {
        super(player, viewing, container);
    }

    @Override
    public List<PatternType> getSelectablePatterns() {
        final List<Holder<BannerPattern>> selectablePatterns = this.container.getSelectablePatterns();
        final List<PatternType> patternTypes = new ArrayList<>(selectablePatterns.size());
        for (final Holder<BannerPattern> selectablePattern : selectablePatterns) {
            patternTypes.add(CraftPatternType.minecraftHolderToBukkit(selectablePattern));
        }
        return patternTypes;
    }

    @Override
    public int getSelectedPatternIndex() {
        return this.container.getSelectedBannerPatternIndex();
    }
}
