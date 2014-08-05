package org.spigotmc;

import java.io.File;
import java.util.List;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class RestartCommand extends Command
{

    public RestartCommand(String name)
    {
        super( name );
        this.description = "Restarts the server";
        this.usageMessage = "/restart";
        this.setPermission( "bukkit.command.restart" );
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args)
    {
        if ( this.testPermission( sender ) )
        {
            MinecraftServer.getServer().processQueue.add( new Runnable()
            {
                @Override
                public void run()
                {
                    RestartCommand.restart();
                }
            } );
        }
        return true;
    }

    public static void restart()
    {
        RestartCommand.restart( SpigotConfig.restartScript );
    }

    private static void restart(final String restartScript)
    {
        AsyncCatcher.enabled = false; // Disable async catcher incase it interferes with us
        try
        {
            String[] split = restartScript.split( " " );
            if ( split.length > 0 && new File( split[0] ).isFile() )
            {
                System.out.println( "Attempting to restart with " + restartScript );

                // Disable Watchdog
                WatchdogThread.doStop();

                // Kick all players
                for ( ServerPlayer p : (List<ServerPlayer>) MinecraftServer.getServer().getPlayerList().players )
                {
                    p.connection.disconnect( CraftChatMessage.fromStringOrEmpty( SpigotConfig.restartMessage, true ) );
                }
                // Give the socket a chance to send the packets
                try
                {
                    Thread.sleep( 100 );
                } catch ( InterruptedException ex )
                {
                }
                // Close the socket so we can rebind with the new process
                MinecraftServer.getServer().getConnection().stop();

                // Give time for it to kick in
                try
                {
                    Thread.sleep( 100 );
                } catch ( InterruptedException ex )
                {
                }

                // Actually shutdown
                try
                {
                    MinecraftServer.getServer().close();
                } catch ( Throwable t )
                {
                }

                // This will be done AFTER the server has completely halted
                Thread shutdownHook = new Thread()
                {
                    @Override
                    public void run()
                    {
                        try
                        {
                            String os = System.getProperty( "os.name" ).toLowerCase(java.util.Locale.ENGLISH);
                            if ( os.contains( "win" ) )
                            {
                                Runtime.getRuntime().exec( "cmd /c start " + restartScript );
                            } else
                            {
                                Runtime.getRuntime().exec( "sh " + restartScript );
                            }
                        } catch ( Exception e )
                        {
                            e.printStackTrace();
                        }
                    }
                };

                shutdownHook.setDaemon( true );
                Runtime.getRuntime().addShutdownHook( shutdownHook );
            } else
            {
                System.out.println( "Startup script '" + SpigotConfig.restartScript + "' does not exist! Stopping server." );

                // Actually shutdown
                try
                {
                    MinecraftServer.getServer().close();
                } catch ( Throwable t )
                {
                }
            }
            System.exit( 0 );
        } catch ( Exception ex )
        {
            ex.printStackTrace();
        }
    }
}
