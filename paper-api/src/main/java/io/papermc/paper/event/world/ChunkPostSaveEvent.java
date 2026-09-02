package io.papermc.paper.event.world;

import org.bukkit.Chunk;
import org.bukkit.event.HandlerList;
import org.bukkit.event.world.ChunkEvent;
import org.jetbrains.annotations.ApiStatus.Internal;
import org.jspecify.annotations.NullMarked;

/// Called during/after a chunk is saved, allowing plugins which store data separate but relating to a chunk to keep
/// that data in lockstep with the chunk's state, saving whenever the chunk itself is saved. Any modifications to the
/// chunk during this event are not guaranteed to be saved to the filesystem.
@NullMarked
public class ChunkPostSaveEvent extends ChunkEvent {
	private static final HandlerList HANDLER_LIST = new HandlerList();

	@Internal
	public ChunkPostSaveEvent(final Chunk chunk) {
		super(chunk);
	}

	public static HandlerList getHandlerList() {
		return HANDLER_LIST;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLER_LIST;
	}
}
