package org.bukkit.craftbukkit.inventory.view;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.world.inventory.ContainerLoom;
import net.minecraft.world.level.block.entity.EnumBannerPatternType;
import org.bukkit.block.banner.PatternType;
import org.bukkit.craftbukkit.block.banner.CraftPatternType;
import org.bukkit.craftbukkit.inventory.CraftInventoryView;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.view.LoomView;

public class CraftLoomView extends CraftInventoryView<ContainerLoom> implements LoomView {

    public CraftLoomView(final HumanEntity player, final Inventory viewing, final ContainerLoom container) {
        super(player, viewing, container);
    }

    @Override
    public List<PatternType> getSelectablePatterns() {
        final List<Holder<EnumBannerPatternType>> selectablePatterns = container.getSelectablePatterns();
        final List<PatternType> patternTypes = new ArrayList<>(selectablePatterns.size());
        for (final Holder<EnumBannerPatternType> selectablePattern : selectablePatterns) {
            patternTypes.add(CraftPatternType.minecraftHolderToBukkit(selectablePattern));
        }
        return patternTypes;
    }

    @Override
    public int getSelectedPatternIndex() {
        return container.getSelectedBannerPatternIndex();
    }
}
