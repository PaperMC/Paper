package io.papermc.paper.adventure;

import io.papermc.paper.PaperServerInternalAPIBridge;
import io.papermc.paper.chat.ChatRenderer;
import io.papermc.paper.chat.vanilla.ChatTypeRenderResult;
import io.papermc.paper.chat.vanilla.ChatTypeRenderer;
import io.papermc.paper.event.player.AbstractChatEvent;
import io.papermc.paper.event.player.AsyncChatEvent;
import io.papermc.paper.event.player.ChatEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.BitSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.audience.ForwardingAudience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.OutgoingChatMessage;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.CraftWorld;
import org.bukkit.craftbukkit.entity.CraftPlayer;
import org.bukkit.craftbukkit.util.LazyPlayerSet;
import org.bukkit.craftbukkit.util.Waitable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;
import org.intellij.lang.annotations.Subst;

import static net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection;

@DefaultQualifier(NonNull.class)
public final class ChatProcessor {
    static final ResourceKey<ChatType> PAPER_RAW = ResourceKey.create(Registries.CHAT_TYPE, ResourceLocation.fromNamespaceAndPath(ResourceLocation.PAPER_NAMESPACE, "raw"));
    static final String DEFAULT_LEGACY_FORMAT = "<%1$s> %2$s"; // copied from PlayerChatEvent/AsyncPlayerChatEvent
    final MinecraftServer server;
    final ServerPlayer player;
    final PlayerChatMessage message;
    final boolean async;
    final String craftbukkit$originalMessage;
    final Component paper$originalMessage;
    final OutgoingChatMessage outgoing;

    static final int MESSAGE_CHANGED = 1;
    static final int FORMAT_CHANGED = 2;
    static final int SENDER_CHANGED = 3; // Not used
    static final int USES_CHAT_TYPE_RENDERER = 4;
    private final BitSet flags = new BitSet(4);

    public ChatProcessor(final MinecraftServer server, final ServerPlayer player, final PlayerChatMessage message, final boolean async) {
        this.server = server;
        this.player = player;
        this.message = message;
        this.async = async;
        this.craftbukkit$originalMessage = message.unsignedContent() != null ? LegacyComponentSerializer.legacySection().serialize(PaperAdventure.asAdventure(message.unsignedContent())) : message.signedContent();
        this.paper$originalMessage = PaperAdventure.asAdventure(this.message.decoratedContent());
        this.outgoing = OutgoingChatMessage.create(this.message);
    }

    @SuppressWarnings("deprecated")
    public void process() {
        final boolean listenersOnAsyncEvent = canYouHearMe(AsyncPlayerChatEvent.getHandlerList());
        final boolean listenersOnSyncEvent = canYouHearMe(PlayerChatEvent.getHandlerList());
        if (listenersOnAsyncEvent || listenersOnSyncEvent) {
            final CraftPlayer player = this.player.getBukkitEntity();
            final AsyncPlayerChatEvent asyncChatEvent = new AsyncPlayerChatEvent(this.async, player, this.craftbukkit$originalMessage, new LazyPlayerSet(this.server));
            this.post(asyncChatEvent);
            if (listenersOnSyncEvent) {
                final PlayerChatEvent chatEvent = new PlayerChatEvent(player, asyncChatEvent.getMessage(), asyncChatEvent.getFormat(), asyncChatEvent.getRecipients());
                chatEvent.setCancelled(asyncChatEvent.isCancelled()); // propagate cancelled state
                this.queueIfAsyncOrRunImmediately(new Waitable<>() {
                    @Override
                    protected Void evaluate() {
                        ChatProcessor.this.post(chatEvent);
                        return null;
                    }
                });
                this.readLegacyModifications(chatEvent.getMessage(), chatEvent.getFormat(), chatEvent.getPlayer());
                this.processModern(
                    this.modernRenderer(chatEvent.getFormat()),
                    this.viewersFromLegacy(chatEvent.getRecipients()),
                    this.modernMessage(chatEvent.getMessage()),
                    chatEvent.getPlayer(),
                    chatEvent.isCancelled()
                );
            } else {
                this.readLegacyModifications(asyncChatEvent.getMessage(), asyncChatEvent.getFormat(), asyncChatEvent.getPlayer());
                this.processModern(
                    this.modernRenderer(asyncChatEvent.getFormat()),
                    this.viewersFromLegacy(asyncChatEvent.getRecipients()),
                    this.modernMessage(asyncChatEvent.getMessage()),
                    asyncChatEvent.getPlayer(),
                    asyncChatEvent.isCancelled()
                );
            }
        } else {
            this.processModern(
                defaultRenderer(),
                new LazyChatAudienceSet(this.server),
                this.paper$originalMessage,
                this.player.getBukkitEntity(),
                false
            );
        }
    }

