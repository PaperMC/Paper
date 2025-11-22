package io.papermc.paper.adventure.providers;

import com.google.common.base.Predicates;
import io.papermc.paper.adventure.PaperAdventure;
import io.papermc.paper.dialog.PaperDialogResponseView;
import io.papermc.paper.registry.data.dialog.action.DialogActionCallback;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.minecraft.core.UUIDUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage") // permitted provider
public class ClickCallbackProviderImpl implements ClickCallback.Provider {
    private static final Key ADVENTURE_CLICK_CALLBACK_KEY = Key.key("paper", "click_callback");
    public static final Key DIALOG_CLICK_CALLBACK_KEY = Key.key("paper", "dialog_click_callback");
    public static final String ID_KEY = "id";

    public static final AdventureClick ADVENTURE_CLICK_MANAGER = new AdventureClick();
    public static final DialogClickManager DIALOG_CLICK_MANAGER = new DialogClickManager();

    @Override
    public @NotNull ClickEvent create(final @NotNull ClickCallback<Audience> callback, final ClickCallback.@NotNull Options options) {
        final CompoundTag tag = new CompoundTag();
        tag.store(ID_KEY, UUIDUtil.CODEC, ADVENTURE_CLICK_MANAGER.addCallback(callback, options));
        return ClickEvent.custom(ADVENTURE_CLICK_CALLBACK_KEY, PaperAdventure.asBinaryTagHolder(tag));
    }

    public static final class AdventureClick extends CallbackManager<ClickCallback<Audience>, UUID> {

        private AdventureClick() {
            super(PaperAdventure.asVanilla(ADVENTURE_CLICK_CALLBACK_KEY)::equals);
        }

        public UUID addCallback(final @NotNull ClickCallback<Audience> callback, final ClickCallback.@NotNull Options options) {
            return this.addCallback(UUID.randomUUID(), callback, options);
        }

        @Override
        void doRunCallback(final @NotNull Audience audience, final Key key, final Tag tag) {
            tag.asCompound().ifPresent(t -> {
                final Optional<UUID> id = t.read(ID_KEY, UUIDUtil.CODEC);
                if (id.isEmpty()) {
                    return;
                }
                this.tryConsumeCallback(id.get(), callback -> {
                    callback.accept(audience);
                });
            });
        }
    }

    public static final class DialogClickManager extends CallbackManager<DialogActionCallback, UUID> {

        public DialogClickManager() {
            super(PaperAdventure.asVanilla(DIALOG_CLICK_CALLBACK_KEY)::equals);
        }

        @Override
        void doRunCallback(final @NotNull Audience audience, final Key key, final Tag tag) {
            tag.asCompound().ifPresent(t -> {
                final Optional<UUID> id = t.read(ID_KEY, UUIDUtil.CODEC);
                if (id.isEmpty()) {
                    return;
                }
                this.tryConsumeCallback(id.get(), callback -> {
                    callback.accept(PaperDialogResponseView.createUnvalidatedResponse(t), audience);
                });
            });
        }
    }


    abstract static class CallbackManager<C, I> {

        private final Predicate<ResourceLocation> locationPredicate;
        protected final Map<I, StoredCallback<C, I>> callbacks = new HashMap<>();
        private final Queue<StoredCallback<C, I>> queue = new ConcurrentLinkedQueue<>();

        protected CallbackManager(final Predicate<ResourceLocation> locationPredicate) {
            this.locationPredicate = locationPredicate;
        }

        protected CallbackManager() {
            this.locationPredicate = Predicates.alwaysTrue();
        }

        public I addCallback(final I id, final @NotNull C callback, final ClickCallback.@NotNull Options options) {
            this.queue.add(new StoredCallback<>(callback, options, id));
            return id;
        }

        public void handleQueue(final int currentTick) {
            // Evict expired entries
            if (currentTick % 100 == 0) {
                this.callbacks.values().removeIf(callback -> !callback.valid());
            }

            // Add entries from queue
            StoredCallback<C, I> callback;
            while ((callback = this.queue.poll()) != null) {
                this.callbacks.put(callback.id(), callback);
            }
        }

        final void tryConsumeCallback(final I key, final Consumer<? super C> callbackConsumer) {
            final StoredCallback<C, I> callback = this.callbacks.get(key);
            if (callback != null && callback.valid()) {
                callback.takeUse();
                callbackConsumer.accept(callback.callback);
            }
        }

        abstract void doRunCallback(final @NotNull Audience audience, final Key key, final Tag tag);

        public final void tryRunCallback(final @NotNull Audience audience, final ResourceLocation key, final Optional<? extends Tag> tag) {
            if (!this.locationPredicate.test(key) || tag.isEmpty()) return;
            this.doRunCallback(audience, PaperAdventure.asAdventure(key), tag.get());
        }
    }

    public static final class StoredCallback<C, I> {
        private final long startedAt = System.nanoTime();
        private final C callback;
        private final long lifetime;
        private final I id;
        private int remainingUses;

        private StoredCallback(final @NotNull C callback, final ClickCallback.@NotNull Options options, final I id) {
            long lifetimeValue;
            this.callback = callback;
            try {
                lifetimeValue = options.lifetime().toNanos();
            } catch (final ArithmeticException ex) {
                lifetimeValue = Long.MAX_VALUE;
            }
            this.lifetime = lifetimeValue;
            this.remainingUses = options.uses();
            this.id = id;
        }

        private void takeUse() {
            if (this.remainingUses != ClickCallback.UNLIMITED_USES) {
                this.remainingUses--;
            }
        }

        public boolean hasRemainingUses() {
            return this.remainingUses == ClickCallback.UNLIMITED_USES || this.remainingUses > 0;
        }

        public boolean expired() {
            if (this.lifetime == Long.MAX_VALUE) return false;
            return System.nanoTime() - this.startedAt >= this.lifetime;
        }

        private boolean valid() {
            return this.hasRemainingUses() && !this.expired();
        }

        public I id() {
            return this.id;
        }
    }
}
