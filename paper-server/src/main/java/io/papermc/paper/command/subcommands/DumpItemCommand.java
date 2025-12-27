package io.papermc.paper.command.subcommands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.command.PaperCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.ComponentLike;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.TextComponent;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.TypedDataComponent;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.jspecify.annotations.NullMarked;

import static net.kyori.adventure.text.Component.join;
import static net.kyori.adventure.text.Component.text;
import static net.kyori.adventure.text.Component.textOfChildren;
import static net.kyori.adventure.text.event.ClickEvent.copyToClipboard;
import static net.kyori.adventure.text.format.NamedTextColor.AQUA;
import static net.kyori.adventure.text.format.NamedTextColor.GRAY;
import static net.kyori.adventure.text.format.NamedTextColor.RED;
import static net.kyori.adventure.text.format.NamedTextColor.WHITE;
import static net.kyori.adventure.text.format.NamedTextColor.YELLOW;
import static net.kyori.adventure.text.format.TextColor.color;
import static net.kyori.adventure.text.format.TextDecoration.ITALIC;

@NullMarked
public final class DumpItemCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("dumpitem")
            .requires(stack -> stack.getSender().hasPermission(PaperCommand.BASE_PERM + "dumpitem"))

            .then(Commands.literal("all")
                .executes(ctx -> {
                    doDumpItem(ctx.getSource().getSender(), true);
                    return Command.SINGLE_SUCCESS;
                })
            )

            .executes(ctx -> {
                doDumpItem(ctx.getSource().getSender(), false);
                return Command.SINGLE_SUCCESS;
            });
    }

    @SuppressWarnings({"unchecked", "OptionalAssignedToNull", "rawtypes"})
    private static void doDumpItem(final CommandSender sender, final boolean includeAllComponents) {
        if (!(sender instanceof final Player player)) {
            sender.sendMessage("Only players can use this command");
            return;
        }
        final ItemStack itemStack = CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand());
        final TextComponent.Builder visualOutput = Component.text();
        final StringBuilder itemCommandBuilder = new StringBuilder();
        final String itemName = itemStack.getItemHolder().unwrapKey().orElseThrow().identifier().toString();
        itemCommandBuilder.append(itemName);
        visualOutput.append(text(itemName, YELLOW)); // item type
        final Set<DataComponentType<?>> referencedComponentTypes = Collections.newSetFromMap(new IdentityHashMap<>());
        final DataComponentPatch patch = itemStack.getComponentsPatch();
        referencedComponentTypes.addAll(patch.entrySet().stream().map(Map.Entry::getKey).toList());
        final DataComponentMap prototype = itemStack.getItem().components();
        if (includeAllComponents) {
            referencedComponentTypes.addAll(prototype.keySet());
        }

        final RegistryAccess.Frozen access = ((CraftServer) sender.getServer()).getServer().registryAccess();
        final RegistryOps<Tag> ops = access.createSerializationContext(NbtOps.INSTANCE);
        final Registry<DataComponentType<?>> registry = access.lookupOrThrow(Registries.DATA_COMPONENT_TYPE);
        final List<ComponentLike> componentComponents = new ArrayList<>();
        final List<String> commandComponents = new ArrayList<>();
        for (final DataComponentType<?> type : referencedComponentTypes) {
            final String path = registry.getResourceKey(type).orElseThrow().identifier().getPath();
            final Optional<?> patchedValue = patch.get(type);
            final TypedDataComponent<?> prototypeValue = prototype.getTyped(type);
            if (patchedValue != null) {
                if (patchedValue.isEmpty()) {
                    componentComponents.add(text().append(text('!', RED), text(path, AQUA)));
                    commandComponents.add("!" + path);
                } else {
                    final Tag serialized = (Tag) ((DataComponentType) type).codecOrThrow().encodeStart(ops, patchedValue.get()).getOrThrow();
                    writeComponentValue(componentComponents::add, commandComponents::add, path, serialized);
                }
            } else if (includeAllComponents && prototypeValue != null) {
                final Tag serialized = prototypeValue.encodeValue(ops).getOrThrow();
                writeComponentValue(componentComponents::add, commandComponents::add, path, serialized);
            }
        }
        if (!componentComponents.isEmpty()) {
            visualOutput.append(
                text("[", color(0x8910CE)),
                join(JoinConfiguration.separator(text(",", GRAY)), componentComponents),
                text("]", color(0x8910CE))
            );
            itemCommandBuilder
                .append("[")
                .append(String.join(",", commandComponents))
                .append("]");
        }
        player.sendMessage(visualOutput.build().compact());
        final Component copyMsg = text("Click to copy item definition to clipboard for use with /give", GRAY, ITALIC);
        sender.sendMessage(copyMsg.clickEvent(copyToClipboard(itemCommandBuilder.toString())));
    }

    private static void writeComponentValue(final Consumer<Component> visualOutput, final Consumer<String> commandOutput, final String path, final Tag serialized) {
        visualOutput.accept(textOfChildren(
            text(path, color(0xFF7FD7)),
            text("=", WHITE),
            PaperAdventure.asAdventure(NbtUtils.toPrettyComponent(serialized))
        ));
        commandOutput.accept(path + "=" + new SnbtPrinterTagVisitor("", 0, new ArrayList<>()).visit(serialized));
    }
}
