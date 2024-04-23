package org.bukkit.craftbukkit.map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.level.saveddata.maps.WorldMap;
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

    @Override
    public int getId() {
        return worldMap.id.id();
    }

    @Override
    public boolean isVirtual() {
        return renderers.size() > 0 && !(renderers.get(0) instanceof CraftMapRenderer);
    }

    @Override
    public Scale getScale() {
        return Scale.valueOf(worldMap.scale);
    }

    @Override
    public void setScale(Scale scale) {
        worldMap.scale = scale.getValue();
    }

    @Override
    public World getWorld() {
        ResourceKey<net.minecraft.world.level.World> dimension = worldMap.dimension;
        WorldServer world = MinecraftServer.getServer().getLevel(dimension);

        if (world != null) {
            return world.getWorld();
        }

        if (worldMap.uniqueId != null) {
            return Bukkit.getServer().getWorld(worldMap.uniqueId);
        }
        return null;
    }

    @Override
    public void setWorld(World world) {
        worldMap.dimension = ((CraftWorld) world).getHandle().dimension();
        worldMap.uniqueId = world.getUID();
    }

    @Override
    public int getCenterX() {
        return worldMap.centerX;
    }

    @Override
    public int getCenterZ() {
        return worldMap.centerZ;
    }

    @Override
    public void setCenterX(int x) {
        worldMap.centerX = x;
    }

    @Override
    public void setCenterZ(int z) {
        worldMap.centerZ = z;
    }

    @Override
    public List<MapRenderer> getRenderers() {
        return new ArrayList<MapRenderer>(renderers);
    }

    @Override
    public void addRenderer(MapRenderer renderer) {
        if (!renderers.contains(renderer)) {
            renderers.add(renderer);
            canvases.put(renderer, new HashMap<CraftPlayer, CraftMapCanvas>());
            renderer.initialize(this);
        }
    }

    @Override
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
            try {
                renderer.render(this, canvas, player);
            } catch (Throwable ex) {
                Bukkit.getLogger().log(Level.SEVERE, "Could not render map using renderer " + renderer.getClass().getName(), ex);
            }

            byte[] buf = canvas.getBuffer();
            for (int i = 0; i < buf.length; ++i) {
                byte color = buf[i];
                // There are 248 valid color id's, 0 -> 127 and -128 -> -9
                if (color >= 0 || color <= -9) render.buffer[i] = color;
            }

            for (int i = 0; i < canvas.getCursors().size(); ++i) {
                render.cursors.add(canvas.getCursors().getCursor(i));
            }
        }

        return render;
    }

    @Override
    public boolean isTrackingPosition() {
        return worldMap.trackingPosition;
    }

    @Override
    public void setTrackingPosition(boolean trackingPosition) {
        worldMap.trackingPosition = trackingPosition;
    }

    @Override
    public boolean isUnlimitedTracking() {
        return worldMap.unlimitedTracking;
    }

    @Override
    public void setUnlimitedTracking(boolean unlimited) {
        worldMap.unlimitedTracking = unlimited;
    }

    @Override
    public boolean isLocked() {
        return worldMap.locked;
    }

    @Override
    public void setLocked(boolean locked) {
        worldMap.locked = locked;
    }
}