    private ChatRenderer modernRenderer(final String format) {
        if (this.flags.get(FORMAT_CHANGED)) {
            return legacyRenderer(format);
        } else {
            return defaultRenderer();
        }
    }

    private Component modernMessage(final String legacyMessage) {
        if (this.flags.get(MESSAGE_CHANGED)) {
            return legacySection().deserialize(legacyMessage);
        } else {
            return this.paper$originalMessage;
        }
    }

    private void readLegacyModifications(final String message, final String format, final Player playerSender) {
        this.flags.set(MESSAGE_CHANGED, !message.equals(this.craftbukkit$originalMessage));
        this.flags.set(FORMAT_CHANGED, !format.equals(DEFAULT_LEGACY_FORMAT));
        this.flags.set(SENDER_CHANGED, playerSender != this.player.getBukkitEntity());
    }

    private void processModern(final ChatRenderer renderer, final Set<Audience> viewers, final Component message, final Player player, final boolean cancelled) {
        final PlayerChatMessage.AdventureView signedMessage = this.message.adventureView();
        final AsyncChatEvent ae = new AsyncChatEvent(this.async, player, viewers, renderer, message, this.paper$originalMessage, signedMessage);
        ae.setCancelled(cancelled); // propagate cancelled state
        this.post(ae);
        final boolean listenersOnSyncEvent = canYouHearMe(ChatEvent.getHandlerList());
        if (listenersOnSyncEvent) {
            this.queueIfAsyncOrRunImmediately(new Waitable<>() {
                @Override
                protected Void evaluate() {
                    final ChatEvent chatEvent = new ChatEvent(player, ae.viewers(), ae.renderer(), ae.message(), ChatProcessor.this.paper$originalMessage/*, ae.usePreviewComponent()*/, signedMessage);
                    chatEvent.setCancelled(ae.isCancelled()); // propagate cancelled state
                    ChatProcessor.this.post(chatEvent);
                    ChatProcessor.this.readModernModifications(chatEvent, renderer);
                    ChatProcessor.this.complete(chatEvent);
                    return null;
                }
            });
        } else {
            this.readModernModifications(ae, renderer);
            this.complete(ae);
        }
    }

    private void readModernModifications(final AbstractChatEvent chatEvent, final ChatRenderer originalRenderer) {
        this.flags.set(MESSAGE_CHANGED, !chatEvent.message().equals(this.paper$originalMessage));
        if (originalRenderer != chatEvent.renderer()) { // don't set to false if it hasn't changed
            if (chatEvent.renderer() instanceof PaperVanillaChatRenderer) {
                this.flags.set(USES_CHAT_TYPE_RENDERER, true);
            } else {
                this.flags.set(FORMAT_CHANGED, true);
            }
        }
    }

