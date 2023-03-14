package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.DecoratedPot;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftDecoratedPot extends CraftBlockEntityState<DecoratedPotBlockEntity> implements DecoratedPot {

    public CraftDecoratedPot(World world, DecoratedPotBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public List<Material> getShards() {
        return getSnapshot().getShards().stream().map(CraftMagicNumbers::getMaterial).collect(Collectors.toUnmodifiableList());
    }

    @Override
    public void addShard(Material material) {
        Preconditions.checkArgument(material != null && material.isItem(), "Material must be an item");

        getSnapshot().getShards().add(CraftMagicNumbers.getItem(material));
    }

    @Override
    public void setShards(List<Material> shard) {
        getSnapshot().getShards().clear();

        for (Material material : shard) {
            addShard(material);
        }
    }
}
