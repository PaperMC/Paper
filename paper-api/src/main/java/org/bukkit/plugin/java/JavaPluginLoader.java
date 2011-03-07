package org.bukkit.plugin.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.Map;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.regex.Pattern;
import org.bukkit.Server;
import org.bukkit.event.CustomEventListener;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.event.server.*;
import org.bukkit.event.vehicle.*;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldEvent;
import org.bukkit.event.world.WorldListener;
import org.bukkit.plugin.*;

/**
 * Represents a Java plugin loader, allowing plugins in the form of .jar
 */
public final class JavaPluginLoader implements PluginLoader {
    private final Server server;
    private final Pattern[] fileFilters = new Pattern[] {
            Pattern.compile("\\.jar$"),
    };
    private final Map<String, Class<?>> classes = new HashMap<String, Class<?>>();
    private final Map<String, File> files = new HashMap<String, File>();

    public JavaPluginLoader(Server instance) {
        server = instance;
    }

    public Plugin loadPlugin(File file) throws InvalidPluginException, InvalidDescriptionException, UnknownDependencyException {
        JavaPlugin result = null;
        PluginDescriptionFile description = null;

        if (!file.exists()) {
            throw new InvalidPluginException(new FileNotFoundException(String.format("%s does not exist", file.getPath())));
        }
        try {
            JarFile jar = new JarFile(file);
            JarEntry entry = jar.getJarEntry("plugin.yml");

            if (entry == null) {
                throw new InvalidPluginException(new FileNotFoundException("Jar does not contain plugin.yml"));
            }

            InputStream stream = jar.getInputStream(entry);
            description = new PluginDescriptionFile(stream);

            stream.close();
            jar.close();
        } catch (IOException ex) {
            throw new InvalidPluginException(ex);
        }

        File dataFolder = getDataFolder(file);

        ArrayList<String> depend;
        try {
            depend = (ArrayList)description.getDepend();
            if(depend == null) {
                depend = new ArrayList<String>();
            }
        } catch (ClassCastException ex) {
             throw new InvalidPluginException(ex);
        }

        ArrayList<File> dependFiles = new ArrayList<File>();

        for(String pluginName : depend) {
            if(files == null) {
                throw new UnknownDependencyException(pluginName);
            }
            File current = files.get(pluginName);
            if(current == null) {
                throw new UnknownDependencyException(pluginName);
            }
            dependFiles.add(current);
        }

        try {
            URL[] urls = new URL[dependFiles.size() + 1];
            urls[0] = file.toURI().toURL();
            int cnt = 1;
            for(File f : dependFiles) {
                urls[cnt++] = f.toURI().toURL();
            }
            ClassLoader loader = new PluginClassLoader(this, urls, getClass().getClassLoader());
            Class<?> jarClass = Class.forName(description.getMain(), true, loader);
            Class<? extends JavaPlugin> plugin = jarClass.asSubclass(JavaPlugin.class);

            Constructor<? extends JavaPlugin> constructor = plugin.getConstructor();
            result = constructor.newInstance();
            
            result.initialize(this, server, description, dataFolder, file, loader);
        } catch (Throwable ex) {
            throw new InvalidPluginException(ex);
        }

        files.put(description.getName(), file);

        return (Plugin)result;
    }

    private File getDataFolder(File file) {
        File dataFolder = null;

        String filename = file.getName();
        int index = file.getName().lastIndexOf(".");

        if (index != -1) {
            String name = filename.substring(0, index);
            dataFolder = new File(file.getParentFile(), name);
        } else {
            // This is if there is no extension, which should not happen
            // Using _ to prevent name collision
            dataFolder = new File(file.getParentFile(), filename + "_");
        }

        //dataFolder.mkdirs();

        return dataFolder;
    }

    public Pattern[] getPluginFileFilters() {
        return fileFilters;
    }

    public Class<?> getClassByName(final String name) {
        return classes.get(name);
    }

    public void setClass(final String name, final Class<?> clazz) {
        if(!classes.containsKey(name)) {
            classes.put(name, clazz);
        }
    }

