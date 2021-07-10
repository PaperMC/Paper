package io.papermc.paper.logging;

import org.apache.logging.log4j.core.Core;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.rewrite.RewritePolicy;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.jetbrains.annotations.NotNull;

@Plugin(
    name = "ExtraClassInfoRewritePolicy",
    category = Core.CATEGORY_NAME,
    elementType = "rewritePolicy",
    printObject = true
)
public final class ExtraClassInfoRewritePolicy implements RewritePolicy {
    @Override
    public LogEvent rewrite(LogEvent source) {
        if (source.getThrown() != null) {
            return new ExtraClassInfoLogEvent(source);
        }
        return source;
    }

    @PluginFactory
    public static @NotNull ExtraClassInfoRewritePolicy createPolicy() {
        return new ExtraClassInfoRewritePolicy();
    }
}
