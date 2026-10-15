package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.tree.RootCommandNode;
import io.netty.buffer.Unpooled;
import io.papermc.paper.configuration.GlobalConfiguration;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.DimensionArgument;
import net.minecraft.commands.synchronization.SuggestionProviders;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundCommandsPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.Normal;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Normal
class DimensionSuggestionsTest {

    private boolean previousSetting;

    @BeforeEach
    void saveSetting() {
        this.previousSetting = GlobalConfiguration.get().commands.serverSideDimensionSuggestions;
    }

    @AfterEach
    void restoreSetting() {
        GlobalConfiguration.get().commands.serverSideDimensionSuggestions = this.previousSetting;
    }

    @Test
    void includesWorldsLoadedAfterLoginAndRemovesUnloadedWorlds() throws Exception {
        GlobalConfiguration.get().commands.serverSideDimensionSuggestions = true;
        CommandDispatcher<CommandSourceStack> server = dispatcher(DimensionArgument.dimension());
        CommandSourceStack serverSource = mock(CommandSourceStack.class);
        SharedSuggestionProvider clientSource = mock(SharedSuggestionProvider.class);
        when(clientSource.levels()).thenReturn(Set.of(Level.OVERWORLD));
        when(clientSource.customSuggestion(any())).thenAnswer(invocation -> {
            com.mojang.brigadier.context.CommandContext<?> context = invocation.getArgument(0);
            return server.getCompletionSuggestions(server.parse(context.getInput(), serverSource));
        });
        CommandDispatcher<SharedSuggestionProvider> client = clientDispatcher(server);
        ResourceKey<Level> addedWorld = ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath("test", "added_world"));

        when(serverSource.levels()).thenReturn(Set.of(Level.OVERWORLD, addedWorld));
        assertEquals(List.of("test:added_world"), suggestions(client, "world test:", clientSource));

        when(serverSource.levels()).thenReturn(Set.of(Level.OVERWORLD));
        assertEquals(List.of(), suggestions(client, "world test:", clientSource));
    }

    @Test
    void keepsLoginWorldSuggestionsWhenDisabled() throws Exception {
        GlobalConfiguration.get().commands.serverSideDimensionSuggestions = false;
        SharedSuggestionProvider clientSource = mock(SharedSuggestionProvider.class);
        when(clientSource.levels()).thenReturn(Set.of(Level.OVERWORLD));
        CommandDispatcher<SharedSuggestionProvider> client = clientDispatcher(dispatcher(DimensionArgument.dimension()));

        assertEquals(List.of("minecraft:overworld"), suggestions(client, "world ", clientSource));
        verify(clientSource, never()).customSuggestion(any());
    }

    @Test
    void preservesExplicitSuggestionProviders() throws Exception {
        GlobalConfiguration.get().commands.serverSideDimensionSuggestions = true;
        CommandDispatcher<CommandSourceStack> server = new CommandDispatcher<>();
        server.register(Commands.literal("world").then(Commands.argument("dimension", DimensionArgument.dimension())
            .suggests(SuggestionProviders.cast(SuggestionProviders.AVAILABLE_SOUNDS))));
        SharedSuggestionProvider clientSource = mock(SharedSuggestionProvider.class);
        Identifier sound = Identifier.fromNamespaceAndPath("test", "custom_sound");
        when(clientSource.getAvailableSounds()).thenAnswer(invocation -> java.util.stream.Stream.of(sound));

        assertEquals(List.of("test:custom_sound"), suggestions(clientDispatcher(server), "world test:", clientSource));
        verify(clientSource, never()).customSuggestion(any());
    }

    @Test
    void doesNotRequestServerSuggestionsForOtherArgumentTypes() throws Exception {
        GlobalConfiguration.get().commands.serverSideDimensionSuggestions = true;
        SharedSuggestionProvider clientSource = mock(SharedSuggestionProvider.class);

        assertEquals(List.of(), suggestions(clientDispatcher(dispatcher(StringArgumentType.word())), "world ", clientSource));
        verify(clientSource, never()).customSuggestion(any());
    }

    private static CommandDispatcher<CommandSourceStack> dispatcher(ArgumentType<?> type) {
        CommandDispatcher<CommandSourceStack> dispatcher = new CommandDispatcher<>();
        dispatcher.register(Commands.literal("world").then(Commands.argument("dimension", type)));
        return dispatcher;
    }

    private static List<String> suggestions(CommandDispatcher<SharedSuggestionProvider> dispatcher, String input, SharedSuggestionProvider source) {
        Suggestions suggestions = dispatcher.getCompletionSuggestions(dispatcher.parse(input, source)).join();
        return suggestions.getList().stream().map(Suggestion::getText).toList();
    }

    @SuppressWarnings("unchecked")
    private static CommandDispatcher<SharedSuggestionProvider> clientDispatcher(CommandDispatcher<CommandSourceStack> server) throws Exception {
        Field inspectorField = Commands.class.getDeclaredField("COMMAND_NODE_INSPECTOR");
        inspectorField.setAccessible(true);
        ClientboundCommandsPacket.NodeInspector<CommandSourceStack> inspector =
            (ClientboundCommandsPacket.NodeInspector<CommandSourceStack>) inspectorField.get(null);
        FriendlyByteBuf buffer = new FriendlyByteBuf(Unpooled.buffer());
        try {
            ClientboundCommandsPacket.STREAM_CODEC.encode(buffer, new ClientboundCommandsPacket(server.getRoot(), inspector));
            ClientboundCommandsPacket packet = ClientboundCommandsPacket.STREAM_CODEC.decode(buffer);
            RootCommandNode<SharedSuggestionProvider> root = packet.getRoot(
                Commands.createValidationContext(RegistryHelper.registryAccess()),
                new ClientboundCommandsPacket.NodeBuilder<SharedSuggestionProvider>() {
                    @Override
                    public ArgumentBuilder<SharedSuggestionProvider, ?> createLiteral(String id) {
                        return LiteralArgumentBuilder.literal(id);
                    }

                    @Override
                    public ArgumentBuilder<SharedSuggestionProvider, ?> createArgument(String id, ArgumentType<?> type, @Nullable Identifier suggestionId) {
                        RequiredArgumentBuilder<SharedSuggestionProvider, ?> argument = RequiredArgumentBuilder.argument(id, type);
                        if (suggestionId != null) {
                            argument.suggests(SuggestionProviders.getProvider(suggestionId));
                        }
                        return argument;
                    }

                    @Override
                    public ArgumentBuilder<SharedSuggestionProvider, ?> configure(ArgumentBuilder<SharedSuggestionProvider, ?> input, boolean executable, boolean restricted) {
                        return input;
                    }
                }
            );
            return new CommandDispatcher<>(root);
        } finally {
            buffer.release();
        }
    }
}