    public EventExecutor createExecutor( Event.Type type, Listener listener ) {
        // TODO: remove multiple Listener type and hence casts
        switch (type) {
        // Player Events
        case PLAYER_JOIN:
            return new EventExecutor() {
                public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerJoin( (PlayerEvent)event );
                }
            };
        case PLAYER_QUIT:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerQuit( (PlayerEvent)event );
                }
            };
        case PLAYER_RESPAWN:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerRespawn( (PlayerRespawnEvent)event );
                }
            };
        case PLAYER_KICK:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerKick( (PlayerKickEvent)event );
                }
            };
        case PLAYER_COMMAND_PREPROCESS:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerCommandPreprocess( (PlayerChatEvent)event );
                }
            };
        case PLAYER_CHAT:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerChat( (PlayerChatEvent)event );
                }
            };
        case PLAYER_MOVE:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerMove( (PlayerMoveEvent)event );
                }
            };
        case PLAYER_TELEPORT:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerTeleport( (PlayerMoveEvent)event );
                }
            };
        case PLAYER_ITEM:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerItem( (PlayerItemEvent)event );
                }
            };
        case PLAYER_LOGIN:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerLogin( (PlayerLoginEvent)event );
                }
            };
        case PLAYER_EGG_THROW:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerEggThrow( (PlayerEggThrowEvent)event );
                }
            };
        case PLAYER_ANIMATION:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerAnimation( (PlayerAnimationEvent)event );
                }
            };
        case INVENTORY_OPEN:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onInventoryOpen( (PlayerInventoryEvent)event );
                }
            };
        case PLAYER_ITEM_HELD:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onItemHeldChange( (PlayerItemHeldEvent)event );
                }
            };
        case PLAYER_DROP_ITEM:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerDropItem( (PlayerDropItemEvent)event );
                }
            };
        case PLAYER_PICKUP_ITEM:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerPickupItem( (PlayerPickupItemEvent)event );
                }
            };
        case PLAYER_TOGGLE_SNEAK:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((PlayerListener)listener).onPlayerToggleSneak( (PlayerToggleSneakEvent)event );
                }
            };

        // Block Events
        case BLOCK_PHYSICS:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((BlockListener)listener).onBlockPhysics( (BlockPhysicsEvent)event );
                }
            };
        case BLOCK_CANBUILD:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((BlockListener)listener).onBlockCanBuild( (BlockCanBuildEvent)event );
                }
            };
        case BLOCK_RIGHTCLICKED:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((BlockListener)listener).onBlockRightClick( (BlockRightClickEvent)event );
                }
            };
        case BLOCK_PLACED:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((BlockListener)listener).onBlockPlace( (BlockPlaceEvent)event );
                }
            };
        case BLOCK_DAMAGED:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((BlockListener)listener).onBlockDamage( (BlockDamageEvent)event );
                }
            };
        case BLOCK_INTERACT:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((BlockListener)listener).onBlockInteract( (BlockInteractEvent)event );
                }
            };
        case BLOCK_FLOW:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((BlockListener)listener).onBlockFlow( (BlockFromToEvent)event );
                }
            };
        case LEAVES_DECAY:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((BlockListener)listener).onLeavesDecay( (LeavesDecayEvent)event );
                }
            };
        case SIGN_CHANGE:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((BlockListener)listener).onSignChange( (SignChangeEvent)event );
                }
            };
        case BLOCK_IGNITE:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((BlockListener)listener).onBlockIgnite( (BlockIgniteEvent)event );
                }
            };
        case REDSTONE_CHANGE:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((BlockListener)listener).onBlockRedstoneChange( (BlockRedstoneEvent)event );
                }
            };
        case BLOCK_BURN:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((BlockListener)listener).onBlockBurn( (BlockBurnEvent)event );
                }
            };
        case BLOCK_BREAK:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((BlockListener)listener).onBlockBreak( (BlockBreakEvent)event );
                }
            };

        // Server Events
        case PLUGIN_ENABLE:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((ServerListener)listener).onPluginEnabled( (PluginEvent)event );
                }
            };
        case PLUGIN_DISABLE:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((ServerListener)listener).onPluginDisabled( (PluginEvent)event );
                }
            };

        case SERVER_COMMAND:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((ServerListener)listener).onServerCommand( (ServerCommandEvent)event );
                }
            };

        // World Events
        case CHUNK_LOADED:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((WorldListener)listener).onChunkLoaded( (ChunkLoadEvent)event );
                }
            };
        case CHUNK_UNLOADED:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((WorldListener)listener).onChunkUnloaded( (ChunkUnloadEvent)event );
                }
            };
        case WORLD_SAVED:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((WorldListener)listener).onWorldSaved( (WorldEvent)event );
                }
            };
        case WORLD_LOADED:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((WorldListener)listener).onWorldLoaded( (WorldEvent)event );
                }
            };

        // Entity Events
        case ENTITY_DAMAGED:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((EntityListener)listener).onEntityDamage( (EntityDamageEvent)event );
                }
            };
        case ENTITY_DEATH:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((EntityListener)listener).onEntityDeath( (EntityDeathEvent)event );
                }
            };
        case ENTITY_COMBUST:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((EntityListener)listener).onEntityCombust( (EntityCombustEvent)event );
                }
            };
        case ENTITY_EXPLODE:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((EntityListener)listener).onEntityExplode( (EntityExplodeEvent)event );
                }
            };
        case EXPLOSION_PRIMED:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((EntityListener)listener).onExplosionPrimed( (ExplosionPrimedEvent)event );
                }
            };
        case ENTITY_TARGET:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((EntityListener)listener).onEntityTarget( (EntityTargetEvent)event );
                }
            };
        case CREATURE_SPAWN:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((EntityListener)listener).onCreatureSpawn( (CreatureSpawnEvent)event );
                }
            };

        // Vehicle Events
        case VEHICLE_CREATE:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((VehicleListener)listener).onVehicleCreate( (VehicleCreateEvent)event );
                }
            };
        case VEHICLE_DAMAGE:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((VehicleListener)listener).onVehicleDamage( (VehicleDamageEvent)event );
                }
            };
        case VEHICLE_COLLISION_BLOCK:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((VehicleListener)listener).onVehicleBlockCollision( (VehicleBlockCollisionEvent)event );
                }
            };
        case VEHICLE_COLLISION_ENTITY:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((VehicleListener)listener).onVehicleEntityCollision( (VehicleEntityCollisionEvent)event );
                }
            };
        case VEHICLE_ENTER:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((VehicleListener)listener).onVehicleEnter( (VehicleEnterEvent)event );
                }
            };
        case VEHICLE_EXIT:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((VehicleListener)listener).onVehicleExit( (VehicleExitEvent)event );
                }
            };
        case VEHICLE_MOVE:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((VehicleListener)listener).onVehicleMove( (VehicleMoveEvent)event );
                }
            };
        case VEHICLE_UPDATE:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((VehicleListener)listener).onVehicleUpdate((VehicleEvent)event);
                }
            };

        // Custom Events
        case CUSTOM_EVENT:
            return new EventExecutor() { public void execute( Listener listener, Event event ) {
                    ((CustomEventListener)listener).onCustomEvent( event );
                }
            };
        }

        throw new IllegalArgumentException( "Event " + type + " is not supported" );
    }

    public void enablePlugin(final Plugin plugin) {
        if (!(plugin instanceof JavaPlugin)) {
            throw new IllegalArgumentException("Plugin is not associated with this PluginLoader");
        }

        if (!plugin.isEnabled()) {
            JavaPlugin jPlugin = (JavaPlugin)plugin;

            jPlugin.setEnabled(true);
            server.getPluginManager().callEvent(new PluginEvent(Event.Type.PLUGIN_ENABLE, plugin));
        }
    }

    public void disablePlugin(Plugin plugin) {
        if (!(plugin instanceof JavaPlugin)) {
            throw new IllegalArgumentException("Plugin is not associated with this PluginLoader");
        }

        if (plugin.isEnabled()) {
            JavaPlugin jPlugin = (JavaPlugin)plugin;
            ClassLoader cloader = jPlugin.getClassLoader();

            jPlugin.setEnabled(false);

            server.getPluginManager().callEvent(new PluginEvent(Event.Type.PLUGIN_DISABLE, plugin));

            files.remove(jPlugin.getDescription().getName());

            if (cloader instanceof PluginClassLoader) {
                PluginClassLoader loader = (PluginClassLoader)cloader;
                Set<String> names = loader.getClasses();

                for (String name : names) {
                    classes.remove(name);
                }
            }
        }
    }
}
