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
import net.minecraft.server.ChatSerializer;
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
            String[] pageArray = new String[pages.size()];

            for (int i = 0; i < pages.size(); i++) {
                String page = pages.getString(i);
                if (resolved) {
                    try {
                        page = CraftChatMessage.fromComponent(ChatSerializer.a(page));
                    } catch (Exception e) {
                        // Ignore and treat as an old book
                    }
                }
                pageArray[i] = page;
            }

            addPage(pageArray);
        }
    }

    CraftMetaBookSigned(Map<String, Object> map) {
        super(map);

        setAuthor(SerializableMeta.getString(map, BOOK_AUTHOR.BUKKIT, true));

        setTitle(SerializableMeta.getString(map, BOOK_TITLE.BUKKIT, true));

        Iterable<?> pages = SerializableMeta.getObject(Iterable.class, map, BOOK_PAGES.BUKKIT, true);
        CraftMetaItem.safelyAdd(pages, this.pages, MAX_PAGE_LENGTH);
        
        generation = SerializableMeta.getObject(Integer.class, map, GENERATION.BUKKIT, true);
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
            for (String page : pages) {                   
                list.add(new NBTTagString(
                    ChatSerializer.a(CraftChatMessage.fromString(page, true)[0])
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
