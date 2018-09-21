package org.bukkit.craftbukkit.attribute;

import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import java.util.Locale;
import net.minecraft.server.AttributeMapBase;
import org.apache.commons.lang3.EnumUtils;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;

public class CraftAttributeMap implements Attributable {

    private final AttributeMapBase handle;

    public CraftAttributeMap(AttributeMapBase handle) {
        this.handle = handle;
    }

    @Override
    public AttributeInstance getAttribute(Attribute attribute) {
        Preconditions.checkArgument(attribute != null, "attribute");
        net.minecraft.server.AttributeInstance nms = handle.a(toMinecraft(attribute.name()));

        return (nms == null) ? null : new CraftAttributeInstance(nms, attribute);
    }

    public static String toMinecraft(String bukkit) {
        int first = bukkit.indexOf('_');
        int second = bukkit.indexOf('_', first + 1);

        StringBuilder sb = new StringBuilder(bukkit.toLowerCase(java.util.Locale.ENGLISH));

        sb.setCharAt(first, '.');
        if (second != -1) {
            sb.deleteCharAt(second);
            sb.setCharAt(second, bukkit.charAt(second + 1));
        }

        return sb.toString();
    }

    public static String toMinecraft(Attribute attribute) {
        return toMinecraft(attribute.name());
    }

    public static Attribute fromMinecraft(String nms) {
        String[] split = nms.split("\\.", 2);

        String generic = split[0];
        String descriptor = CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, split[1]); // movementSpeed -> MOVEMENT_SPEED
        String fin = generic + "_" + descriptor;
        return EnumUtils.getEnum(Attribute.class, fin.toUpperCase(Locale.ROOT)); // so we can return null without throwing exceptions
    }
}
