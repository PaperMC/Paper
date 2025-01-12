package io.papermc.paper.block.property;

import com.google.common.collect.BiMap;
import java.util.Collections;
import java.util.Set;
import org.bukkit.Note;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Unmodifiable;

@ApiStatus.Internal
record NoteBlockProperty(String name, BiMap<Integer, Note> cache) implements AsIntegerProperty<Note> {
    
    NoteBlockProperty(final String name) {
        this(name, AsIntegerProperty.createCache(24, Note::new));
    }

    @Override
    public Class<Note> type() {
        return Note.class;
    }

    @Override
    public String name(final Note value) {
        return String.valueOf(value.getId());
    }

    @Override
    public boolean isValidName(final String name) {
        try {
            final Integer value = Integer.valueOf(name);
            if (this.cache.containsKey(value)) {
                return true;
            }
        } catch (final NumberFormatException ignored) {
        }
        return false;
    }

    @Override
    public Note value(final String name) {
        try {
            final Integer value = Integer.valueOf(name);
            if (this.cache.containsKey(value)) {
                return this.cache.get(value);
            }
            throw ExceptionCreator.INSTANCE.create(name, ExceptionCreator.Type.NAME, this);
        } catch (final NumberFormatException exception) {
            throw ExceptionCreator.INSTANCE.create(name, ExceptionCreator.Type.NAME, this);
        }
    }

    @Override
    public boolean isValidValue(final Note note) {
        return this.cache.inverse().containsKey(note);
    }

    @Override
    public @Unmodifiable Set<Note> values() {
        return Collections.unmodifiableSet(this.cache.values());
    }
}
