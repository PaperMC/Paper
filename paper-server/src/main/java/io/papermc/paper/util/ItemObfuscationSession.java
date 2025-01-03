package io.papermc.paper.util;

import com.google.common.base.Preconditions;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The item obfuscation session may be started
 */
@NullMarked
public class ItemObfuscationSession implements SafeAutoClosable {

    static final ThreadLocal<ItemObfuscationSession> THREAD_LOCAL_SESSION = ThreadLocal.withInitial(ItemObfuscationSession::new);
    static final ItemObfuscationSession NOOP = new ItemObfuscationSession() {
        @Override
        public boolean isNotSanitizing() {
            return true;
        }
    };

    public static ItemObfuscationSession noop() {
        return NOOP;
    }
    public static ItemObfuscationSession currentSession() {
        return ItemObfuscationBinding.ENABLED ? THREAD_LOCAL_SESSION.get() : NOOP;
    }

    public static @Nullable ItemObfuscationSession start(final boolean sanitize) {
        if (!ItemObfuscationBinding.ENABLED) return noop();

        final ItemObfuscationSession sanitizer = THREAD_LOCAL_SESSION.get();
        if (sanitize) {
            sanitizer.start();
        }
        return sanitizer;
    }

    public static @Nullable SafeAutoClosable withContext(final ItemStack itemStack) {
        if (!ItemObfuscationBinding.ENABLED) return () -> {
        };
        final ItemObfuscationSession sanitizer = THREAD_LOCAL_SESSION.get();

        // Don't pass any context if we are not currently sanitizing
        if (sanitizer.isNotSanitizing()) {
            return () -> {
            };
        }

        // Store the current context as a parent in the next context for later restoration.
        final ObfuscationContext newContext = new ObfuscationContext(sanitizer, sanitizer.context(), itemStack);
        sanitizer.switchContext(newContext);
        return newContext;
    }

    private boolean isSanitizing;
    private ObfuscationContext context = null;

    public void switchContext(final ObfuscationContext context) {
        Preconditions.checkState(this.isSanitizing, "Attempted to switch context while not sanitizing");
        this.context = context;
    }

    public ObfuscationContext context() {
        return this.context;
    }

    public void start() {
        this.isSanitizing = true;
    }

    @Override
    public void close() {
        this.isSanitizing = false;
    }

    public boolean isNotSanitizing() {
        return !this.isSanitizing;
    }

    public record ObfuscationContext(
        ItemObfuscationSession parent,
        @Nullable ObfuscationContext previousContext,
        ItemStack itemStack
    ) implements SafeAutoClosable {

        @Override
        public void close() {
            // Restore the previous context when this context is closed.
            this.parent().switchContext(this.previousContext);
        }
    }

}
