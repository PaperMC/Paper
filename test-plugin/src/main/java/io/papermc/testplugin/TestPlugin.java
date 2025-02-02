package io.papermc.testplugin;

import com.mojang.brigadier.arguments.StringArgumentType;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.MenuType;
import org.bukkit.inventory.Merchant;
import org.bukkit.inventory.view.builder.LocationInventoryViewBuilder;
import org.bukkit.inventory.view.builder.MerchantInventoryViewBuilder;
import org.bukkit.plugin.java.JavaPlugin;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);
        getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, (handler) -> {
            handler.registrar().register(
                    literal("menu-open")
                            .then(argument("menu-type", ArgumentTypes.resource(RegistryKey.MENU))
                                    .executes(c -> open(c.getSource().getSender(), null, null, null, c.getArgument("menu-type", MenuType.class)))
                                    .then(argument("location", ArgumentTypes.blockPosition())
                                            .executes(c -> open(c.getSource().getSender(), c.getArgument("location", BlockPositionResolver.class).resolve(c.getSource()), null, null, c.getArgument("menu-type", MenuType.class)))
                                            .then(argument("title", StringArgumentType.greedyString())
                                                    .executes(c -> open(c.getSource().getSender(), c.getArgument("location", BlockPositionResolver.class).resolve(c.getSource()), null, Component.text(c.getArgument("title", String.class)), c.getArgument("menu-type", MenuType.class)))
                                            )
                                    )
                                    .then(argument("entity", ArgumentTypes.entity())
                                            .executes(c -> open(c.getSource().getSender(), null, c.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(c.getSource()).getFirst(), null, c.getArgument("menu-type", MenuType.class)))
                                            .then(argument("title", StringArgumentType.greedyString())
                                                    .executes(c -> open(c.getSource().getSender(), null, c.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(c.getSource()).getFirst(), Component.text(c.getArgument("title", String.class)), c.getArgument("menu-type", MenuType.class)))
                                            )
                                    )
                            )
                            .build()
            );
        });
        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);
    }

    @NullMarked
    private int open(CommandSender sender, @Nullable BlockPosition at, @Nullable Entity entity, @Nullable Component title, @Nullable MenuType type) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Not Player");
            return 1;
        }

        final var builder = type.typed().builder();
        if (at != null && builder instanceof LocationInventoryViewBuilder<InventoryView> locbuilder) {
            locbuilder.location(at.toLocation(player.getWorld()));
        }

        if (entity instanceof Merchant merchant && builder instanceof MerchantInventoryViewBuilder<InventoryView> merchantbuilder) {
            merchantbuilder.merchant(merchant);
        }

        if (title != null) {
            builder.title(title);
        }

        player.openInventory(builder.build(player));
        return 1;
    }
}
