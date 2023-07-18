package org.bukkit.craftbukkit.block;

import com.google.common.base.Preconditions;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.World;
import org.bukkit.block.DecoratedPot;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;

public class CraftDecoratedPot extends CraftBlockEntityState<DecoratedPotBlockEntity> implements DecoratedPot {

    public CraftDecoratedPot(World world, DecoratedPotBlockEntity tileEntity) {
        super(world, tileEntity);
    }

    @Override
    public void setSherd(Side face, Material sherd) {
        Preconditions.checkArgument(face != null, "face must not be null");
        Preconditions.checkArgument(sherd == null || sherd == Material.BRICK || Tag.ITEMS_DECORATED_POT_SHERDS.isTagged(sherd), "sherd is not a valid sherd material: %s", sherd);

        Item sherdItem = (sherd != null) ? CraftMagicNumbers.getItem(sherd) : Items.BRICK;
        DecoratedPotBlockEntity.a decorations = getSnapshot().getDecorations(); // PAIL rename Decorations

        switch (face) {
            case BACK -> getSnapshot().decorations = new DecoratedPotBlockEntity.a(sherdItem, decorations.left(), decorations.right(), decorations.front());
            case LEFT -> getSnapshot().decorations = new DecoratedPotBlockEntity.a(decorations.back(), sherdItem, decorations.right(), decorations.front());
            case RIGHT -> getSnapshot().decorations = new DecoratedPotBlockEntity.a(decorations.back(), decorations.left(), sherdItem, decorations.front());
            case FRONT -> getSnapshot().decorations = new DecoratedPotBlockEntity.a(decorations.back(), decorations.left(), decorations.right(), sherdItem);
            default -> throw new IllegalArgumentException("Unexpected value: " + face);
        }
    }

    @Override
    public Material getSherd(Side face) {
        Preconditions.checkArgument(face != null, "face must not be null");

        DecoratedPotBlockEntity.a decorations = getSnapshot().getDecorations(); // PAIL rename Decorations
        Item sherdItem = switch (face) {
            case BACK -> decorations.back();
            case LEFT -> decorations.left();
            case RIGHT -> decorations.right();
            case FRONT -> decorations.front();
            default -> throw new IllegalArgumentException("Unexpected value: " + face);
        };

        return CraftMagicNumbers.getMaterial(sherdItem);
    }

    @Override
    public Map<Side, Material> getSherds() {
        DecoratedPotBlockEntity.a decorations = getSnapshot().getDecorations(); // PAIL rename Decorations

        Map<Side, Material> sherds = new EnumMap<>(Side.class);
        sherds.put(Side.BACK, CraftMagicNumbers.getMaterial(decorations.back()));
        sherds.put(Side.LEFT, CraftMagicNumbers.getMaterial(decorations.left()));
        sherds.put(Side.RIGHT, CraftMagicNumbers.getMaterial(decorations.right()));
        sherds.put(Side.FRONT, CraftMagicNumbers.getMaterial(decorations.front()));
        return sherds;
    }

    @Override
    public List<Material> getShards() {
        return getSnapshot().getDecorations().sorted().map(CraftMagicNumbers::getMaterial).collect(Collectors.toUnmodifiableList());
    }
}
