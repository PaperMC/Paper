package io.papermc.testplugin;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.EntitySelectorArgumentResolver;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.literal;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        // io.papermc.testplugin.brigtests.Registration.registerViaOnEnable(this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            event.registrar().register(
                literal("hide")
                    .executes(ctx -> {
                        ctx.getSource().getSender().sendMessage("hide");
                        return Command.SINGLE_SUCCESS;
                    })
                    .then(literal("something-else")
                              .executes(ctx -> {
                                    ctx.getSource().getSender().sendMessage("hide something-else");
                                    return Command.SINGLE_SUCCESS;
                              })).build());

            event.registrar().register(
                literal("chat")
                    .then(literal("hide")
                              .executes(ctx -> {
                                    ctx.getSource().getSender().sendMessage("chat hide");
                                    return Command.SINGLE_SUCCESS;
                              })).build());
        });

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event ->  {
            final Commands registrar = event.registrar();
            final var node = Commands.literal("target").then(
                Commands.literal("type").executes(ctx -> {
                    final CommandSourceStack source = ctx.getSource();
                    source.getSender().sendMessage(source.getExecutor().getType().toString());
                    return Command.SINGLE_SUCCESS;
                })
            ).build();
            registrar.register(node);

            event.registrar().register(
                Commands.literal("test-execute").then(
                    Commands.argument("entity", ArgumentTypes.entities())
                        .fork(node, ctx -> {
                            return ctx.getArgument("entity", EntitySelectorArgumentResolver.class).resolve(ctx.getSource()).stream().map(e -> ctx.getSource().withExecutor(e)).toList();
                        })
                ).build()
            );

            event.registrar().register(
                Commands.literal("root-execute")
                    .redirect(event.registrar().getDispatcher().getRoot())
                    .build()
            );
        });
    }
}
