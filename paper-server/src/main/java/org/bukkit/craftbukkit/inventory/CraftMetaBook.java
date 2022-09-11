package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.chat.IChatBaseComponent;
import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.inventory.CraftMetaItem.SerializableMeta;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.craftbukkit.util.CraftMagicNumbers;
import org.bukkit.inventory.meta.BookMeta;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaBook extends CraftMetaItem implements BookMeta {
    static final ItemMetaKey BOOK_TITLE = new ItemMetaKey("title");
    static final ItemMetaKey BOOK_AUTHOR = new ItemMetaKey("author");
    static final ItemMetaKey BOOK_PAGES = new ItemMetaKey("pages");
    static final ItemMetaKey RESOLVED = new ItemMetaKey("resolved");
    static final ItemMetaKey GENERATION = new ItemMetaKey("generation");
    static final int MAX_PAGES = 100;
    static final int MAX_PAGE_LENGTH = 320; // 256 limit + 64 characters to allow for psuedo colour codes
    static final int MAX_TITLE_LENGTH = 32;

    protected String title;
    protected String author;
    // We store the pages in their raw original text representation. See SPIGOT-5063, SPIGOT-5350, SPIGOT-3206
    // For writable books (CraftMetaBook) the pages are stored as plain Strings.
    // For written books (CraftMetaBookSigned) the pages are stored in Minecraft's JSON format.
    protected List<String> pages; // null and empty are two different states internally
    protected Boolean resolved = null;
    protected Integer generation;

    CraftMetaBook(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaBook) {
            CraftMetaBook bookMeta = (CraftMetaBook) meta;
            this.title = bookMeta.title;
            this.author = bookMeta.author;
            this.resolved = bookMeta.resolved;
            this.generation = bookMeta.generation;

            if (bookMeta.pages != null) {
                this.pages = new ArrayList<String>(bookMeta.pages.size());
                if (meta instanceof CraftMetaBookSigned) {
                    if (this instanceof CraftMetaBookSigned) {
                        pages.addAll(bookMeta.pages);
                    } else {
                        // Convert from JSON to plain Strings:
                        pages.addAll(Lists.transform(bookMeta.pages, CraftChatMessage::fromJSONComponent));
                    }
                } else {
                    if (this instanceof CraftMetaBookSigned) {
                        // Convert from plain Strings to JSON:
                        // This happens for example during book signing.
                        for (String page : bookMeta.pages) {
                            // We don't insert any non-plain text features (such as clickable links) during this conversion.
                            IChatBaseComponent component = CraftChatMessage.fromString(page, true, true)[0];
                            pages.add(CraftChatMessage.toJSON(component));
                        }
                    } else {
                        pages.addAll(bookMeta.pages);
                    }
                }
            }
        }
    }

    CraftMetaBook(NBTTagCompound tag) {
        super(tag);

        if (tag.contains(BOOK_TITLE.NBT)) {
            this.title = tag.getString(BOOK_TITLE.NBT);
        }

        if (tag.contains(BOOK_AUTHOR.NBT)) {
            this.author = tag.getString(BOOK_AUTHOR.NBT);
        }

        if (tag.contains(RESOLVED.NBT)) {
            this.resolved = tag.getBoolean(RESOLVED.NBT);
        }

        if (tag.contains(GENERATION.NBT)) {
            generation = tag.getInt(GENERATION.NBT);
        }

        if (tag.contains(BOOK_PAGES.NBT)) {
            NBTTagList pages = tag.getList(BOOK_PAGES.NBT, CraftMagicNumbers.NBT.TAG_STRING);
            this.pages = new ArrayList<String>(pages.size());

            boolean expectJson = (this instanceof CraftMetaBookSigned);
            // Note: We explicitly check for and truncate oversized books and pages,
            // because they can come directly from clients when handling book edits.
            for (int i = 0; i < Math.min(pages.size(), MAX_PAGES); i++) {
                String page = pages.getString(i);
                // There was an issue on previous Spigot versions which would
                // result in book items with pages in the wrong text
                // representation. See SPIGOT-182, SPIGOT-164
                if (expectJson) {
                    page = CraftChatMessage.fromJSONOrStringToJSON(page, false, true, MAX_PAGE_LENGTH, false);
                } else {
                    page = validatePage(page);
                }
                this.pages.add(page);
            }
        }
    }

    CraftMetaBook(Map<String, Object> map) {
        super(map);

        setAuthor(SerializableMeta.getString(map, BOOK_AUTHOR.BUKKIT, true));

        setTitle(SerializableMeta.getString(map, BOOK_TITLE.BUKKIT, true));

        Iterable<?> pages = SerializableMeta.getObject(Iterable.class, map, BOOK_PAGES.BUKKIT, true);
        if (pages != null) {
            this.pages = new ArrayList<String>();
            for (Object page : pages) {
                if (page instanceof String) {
                    internalAddPage(deserializePage((String) page));
                }
            }
        }

        resolved = SerializableMeta.getObject(Boolean.class, map, RESOLVED.BUKKIT, true);
        generation = SerializableMeta.getObject(Integer.class, map, GENERATION.BUKKIT, true);
    }

    protected String deserializePage(String pageData) {
        // We expect the page data to already be a plain String.
        return validatePage(pageData);
    }

    protected String convertPlainPageToData(String page) {
        // Writable books store their data as plain Strings, so we don't need to convert anything.
        return page;
    }

    protected String convertDataToPlainPage(String pageData) {
        // pageData is expected to already be a plain String.
        return pageData;
    }

    @Override
    void applyToItem(NBTTagCompound itemData) {
        super.applyToItem(itemData);

        if (hasTitle()) {
            itemData.putString(BOOK_TITLE.NBT, this.title);
        }

        if (hasAuthor()) {
            itemData.putString(BOOK_AUTHOR.NBT, this.author);
        }

        if (pages != null) {
            NBTTagList list = new NBTTagList();
            for (String page : pages) {
                list.add(NBTTagString.valueOf(page));
            }
            itemData.put(BOOK_PAGES.NBT, list);
        }

        if (resolved != null) {
            itemData.putBoolean(RESOLVED.NBT, resolved);
        }

        if (generation != null) {
            itemData.putInt(GENERATION.NBT, generation);
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isBookEmpty();
    }

    boolean isBookEmpty() {
        return !((pages != null) || hasAuthor() || hasTitle() || hasGeneration() || (resolved != null));
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.WRITTEN_BOOK || type == Material.WRITABLE_BOOK;
    }

    @Override
    public boolean hasAuthor() {
        return this.author != null;
    }

    @Override
    public boolean hasTitle() {
        return this.title != null;
    }

    @Override
    public boolean hasPages() {
        return (pages != null) && !pages.isEmpty();
    }

    @Override
    public boolean hasGeneration() {
        return generation != null;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public boolean setTitle(final String title) {
        if (title == null) {
            this.title = null;
            return true;
        } else if (title.length() > MAX_TITLE_LENGTH) {
            return false;
        }

        this.title = title;
        return true;
    }

    @Override
    public String getAuthor() {
        return this.author;
    }

    @Override
    public void setAuthor(final String author) {
        this.author = author;
    }

    @Override
    public Generation getGeneration() {
        return (generation == null) ? null : Generation.values()[generation];
    }

    @Override
    public void setGeneration(Generation generation) {
        this.generation = (generation == null) ? null : generation.ordinal();
    }

    @Override
    public String getPage(final int page) {
        Validate.isTrue(isValidPage(page), "Invalid page number");
        // assert: pages != null
        return convertDataToPlainPage(pages.get(page - 1));
    }

    @Override
    public void setPage(final int page, final String text) {
        if (!isValidPage(page)) {
            throw new IllegalArgumentException("Invalid page number " + page + "/" + getPageCount());
        }
        // assert: pages != null

        String newText = validatePage(text);
        pages.set(page - 1, convertPlainPageToData(newText));
    }

    @Override
    public void setPages(final String... pages) {
        setPages(Arrays.asList(pages));
    }

    @Override
    public void addPage(final String... pages) {
        for (String page : pages) {
            page = validatePage(page);
            internalAddPage(convertPlainPageToData(page));
        }
    }

    String validatePage(String page) {
        if (page == null) {
            page = "";
        } else if (page.length() > MAX_PAGE_LENGTH) {
            page = page.substring(0, MAX_PAGE_LENGTH);
        }
        return page;
    }

    private void internalAddPage(String page) {
        // asserted: page != null
        if (this.pages == null) {
            this.pages = new ArrayList<String>();
        } else if (this.pages.size() >= MAX_PAGES) {
            return;
        }
        this.pages.add(page);
    }

    @Override
    public int getPageCount() {
        return (pages == null) ? 0 : pages.size();
    }

    @Override
    public List<String> getPages() {
        if (pages == null) return ImmutableList.of();
        return pages.stream().map(this::convertDataToPlainPage).collect(ImmutableList.toImmutableList());
    }

    @Override
    public void setPages(List<String> pages) {
        if (pages.isEmpty()) {
            this.pages = null;
            return;
        }

        if (this.pages != null) {
            this.pages.clear();
        }
        for (String page : pages) {
            addPage(page);
        }
    }

    private boolean isValidPage(int page) {
        return page > 0 && page <= getPageCount();
    }

    // TODO Expose this attribute in Bukkit?
    public boolean isResolved() {
        return (resolved == null) ? false : resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    @Override
    public CraftMetaBook clone() {
        CraftMetaBook meta = (CraftMetaBook) super.clone();
        if (this.pages != null) {
            meta.pages = new ArrayList<String>(this.pages);
        }
        return meta;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (hasTitle()) {
            hash = 61 * hash + this.title.hashCode();
        }
        if (hasAuthor()) {
            hash = 61 * hash + 13 * this.author.hashCode();
        }
        if (this.pages != null) {
            hash = 61 * hash + 17 * this.pages.hashCode();
        }
        if (this.resolved != null) {
            hash = 61 * hash + 17 * this.resolved.hashCode();
        }
        if (hasGeneration()) {
            hash = 61 * hash + 19 * this.generation.hashCode();
        }
        return original != hash ? CraftMetaBook.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBook) {
            CraftMetaBook that = (CraftMetaBook) meta;

            return (hasTitle() ? that.hasTitle() && this.title.equals(that.title) : !that.hasTitle())
                    && (hasAuthor() ? that.hasAuthor() && this.author.equals(that.author) : !that.hasAuthor())
                    && (Objects.equals(this.pages, that.pages))
                    && (Objects.equals(this.resolved, that.resolved))
                    && (hasGeneration() ? that.hasGeneration() && this.generation.equals(that.generation) : !that.hasGeneration());
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBook || isBookEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (hasTitle()) {
            builder.put(BOOK_TITLE.BUKKIT, title);
        }

        if (hasAuthor()) {
            builder.put(BOOK_AUTHOR.BUKKIT, author);
        }

        if (pages != null) {
            builder.put(BOOK_PAGES.BUKKIT, ImmutableList.copyOf(pages));
        }

        if (resolved != null) {
            builder.put(RESOLVED.BUKKIT, resolved);
        }

        if (generation != null) {
            builder.put(GENERATION.BUKKIT, generation);
        }

        return builder;
    }
}
