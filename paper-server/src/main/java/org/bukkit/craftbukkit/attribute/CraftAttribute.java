package org.bukkit.craftbukkit.attribute;

import com.google.common.base.Preconditions;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.util.OldEnumHolderable;
import java.util.Locale;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.legacy.FieldRename;
import org.bukkit.craftbukkit.util.ApiVersion;

public class CraftAttribute extends OldEnumHolderable<Attribute, net.minecraft.world.entity.ai.attributes.Attribute> implements Attribute {

    private static int count = 0;

    public static Attribute minecraftToBukkit(net.minecraft.world.entity.ai.attributes.Attribute minecraft) {
        return CraftRegistry.minecraftToBukkit(minecraft, Registries.ATTRIBUTE);
    }

    public static Attribute minecraftHolderToBukkit(Holder<net.minecraft.world.entity.ai.attributes.Attribute> minecraft) {
        return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.ATTRIBUTE);
    }

    public static Attribute stringToBukkit(String string) {
        Preconditions.checkArgument(string != null);

        // We currently do not have any version-dependent remapping, so we can use current version
        // First convert from when only the names where saved
        string = FieldRename.convertAttributeName(ApiVersion.CURRENT, string);
        string = string.toLowerCase(Locale.ROOT);
        NamespacedKey key = NamespacedKey.fromString(string);
        if (key == null) return null; // Paper - Fixup NamespacedKey handling

        // Now also convert from when keys where saved
        return CraftRegistry.get(RegistryKey.ATTRIBUTE, key, ApiVersion.CURRENT);
    }

    public static net.minecraft.world.entity.ai.attributes.Attribute bukkitToMinecraft(Attribute bukkit) {
        return CraftRegistry.bukkitToMinecraft(bukkit);
    }

    public static Holder<net.minecraft.world.entity.ai.attributes.Attribute> bukkitToMinecraftHolder(Attribute bukkit) {
        return CraftRegistry.bukkitToMinecraftHolder(bukkit);
    }

    public static String bukkitToString(Attribute bukkit) {
        Preconditions.checkArgument(bukkit != null);

        return bukkit.getKey().toString();
    }

    public CraftAttribute(final Holder<net.minecraft.world.entity.ai.attributes.Attribute> holder) {
        super(holder, count++);
    }

    @Override
    public Sentiment getSentiment() {
        return Sentiment.valueOf(this.getHandle().sentiment.name());
    }

    @Override
    public String getTranslationKey() {
        return this.getHandle().getDescriptionId();
    }

    @Override
    public String translationKey() {
        return this.getHandle().getDescriptionId();
    }
}
