package io.papermc.checkstyle.filters;

import com.puppycrawl.tools.checkstyle.AbstractAutomaticBean;
import com.puppycrawl.tools.checkstyle.api.AuditEvent;
import com.puppycrawl.tools.checkstyle.api.Filter;
import com.puppycrawl.tools.checkstyle.api.Violation;
import io.papermc.checkstyle.filters.util.BetterSuppressFilterElement;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import org.jspecify.annotations.Nullable;

public class BetterSuppressionSingleFilter extends AbstractAutomaticBean implements Filter {

    private static final MethodHandle VIOLATION_ARGS;

    static {
        try {
            VIOLATION_ARGS = MethodHandles.privateLookupIn(Violation.class, MethodHandles.lookup())
                .findGetter(Violation.class, "args", Object[].class);
        } catch (final IllegalAccessException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    private @Nullable BetterSuppressFilterElement filter;
    private @Nullable Pattern checks;
    private @Nullable Pattern messageKey;
    private @Nullable List<Pattern> arguments;

    public void setChecks(final Pattern checks) {
        this.checks = checks;
    }

    public void setMessageKey(final Pattern messageKey) {
        this.messageKey = messageKey;
    }

    public void setArguments(final String... arguments) {
        this.arguments = Arrays.stream(arguments).map(Pattern::compile).toList();
    }

    @Override
    protected void finishLocalSetup() {
        this.filter = new BetterSuppressFilterElement(
            null,
            this.checks,
            null,
            null,
            null,
            null,
            this.messageKey,
            this.arguments
        );
    }

    @Override
    public boolean accept(final AuditEvent event) {
        return this.filter.accept(event);
    }
}