    private void complete(final AbstractChatEvent event) {
        if (event.isCancelled()) {
            return;
        }

        final CraftPlayer player = ((CraftPlayer) event.getPlayer());
        final Component displayName = displayName(player);
        final Component message = event.message();
        final ChatRenderer renderer = event.renderer();

        final Set<Audience> viewers = event.viewers();
        final ResourceKey<ChatType> chatTypeKey = renderer instanceof ChatRenderer.Default ? ChatType.CHAT : PAPER_RAW;
        final ChatType.Bound chatType = ChatType.bind(chatTypeKey, this.player.level().registryAccess(), PaperAdventure.asVanilla(displayName(player)));

        OutgoingChat outgoingChat = viewers instanceof LazyChatAudienceSet lazyAudienceSet && lazyAudienceSet.isLazy() ? new ServerOutgoingChat() : new ViewersOutgoingChat();
        if (this.flags.get(USES_CHAT_TYPE_RENDERER)) {
            if (!(renderer instanceof PaperVanillaChatRenderer vanillaChatRenderer)) {
                throw new IllegalStateException("BUG: This should only be a vanilla renderer");
            }
            outgoingChat.sendChatTypeRendered(player, displayName, message, viewers, vanillaChatRenderer, vanillaChatRenderer.viewerUnaware());
        } else if (this.flags.get(FORMAT_CHANGED)) {
            if (renderer instanceof ChatRenderer.ViewerUnaware unaware) {
                outgoingChat.sendFormatChangedViewerUnaware(player, PaperAdventure.asVanilla(unaware.render(player, displayName, message)), viewers, chatType);
            } else {
                outgoingChat.sendFormatChangedViewerAware(player, displayName, message, renderer, viewers, chatType);
            }
        } else if (this.flags.get(MESSAGE_CHANGED)) {
            if (!(renderer instanceof ChatRenderer.ViewerUnaware unaware)) {
                throw new IllegalStateException("BUG: This should be a ViewerUnaware renderer at this point");
            }
            final Component renderedComponent = chatTypeKey == ChatType.CHAT ? message : unaware.render(player, displayName, message);
            outgoingChat.sendMessageChanged(player, PaperAdventure.asVanilla(renderedComponent), viewers, chatType);
        } else {
            outgoingChat.sendOriginal(player, viewers, chatType);
        }
    }

    interface OutgoingChat {
        default void sendFormatChangedViewerUnaware(CraftPlayer player, net.minecraft.network.chat.Component renderedMessage, Set<Audience> viewers, ChatType.Bound chatType) {
            this.sendMessageChanged(player, renderedMessage, viewers, chatType);
        }

        void sendChatTypeRendered(CraftPlayer player, Component displayName, Component message, Set<Audience> viewers, PaperVanillaChatRenderer vanillaChatRenderer, boolean viewerUnaware);

        void sendFormatChangedViewerAware(CraftPlayer player, Component displayName, Component message, ChatRenderer renderer, Set<Audience> viewers, ChatType.Bound chatType);

        void sendMessageChanged(CraftPlayer player, net.minecraft.network.chat.Component renderedMessage, Set<Audience> viewers, ChatType.Bound chatType);

        void sendOriginal(CraftPlayer player, Set<Audience> viewers, ChatType.Bound chatType);
    }

    final class ServerOutgoingChat implements OutgoingChat {
        @Override
        public void sendChatTypeRendered(final CraftPlayer player, final Component displayName, final Component message, final Set<Audience> viewers, final PaperVanillaChatRenderer vanillaChatRenderer, final boolean viewerUnaware) {
            ChatContentProvider provider = viewerUnaware ? ChatContentProvider.ofStaticViewerUnaware(vanillaChatRenderer.compute(player, displayName, message, null)) : ChatContentProvider.ofViewerAware((viewer) -> vanillaChatRenderer.compute(player, displayName, message, viewer));
            ChatProcessor.this.server.getPlayerList().broadcastChatMessage(ChatProcessor.this.message, ChatProcessor.this.player, provider);
        }

        @Override
        public void sendFormatChangedViewerAware(CraftPlayer player, Component displayName, Component message, ChatRenderer renderer, Set<Audience> viewers, ChatType.Bound chatType) {
            ChatProcessor.this.server.getPlayerList().broadcastChatMessage(ChatProcessor.this.message, ChatProcessor.this.player, ChatContentProvider.ofViewerAware((viewer) -> new RenderedResult(chatType, PaperAdventure.asVanilla(renderer.render(player, displayName, message, viewer)))));
        }

        @Override
        public void sendMessageChanged(CraftPlayer player, net.minecraft.network.chat.Component renderedMessage, Set<Audience> viewers, ChatType.Bound chatType) {
            ChatProcessor.this.server.getPlayerList().broadcastChatMessage(ChatProcessor.this.message.withUnsignedContent(renderedMessage), ChatProcessor.this.player, chatType);
        }

        @Override
        public void sendOriginal(CraftPlayer player, Set<Audience> viewers, ChatType.Bound chatType) {
            ChatProcessor.this.server.getPlayerList().broadcastChatMessage(ChatProcessor.this.message, ChatProcessor.this.player, chatType);
        }
    }

