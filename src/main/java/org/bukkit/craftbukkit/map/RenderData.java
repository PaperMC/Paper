package org.bukkit.craftbukkit.map;

import java.util.ArrayList;
import org.bukkit.map.MapCursor;

public class RenderData {

    public byte[] buffer; // Paper
    public final ArrayList<MapCursor> cursors;

    public RenderData() {
        this.buffer = new byte[128 * 128];
        this.cursors = new ArrayList<MapCursor>();
    }

}
