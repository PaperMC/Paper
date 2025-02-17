package io.papermc.paper.util;

import io.papermc.paper.entity.PoiType;
import org.bukkit.Location;
import org.jspecify.annotations.NullMarked;

@NullMarked
public record PaperPoiSearchResult(PoiType poiType, Location location) implements PoiSearchResult {
}