    final class ViewersOutgoingChat implements OutgoingChat {
        @Override
        public void sendChatTypeRendered(final CraftPlayer player, final Component displayName, final Component message, final Set<Audience> viewers, final PaperVanillaChatRenderer vanillaChatRenderer, final boolean viewerUnaware) {
            ChatContentProvider provider = viewerUnaware ? ChatContentProvider.ofStaticViewerUnaware(vanillaChatRenderer.compute(player, displayName, message, null)) : ChatContentProvider.ofViewerAware((viewer) -> vanillaChatRenderer.compute(player, displayName, message, viewer));
            this.broadcastToViewers(viewers, provider);
        }

        @Override
        public void sendFormatChangedViewerAware(CraftPlayer player, Component displayName, Component message, ChatRenderer renderer, Set<Audience> viewers, ChatType.Bound chatType) {
            this.broadcastToViewers(viewers, ChatContentProvider.ofViewerAware((v) -> PaperAdventure.asVanilla(renderer.render(player, displayName, message, v)), chatType));
        }

        @Override
        public void sendMessageChanged(CraftPlayer player, net.minecraft.network.chat.Component renderedMessage, Set<Audience> viewers, ChatType.Bound chatType) {
            this.broadcastToViewers(viewers, ChatContentProvider.ofStaticViewerUnaware(chatType, renderedMessage));
        }

        @Override
        public void sendOriginal(CraftPlayer player, Set<Audience> viewers, ChatType.Bound chatType) {
            this.broadcastToViewers(viewers, ChatContentProvider.ofStaticViewerUnaware(chatType, null));
        }

        private void broadcastToViewers(Collection<Audience> viewers, final ChatContentProvider provider) {
            for (Audience viewer : viewers) {
                if (acceptsNative(viewer)) {
                    this.sendNative(viewer, provider);
                } else {
                    final RenderedResult frame = provider.render(viewer);
                    final ChatType.Bound chatType = frame.bound();
                    final net.minecraft.network.chat.@Nullable Component unsigned = frame.unsigned();

                    final PlayerChatMessage msg = unsigned == null ? ChatProcessor.this.message : ChatProcessor.this.message.withUnsignedContent(unsigned);
                    viewer.sendMessage(msg.adventureView(), this.adventure(chatType));
                }
            }
        }

        private static final Map<String, net.kyori.adventure.chat.ChatType> BUILT_IN_CHAT_TYPES = Util.make(() -> {
            final Map<String, net.kyori.adventure.chat.ChatType> map = new HashMap<>();
            for (final Field declaredField : net.kyori.adventure.chat.ChatType.class.getDeclaredFields()) {
                if (Modifier.isStatic(declaredField.getModifiers()) && declaredField.getType().equals(ChatType.class)) {
                    try {
                        final net.kyori.adventure.chat.ChatType type = (net.kyori.adventure.chat.ChatType) declaredField.get(null);
                        map.put(type.key().asString(), type);
                    } catch (final ReflectiveOperationException ignore) {
                    }
                }
            }
            return map;
        });

        private net.kyori.adventure.chat.ChatType.Bound adventure(ChatType.Bound chatType) {
            @Subst("key:value") final String stringKey = Objects.requireNonNull(
                chatType.chatType().unwrapKey().orElseThrow().location(),
                () -> "No key for '%s' in CHAT_TYPE registry.".formatted(chatType)
            ).toString();
            net.kyori.adventure.chat.@Nullable ChatType adventure = BUILT_IN_CHAT_TYPES.get(stringKey);
            if (adventure == null) {
                adventure = net.kyori.adventure.chat.ChatType.chatType(Key.key(stringKey));
            }
            return adventure.bind(
                PaperAdventure.asAdventure(chatType.name()),
                chatType.targetName().map(PaperAdventure::asAdventure).orElse(null)
            );
        }

        private static boolean acceptsNative(final Audience viewer) {
            if (viewer instanceof Player || viewer instanceof ConsoleCommandSender) {
                return true;
            }
            if (viewer instanceof ForwardingAudience.Single single) {
                return acceptsNative(single.audience());
            }
            return false;
        }

