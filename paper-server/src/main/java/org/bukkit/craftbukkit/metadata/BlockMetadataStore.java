package org.bukkit.craftbukkit.metadata;

import com.google.common.base.Preconditions;
import java.util.List;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.metadata.MetadataStore;
import org.bukkit.metadata.MetadataStoreBase;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.plugin.Plugin;

/**
 * A BlockMetadataStore stores metadata values for {@link Block} objects.
 */
public class BlockMetadataStore extends MetadataStoreBase<Block> implements MetadataStore<Block> {

    private final World owningWorld;

    /**
     * Initializes a BlockMetadataStore.
     * @param owningWorld The world to which this BlockMetadataStore belongs.
     */
    public BlockMetadataStore(World owningWorld) {
        this.owningWorld = owningWorld;
    }

    /**
     * Generates a unique metadata key for a {@link Block} object based on its coordinates in the world.
     * @param block the block
     * @param metadataKey The name identifying the metadata value
     * @return a unique metadata key
     * @see MetadataStoreBase#disambiguate(Object, String)
     */
    @Override
    protected String disambiguate(Block block, String metadataKey) {
        return Integer.toString(block.getX()) + ":" + Integer.toString(block.getY()) + ":" + Integer.toString(block.getZ()) + ":" + metadataKey;
    }

    /**
     * Retrieves the metadata for a {@link Block}, ensuring the block being asked for actually belongs to this BlockMetadataStore's
     * owning world.
     * @see MetadataStoreBase#getMetadata(Object, String)
     */
    @Override
    public List<MetadataValue> getMetadata(Block block, String metadataKey) {
        Preconditions.checkArgument(block.getWorld() == this.owningWorld, "Block does not belong to world %s", owningWorld.getName());
        return super.getMetadata(block, metadataKey);
    }

    /**
     * Tests to see if a metadata value has been added to a {@link Block}, ensuring the block being interrogated belongs
     * to this BlockMetadataStore's owning world.
     * @see MetadataStoreBase#hasMetadata(Object, String)
     */
    @Override
    public boolean hasMetadata(Block block, String metadataKey) {
        Preconditions.checkArgument(block.getWorld() == this.owningWorld, "Block does not belong to world %s", owningWorld.getName());
        return super.hasMetadata(block, metadataKey);
    }

    /**
     * Removes metadata from from a {@link Block} belonging to a given {@link Plugin}, ensuring the block being deleted from belongs
     * to this BlockMetadataStore's owning world.
     * @see MetadataStoreBase#removeMetadata(Object, String, org.bukkit.plugin.Plugin)
     */
    @Override
    public void removeMetadata(Block block, String metadataKey, Plugin owningPlugin) {
        Preconditions.checkArgument(block.getWorld() == this.owningWorld, "Block does not belong to world %s", owningWorld.getName());
        super.removeMetadata(block, metadataKey, owningPlugin);
    }

    /**
     * Sets or overwrites a metadata value on a {@link Block} from a given {@link Plugin}, ensuring the target block belongs
     * to this BlockMetadataStore's owning world.
     * @see MetadataStoreBase#setMetadata(Object, String, org.bukkit.metadata.MetadataValue)
     */
    @Override
    public void setMetadata(Block block, String metadataKey, MetadataValue newMetadataValue) {
        Preconditions.checkArgument(block.getWorld() == this.owningWorld, "Block does not belong to world %s", owningWorld.getName());
        super.setMetadata(block, metadataKey, newMetadataValue);
    }
}
