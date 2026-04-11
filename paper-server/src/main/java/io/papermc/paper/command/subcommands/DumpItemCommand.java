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
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.SnbtPrinterTagVisitor;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.item.ItemStack;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.inventory.CraftItemStack;
import org.bukkit.entity.Player;

import static java.util.Objects.requireNonNull;
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

public final class DumpItemCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> create() {
        return Commands.literal("dumpitem")
            .requires(PaperCommand.hasPermission("dumpitem"))
            .executes(context -> {
                if (!(context.getSource().getExecutor() instanceof Player player)) {
                    throw net.minecraft.commands.CommandSourceStack.ERROR_NOT_PLAYER.create();
                }
                doDumpItem(player, false, context.getSource().getSender());
                return Command.SINGLE_SUCCESS;
            })
            .then(Commands.literal("all")
                .executes(context -> {
                    if (!(context.getSource().getExecutor() instanceof Player player)) {
                        throw net.minecraft.commands.CommandSourceStack.ERROR_NOT_PLAYER.create();
                    }
                    doDumpItem(player, true, context.getSource().getSender());
                    return Command.SINGLE_SUCCESS;
                })
            );
    }

    @SuppressWarnings({"unchecked" , "rawtypes"})
    private static void doDumpItem(final Player player, final boolean includeAllComponents, final CommandSender sender) {
        final TextComponent.Builder output = Component.text();
        final StringBuilder itemToCopy = new StringBuilder();
        final ItemStack item = CraftItemStack.asNMSCopy(player.getInventory().getItemInMainHand());
        final String itemName = item.typeHolder().unwrapKey().orElseThrow().identifier().toString();
        itemToCopy.append(itemName);
        output.append(text(itemName, YELLOW)); // item type

        final Set<DataComponentType<?>> remainingComponents = Collections.newSetFromMap(new IdentityHashMap<>());
        final DataComponentPatch patch = item.getComponentsPatch();
        remainingComponents.addAll(patch.entrySet().stream().map(Map.Entry::getKey).toList());
        final DataComponentMap prototype = item.getPrototype();
        if (includeAllComponents) {
            remainingComponents.addAll(prototype.keySet());
        }
        remainingComponents.removeIf(DataComponentType::isTransient);

        final RegistryOps<Tag> ops = CraftRegistry.getMinecraftRegistry().createSerializationContext(NbtOps.INSTANCE);
        final List<ComponentLike> writtenComponents = new ArrayList<>();
        final List<String> componentsToCopy = new ArrayList<>();
        for (final Map.Entry<DataComponentType<?>, Optional<?>> entry : patch.entrySet()) { // patch
            final DataComponentType<?> type = entry.getKey();
            if (remainingComponents.remove(type)) {
                final String path = requireNonNull(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type)).getPath();
                final Optional<?> patchedValue = entry.getValue();
                if (patchedValue.isEmpty()) {
                    writtenComponents.add(text().append(text('!', RED), text(path, AQUA)));
                    componentsToCopy.add("!" + path);
                } else {
                    final Tag serialized = (Tag) ((DataComponentType) type).codecOrThrow().encodeStart(ops, patchedValue.get()).getOrThrow();
                    writeComponentValue(writtenComponents::add, componentsToCopy::add, path, serialized);
                }
            }
        }

        if (includeAllComponents) {
            for (final DataComponentType<?> type : remainingComponents) { // prototype
                final String path = requireNonNull(BuiltInRegistries.DATA_COMPONENT_TYPE.getKey(type)).getPath();
                final Tag serialized = requireNonNull(prototype.getTyped(type)).encodeValue(ops).getOrThrow();
                writeComponentValue(writtenComponents::add, componentsToCopy::add, path, serialized);
            }
        }

        if (!writtenComponents.isEmpty()) {
            output.append(
                text("[" , color(0x8910CE)),
                join(JoinConfiguration.separator(text("," , GRAY)), writtenComponents),
                text("]" , color(0x8910CE))
            );
            itemToCopy
                .append("[")
                .append(String.join("," , componentsToCopy))
                .append("]");
        }
        player.sendMessage(output.build().compact());
        final Component copyMsg = text("Click to copy item definition to clipboard for use with /give" , GRAY, ITALIC);
        sender.sendMessage(copyMsg.clickEvent(copyToClipboard(itemToCopy.toString())));
    }

    private static void writeComponentValue(final Consumer<Component> output, final Consumer<String> copyOutput, final String path, final Tag serialized) {
        output.accept(textOfChildren(
            text(path, color(0xFF7FD7)),
            text("=" , WHITE),
            PaperAdventure.asAdventure(NbtUtils.toPrettyComponent(serialized))
        ));
        copyOutput.accept(path + "=" + new SnbtPrinterTagVisitor("" , 0, new ArrayList<>()).visit(serialized));
    }
}