        private void sendNative(final Audience viewer, final ChatContentProvider provider) {
            switch (viewer) {
                case final ConsoleCommandSender sender -> this.sendToServer(provider);
                case final CraftPlayer craftPlayer -> {
                    final RenderedResult frame = provider.render(viewer);
                    final ChatType.Bound toChatType = frame.bound();
                    final net.minecraft.network.chat.@Nullable Component messageContent = frame.unsigned();

                    craftPlayer.getHandle().sendChatMessage(ChatProcessor.this.outgoing, ChatProcessor.this.player.shouldFilterMessageTo(craftPlayer.getHandle()), toChatType, messageContent);
                }
                case final ForwardingAudience.Single single -> this.sendNative(single.audience(), provider);
                default -> throw new IllegalStateException("Should only be a Player or Console or ForwardingAudience.Single pointing to one!");
            }
        }

        private void sendToServer(final ChatContentProvider chatContentProvider) {
            final RenderedResult frame = chatContentProvider.render(ChatProcessor.this.server.console);
            final PlayerChatMessage toConsoleMessage = frame.unsigned() == null ? ChatProcessor.this.message : ChatProcessor.this.message.withUnsignedContent(frame.unsigned());
            ChatProcessor.this.server.logChatMessage(toConsoleMessage.decoratedContent(), frame.bound(), ChatProcessor.this.server.getPlayerList().verifyChatTrusted(toConsoleMessage) ? null : "Not Secure");
        }
    }

    private Set<Audience> viewersFromLegacy(final Set<Player> recipients) {
        if (recipients instanceof LazyPlayerSet lazyPlayerSet && lazyPlayerSet.isLazy()) {
            return new LazyChatAudienceSet(this.server);
        }
        final HashSet<Audience> viewers = new HashSet<>(recipients);
        viewers.add(this.server.console);
        return viewers;
    }

    static String legacyDisplayName(final CraftPlayer player) {
        if (((org.bukkit.craftbukkit.CraftWorld) player.getWorld()).getHandle().paperConfig().scoreboards.useVanillaWorldScoreboardNameColoring) {
            return legacySection().serialize(player.teamDisplayName()) + ChatFormatting.RESET;
        }
        return player.getDisplayName();
    }

    static Component displayName(final CraftPlayer player) {
        if (((CraftWorld) player.getWorld()).getHandle().paperConfig().scoreboards.useVanillaWorldScoreboardNameColoring) {
            return player.teamDisplayName();
        }
        return player.displayName();
    }

    private static ChatRenderer.Default defaultRenderer() {
        return (ChatRenderer.Default) ChatRenderer.defaultRenderer();
    }

    private static ChatRenderer legacyRenderer(final String format) {
        if (DEFAULT_LEGACY_FORMAT.equals(format)) {
            return defaultRenderer();
        }
        return ChatRenderer.viewerUnaware((player, sourceDisplayName, message) -> legacySection().deserialize(legacyFormat(format, player, legacySection().serialize(message))));
    }

    static String legacyFormat(final String format, Player player, String message) {
        return String.format(format, legacyDisplayName((CraftPlayer) player), message);
    }

    private void queueIfAsyncOrRunImmediately(final Waitable<Void> waitable) {
        if (this.async) {
            this.server.processQueue.add(waitable);
        } else {
            waitable.run();
        }
        try {
            waitable.get();
        } catch (final InterruptedException e) {
            Thread.currentThread().interrupt(); // tag, you're it
        } catch (final ExecutionException e) {
            throw new RuntimeException("Exception processing chat", e.getCause());
        }
    }

    private void post(final Event event) {
        this.server.server.getPluginManager().callEvent(event);
    }

    static boolean canYouHearMe(final HandlerList handlers) {
        return handlers.getRegisteredListeners().length > 0;
    }

    public record PaperVanillaChatRenderer(ChatTypeRenderer.ViewerAware vanillaChatTypeRenderer, boolean viewerUnaware) implements ChatRenderer {

        /*
        We wrap the viewer unaware implementation in a viewer aware implementation in order to prevent code complexity.
        In this case, the viewerUnaware flag on this renderer indicates that optimizations can occur.
        We just ignore the viewer param as its treated as effectively undefined when viewerUnaware is true.
         */
        public static ChatRenderer of(ChatTypeRenderer renderer) {
            return switch (renderer) {
                case ChatTypeRenderer.ViewerAware viewerAware -> new PaperVanillaChatRenderer(viewerAware, false);
                case ChatTypeRenderer.ViewerUnaware viewerUnaware -> new PaperVanillaChatRenderer((source, sourceDisplayName, message, viewer) -> viewerUnaware.render(source, sourceDisplayName, message), true);
            };
        }

