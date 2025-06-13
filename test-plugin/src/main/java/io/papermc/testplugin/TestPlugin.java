package io.papermc.testplugin;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.predicate.BlockPredicate;
import io.papermc.paper.command.brigadier.argument.resolvers.AngleResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.BlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.ColumnBlockPositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.FinePositionResolver;
import io.papermc.paper.command.brigadier.argument.resolvers.ColumnFinePositionResolver;
import io.papermc.paper.math.BlockPosition;
import io.papermc.paper.math.ColumnBlockPosition;
import io.papermc.paper.math.FinePosition;
import io.papermc.paper.math.ColumnFinePosition;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import java.util.EnumSet;
import java.util.Set;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Axis;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public final class TestPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        this.getServer().getPluginManager().registerEvents(this, this);

        Set<LiteralArgumentBuilder<CommandSourceStack>> commands = Set.of(
            angle(), swizzle(), blockPredicate(), particle(), columnFine(), columnBlock()
        );

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS.newHandler(
            event -> commands.forEach(builder -> event.registrar().register(builder.build()))
        ));
    }

    private LiteralArgumentBuilder<CommandSourceStack> angle() {
        return Commands.literal("angle")
            .then(Commands.argument("angle", ArgumentTypes.angle())
                .executes(ctx -> {
                    float angle = ctx.getArgument("angle", AngleResolver.class).resolve(ctx.getSource());
                    ctx.getSource().getSender().sendRichMessage("Your selected angle is <aqua>:" + angle);
                    return 1;
                })
            );
    }

    private LiteralArgumentBuilder<CommandSourceStack> swizzle() {
        return Commands.literal("swizzle")
            .then(Commands.argument("swizzle", ArgumentTypes.swizzle())
                .executes(ctx -> {
                    EnumSet<Axis> axes = (EnumSet<Axis>) ctx.getArgument("swizzle", EnumSet.class);

                    StringBuilder builder = new StringBuilder();
                    if (axes.contains(Axis.X)) {
                        builder.append("<red>X ");
                    }
                    if (axes.contains(Axis.Y)) {
                        builder.append("<green>Y ");
                    }
                    if (axes.contains(Axis.Z)) {
                        builder.append("<blue>Z");
                    }

                    String axesString = builder.toString().trim();
                    ctx.getSource().getSender().sendRichMessage("You selected: <axes>",
                        Placeholder.parsed("axes", axesString.isEmpty() ? "<gray>None" : axesString)
                    );
                    return 1;
                })
            );
    }

    private LiteralArgumentBuilder<CommandSourceStack> blockPredicate() {
        return Commands.literal("blockpredicate")
            .then(Commands.argument("position", ArgumentTypes.blockPosition())
                .then(Commands.argument("blockpredicate", ArgumentTypes.blockPredicate())
                    .executes(ctx -> {
                        BlockPredicate predicate = ctx.getArgument("blockpredicate", BlockPredicate.class);
                        BlockPosition position = ctx.getArgument("position", BlockPositionResolver.class).resolve(ctx.getSource());
                        World world = ctx.getSource().getLocation().getWorld();

                        if (predicate.test(position.toLocation(world).getBlock())) {
                            ctx.getSource().getSender().sendRichMessage("<green>Obviously that block matches, lol.");
                        } else {
                            ctx.getSource().getSender().sendRichMessage("<red>Why would that match. Use your eyes ☠️");
                        }

                        return 1;
                    })
                )
            );
    }

    private LiteralArgumentBuilder<CommandSourceStack> particle() {
        return Commands.literal("particle")
            .then(Commands.argument("particle", ArgumentTypes.particle())
                .then(Commands.argument("position", ArgumentTypes.finePosition(true))
                    .executes(ctx -> {
                        Particle particle = ctx.getArgument("particle", Particle.class);
                        FinePosition position = ctx.getArgument("position", FinePositionResolver.class).resolve(ctx.getSource());
                        World world = ctx.getSource().getLocation().getWorld();

                        // TODO: Fix particles
                        world.spawnParticle(particle, position.toLocation(world), 10);
                        return 1;
                    })
                )
            );
    }

    private LiteralArgumentBuilder<CommandSourceStack> columnFine() {
        return Commands.literal("columnfine")
            .then(Commands.argument("columnfine", ArgumentTypes.columnFinePosition())
                .executes(ctx -> {
                    ColumnFinePosition pos = ctx.getArgument("columnfine", ColumnFinePositionResolver.class).resolve(ctx.getSource());
                    ctx.getSource().getSender().sendRichMessage("The position you selected is at x: <red><x></red> z: <blue><z></blue>",
                        Placeholder.unparsed("x", Double.toString(pos.x())),
                        Placeholder.unparsed("z", Double.toString(pos.z()))
                    );
                    return 1;
                })
            );
    }

    private LiteralArgumentBuilder<CommandSourceStack> columnBlock() {
        return Commands.literal("columnblock")
            .then(Commands.argument("columnblock", ArgumentTypes.columnBlockPosition())
                .executes(ctx -> {
                    ColumnBlockPosition pos = ctx.getArgument("columnblock", ColumnBlockPositionResolver.class).resolve(ctx.getSource());
                    ctx.getSource().getSender().sendRichMessage("The position you selected is at x: <red><x></red> z: <blue><z></blue>",
                        Placeholder.unparsed("x", Double.toString(pos.x())),
                        Placeholder.unparsed("z", Double.toString(pos.z()))
                    );
                    return 1;
                })
            );
    }
}
