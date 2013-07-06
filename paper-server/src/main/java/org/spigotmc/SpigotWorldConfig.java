package org.spigotmc;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

public class SpigotWorldConfig
{

    private final String worldName;
    private final YamlConfiguration config;
    private boolean verbose;

    public SpigotWorldConfig(String worldName)
    {
        this.worldName = worldName;
        this.config = SpigotConfig.config;
        this.init();
    }

    public void init()
    {
        this.verbose = this.getBoolean( "verbose", true );

        this.log( "-------- World Settings For [" + this.worldName + "] --------" );
        SpigotConfig.readConfig( SpigotWorldConfig.class, this );
    }

    private void log(String s)
    {
        if ( this.verbose )
        {
            Bukkit.getLogger().info( s );
        }
    }

    private void set(String path, Object val)
    {
        this.config.set( "world-settings.default." + path, val );
    }

    public boolean getBoolean(String path, boolean def)
    {
        this.config.addDefault( "world-settings.default." + path, def );
        return this.config.getBoolean( "world-settings." + this.worldName + "." + path, this.config.getBoolean( "world-settings.default." + path ) );
    }

    public double getDouble(String path, double def)
    {
        this.config.addDefault( "world-settings.default." + path, def );
        return this.config.getDouble( "world-settings." + this.worldName + "." + path, this.config.getDouble( "world-settings.default." + path ) );
    }

    public int getInt(String path)
    {
        return this.config.getInt( "world-settings." + this.worldName + "." + path );
    }

    public int getInt(String path, int def)
    {
        this.config.addDefault( "world-settings.default." + path, def );
        return this.config.getInt( "world-settings." + this.worldName + "." + path, this.config.getInt( "world-settings.default." + path ) );
    }

    public <T> List getList(String path, T def)
    {
        this.config.addDefault( "world-settings.default." + path, def );
        return (List<T>) this.config.getList( "world-settings." + this.worldName + "." + path, this.config.getList( "world-settings.default." + path ) );
    }

    public String getString(String path, String def)
    {
        this.config.addDefault( "world-settings.default." + path, def );
        return this.config.getString( "world-settings." + this.worldName + "." + path, this.config.getString( "world-settings.default." + path ) );
    }

    private Object get(String path, Object def)
    {
        this.config.addDefault( "world-settings.default." + path, def );
        return this.config.get( "world-settings." + this.worldName + "." + path, this.config.get( "world-settings.default." + path ) );
    }
}
