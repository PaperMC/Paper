package io.papermc.paper.command.brigadier.position;

import io.papermc.paper.command.brigadier.argument.position.ColumnBlockPosition;

public record ColumnBlockPositionImpl(int blockX, int blockZ) implements ColumnBlockPosition {
}
