package org.bukkit.craftbukkit.inventory;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.ImmutableMap;
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
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;
import org.bukkit.craftbukkit.util.CraftChatMessage;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.WritableBookMeta;

// Spigot start
import java.util.AbstractList;
import net.md_5.bungee.api.chat.BaseComponent;
// Spigot end

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaBook extends CraftMetaItem implements BookMeta, WritableBookMeta {
    @ItemMetaKey.Specific(ItemMetaKey.Specific.To.NBT)
    static final ItemMetaKeyType<WritableBookContent> BOOK_CONTENT = new ItemMetaKeyType<>(DataComponents.WRITABLE_BOOK_CONTENT);
    static final ItemMetaKey BOOK_PAGES = new ItemMetaKey("pages");
    static final int MAX_PAGES = WritableBookContent.MAX_PAGES; // SPIGOT-6911: Use Minecraft limits // Paper
    static final int MAX_PAGE_LENGTH = WritableBookContent.PAGE_EDIT_LENGTH; // SPIGOT-6911: Use Minecraft limits

    // We store the pages in their raw original text representation. See SPIGOT-5063, SPIGOT-5350, SPIGOT-3206
    // For writable books (CraftMetaBook) the pages are stored as plain Strings.
    protected List<String> pages; // null and empty are two different states internally

    CraftMetaBook(CraftMetaItem meta) {
        super(meta);

        if (meta instanceof CraftMetaBook) {
            CraftMetaBook bookMeta = (CraftMetaBook) meta;

            if (bookMeta.pages != null) {
                this.pages = new ArrayList<>(bookMeta.pages.size());

                this.pages.addAll(bookMeta.pages);
            }
        } else if (meta instanceof CraftMetaBookSigned) {
            CraftMetaBookSigned bookMeta = (CraftMetaBookSigned) meta;

            if (bookMeta.pages != null) {
                this.pages = new ArrayList<>(bookMeta.pages.size());

                // Convert from JSON to plain Strings:
                this.pages.addAll(Lists.transform(bookMeta.pages, CraftChatMessage::fromComponent));
            }
        }
    }

    CraftMetaBook(DataComponentPatch tag, java.util.Set<net.minecraft.core.component.DataComponentType<?>> extraHandledDcts) {
        super(tag, extraHandledDcts);

        getOrEmpty(tag, CraftMetaBook.BOOK_CONTENT).ifPresent((writable) -> {
            List<Filterable<String>> pages = writable.pages();
            this.pages = new ArrayList<>(pages.size());

            // Note: We explicitly check for and truncate oversized books and pages,
            // because they can come directly from clients when handling book edits.
            for (int i = 0; i < Math.min(pages.size(), CraftMetaBook.MAX_PAGES); i++) {
                String page = pages.get(i).raw();
                page = this.validatePage(page);

                this.pages.add(page);
            }
        });
    }

    CraftMetaBook(Map<String, Object> map) {
        super(map);

        Iterable<?> pages = SerializableMeta.getObject(Iterable.class, map, CraftMetaBook.BOOK_PAGES.BUKKIT, true);
        if (pages != null) {
            this.pages = new ArrayList<>();
            for (Object page : pages) {
                if (page instanceof String) {
                    this.internalAddPage(this.validatePage((String) page));
                }
            }
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (this.pages != null) {
            List<Filterable<String>> list = new ArrayList<>();
            for (String page : this.pages) {
                list.add(Filterable.from(FilteredText.passThrough(page)));
            }
            tag.put(CraftMetaBook.BOOK_CONTENT, new WritableBookContent(list));
        }
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && this.isBookEmpty();
    }

    boolean isBookEmpty() {
        return this.pages == null;
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.WRITABLE_BOOK;
    }

    @Override
    public boolean hasAuthor() {
        return false;
    }

    @Override
    public boolean hasTitle() {
        return false;
    }

    @Override
    public boolean hasPages() {
        return (this.pages != null) && !this.pages.isEmpty();
    }

    @Override
    public boolean hasGeneration() {
        return false;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public boolean setTitle(final String title) {
        return false;
    }

    @Override
    public String getAuthor() {
        return null;
    }

    @Override
    public void setAuthor(final String author) {
    }

    @Override
    public Generation getGeneration() {
        return null;
    }

    @Override
    public void setGeneration(Generation generation) {
    }

    @Override
    public net.kyori.adventure.text.Component title() {
        return null;
    }

    @Override
    public org.bukkit.inventory.meta.BookMeta title(net.kyori.adventure.text.Component title) {
        return this;
    }

    @Override
    public net.kyori.adventure.text.Component author() {
        return null;
    }

    @Override
    public org.bukkit.inventory.meta.BookMeta author(net.kyori.adventure.text.Component author) {
        return this;
    }

    @Override
    public net.kyori.adventure.text.Component page(final int page) {
        Preconditions.checkArgument(this.isValidPage(page), "Invalid page number (%s/%s)", page, this.getPageCount());
        return net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().deserialize(this.pages.get(page - 1));
    }

    @Override
    public void page(final int page, net.kyori.adventure.text.Component data) {
        Preconditions.checkArgument(this.isValidPage(page), "Invalid page number (%s/%s)", page, this.getPageCount());
        if (data == null) {
            data = net.kyori.adventure.text.Component.empty();
        }
        this.pages.set(page - 1, net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(data));
    }

    @Override
    public List<net.kyori.adventure.text.Component> pages() {
        if (this.pages == null) return ImmutableList.of();
        return this.pages.stream().map(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection()::deserialize).collect(ImmutableList.toImmutableList());
    }

    @Override
    public BookMeta pages(List<net.kyori.adventure.text.Component> pages) {
        if (this.pages != null) this.pages.clear();
        for (net.kyori.adventure.text.Component page : pages) {
            this.addPages(page);
        }
        return this;
    }

    @Override
    public BookMeta pages(net.kyori.adventure.text.Component... pages) {
        if (this.pages != null) this.pages.clear();
        this.addPages(pages);
        return this;
    }

    @Override
    public void addPages(net.kyori.adventure.text.Component... pages) {
        if (this.pages == null) this.pages = new ArrayList<>();
        for (net.kyori.adventure.text.Component page : pages) {
            if (this.pages.size() >= MAX_PAGES) {
                return;
            }

            if (page == null) {
                page = net.kyori.adventure.text.Component.empty();
            }

            this.pages.add(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection().serialize(page));
        }
    }

    private CraftMetaBook(List<net.kyori.adventure.text.Component> pages) {
        super((org.bukkit.craftbukkit.inventory.CraftMetaItem) org.bukkit.Bukkit.getItemFactory().getItemMeta(org.bukkit.Material.WRITABLE_BOOK));
        this.pages = pages.subList(0, Math.min(MAX_PAGES, pages.size())).stream().map(net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer.legacySection()::serialize).collect(java.util.stream.Collectors.toList());
    }

    static class CraftMetaBookBuilder implements BookMetaBuilder {
        protected final List<net.kyori.adventure.text.Component> pages = new java.util.ArrayList<>();

        @Override
        public BookMetaBuilder title(net.kyori.adventure.text.Component title) {
            return this;
        }

        @Override
        public BookMetaBuilder author(net.kyori.adventure.text.Component author) {
            return this;
        }

        @Override
        public BookMetaBuilder addPage(net.kyori.adventure.text.Component page) {
            this.pages.add(page);
            return this;
        }

        @Override
        public BookMetaBuilder pages(net.kyori.adventure.text.Component... pages) {
            java.util.Collections.addAll(this.pages, pages);
            return this;
        }

        @Override
        public BookMetaBuilder pages(java.util.Collection<net.kyori.adventure.text.Component> pages) {
            this.pages.addAll(pages);
            return this;
        }

        @Override
        public BookMeta build() {
            return new CraftMetaBook(this.pages);
        }
    }

    @Override
    public BookMetaBuilder toBuilder() {
        return new CraftMetaBookBuilder();
    }

    @Override
    public String getPage(final int page) {
        Preconditions.checkArgument(this.isValidPage(page), "Invalid page number (%s)", page);
        // assert: pages != null
        return this.pages.get(page - 1);
    }

    @Override
    public void setPage(final int page, final String text) {
        Preconditions.checkArgument(this.isValidPage(page), "Invalid page number (%s/%s)", page, this.getPageCount());
        // assert: pages != null

        String newText = this.validatePage(text);
        this.pages.set(page - 1, newText);
    }

    @Override
    public void setPages(final String... pages) {
        this.setPages(Arrays.asList(pages));
    }

    @Override
    public void addPage(final String... pages) {
        for (String page : pages) {
            page = this.validatePage(page);
            this.internalAddPage(page);
        }
    }

    String validatePage(String page) {
        if (page == null) {
            page = "";
        } else if (page.length() > CraftMetaBook.MAX_PAGE_LENGTH) {
            page = page.substring(0, CraftMetaBook.MAX_PAGE_LENGTH);
        }
        return page;
    }

    private void internalAddPage(String page) {
        // asserted: page != null
        if (this.pages == null) {
            this.pages = new ArrayList<>();
        } else if (this.pages.size() >= CraftMetaBook.MAX_PAGES) {
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
        return this.pages.stream().collect(ImmutableList.toImmutableList());
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

    @Override
    public CraftMetaBook clone() {
        CraftMetaBook meta = (CraftMetaBook) super.clone();
        if (this.pages != null) {
            meta.pages = new ArrayList<>(this.pages);
        }
        meta.spigot = meta.new SpigotMeta(); // Spigot
        return meta;
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();
        if (this.pages != null) {
            hash = 61 * hash + 17 * this.pages.hashCode();
        }
        return original != hash ? CraftMetaBook.class.hashCode() ^ hash : hash;
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaBook other) {
            return Objects.equals(this.pages, other.pages);
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaBook || this.isBookEmpty());
    }

    @Override
    ImmutableMap.Builder<String, Object> serialize(ImmutableMap.Builder<String, Object> builder) {
        super.serialize(builder);

        if (this.pages != null) {
            builder.put(CraftMetaBook.BOOK_PAGES.BUKKIT, ImmutableList.copyOf(this.pages));
        }

        return builder;
    }

    // Spigot start
    private BookMeta.Spigot spigot = new SpigotMeta();
    private class SpigotMeta extends BookMeta.Spigot {

        private String pageToJSON(String page) {
            // Convert from plain String to JSON (similar to conversion between writable books and written books):
            Component component = CraftChatMessage.fromString(page, true, true)[0];
            return CraftChatMessage.toJSON(component);
        }

        private String componentsToPage(BaseComponent[] components) {
            // Convert component to plain String:
            Component component = CraftChatMessage.bungeeToVanilla(components);
            return CraftChatMessage.fromComponent(component);
        }

        @Override
        public BaseComponent[] getPage(final int page) {
            Preconditions.checkArgument(CraftMetaBook.this.isValidPage(page), "Invalid page number");
            return CraftChatMessage.jsonToBungee(this.pageToJSON(CraftMetaBook.this.pages.get(page - 1)));
        }

        @Override
        public void setPage(final int page, final BaseComponent... text) {
            if (!CraftMetaBook.this.isValidPage(page)) {
                throw new IllegalArgumentException("Invalid page number " + page + "/" + CraftMetaBook.this.getPageCount());
            }

            BaseComponent[] newText = text == null ? new BaseComponent[0] : text;
            CraftMetaBook.this.pages.set(page - 1, this.componentsToPage(newText));
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

                CraftMetaBook.this.internalAddPage(this.componentsToPage(page));
            }
        }

        @Override
        public List<BaseComponent[]> getPages() {
            if (CraftMetaBook.this.pages == null) return ImmutableList.of();
            final List<String> copy = ImmutableList.copyOf(CraftMetaBook.this.pages);
            return new AbstractList<>() {

                @Override
                public BaseComponent[] get(int index) {
                    return CraftChatMessage.jsonToBungee(SpigotMeta.this.pageToJSON(copy.get(index)));
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
                CraftMetaBook.this.pages = null;
                return;
            }

            if (CraftMetaBook.this.pages != null) {
                CraftMetaBook.this.pages.clear();
            }

            for (BaseComponent[] page : pages) {
                this.addPage(page);
            }
        }
    }

    @Override
    public BookMeta.Spigot spigot() {
        return this.spigot;
    }
    // Spigot end
}
