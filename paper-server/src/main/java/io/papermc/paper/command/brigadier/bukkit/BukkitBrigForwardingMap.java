package io.papermc.paper.command.brigadier.bukkit;

import com.google.common.collect.Iterators;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.CommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.PaperBrigadier;
import io.papermc.paper.command.brigadier.PaperCommands;
import io.papermc.paper.command.brigadier.PluginVanillaCommandWrapper;
import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.Stream;
import org.bukkit.command.Command;
import org.bukkit.craftbukkit.command.VanillaCommandWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/*
This map is supposed to act as a legacy bridge for the command map and the command dispatcher.
 */
public class BukkitBrigForwardingMap extends HashMap<String, Command> {

    public static BukkitBrigForwardingMap INSTANCE = new BukkitBrigForwardingMap();

    private final EntrySet entrySet = new EntrySet();
    private final KeySet keySet = new KeySet();
    private final Values values = new Values();

    // Previous dispatcher used to get commands to migrate to another dispatcher

    public CommandDispatcher<CommandSourceStack> getDispatcher() {
        return PaperCommands.INSTANCE.getDispatcherInternal();
    }

    @Override
    public int size() {
        return this.getDispatcher().getRoot().getChildren().size();
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        if (!(key instanceof String stringKey)) {
            return false;
        }

        // Do any children match?
        return this.getDispatcher().getRoot().getChild(stringKey) != null;
    }

    @Override
    public boolean containsValue(@Nullable final Object value) {
        if (!(value instanceof Command)) {
            return false;
        }

        for (CommandNode<CommandSourceStack> child : this.getDispatcher().getRoot().getChildren()) {
            // If child is a bukkit command node, we can convert it!
            if (child instanceof BukkitCommandNode bukkitCommandNode) {
                return bukkitCommandNode.getBukkitCommand().equals(value);
            }
        }

        return false;
    }

