package io.papermc.paper.plugin.lifecycle.event.registrar;

import io.papermc.paper.plugin.lifecycle.event.LifecycleEventOwner;
import io.papermc.paper.plugin.lifecycle.event.PaperLifecycleEvent;
import io.papermc.paper.plugin.lifecycle.event.types.OwnerAwareLifecycleEvent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public class RegistrarEventImpl<R extends PaperRegistrar<? super O>, O extends LifecycleEventOwner> implements PaperLifecycleEvent, OwnerAwareLifecycleEvent<O>, RegistrarEvent<R> {

    private final R registrar;
    private final Class<? extends O> ownerClass;

    public RegistrarEventImpl(final R registrar, final Class<? extends O> ownerClass) {
        this.registrar = registrar;
        this.ownerClass = ownerClass;
    }

    @Override
    public R registrar() {
        return this.registrar;
    }

    @Override
    public final void setOwner(final @Nullable O owner) {
        this.registrar.setCurrentContext(owner);
    }

    @Override
    public final @Nullable O castOwner(final LifecycleEventOwner owner) {
        return this.ownerClass.isInstance(owner) ? this.ownerClass.cast(owner) : null;
    }

    @Override
    public void invalidate() {
        this.registrar.invalidate();
    }

    @Override
    public String toString() {
        return "RegistrarEventImpl{" +
            "registrar=" + this.registrar +
            ", ownerClass=" + this.ownerClass +
            '}';
    }

    public static class ReloadableImpl<R extends PaperRegistrar<? super O>, O extends LifecycleEventOwner> extends RegistrarEventImpl<R, O> implements ReloadableRegistrarEvent<R> {

        private final ReloadableRegistrarEvent.Cause cause;

        public ReloadableImpl(final R registrar, final Class<? extends O> ownerClass, final Cause cause) {
            super(registrar, ownerClass);
            this.cause = cause;
        }

        @Override
        public Cause cause() {
            return this.cause;
        }

        @Override
        public String toString() {
            return "ReloadableImpl{" +
                "cause=" + this.cause +
                "} " + super.toString();
        }
    }
}
