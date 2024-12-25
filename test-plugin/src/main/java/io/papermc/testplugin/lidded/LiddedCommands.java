package io.papermc.testplugin.lidded;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic3CommandExceptionType;
import io.papermc.paper.block.LidMode;
import io.papermc.paper.block.LidState;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventManager;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.testplugin.TestPlugin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.block.BlockState;
import org.bukkit.block.Lidded;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import static net.kyori.adventure.text.Component.text;

public class LiddedCommands {

    private static final Dynamic3CommandExceptionType NOT_LIDDED = new Dynamic3CommandExceptionType(
        (objPos, objExpected, objActual) -> {
            if (!(objPos instanceof BlockPosition pos) || !(objExpected instanceof String expected)
                || !(objActual instanceof String actual)) {
                TestPlugin.getPlugin(TestPlugin.class).getLogger().warning(
                    "Internal Exception while making error message" + objPos.getClass()
                        + objExpected.getClass() + objActual.getClass());
                return new LiteralMessage("Internal Exception while making error message");
            }

            return new LiteralMessage(
                "Block at %d, %d, %d is not a lidded block. Expected: \"%s\", Actual: \"%s\"".formatted(
                    pos.blockX(), pos.blockY(), pos.blockZ(), expected, actual));
        });


    public static void registerAll(JavaPlugin plugin) {
        LifecycleEventManager<Plugin> lifecycleManager = plugin.getLifecycleManager();

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final io.papermc.paper.command.brigadier.Commands registrar = event.registrar();
            LiddedCommands.registerNew(registrar);
            LiddedCommands.registerOld(registrar);
        });
    }

    public static void registerNew(io.papermc.paper.command.brigadier.Commands commands) {
        commands.register(io.papermc.paper.command.brigadier.Commands.literal("test_lidded_new")
            .then(io.papermc.paper.command.brigadier.Commands.argument("pos",
                    ArgumentTypes.blockPosition())
                .then(io.papermc.paper.command.brigadier.Commands.literal("query")
                    .executes(context -> {
                        BlockPosition pos = context.getArgument("pos",
                            BlockPositionResolver.class).resolve(context.getSource());
                        io.papermc.paper.block.Lidded lidded = getPaperLidded(context, pos);
                        if (!(lidded instanceof BlockState state)) {
                            throw new IllegalStateException("Impossible");
                        }
                        Component msg = text()
                            .append(text("---", NamedTextColor.DARK_GRAY)).appendNewline()
                            .append(text("Lidded Block: "))
                            .append(text(state.getType().key().asString())).appendNewline()
                            .append(text("Lid Mode: "))
                            .append(text(lidded.getLidMode().name())).appendNewline()
                            .append(text("Effective LidState: "))
                            .append(text(lidded.getEffectiveLidState().name(),
                                lidded.getEffectiveLidState() == LidState.OPEN
                                    ? NamedTextColor.GREEN : NamedTextColor.RED)).appendSpace()
                            .append(text("True LidState: "))
                            .append(text(lidded.getTrueLidState().name(),
                                lidded.getTrueLidState() == LidState.OPEN ? NamedTextColor.GREEN
                                    : NamedTextColor.RED))
                            .build();
                        context.getSource().getSender().sendMessage(msg);
                        return 1;
                    })
                )
                .then(io.papermc.paper.command.brigadier.Commands.literal("set")
                    .then(io.papermc.paper.command.brigadier.Commands.argument("lidMode",
                            LidModeArgument.lidMode())
                        .executes(context -> {
                                BlockPosition pos = context.getArgument("pos",
                                    BlockPositionResolver.class).resolve(context.getSource());
                                io.papermc.paper.block.Lidded lidded = getPaperLidded(context, pos);
                                if (!(lidded instanceof BlockState state)) {
                                    throw new IllegalStateException("Impossible");
                                }

                                LidMode previousMode = lidded.getLidMode();
                                LidState oldEffectiveState = lidded.getEffectiveLidState();
                                LidState oldTrueState = lidded.getTrueLidState();
                                LidMode targetMode = LidModeArgument.getLidMode(context, "lidMode");
                                LidMode resultantMode = lidded.setLidMode(targetMode);

                                Component msg = text()
                                    .append(text("---", NamedTextColor.DARK_GRAY)).appendNewline()
                                    .append(text("Lidded Block: "))
                                    .append(text(state.getType().key().asString())).appendNewline()
                                    .append(text("Lid Mode: "))
                                    .append(text("Old: "))
                                    .append(text(previousMode.name())).appendSpace()
                                    .append(text("Target: "))
                                    .append(text(targetMode.name())).appendSpace()
                                    .append(text("New: "))
                                    .append(text(resultantMode.name())).appendNewline()
                                    .append(text("Effective LidState: "))
                                    .append(text("Old: "))
                                    .append(text(oldEffectiveState.name(),
                                        oldEffectiveState == LidState.OPEN ? NamedTextColor.GREEN
                                            : NamedTextColor.RED)).appendSpace()
                                    .append(text("New: "))
                                    .append(text(lidded.getEffectiveLidState().name(),
                                        lidded.getEffectiveLidState() == LidState.OPEN
                                            ? NamedTextColor.GREEN : NamedTextColor.RED))
                                    .appendNewline()
                                    .append(text("True LidState: "))
                                    .append(text("Old: "))
                                    .append(text(oldTrueState.name(),
                                        oldTrueState == LidState.OPEN ? NamedTextColor.GREEN
                                            : NamedTextColor.RED)).appendSpace()
                                    .append(text("New: "))
                                    .append(text(lidded.getTrueLidState().name(),
                                        lidded.getTrueLidState() == LidState.OPEN ? NamedTextColor.GREEN
                                            : NamedTextColor.RED))
                                    .build();
                                context.getSource().getSender().sendMessage(msg);

                                return 1;
                            }
                        )
                    )
                )
            )
            .build()
        );
    }

    private static io.papermc.paper.block.Lidded getPaperLidded(
        CommandContext<CommandSourceStack> context,
        BlockPosition pos
    )
        throws CommandSyntaxException {

        Location targetLoc = new Location(context.getSource().getLocation().getWorld(),
            pos.blockX(), pos.blockY(), pos.blockZ());
        BlockState state = targetLoc.getBlock().getState();
        if (state instanceof io.papermc.paper.block.Lidded lidded) {
            return lidded;
        }
        throw NOT_LIDDED.create(pos, "A block Implementing Paper Lidded",
            state.getType().key().asString());
    }

    public static void registerOld(io.papermc.paper.command.brigadier.Commands commands) {
        commands.register(
            io.papermc.paper.command.brigadier.Commands.literal("test_lidded_old")
                .then(io.papermc.paper.command.brigadier.Commands.argument("pos",
                        ArgumentTypes.blockPosition())
                    .then(io.papermc.paper.command.brigadier.Commands.literal("is_open")
                        .executes(context -> {
                            BlockPosition pos = context.getArgument("pos",
                                BlockPositionResolver.class).resolve(context.getSource());
                            Lidded lidded = getBukkitLidded(context, pos);
                            if (!(lidded instanceof BlockState state)) {
                                throw new IllegalStateException("Impossible");
                            }

                            Component msg = text()
                                .append(text("Lidded Block: "))
                                .append(text(state.getType().key().asString()))
                                .append(text(" is open: "))
                                .append(text(lidded.isOpen() ? "Yes" : "No",
                                    lidded.isOpen() ? NamedTextColor.GREEN : NamedTextColor.RED))
                                .build();

                            context.getSource().getSender().sendMessage(msg);

                            return 1;
                        })
                    )
                    .then(io.papermc.paper.command.brigadier.Commands.literal("open")
                        .executes(context -> {
                            BlockPosition pos = context.getArgument("pos",
                                BlockPositionResolver.class).resolve(context.getSource());
                            Lidded lidded = getBukkitLidded(context, pos);
                            if (!(lidded instanceof BlockState state)) {
                                throw new IllegalStateException("Impossible");
                            }

                            lidded.open();

                            Component msg = text()
                                .append(text("Lidded Block: "))
                                .append(text(state.getType().key().asString()))
                                .append(text(" set "))
                                .append(text("open", NamedTextColor.GREEN))
                                .build();

                            context.getSource().getSender().sendMessage(msg);

                            return 1;
                        })
                    )
                    .then(io.papermc.paper.command.brigadier.Commands.literal("close")
                        .executes(context -> {
                            BlockPosition pos = context.getArgument("pos",
                                BlockPositionResolver.class).resolve(context.getSource());
                            Lidded lidded = getBukkitLidded(context, pos);
                            if (!(lidded instanceof BlockState state)) {
                                throw new IllegalStateException("Impossible");
                            }

                            lidded.close();
                            Component msg = text()
                                .append(text("Lidded Block: "))
                                .append(text(state.getType().key().asString()))
                                .append(text(" set "))
                                .append(text("closed", NamedTextColor.RED))
                                .build();

                            context.getSource().getSender().sendMessage(msg);

                            return 1;
                        })
                    )
                )
                .build()
        );
    }

    private static Lidded getBukkitLidded(
        CommandContext<CommandSourceStack> context,
        BlockPosition pos
    ) throws CommandSyntaxException {
        Location targetLoc = new Location(context.getSource().getLocation().getWorld(),
            pos.blockX(), pos.blockY(), pos.blockZ());
        BlockState state = targetLoc.getBlock().getState();
        if (state instanceof Lidded lidded) {
            return lidded;
        }
        throw NOT_LIDDED.create(pos, "A block Implementing Bukkit Lidded",
            state.getType().key().asString());
    }
}
