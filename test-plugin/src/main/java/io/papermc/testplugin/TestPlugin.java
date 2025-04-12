package io.papermc.testplugin;

import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.event.connection.common.PlayerConnectionValidateLoginEvent;
import io.papermc.paper.event.connection.configuration.AsyncPlayerConnectionConfigurateEvent;
import io.papermc.paper.event.connection.configuration.PlayerConnectionInitialConfigurateEvent;
import io.papermc.paper.event.connection.configuration.PlayerConnectionReconfigurateEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    private String clear;

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

       registerCommand("clearchat", new BasicCommand() {
           @Override
           public void execute(final CommandSourceStack commandSourceStack, final String[] args) {
               if (commandSourceStack.getSender() instanceof Player player) {
                   clear = args[0];
                   player.getConnection().configurate();
               }
           }
       });
    }


    // // First: Check if on login they have any cookies or not. If they do kick them, else let them in.
    // @EventHandler
    // public void onPlayerPreConfigurate(AsyncPlayerPreLoginEvent event) {
    //     byte[] noCookies = event.getPlayerLoginConnection().retrieveCookie(NamespacedKey.fromString("paper:has_cookies")).join();
    //     boolean doYouHaveCookies = noCookies != null;
    //     System.out.println("Does player have cookies? " + doYouHaveCookies);
    //
    //     if (doYouHaveCookies) {
    //         event.disallow(AsyncPlayerPreLoginEvent.Result.KICK_OTHER, Component.text("No more cookies for you!"));
    //     }
    // }
    //
    // // On initial configuration, store the cookie
    // @EventHandler
    // public void onPlayerPreConfigurate(PlayerConnectionInitialConfigurateEvent event) {
    //     System.out.println("Giving " + event.getConfigurationConnection().getProfile().getName() + " a million cookies!");
    //     event.getConfigurationConnection().storeCookie(NamespacedKey.fromString("paper:has_cookies"), new byte[0]);
    // }
    //
    // // Now during config task, get their cookie state.
    // @EventHandler
    // public void asyncConfigurate(AsyncPlayerConnectionConfigurateEvent event) throws InterruptedException {
    //     Thread.sleep(5000); // wait 5 seconds cause i hate u
    //     event.getConfigurationConnection().transfer("127.0.0.1", 25565); // Transfer them to a server, now with our special cookie.
    // }

    @EventHandler
    public void loginEvent(PlayerConnectionReconfigurateEvent event) {
        if (Boolean.valueOf(clear)) {
            event.getConfigurationConnection().clearChat();
        }
        event.getConfigurationConnection().completeConfiguration();
    }

    @EventHandler
    public void onPostConnection(PlayerConnectionValidateLoginEvent event) {
        //event.disallow(PostPlayerConnectionLoginEvent.Result.KICK_FULL, Component.text("I HATE U!"));
    }

}
