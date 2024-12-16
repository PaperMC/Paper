package org.bukkit.craftbukkit.map;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.map.MapCursor;

public class RenderData {

    public final List<MapCursor> cursors = new ArrayList<>();
    public byte[] buffer = new byte[128 * 128];
}
