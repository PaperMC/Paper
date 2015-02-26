package org.bukkit.craftbukkit.inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.minecraft.server.NBTTagCompound;
import net.minecraft.server.NBTTagList;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.inventory.meta.BookMeta;

import com.google.common.collect.ImmutableMap.Builder;
import net.minecraft.server.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.IChatBaseComponent;
import net.minecraft.server.NBTTagString;
import org.bukkit.craftbukkit.util.CraftChatMessage;

@DelegateDeserialization(SerializableMeta.class)
class CraftMetaBookSigned extends CraftMetaBook implements BookMeta {

    CraftMetaBookSigned(CraftMetaItem meta) {
        super(meta);
    }

    CraftMetaBookSigned(NBTTagCompound tag) {
        super(tag, false);

        boolean resolved = true;
        if (tag.hasKey(RESOLVED.NBT)) {
            resolved = tag.getBoolean(RESOLVED.NBT);
        }

        if (tag.hasKey(BOOK_PAGES.NBT)) {
            NBTTagList pages = tag.getList(BOOK_PAGES.NBT, 8);

            for (int i = 0; i < pages.size(); i++) {
                String page = pages.getString(i);
                if (resolved) {
                    try {
                        this.pages.add(ChatSerializer.a(page));
                        continue;
                    } catch (Exception e) {
                        // Ignore and treat as an old book
                    }
                }
                addPage(page);
            }
        }
    }

    CraftMetaBookSigned(Map<String, Object> map) {
        super(map);
    }

    @Override
    void applyToItem(NBTTagCompound itemData) {
        super.applyToItem(itemData, false);

        if (hasTitle()) {
            itemData.setString(BOOK_TITLE.NBT, this.title);
        } else {
            itemData.setString(BOOK_TITLE.NBT, " ");
        }

        if (hasAuthor()) {
            itemData.setString(BOOK_AUTHOR.NBT, this.author);
        } else {
            itemData.setString(BOOK_AUTHOR.NBT, " ");
        }

        if (hasPages()) {
            NBTTagList list = new NBTTagList();
            for (IChatBaseComponent page : pages) {
                list.add(new NBTTagString(
                    ChatSerializer.a(page)
                ));
            }
            itemData.set(BOOK_PAGES.NBT, list);
        }        
        itemData.setBoolean(RESOLVED.NBT, true);

        if (generation != null) {
            itemData.setInt(GENERATION.NBT, generation);
        } else {
            itemData.setInt(GENERATION.NBT, 0);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty();
    }

    @Override
    boolean applicableTo(Material type) {
        switch (type) {
        case WRITTEN_BOOK:
        case BOOK_AND_QUILL:
            return true;
        default:
            return false;
        }
    }

    @Override
    public CraftMetaBookSigned clone() {
        CraftMetaBookSigned meta = (CraftMetaBookSigned) super.clone();
        return meta;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        return original != hash ? CraftMetaBookSigned.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        return super.equalsCommon(meta);
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBookSigned || isBookEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);
        return builder;
    }
}
