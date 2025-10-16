package io.papermc.paper.profile;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import org.jspecify.annotations.NullMarked;

@NullMarked
public class MutablePropertyMap extends PropertyMap {
    private final Multimap<String, Property> properties = HashMultimap.create();

    public MutablePropertyMap() {
        super(ImmutableMultimap.of());
    }

    public MutablePropertyMap(final Multimap<String, Property> properties) {
        this();
        this.putAll(properties);
    }

    @Override
    protected Multimap<String, Property> delegate() {
        return this.properties;
    }
}
