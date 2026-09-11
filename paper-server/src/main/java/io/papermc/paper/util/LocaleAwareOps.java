package io.papermc.paper.util;

import com.mojang.serialization.DynamicOps;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.DelegatingOps;
import net.minecraft.resources.RegistryOps;
import java.util.Locale;

public interface LocaleAwareOps {
    Locale locale();

    class Delegating<T> extends DelegatingOps<T> implements LocaleAwareOps {
        private final Locale locale;

        public Delegating(final DynamicOps<T> delegate, final Locale locale) {
            super(delegate);
            this.locale = locale;
        }

        @Override
        public Locale locale() {
            return locale;
        }
    }

    class Registry<T> extends RegistryOps<T> implements LocaleAwareOps {
        private final Locale locale;

        public Registry(final DynamicOps<T> parent, final HolderLookup.Provider lookupProvider, final Locale locale) {
            super(parent, new RegistryOps.HolderLookupAdapter(lookupProvider));
            this.locale = locale;
        }

        @Override
        public Locale locale() {
            return locale;
        }
    }
}
