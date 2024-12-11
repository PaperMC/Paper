package io.papermc.paper.logging;

import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.impl.ExtendedClassInfo;
import org.apache.logging.log4j.core.impl.ExtendedStackTraceElement;
import org.apache.logging.log4j.core.impl.ThrowableProxy;

public class ExtraClassInfoLogEvent extends DelegateLogEvent {

    private boolean fixed;

    public ExtraClassInfoLogEvent(LogEvent original) {
        super(original);
    }

    @Override
    public ThrowableProxy getThrownProxy() {
        if (fixed) {
            return super.getThrownProxy();
        }
        rewriteStackTrace(super.getThrownProxy());
        fixed = true;
        return super.getThrownProxy();
    }

    private void rewriteStackTrace(ThrowableProxy throwable) {
        ExtendedStackTraceElement[] stackTrace = throwable.getExtendedStackTrace();
        for (int i = 0; i < stackTrace.length; i++) {
            ExtendedClassInfo classInfo = stackTrace[i].getExtraClassInfo();
            if (classInfo.getLocation().equals("?")) {
                StackTraceElement element = stackTrace[i].getStackTraceElement();
                String classLoaderName = element.getClassLoaderName();
                if (classLoaderName != null) {
                    stackTrace[i] = new ExtendedStackTraceElement(element,
                        new ExtendedClassInfo(classInfo.getExact(), classLoaderName, "?"));
                }
            }
        }
        if (throwable.getCauseProxy() != null) {
            rewriteStackTrace(throwable.getCauseProxy());
        }
        if (throwable.getSuppressedProxies() != null) {
            for (ThrowableProxy proxy : throwable.getSuppressedProxies()) {
                rewriteStackTrace(proxy);
            }
        }
    }
}
