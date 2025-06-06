package io.papermc.paper.dialog;

import java.util.Optional;
import java.util.ServiceLoader;

public interface DialogBuilderBridge {
    Optional<DialogBuilderBridge> BRIDGE = ServiceLoader.load(DialogBuilderBridge.class).findFirst();
}
