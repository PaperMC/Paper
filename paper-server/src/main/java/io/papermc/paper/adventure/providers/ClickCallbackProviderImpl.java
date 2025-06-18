package io.papermc.paper.adventure.providers;

import io.papermc.paper.adventure.PaperAdventure;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.nbt.api.BinaryTagHolder;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@SuppressWarnings("UnstableApiUsage") // permitted provider
public class ClickCallbackProviderImpl implements ClickCallback.Provider {
    private static final Key CLICK_CALLBACK_KEY = Key.key("paper", "click_callback");
    public static final ResourceLocation CLICK_CALLBACK_RESOURCE_LOCATION = PaperAdventure.asVanilla(CLICK_CALLBACK_KEY);

    public static final CallbackManager CALLBACK_MANAGER = new CallbackManager();

    @Override
    public @NotNull ClickEvent create(final @NotNull ClickCallback<Audience> callback, final ClickCallback.@NotNull Options options) {
        return ClickEvent.custom(CLICK_CALLBACK_KEY, BinaryTagHolder.binaryTagHolder(CALLBACK_MANAGER.addCallback(callback, options).toString()));
    }

    public static final class CallbackManager {

        private final Map<String , StoredCallback> callbacks = new HashMap<>();
        private final Queue<StoredCallback> queue = new ConcurrentLinkedQueue<>();

        private CallbackManager() {
        }

        public UUID addCallback(final @NotNull ClickCallback<Audience> callback, final ClickCallback.@NotNull Options options) {
            final UUID id = UUID.randomUUID();
            this.queue.add(new StoredCallback(callback, options, id));
            return id;
        }

        public void handleQueue(final int currentTick) {
            // Evict expired entries
            if (currentTick % 100 == 0) {
                this.callbacks.values().removeIf(callback -> !callback.valid());
            }

            // Add entries from queue
            StoredCallback callback;
            while ((callback = this.queue.poll()) != null) {
                this.callbacks.put(callback.id().toString(), callback);
            }
        }

        public void runCallback(final @NotNull Audience audience, final String id) {
            final StoredCallback callback = this.callbacks.get(id);
            if (callback != null && callback.valid()) { //TODO Message if expired/invalid?
                callback.takeUse();
                callback.callback.accept(audience);
            }
        }
    }

    private static final class StoredCallback {
        private final long startedAt = System.nanoTime();
        private final ClickCallback<Audience> callback;
        private final long lifetime;
        private final UUID id;
        private int remainingUses;

        private StoredCallback(final @NotNull ClickCallback<Audience> callback, final ClickCallback.@NotNull Options options, final UUID id) {
            this.callback = callback;
            this.lifetime = options.lifetime().toNanos();
            this.remainingUses = options.uses();
            this.id = id;
        }

        public void takeUse() {
            if (this.remainingUses != ClickCallback.UNLIMITED_USES) {
                this.remainingUses--;
            }
        }

        public boolean hasRemainingUses() {
            return this.remainingUses == ClickCallback.UNLIMITED_USES || this.remainingUses > 0;
        }

        public boolean expired() {
            return System.nanoTime() - this.startedAt >= this.lifetime;
        }

        public boolean valid() {
            return hasRemainingUses() && !expired();
        }

        public UUID id() {
            return this.id;
        }
    }
}
