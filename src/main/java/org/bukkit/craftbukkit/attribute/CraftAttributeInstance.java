package org.bukkit.craftbukkit.attribute;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.attribute.AttributeModifier;

public class CraftAttributeInstance implements AttributeInstance {

    private final net.minecraft.server.AttributeInstance handle;
    private final Attribute attribute;

    public CraftAttributeInstance(net.minecraft.server.AttributeInstance handle, Attribute attribute) {
        this.handle = handle;
        this.attribute = attribute;
    }

    @Override
    public Attribute getAttribute() {
        return attribute;
    }

    @Override
    public double getBaseValue() {
        return handle.b();
    }

    @Override
    public void setBaseValue(double d) {
        handle.setValue(d);
    }

    @Override
    public Collection<AttributeModifier> getModifiers() {
        List<AttributeModifier> result = new ArrayList<AttributeModifier>();
        for (net.minecraft.server.AttributeModifier nms : handle.c()) {
            result.add(convert(nms));
        }

        return result;
    }

    @Override
    public void addModifier(AttributeModifier modifier) {
        Preconditions.checkArgument(modifier != null, "modifier");
        handle.b(convert(modifier));
    }

    @Override
    public void removeModifier(AttributeModifier modifier) {
        Preconditions.checkArgument(modifier != null, "modifier");
        handle.c(convert(modifier));
    }

    @Override
    public double getValue() {
        return handle.getValue();
    }

    @Override
    public double getDefaultValue() {
       return handle.getAttribute().getDefault();
    }

    private static net.minecraft.server.AttributeModifier convert(AttributeModifier bukkit) {
        return new net.minecraft.server.AttributeModifier(bukkit.getUniqueId(), bukkit.getName(), bukkit.getAmount(), bukkit.getOperation().ordinal());
    }

    private static AttributeModifier convert(net.minecraft.server.AttributeModifier nms) {
        return new AttributeModifier(nms.a(), nms.b(), nms.d(), AttributeModifier.Operation.values()[nms.c()]);
    }
}
