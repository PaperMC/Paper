package com.destroystokyo.paper.util.misc;

import net.minecraft.server.EntityPlayer;

public class PlayerDistanceTrackingAreaMap extends DistanceTrackingAreaMap<EntityPlayer> {

    public PlayerDistanceTrackingAreaMap() {
        super();
    }

    public PlayerDistanceTrackingAreaMap(final PooledLinkedHashSets<EntityPlayer> pooledHashSets) {
        super(pooledHashSets);
    }

    public PlayerDistanceTrackingAreaMap(final PooledLinkedHashSets<EntityPlayer> pooledHashSets, final ChangeCallback<EntityPlayer> addCallback,
                                         final ChangeCallback<EntityPlayer> removeCallback, final DistanceChangeCallback<EntityPlayer> distanceChangeCallback) {
        super(pooledHashSets, addCallback, removeCallback, distanceChangeCallback);
    }

    @Override
    protected PooledLinkedHashSets.PooledObjectLinkedOpenHashSet<EntityPlayer> getEmptySetFor(final EntityPlayer player) {
        return player.cachedSingleHashSet;
    }
}
