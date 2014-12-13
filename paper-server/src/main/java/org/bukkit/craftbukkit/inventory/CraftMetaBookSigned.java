package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.network.Filterable;
import net.minecraft.server.network.FilteredText;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.inventory.meta.BookMeta;

// Spigot start
import java.util.AbstractList;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.chat.ComponentSerializer;
// Spigot end

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaBookSigned extends CraftMetaItem implements BookMeta {
    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKeyType<WrittenBookContent> BOOK_CONTENT = new ItemMetaKeyType<>(DataComponents.WRITTEN_BOOK_CONTENT);
    static final ItemMetaKey BOOK_TITLE = new ItemMetaKey("title");
    static final ItemMetaKey BOOK_AUTHOR = new ItemMetaKey("author");
    static final ItemMetaKey BOOK_PAGES = new ItemMetaKey("pages");
    static final ItemMetaKey RESOLVED = new ItemMetaKey("resolved");
    static final ItemMetaKey GENERATION = new ItemMetaKey("generation");
    static final int MAX_PAGES = Integer.MAX_VALUE; // SPIGOT-6911: Use Minecraft limits
    static final int MAX_PAGE_LENGTH = WritableBookContent.PAGE_EDIT_LENGTH; // SPIGOT-6911: Use Minecraft limits
    static final int MAX_TITLE_LENGTH = WrittenBookContent.TITLE_MAX_LENGTH; // SPIGOT-6911: Use Minecraft limits

    protected String title;
    protected String author;
    // Stored as components and serialized as JSON. See SPIGOT-5063, SPIGOT-5350, SPIGOT-3206
    protected List<Component> pages; // null and empty are two different states internally
    protected boolean resolved;
    protected int generation;

    CraftMetaBookSigned(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaBookSigned) {
            CraftMetaBookSigned bookMeta = (CraftMetaBookSigned) meta;
            this.title = bookMeta.title;
            this.author = bookMeta.author;
            this.resolved = bookMeta.resolved;
            this.generation = bookMeta.generation;

            if (bookMeta.pages != null) {
                this.pages = new ArrayList<Component>(bookMeta.pages.size());
                this.pages.addAll(bookMeta.pages);
            }
        } else if (meta instanceof CraftMetaBook) {
            CraftMetaBook bookMeta = (CraftMetaBook) meta;

            if (bookMeta.pages != null) {
                this.pages = new ArrayList<Component>(bookMeta.pages.size());

                // Convert from plain Strings to JSON:
                // This happens for example during book signing.
                for (String page : bookMeta.pages) {
                    // We don't insert any non-plain text features (such as clickable links) during this conversion.
                    Component component = CraftChatMessage.fromString(page, true, true)[0];
                    this.pages.add(component);
                }
            }
        }
    }

    CraftMetaBookSigned(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, CraftMetaBookSigned.BOOK_CONTENT).ifPresent((written) -> {
            this.title = written.title().raw();
            this.author = written.author();
            this.resolved = written.resolved();
            this.generation = written.generation();

            List<Filterable<Component>> pages = written.pages();
            this.pages = new ArrayList<Component>(pages.size());
            // Note: We explicitly check for and truncate oversized books and pages,
            // because they can come directly from clients when handling book edits.
            for (int i = 0; i < Math.min(pages.size(), CraftMetaBookSigned.MAX_PAGES); i++) {
                Component page = pages.get(i).raw();

                this.pages.add(page);
            }
        });
    }

    CraftMetaBookSigned(Map<String, Object> map) {
        super(map);

        this.setAuthor(SerializableMeta.getString(map, CraftMetaBookSigned.BOOK_AUTHOR.BUKKIT, true));

        this.setTitle(SerializableMeta.getString(map, CraftMetaBookSigned.BOOK_TITLE.BUKKIT, true));

        Iterable<?> pages = SerializableMeta.getObject(Iterable.class, map, CraftMetaBookSigned.BOOK_PAGES.BUKKIT, true);
        if (pages != null) {
            this.pages = new ArrayList<Component>();
            for (Object page : pages) {
                if (page instanceof String) {
                    this.internalAddPage(CraftChatMessage.fromJSONOrString((String) page, false, true, CraftMetaBookSigned.MAX_PAGE_LENGTH, false));
                }
            }
        }

        this.resolved = SerializableMeta.getBoolean(map, CraftMetaBookSigned.RESOLVED.BUKKIT);
        this.generation = SerializableMeta.getInteger(map, CraftMetaBookSigned.GENERATION.BUKKIT);
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator itemData) {
        super.applyToItem(itemData);

        if (this.pages != null) {
            List<Filterable<Component>> list = new ArrayList<>();
            for (Component page : this.pages) {
                list.add(Filterable.passThrough(page));
            }
            itemData.put(CraftMetaBookSigned.BOOK_CONTENT, new WrittenBookContent(Filterable.from(FilteredText.passThrough(this.title)), this.author, this.generation, list, this.resolved));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isBookEmpty();
    }

    boolean isBookEmpty() {
        return !((this.pages != null) || this.hasAuthor() || this.hasTitle() || this.hasGeneration() || this.resolved);
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.WRITTEN_BOOK;
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
        return (this.pages != null) && !this.pages.isEmpty();
    }

    @Override
    public boolean hasGeneration() {
        return this.generation != 0;
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
        } else if (title.length() > CraftMetaBookSigned.MAX_TITLE_LENGTH) {
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
        return Generation.values()[this.generation];
    }

    @Override
    public void setGeneration(Generation generation) {
        this.generation = (generation == null) ? 0 : generation.ordinal();
    }

    @Override
    public String getPage(final int page) {
        Preconditions.checkArgument(this.isValidPage(page), "Invalid page number (%s)", page);
        // assert: pages != null
        return CraftChatMessage.fromComponent(this.pages.get(page - 1));
    }

    @Override
    public void setPage(final int page, final String text) {
        Preconditions.checkArgument(this.isValidPage(page), "Invalid page number (%s/%s)", page, this.getPageCount());
        // assert: pages != null

        String newText = this.validatePage(text);
        this.pages.set(page - 1, CraftChatMessage.fromStringOrEmpty(newText, true));
    }

    @Override
    public void setPages(final String... pages) {
        this.setPages(Arrays.asList(pages));
    }

    @Override
    public void addPage(final String... pages) {
        for (String page : pages) {
            page = this.validatePage(page);
            this.internalAddPage(CraftChatMessage.fromStringOrEmpty(page, true));
        }
    }

    String validatePage(String page) {
        if (page == null) {
            page = "";
        } else if (page.length() > CraftMetaBookSigned.MAX_PAGE_LENGTH) {
            page = page.substring(0, CraftMetaBookSigned.MAX_PAGE_LENGTH);
        }
        return page;
    }

    private void internalAddPage(Component page) {
        // asserted: page != null
        if (this.pages == null) {
            this.pages = new ArrayList<Component>();
        } else if (this.pages.size() >= CraftMetaBookSigned.MAX_PAGES) {
            return;
        }
        this.pages.add(page);
    }

    @Override
    public int getPageCount() {
        return (this.pages == null) ? 0 : this.pages.size();
    }

    @Override
    public List<String> getPages() {
        if (this.pages == null) return ImmutableList.of();
        return this.pages.stream().map(CraftChatMessage::fromComponent).collect(ImmutableList.toImmutableList());
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
            this.addPage(page);
        }
    }

    private boolean isValidPage(int page) {
        return page > 0 && page <= this.getPageCount();
    }

    // TODO Expose this attribute in Bukkit?
    public boolean isResolved() {
        return this.resolved;
    }

    public void setResolved(boolean resolved) {
        this.resolved = resolved;
    }

    @Override
    public CraftMetaBookSigned clone() {
        CraftMetaBookSigned meta = (CraftMetaBookSigned) super.clone();
        if (this.pages != null) {
            meta.pages = new ArrayList<Component>(this.pages);
        }
        meta.spigot = meta.new SpigotMeta(); // Spigot
        return meta;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.hasTitle()) {
            hash = 61 * hash + this.title.hashCode();
        }
        if (this.hasAuthor()) {
            hash = 61 * hash + 13 * this.author.hashCode();
        }
        if (this.pages != null) {
            hash = 61 * hash + 17 * this.pages.hashCode();
        }
        if (this.resolved) {
            hash = 61 * hash + 17 * Boolean.hashCode(this.resolved);
        }
        if (this.hasGeneration()) {
            hash = 61 * hash + 19 * Integer.hashCode(this.generation);
        }
        return original != hash ? CraftMetaBook.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBookSigned that) {

            return (this.hasTitle() ? that.hasTitle() && this.title.equals(that.title) : !that.hasTitle())
                    && (this.hasAuthor() ? that.hasAuthor() && this.author.equals(that.author) : !that.hasAuthor())
                    && (Objects.equals(this.pages, that.pages))
                    && (Objects.equals(this.resolved, that.resolved))
                    && (Objects.equals(this.generation, that.generation));
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBookSigned || this.isBookEmpty());
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.hasTitle()) {
            builder.put(CraftMetaBookSigned.BOOK_TITLE.BUKKIT, this.title);
        }

        if (this.hasAuthor()) {
            builder.put(CraftMetaBookSigned.BOOK_AUTHOR.BUKKIT, this.author);
        }

        if (this.pages != null) {
            builder.put(CraftMetaBookSigned.BOOK_PAGES.BUKKIT, ImmutableList.copyOf(Lists.transform(this.pages, CraftChatMessage::toJSON)));
        }

        if (this.resolved) {
            builder.put(CraftMetaBookSigned.RESOLVED.BUKKIT, this.resolved);
        }

        if (this.generation != 0) {
            builder.put(CraftMetaBookSigned.GENERATION.BUKKIT, this.generation);
        }

        return builder;
    }

    // Spigot start
    private BookMeta.Spigot spigot = new SpigotMeta();
    private class SpigotMeta extends BookMeta.Spigot {

        private String pageToJSON(Component page) {
            // Page data is already in JSON format:
            return CraftChatMessage.toJSON(page);
        }

        private Component componentsToPage(BaseComponent[] components) {
            // asserted: components != null
            // Pages are in JSON format:
            return CraftChatMessage.fromJSON(ComponentSerializer.toString(components));
        }

        @Override
        public BaseComponent[] getPage(final int page) {
            Preconditions.checkArgument(CraftMetaBookSigned.this.isValidPage(page), "Invalid page number");
            return ComponentSerializer.parse(this.pageToJSON(CraftMetaBookSigned.this.pages.get(page - 1)));
        }

        @Override
        public void setPage(final int page, final BaseComponent... text) {
            if (!CraftMetaBookSigned.this.isValidPage(page)) {
                throw new IllegalArgumentException("Invalid page number " + page + "/" + CraftMetaBookSigned.this.getPageCount());
            }

            BaseComponent[] newText = text == null ? new BaseComponent[0] : text;
            CraftMetaBookSigned.this.pages.set(page - 1, this.componentsToPage(newText));
        }

        @Override
        public void setPages(final BaseComponent[]... pages) {
            this.setPages(Arrays.asList(pages));
        }

        @Override
        public void addPage(final BaseComponent[]... pages) {
            for (BaseComponent[] page : pages) {
                if (page == null) {
                    page = new BaseComponent[0];
                }

                CraftMetaBookSigned.this.internalAddPage(this.componentsToPage(page));
            }
        }

        @Override
        public List<BaseComponent[]> getPages() {
            if (CraftMetaBookSigned.this.pages == null) return ImmutableList.of();
            final List<Component> copy = ImmutableList.copyOf(CraftMetaBookSigned.this.pages);
            return new AbstractList<BaseComponent[]>() {

                @Override
                public BaseComponent[] get(int index) {
                    return ComponentSerializer.parse(SpigotMeta.this.pageToJSON(copy.get(index)));
                }

                @Override
                public int size() {
                    return copy.size();
                }
            };
        }

        @Override
        public void setPages(List<BaseComponent[]> pages) {
            if (pages.isEmpty()) {
                CraftMetaBookSigned.this.pages = null;
                return;
            }

            if (CraftMetaBookSigned.this.pages != null) {
                CraftMetaBookSigned.this.pages.clear();
            }

            for (BaseComponent[] page : pages) {
                this.addPage(page);
            }
        }
    };

    @Override
    public BookMeta.Spigot spigot() {
        return this.spigot;
    }
    // Spigot end
}
