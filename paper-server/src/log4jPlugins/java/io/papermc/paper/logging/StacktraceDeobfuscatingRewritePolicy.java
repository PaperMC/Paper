package io.papermc.paper.logging;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.impl.Log4jLogEvent;
import org.checkerframework.checker.nullness.qual.NonNull;

@Plugin(
    name = "StacktraceDeobfuscatingRewritePolicy",
    category = Core.CATEGORY_NAME,
    elementType = "rewritePolicy",
    printObject = true
)
public final class StacktraceDeobfuscatingRewritePolicy implements RewritePolicy {
    private static final MethodHandle DEOBFUSCATE_THROWABLE;

    static {
        try {
            final Class<?> cls = Class.forName("io.papermc.paper.util.StacktraceDeobfuscator");
            final MethodHandles.Lookup lookup = MethodHandles.lookup();
            final VarHandle instanceHandle = lookup.findStaticVarHandle(cls, "INSTANCE", cls);
            final Object deobfuscator = instanceHandle.get();
            DEOBFUSCATE_THROWABLE = lookup
                .unreflect(cls.getDeclaredMethod("deobfuscateThrowable", Throwable.class))
                .bindTo(deobfuscator);
        } catch (final ReflectiveOperationException ex) {
            throw new IllegalStateException(ex);
        }
    }

    private StacktraceDeobfuscatingRewritePolicy() {
    }

    @Override
    public @NonNull LogEvent rewrite(final @NonNull LogEvent rewrite) {
        final Throwable thrown = rewrite.getThrown();
        if (thrown != null) {
            deobfuscateThrowable(thrown);
            return new Log4jLogEvent.Builder(rewrite)
                .setThrownProxy(null)
                .build();
        }
        return rewrite;
    }

    private static void deobfuscateThrowable(final Throwable thrown) {
        try {
            DEOBFUSCATE_THROWABLE.invoke(thrown);
        } catch (final Error e) {
            throw e;
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
    }

    @PluginFactory
    public static @NonNull StacktraceDeobfuscatingRewritePolicy createPolicy() {
        return new StacktraceDeobfuscatingRewritePolicy();
    }
}
