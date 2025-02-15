package io.papermc.paper.util;

import io.papermc.paper.entity.PoiType;
import org.bukkit.Location;

public class PaperPoiSearchResult implements PoiSearchResult {

    private final PoiType type;
    private final Location location;
    public PaperPoiSearchResult(final PoiType type, final Location location) {
        this.location = location;
        this.type = type;
    }

    @Override
    public PoiType getPoiType() {
        return this.type;
    }

    @Override
    public Location getLocation() {
        return this.location;
    }
}
