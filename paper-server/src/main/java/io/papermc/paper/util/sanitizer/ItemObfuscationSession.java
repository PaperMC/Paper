package io.papermc.paper.util.sanitizer;

import java.util.function.UnaryOperator;
import com.google.common.base.Preconditions;
import io.papermc.paper.util.SafeAutoClosable;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

/**
 * The item obfuscation session may be started by a thread to indicate that items should be obfuscated when serialized
 * for network usage.
 * <p>
 * A session is persistent throughout an entire thread and will be "activated" by passing an {@link ObfuscationContext}
 * to start/switch context methods.
 */
@NullMarked
public class ItemObfuscationSession implements SafeAutoClosable {

    static final ThreadLocal<ItemObfuscationSession> THREAD_LOCAL_SESSION = ThreadLocal.withInitial(ItemObfuscationSession::new);

    public static ItemObfuscationSession currentSession() {
        return THREAD_LOCAL_SESSION.get();
    }

    /**
     * Obfuscation level on a specific context.
     */
    public enum ObfuscationLevel {
        NONE,
        OVERSIZED,
        ALL;

        public boolean obfuscateOversized() {
            return switch (this) {
                case OVERSIZED, ALL -> true;
                default -> false;
            };
        }

        public boolean isObfuscating() {
            return this != NONE;
        }
    }

    public static ItemObfuscationSession start(final ObfuscationLevel level) {
        final ItemObfuscationSession sanitizer = THREAD_LOCAL_SESSION.get();
        sanitizer.switchContext(new ObfuscationContext(sanitizer, null, null, level));
        return sanitizer;
    }

    /**
     * Updates the context of the currently running session by requiring the unary operator to emit a new context
     * based on the current one.
     * The method expects the caller to use the withers on the context.
     *
     * @param contextUpdater the operator to construct the new context.
     * @return the context callback to close once the context expires.
     */
    public static SafeAutoClosable withContext(final UnaryOperator<ObfuscationContext> contextUpdater) {
        final ItemObfuscationSession session = THREAD_LOCAL_SESSION.get();

        // Don't pass any context if we are not currently sanitizing
        if (!session.obfuscationLevel().isObfuscating()) return () -> {
        };

        final ObfuscationContext newContext = contextUpdater.apply(session.context());
        Preconditions.checkState(newContext != session.context(), "withContext yielded same context instance, this will break the stack on close");
        session.switchContext(newContext);
        return newContext;
    }

    private final ObfuscationContext root = new ObfuscationContext(this, null, null, ObfuscationLevel.NONE);
    private ObfuscationContext context = root;

    public void switchContext(final ObfuscationContext context) {
        this.context = context;
    }

    public ObfuscationContext context() {
        return this.context;
    }

    @Override
    public void close() {
        this.context = root;
    }

    public ObfuscationLevel obfuscationLevel() {
        return this.context.level;
    }

    public record ObfuscationContext(
        ItemObfuscationSession parent,
        @Nullable ObfuscationContext previousContext,
        @Nullable ItemStack itemStack,
        ObfuscationLevel level
    ) implements SafeAutoClosable {

        public ObfuscationContext itemStack(final ItemStack itemStack) {
            return new ObfuscationContext(this.parent, this, itemStack, this.level);
        }

        public ObfuscationContext level(final ObfuscationLevel obfuscationLevel) {
            return new ObfuscationContext(this.parent, this, this.itemStack, obfuscationLevel);
        }

        @Override
        public void close() {
            // Restore the previous context when this context is closed.
            this.parent().switchContext(this.previousContext);
        }
    }

}
