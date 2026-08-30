package io.papermc.checkstyle.filters.util;

import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.Violation;
import com.puppycrawl.tools.checkstyle.filters.SuppressFilterElement;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.IntStream;
import org.jspecify.annotations.Nullable;

public class BetterSuppressFilterElement extends SuppressFilterElement {

    private static final MethodHandle VIOLATION_ARGS;

    static {
        try {
            VIOLATION_ARGS = MethodHandles.privateLookupIn(Violation.class, MethodHandles.lookup()).findGetter(Violation.class, "args", Object[].class);
        } catch (final IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private final @Nullable Pattern messageKey;
    private final @Nullable List<Pattern> arguments;

    public BetterSuppressFilterElement(
        final @Nullable Pattern files,
        final @Nullable Pattern checks,
        final @Nullable Pattern message,
        final @Nullable String moduleId,
        final @Nullable String lines,
        final @Nullable String columns,
        final @Nullable Pattern messageKey,
        final @Nullable List<Pattern> arguments
    ) {
        super(files, checks, message, moduleId, lines, columns);
        this.messageKey = messageKey;
        this.arguments = arguments;
    }

    @Override
    public boolean accept(final AuditEvent event) {
        return super.accept(event) || !this.isMessageKeyMatching(event) || !this.isMessageArgumentsMatching(event);
    }

    private boolean isMessageKeyMatching(final AuditEvent event) {
        return this.messageKey == null || this.messageKey.matcher(event.getViolation().getKey()).find();
    }

    private boolean isMessageArgumentsMatching(final AuditEvent event) {
        if (this.arguments == null || this.arguments.isEmpty()) {
            return true;
        }
        final Object[] args;
        try {
            args = ((Object[]) VIOLATION_ARGS.invoke(event.getViolation()));
        } catch (final Throwable e) {
            throw new RuntimeException(e);
        }
        return IntStream.range(0, Math.min(this.arguments.size(), args.length)).allMatch(i -> {
            final Object arg = args[i];
            final Pattern argPattern = this.arguments.get(i);
            return argPattern.matcher(arg.toString()).find();
        });
    }
}
