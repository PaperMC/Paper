package io.papermc.paper.util;

import com.sun.security.auth.module.NTSystem;
import com.sun.security.auth.module.UnixSystem;
import java.util.Set;
import org.apache.commons.lang3.SystemUtils;

public class ServerEnvironment {
    private static final boolean RUNNING_AS_ROOT_OR_ADMIN;
    private static final String WINDOWS_HIGH_INTEGRITY_LEVEL = "S-1-16-12288";

    static {
        if (SystemUtils.IS_OS_WINDOWS) {
            RUNNING_AS_ROOT_OR_ADMIN = Set.of(new NTSystem().getGroupIDs()).contains(WINDOWS_HIGH_INTEGRITY_LEVEL);
        } else {
            try {
                RUNNING_AS_ROOT_OR_ADMIN = new UnixSystem().getUid() == 0;
            } catch (UnsatisfiedLinkError ignored) {
                // Java 27 can throw when the UID has no passwd entry (https://github.com/PaperMC/Paper/issues/14391).
                // This check only controls a warning, so don't prevent startup.
                RUNNING_AS_ROOT_OR_ADMIN = false;
            }
        }
    }

    public static boolean userIsRootOrAdmin() {
        return RUNNING_AS_ROOT_OR_ADMIN;
    }

    public static String awtDependencyCheck() {
        try {
            new java.awt.Color(0);
        } catch (UnsatisfiedLinkError e) {
            return e.getClass().getName() + ": " + e.getMessage();
        }

        return null;
    }
}
