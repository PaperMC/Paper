package io.papermc.testplugin;

import com.mojang.brigadier.Command;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.range.DoubleRangeProvider;
import io.papermc.paper.plugin.bootstrap.BootstrapContext;
import io.papermc.paper.plugin.bootstrap.PluginBootstrap;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.testplugin.example.MaterialArgumentType;
import java.util.Collections;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class TestPluginBootstrap implements PluginBootstrap {

    @Override
    public void bootstrap(@NotNull BootstrapContext context) {
        final LifecycleEventManager<BootstrapContext> lifecycleManager = context.getLifecycleManager();
        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(Commands.literal("material")
                    .then(Commands.literal("item")
                        .then(Commands.argument("mat", MaterialArgumentType.item())
                            .executes(ctx -> {
                                ctx.getSource().getSender().sendPlainMessage(ctx.getArgument("mat", Material.class).name());
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    ).then(Commands.literal("block")
                        .then(Commands.argument("mat", MaterialArgumentType.block())
                            .executes(ctx -> {
                                ctx.getSource().getSender().sendPlainMessage(ctx.getArgument("mat", Material.class).name());
                                return Command.SINGLE_SUCCESS;
                            })
                        )
                    )
                    .build(),
                null,
                Collections.emptyList()
            );
        });

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS.newHandler(event -> {
            final Commands commands = event.registrar();
            commands.register(Commands.literal("heya")
                    .then(Commands.argument("range", ArgumentTypes.doubleRange())
                        .executes((ct) -> {
                            ct.getSource().getSender().sendPlainMessage(ct.getArgument("range", DoubleRangeProvider.class).range().toString());
                            return 1;
                        })
                    ).build(),
                null,
                Collections.emptyList()
            );
        }).priority(10));
    }

}
