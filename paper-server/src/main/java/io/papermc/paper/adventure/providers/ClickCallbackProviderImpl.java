package io.papermc.paper.adventure.providers;

import io.papermc.paper.adventure.PaperAdventure;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.event.ClickCallback;
import net.kyori.adventure.text.event.ClickEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("UnstableApiUsage") // permitted provider
public class ClickCallbackProviderImpl implements ClickCallback.Provider {
    private static final Key CLICK_CALLBACK_KEY = Key.key("paper", "click_callback");
    private static final String ID_KEY = "id";

    public static final ResourceLocation CLICK_CALLBACK_RESOURCE_LOCATION = PaperAdventure.asVanilla(CLICK_CALLBACK_KEY);
    public static final CallbackManager CALLBACK_MANAGER = new CallbackManager();

    @Override
    public @NotNull ClickEvent create(final @NotNull ClickCallback<Audience> callback, final ClickCallback.@NotNull Options options) {
        final CompoundTag tag = new CompoundTag();
        tag.putString(ID_KEY, CALLBACK_MANAGER.addCallback(callback, options).toString());
        return ClickEvent.custom(CLICK_CALLBACK_KEY, PaperAdventure.asBinaryTagHolder(tag));
    }

    public static final class CallbackManager {

        private final Map<UUID, StoredCallback> callbacks = new HashMap<>();
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
                this.callbacks.put(callback.id(), callback);
            }
        }

        public void tryRunCallback(final @NotNull Audience audience, final Tag tag) {
            tag.asCompound().flatMap(t -> t.getString(ID_KEY)).ifPresent(s -> {
                final UUID id;
                try {
                    id = UUID.fromString(s);
                } catch (final IllegalArgumentException ignored) {
                    return;
                }

                final StoredCallback callback = this.callbacks.get(id);
                if (callback != null && callback.valid()) {
                    callback.takeUse();
                    callback.callback.accept(audience);
                }
            });
        }
    }

    private static final class StoredCallback {
        private final long startedAt = System.nanoTime();
        private final ClickCallback<Audience> callback;
        private final long lifetime;
        private final UUID id;
        private int remainingUses;

        private StoredCallback(final @NotNull ClickCallback<Audience> callback, final ClickCallback.@NotNull Options options, final UUID id) {
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

        public void takeUse() {
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

        public boolean valid() {
            return hasRemainingUses() && !expired();
        }

        public UUID id() {
            return this.id;
        }
    }
}
