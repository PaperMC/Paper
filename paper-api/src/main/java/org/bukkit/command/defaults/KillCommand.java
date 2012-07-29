package org.bukkit.command.defaults;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent;

public class KillCommand extends VanillaCommand {
    public KillCommand() {
        super("kill");
        this.description = "Commits suicide, only usable as a player";
        this.usageMessage = "/kill";
        this.setPermission("bukkit.command.kill");
    }

    @Override
    public boolean execute(CommandSender sender, String currentAlias, String[] args) {
        if (!testPermission(sender)) return true;

        if (sender instanceof Player) {
            Player player = (Player) sender;

            EntityDamageEvent ede = new EntityDamageEvent(player, EntityDamageEvent.DamageCause.SUICIDE, 1000);
            Bukkit.getPluginManager().callEvent(ede);
            if (ede.isCancelled()) return true;

            ede.getEntity().setLastDamageCause(ede);
            player.damage(ede.getDamage());
            sender.sendMessage("Ouch. That look like it hurt.");
        } else {
            sender.sendMessage("You can only perform this command as a player");
        }

        return true;
    }

    @Override
    public boolean matches(String input) {
        return input.equalsIgnoreCase("kill");
    }
}
