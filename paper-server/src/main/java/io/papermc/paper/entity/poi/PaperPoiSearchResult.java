package io.papermc.paper.entity.poi;

import net.minecraft.world.entity.ai.village.poi.PoiRecord;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PaperPoiSearchResult(PoiType poiType, Location location) implements PoiSearchResult {

    @Override
    public Location location() {
        return location.clone();
    }

    public static PoiSearchResult from(PoiRecord record, World world) {
        return new PaperPoiSearchResult(PaperPoiType.minecraftHolderToBukkit(record.getPoiType()), CraftLocation.toBukkit(record.getPos(), world));
    }
}
