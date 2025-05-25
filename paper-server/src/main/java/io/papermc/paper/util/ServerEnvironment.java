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
            RUNNING_AS_ROOT_OR_ADMIN = new UnixSystem().getUid() == 0;
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
