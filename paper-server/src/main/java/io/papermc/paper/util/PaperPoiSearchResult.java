package io.papermc.paper.util;

import io.papermc.paper.entity.PaperPoiType;
import io.papermc.paper.entity.poi.PoiSearchResult;
import io.papermc.paper.entity.poi.PoiType;
import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PaperPoiSearchResult(PoiType poiType, Location location) implements PoiSearchResult {

    public static PoiSearchResult from(PoiRecord record, World world) {
        return new PaperPoiSearchResult(PaperPoiType.minecraftHolderToBukkit(record.getPoiType()), CraftLocation.toBukkit(record.getPos(), world));
    }
}
