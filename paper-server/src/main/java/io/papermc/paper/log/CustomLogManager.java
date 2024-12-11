package io.papermc.paper.log;

import java.util.logging.LogManager;

public class CustomLogManager extends LogManager {
    private static CustomLogManager instance;

    public CustomLogManager() {
        instance = this;
    }

    @Override
    public void reset() {
        // Ignore calls to this method
    }

    private void superReset() {
        super.reset();
    }

    public static void forceReset() {
        if (instance != null) {
            instance.superReset();
        }
    }
}