        // api compatability renderer
        @Override
        public Component render(final Player source, final Component sourceDisplayName, final Component message, final Audience viewer) {
            ChatType.Bound nms = compute(source, sourceDisplayName, message, viewer).bound();
            ChatType chatTypeHolder = nms.chatType().value();

            return PaperAdventure.asAdventure(chatTypeHolder.chat().decorate(PaperAdventure.asVanilla(message), nms));
        }

        private RenderedResult compute(final Player source, final Component sourceDisplayName, final Component message, final Audience viewer) {
            ChatTypeRenderResult result = vanillaChatTypeRenderer.render(source, sourceDisplayName, message, viewer);
            return new RenderedResult(toHandle(((CraftPlayer) source).getHandle(), result.boundChat()), PaperAdventure.asVanilla(result.unsignedContent()));
        }

        public static net.minecraft.network.chat.ChatType.Bound toHandle(ServerPlayer player, net.kyori.adventure.chat.ChatType.Bound boundChatType) {
            Holder<ChatType> chatTypeHolder = toNmsChatType(player, boundChatType.type());

            return new net.minecraft.network.chat.ChatType.Bound(
                chatTypeHolder,
                io.papermc.paper.adventure.PaperAdventure.asVanilla(boundChatType.name()),
                Optional.ofNullable(io.papermc.paper.adventure.PaperAdventure.asVanilla(boundChatType.target()))
            );
        }

        private static Holder<ChatType> toNmsChatType(ServerPlayer player, net.kyori.adventure.chat.ChatType chatType) {
            net.minecraft.core.Registry<net.minecraft.network.chat.ChatType> chatTypeRegistry = player.registryAccess().lookupOrThrow(net.minecraft.core.registries.Registries.CHAT_TYPE);
            if (chatType instanceof PaperServerInternalAPIBridge.PaperInternalChatType paperInternalChatType) {
                return paperInternalChatType.chatType();
            }

            return chatTypeRegistry.get(ResourceKey.create(Registries.CHAT_TYPE, PaperAdventure.asVanilla(chatType.key())))
                .orElseThrow(() -> new IllegalStateException("Expected a registered chat type!"));
        }

    }

    /*
    This class is used for providing chat type / unsigned content to users.

    The isViewerUnaware flag indicates that render may be called with an undefined audience, which can be
    used for slight optimizations.
     */
    public interface ChatContentProvider {

        static ChatContentProvider ofViewerAware(Function<Audience, net.minecraft.network.chat.Component> function, ChatType.Bound chatType) {
            return ofViewerAware((v) -> new RenderedResult(chatType, function.apply(v)));
        }

        static ChatContentProvider ofViewerAware(Function<Audience, RenderedResult> function) {
            return new ChatContentProvider() {
                @Override
                public RenderedResult render(final Audience audience) {
                    return function.apply(audience);
                }

                @Override
                public boolean isViewerUnaware() {
                    return false;
                }
            };
        }

        static ChatContentProvider ofStaticViewerUnaware(ChatType.Bound chatType, net.minecraft.network.chat.@Nullable Component unsignedContent) {
            return ofStaticViewerUnaware(new RenderedResult(chatType, unsignedContent));
        }

        static ChatContentProvider ofStaticViewerUnaware(RenderedResult result) {
            record ViewerUnawareStatic(RenderedResult staticFrame) implements ChatContentProvider {

                @Override
                public RenderedResult render(final Audience audience) {
                    return this.staticFrame;
                }

                @Override
                public boolean isViewerUnaware() {
                    return true;
                }
            }

            return new ViewerUnawareStatic(result);
        }

        RenderedResult render(Audience audience);

        boolean isViewerUnaware();
    }

    public record RenderedResult(ChatType.Bound bound, net.minecraft.network.chat.@Nullable Component unsigned) {

        public net.minecraft.network.chat.Component unsignedOrElse(net.minecraft.network.chat.Component component) {
            return this.unsigned != null ? this.unsigned : component;
        }
    }
}
