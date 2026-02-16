package org.bukkit.craftbukkit.util;

import java.io.IOException;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.LevelStorageSource;

public final class DynamicBuiltinPacks {

    public static final DynamicBuiltinPack BUKKIT = new DynamicBuiltinPack(
        "bukkit",
        Component.literal("Persistent resources provided by plugins")
    );

    private static final List<DynamicBuiltinPack> PACKS = List.of(BUKKIT);

    public static void refreshAllMetadata(final LevelStorageSource.LevelStorageAccess storage) throws IOException {
        final DynamicBuiltinPack.LevelPathAccess access = DynamicBuiltinPack.LevelPathAccess.fromStorageSource(storage);
        for (final DynamicBuiltinPack pack : PACKS) {
            pack.refreshMetadata(access);
        }
    }
}