    @Override
    public Command get(Object key) {
        CommandNode<?> node = this.getDispatcher().getRoot().getChild((String) key);
        if (node == null) {
            return null;
        }

        if (node instanceof BukkitCommandNode bukkitCommandNode) {
            return bukkitCommandNode.getBukkitCommand();
        }

        return PaperBrigadier.wrapNode(node);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @Nullable
    @Override
    public Command put(String key, Command value) {
        Command old = this.get(key);
        this.getDispatcher().getRoot().removeCommand(key); // Override previous command
        if (value instanceof VanillaCommandWrapper wrapper && wrapper.getName().equals(key)) {
            // Don't break when some plugin tries to remove and add back a plugin command registered with modern API...
            this.getDispatcher().getRoot().addChild((CommandNode) wrapper.vanillaCommand);
        } else {
            this.getDispatcher().getRoot().addChild(BukkitCommandNode.of(key, value));
        }
        return old;
    }

    @Override
    public Command remove(Object key) {
        if (!(key instanceof String string)) {
            return null;
        }

        Command old = this.get(key);
         if (old != null) {
             this.getDispatcher().getRoot().removeCommand(string);
         }

        return old;
    }

    @Override
    public boolean remove(Object key, Object value) {
        Command old = this.get(key);
        if (Objects.equals(old, value)) {
            this.remove(key);
            return true;
        }

        return false;
    }

    @Override
    public void putAll(@NotNull Map<? extends String, ? extends Command> m) {
        for (Entry<? extends String, ? extends Command> entry : m.entrySet()) {
            this.put(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clear() {
        this.getDispatcher().getRoot().clearAll();
    }

    @NotNull
    @Override
    public Set<String> keySet() {
        return this.keySet;
    }

    @NotNull
    @Override
    public Collection<Command> values() {
        return this.values;
    }

    @NotNull
    @Override
    public Set<Entry<String, Command>> entrySet() {
        return this.entrySet;
    }

    final class Values extends AbstractCollection<Command> {

        @Override
        public Iterator<Command> iterator() {
            // AVOID CME since commands can modify multiple commands now through aliases, which means it may appear in the iterator even if removed.
            // Oh well!
            Iterator<CommandNode<CommandSourceStack>> iterator = new ArrayList<>(BukkitBrigForwardingMap.this.getDispatcher().getRoot().getChildren()).iterator();

            return new Iterator<>() {

                private CommandNode<CommandSourceStack> lastFetched;

                @Override
                public void remove() {
                    if (this.lastFetched == null) {
                        throw new IllegalStateException("next not yet called");
                    }

                    BukkitBrigForwardingMap.this.remove(this.lastFetched.getName());
                    iterator.remove();
                }

                @Override
                public boolean hasNext() {
                    return iterator.hasNext();
                }

                @Override
                public Command next() {
                    CommandNode<CommandSourceStack> next = iterator.next();
                    this.lastFetched = next;
                    if (next instanceof BukkitCommandNode bukkitCommandNode) {
                        return bukkitCommandNode.getBukkitCommand();
                    } else {
                        return PaperBrigadier.wrapNode(next);
                    }
                }
            };
        }

        @Override
        public int size() {
            return BukkitBrigForwardingMap.this.getDispatcher().getRoot().getChildren().size();
        }

        @Override
        public void clear() {
            BukkitBrigForwardingMap.this.clear();
        }
    }


    final class KeySet extends AbstractSet<String> {

        @Override
        public int size() {
            return BukkitBrigForwardingMap.this.size();
        }

        @Override
        public void clear() {
            BukkitBrigForwardingMap.this.clear();
        }

        @Override
        public Iterator<String> iterator() {
            return Iterators.transform(BukkitBrigForwardingMap.this.values.iterator(), Command::getName); // Wrap around the values iterator for consistency
        }

        @Override
        public boolean contains(Object o) {
            return BukkitBrigForwardingMap.this.containsKey(o);
        }

        @Override
        public boolean remove(Object o) {
            return BukkitBrigForwardingMap.this.remove(o) != null;
        }

        @Override
        public Spliterator<String> spliterator() {
            return this.entryStream().spliterator();
        }

        @Override
        public void forEach(Consumer<? super String> action) {
            this.entryStream().forEach(action);
        }

        private Stream<String> entryStream() {
            return BukkitBrigForwardingMap.this.getDispatcher().getRoot().getChildren().stream().map(CommandNode::getName);
        }
    }

    final class EntrySet extends AbstractSet<Entry<String, Command>> {
        @Override
        public int size() {
            return BukkitBrigForwardingMap.this.size();
        }


        @Override
        public void clear() {
            BukkitBrigForwardingMap.this.clear();
        }

        @Override
        public Iterator<Entry<String, Command>> iterator() {
            return this.entryStream().iterator();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry<?, ?> entry)) {
                return false;
            }

            Object key = entry.getKey();
            Command candidate = get(key);
            return candidate != null && candidate.equals(entry.getValue());
        }

        @Override
        public boolean remove(Object o) {
            if (o instanceof Map.Entry<?, ?> e) {
                Object key = e.getKey();
                Object value = e.getValue();
                return BukkitBrigForwardingMap.this.remove(key, value);
            }
            return false;
        }

        @Override
        public Spliterator<Entry<String, Command>> spliterator() {
            return this.entryStream().spliterator();
        }

        @Override
        public void forEach(Consumer<? super Entry<String, Command>> action) {
            this.entryStream().forEach(action);
        }

        private Stream<Map.Entry<String, Command>> entryStream() {
            return BukkitBrigForwardingMap.this.getDispatcher().getRoot().getChildren().stream().map(BukkitBrigForwardingMap.this::nodeToEntry);
        }
    }

    private Map.Entry<String, Command> nodeToEntry(CommandNode<?> node) {
        if (node instanceof BukkitCommandNode bukkitCommandNode) {
            return this.mutableEntry(bukkitCommandNode.getName(), bukkitCommandNode.getBukkitCommand());
        } else {
            Command wrapped = PaperBrigadier.wrapNode(node);
            return this.mutableEntry(node.getName(), wrapped);
        }
    }

    private Map.Entry<String, Command> mutableEntry(String key, Command command) {
        return new Entry<>() {
            @Override
            public String getKey() {
                return key;
            }

            @Override
            public Command getValue() {
                return command;
            }

            @Override
            public Command setValue(Command value) {
                return BukkitBrigForwardingMap.this.put(key, value);
            }
        };
    }

}
