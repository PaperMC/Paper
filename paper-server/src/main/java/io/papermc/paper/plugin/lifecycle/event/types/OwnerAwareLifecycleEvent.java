package io.papermc.paper.plugin.lifecycle.event.types;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public interface OwnerAwareLifecycleEvent<O extends LifecycleEventOwner> extends LifecycleEvent {

    void setOwner(@Nullable O owner);

    @Nullable O castOwner(LifecycleEventOwner owner);
}
