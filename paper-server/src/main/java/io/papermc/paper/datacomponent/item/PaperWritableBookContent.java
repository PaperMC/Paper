package io.papermc.paper.datacomponent.item;

import com.google.common.base.Preconditions;
import io.papermc.paper.text.Filtered;
import io.papermc.paper.util.MCUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.server.network.Filterable;
import org.bukkit.craftbukkit.util.Handleable;
import org.jetbrains.annotations.Unmodifiable;

public record PaperWritableBookContent(
    net.minecraft.world.item.component.WritableBookContent impl
) implements WritableBookContent, Handleable<net.minecraft.world.item.component.WritableBookContent> {

    @Override
    public net.minecraft.world.item.component.WritableBookContent getHandle() {
        return this.impl;
    }

    @Override
    public @Unmodifiable List<Filtered<String>> pages() {
        return MCUtil.transformUnmodifiable(this.impl.pages(), input -> Filtered.of(input.raw(), input.filtered().orElse(null)));
    }

    static final class BuilderImpl implements WritableBookContent.Builder {

        private final List<Filterable<String>> pages = new ObjectArrayList<>();

        private static void validatePageLength(final String page) {
            Preconditions.checkArgument(
                page.length() <= net.minecraft.world.item.component.WritableBookContent.PAGE_EDIT_LENGTH,
                "Cannot have page length more than %s, had %s",
                net.minecraft.world.item.component.WritableBookContent.PAGE_EDIT_LENGTH,
                page.length()
            );
        }

        private static void validatePageCount(final int current, final int add) {
            final int newSize = current + add;
            Preconditions.checkArgument(
                newSize <= net.minecraft.world.item.component.WritableBookContent.MAX_PAGES,
                "Cannot have more than %s pages, had %s",
                net.minecraft.world.item.component.WritableBookContent.MAX_PAGES,
                newSize
            );
        }

        @Override
        public WritableBookContent.Builder addPage(final String page) {
            validatePageLength(page);
            validatePageCount(this.pages.size(), 1);
            this.pages.add(Filterable.passThrough(page));
            return this;
        }

        @Override
        public WritableBookContent.Builder addPages(final List<String> pages) {
            validatePageCount(this.pages.size(), pages.size());
            for (final String page : pages) {
                validatePageLength(page);
                this.pages.add(Filterable.passThrough(page));
            }
            return this;
        }

        @Override
        public WritableBookContent.Builder addFilteredPage(final Filtered<String> page) {
            validatePageLength(page.raw());
            if (page.filtered() != null) {
                validatePageLength(page.filtered());
            }
            validatePageCount(this.pages.size(), 1);
            this.pages.add(new Filterable<>(page.raw(), Optional.ofNullable(page.filtered())));
            return this;
        }

        @Override
        public WritableBookContent.Builder addFilteredPages(final List<Filtered<String>> pages) {
            validatePageCount(this.pages.size(), pages.size());
            for (final Filtered<String> page : pages) {
                validatePageLength(page.raw());
                if (page.filtered() != null) {
                    validatePageLength(page.filtered());
                }
                this.pages.add(new Filterable<>(page.raw(), Optional.ofNullable(page.filtered())));
            }
            return this;
        }

        @Override
        public WritableBookContent build() {
            if (this.pages.isEmpty()) {
                return new PaperWritableBookContent(net.minecraft.world.item.component.WritableBookContent.EMPTY);
            }

            return new PaperWritableBookContent(
                new net.minecraft.world.item.component.WritableBookContent(new ObjectArrayList<>(this.pages))
            );
        }
    }
}
