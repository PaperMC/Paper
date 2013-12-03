package org.bukkit.craftbukkit.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.server.WorldMap;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;

public final class CraftMapView implements MapView {

    private final Map<CraftPlayer, RenderData> renderCache = new HashMap<CraftPlayer, RenderData>();
    private final List<MapRenderer> renderers = new ArrayList<MapRenderer>();
    private final Map<MapRenderer, Map<CraftPlayer, CraftMapCanvas>> canvases = new HashMap<MapRenderer, Map<CraftPlayer, CraftMapCanvas>>();
    protected final WorldMap worldMap;

    public CraftMapView(WorldMap worldMap) {
        this.worldMap = worldMap;
        addRenderer(new CraftMapRenderer(this, worldMap));
    }

    public short getId() {
        String text = worldMap.id;
        if (text.startsWith("map_")) {
            try {
                return Short.parseShort(text.substring("map_".length()));
            }
            catch (NumberFormatException ex) {
                throw new IllegalStateException("Map has non-numeric ID");
            }
        } else {
            throw new IllegalStateException("Map has invalid ID");
        }
    }

    public boolean isVirtual() {
        return renderers.size() > 0 && !(renderers.get(0) instanceof CraftMapRenderer);
    }

    public Scale getScale() {
        return Scale.valueOf(worldMap.scale);
    }

    public void setScale(Scale scale) {
        worldMap.scale = scale.getValue();
    }

    public World getWorld() {
        byte dimension = worldMap.map;
        for (World world : Bukkit.getServer().getWorlds()) {
            if (((CraftWorld) world).getHandle().dimension == dimension) {
                return world;
            }
        }
        return null;
    }

    public void setWorld(World world) {
        worldMap.map = (byte) ((CraftWorld) world).getHandle().dimension;
    }

    public int getCenterX() {
        return worldMap.centerX;
    }

    public int getCenterZ() {
        return worldMap.centerZ;
    }

    public void setCenterX(int x) {
        worldMap.centerX = x;
    }

    public void setCenterZ(int z) {
        worldMap.centerZ = z;
    }

    public List<MapRenderer> getRenderers() {
        return new ArrayList<MapRenderer>(renderers);
    }

    public void addRenderer(MapRenderer renderer) {
        if (!renderers.contains(renderer)) {
            renderers.add(renderer);
            canvases.put(renderer, new HashMap<CraftPlayer, CraftMapCanvas>());
            renderer.initialize(this);
        }
    }

    public boolean removeRenderer(MapRenderer renderer) {
        if (renderers.contains(renderer)) {
            renderers.remove(renderer);
            for (Map.Entry<CraftPlayer, CraftMapCanvas> entry : canvases.get(renderer).entrySet()) {
                for (int x = 0; x < 128; ++x) {
                    for (int y = 0; y < 128; ++y) {
                        entry.getValue().setPixel(x, y, (byte) -1);
                    }
                }
            }
            canvases.remove(renderer);
            return true;
        } else {
            return false;
        }
    }

    private boolean isContextual() {
        for (MapRenderer renderer : renderers) {
            if (renderer.isContextual()) return true;
        }
        return false;
    }

    public RenderData render(CraftPlayer player) {
        boolean context = isContextual();
        RenderData render = renderCache.get(context ? player : null);

        if (render == null) {
            render = new RenderData();
            renderCache.put(context ? player : null, render);
        }

        if (context && renderCache.containsKey(null)) {
            renderCache.remove(null);
        }

        Arrays.fill(render.buffer, (byte) 0);
        render.cursors.clear();

        for (MapRenderer renderer : renderers) {
            CraftMapCanvas canvas = canvases.get(renderer).get(renderer.isContextual() ? player : null);
            if (canvas == null) {
                canvas = new CraftMapCanvas(this);
                canvases.get(renderer).put(renderer.isContextual() ? player : null, canvas);
            }

            canvas.setBase(render.buffer);
            renderer.render(this, canvas, player);

            byte[] buf = canvas.getBuffer();
            for (int i = 0; i < buf.length; ++i) {
                byte color = buf[i];
                // There are 143 valid color id's, 0 -> 127 and -128 -> -113
                if (color >= 0 || color <= -113) render.buffer[i] = color;
            }

            for (int i = 0; i < canvas.getCursors().size(); ++i) {
                render.cursors.add(canvas.getCursors().getCursor(i));
            }
        }

        return render;
    }

}
