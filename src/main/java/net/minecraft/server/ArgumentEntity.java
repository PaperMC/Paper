package net.minecraft.server;

import com.google.common.collect.Iterables;
import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ArgumentEntity implements ArgumentType<EntitySelector> {

    private static final Collection<String> g = Arrays.asList("Player", "0123", "@e", "@e[type=foo]", "dd12be42-52a9-4a91-a8a1-11c01849e498");
    public static final SimpleCommandExceptionType a = new SimpleCommandExceptionType(new ChatMessage("argument.entity.toomany"));
    public static final SimpleCommandExceptionType b = new SimpleCommandExceptionType(new ChatMessage("argument.player.toomany"));
    public static final SimpleCommandExceptionType c = new SimpleCommandExceptionType(new ChatMessage("argument.player.entities"));
    public static final SimpleCommandExceptionType d = new SimpleCommandExceptionType(new ChatMessage("argument.entity.notfound.entity"));
    public static final SimpleCommandExceptionType e = new SimpleCommandExceptionType(new ChatMessage("argument.entity.notfound.player"));
    public static final SimpleCommandExceptionType f = new SimpleCommandExceptionType(new ChatMessage("argument.entity.selector.not_allowed"));
    private final boolean h;
    private final boolean i;

    protected ArgumentEntity(boolean flag, boolean flag1) {
        this.h = flag;
        this.i = flag1;
    }

    public static ArgumentEntity a() {
        return new ArgumentEntity(true, false);
    }

    public static Entity a(CommandContext<CommandListenerWrapper> commandcontext, String s) throws CommandSyntaxException {
        return ((EntitySelector) commandcontext.getArgument(s, EntitySelector.class)).a((CommandListenerWrapper) commandcontext.getSource());
    }

    public static ArgumentEntity multipleEntities() {
        return new ArgumentEntity(false, false);
    }

    public static Collection<? extends Entity> b(CommandContext<CommandListenerWrapper> commandcontext, String s) throws CommandSyntaxException {
        Collection<? extends Entity> collection = c(commandcontext, s);

        if (collection.isEmpty()) {
            throw ArgumentEntity.d.create();
        } else {
            return collection;
        }
    }

    public static Collection<? extends Entity> c(CommandContext<CommandListenerWrapper> commandcontext, String s) throws CommandSyntaxException {
        return ((EntitySelector) commandcontext.getArgument(s, EntitySelector.class)).getEntities((CommandListenerWrapper) commandcontext.getSource());
    }

    public static Collection<EntityPlayer> d(CommandContext<CommandListenerWrapper> commandcontext, String s) throws CommandSyntaxException {
        return ((EntitySelector) commandcontext.getArgument(s, EntitySelector.class)).d((CommandListenerWrapper) commandcontext.getSource());
    }

    public static ArgumentEntity c() {
        return new ArgumentEntity(true, true);
    }

    public static EntityPlayer e(CommandContext<CommandListenerWrapper> commandcontext, String s) throws CommandSyntaxException {
        return ((EntitySelector) commandcontext.getArgument(s, EntitySelector.class)).c((CommandListenerWrapper) commandcontext.getSource());
    }

    public static ArgumentEntity d() {
        return new ArgumentEntity(false, true);
    }

    public static Collection<EntityPlayer> f(CommandContext<CommandListenerWrapper> commandcontext, String s) throws CommandSyntaxException {
        List<EntityPlayer> list = ((EntitySelector) commandcontext.getArgument(s, EntitySelector.class)).d((CommandListenerWrapper) commandcontext.getSource());

        if (list.isEmpty()) {
            throw ArgumentEntity.e.create();
        } else {
            return list;
        }
    }

    public EntitySelector parse(StringReader stringreader) throws CommandSyntaxException {
        // CraftBukkit start
        return parse(stringreader, false);
    }

    public EntitySelector parse(StringReader stringreader, boolean overridePermissions) throws CommandSyntaxException {
        // CraftBukkit end
        boolean flag = false;
        ArgumentParserSelector argumentparserselector = new ArgumentParserSelector(stringreader);
        EntitySelector entityselector = argumentparserselector.parse(overridePermissions); // CraftBukkit

        if (entityselector.a() > 1 && this.h) {
            if (this.i) {
                stringreader.setCursor(0);
                throw ArgumentEntity.b.createWithContext(stringreader);
            } else {
                stringreader.setCursor(0);
                throw ArgumentEntity.a.createWithContext(stringreader);
            }
        } else if (entityselector.b() && this.i && !entityselector.c()) {
            stringreader.setCursor(0);
            throw ArgumentEntity.c.createWithContext(stringreader);
        } else {
            return entityselector;
        }
    }

    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandcontext, SuggestionsBuilder suggestionsbuilder) {
        if (commandcontext.getSource() instanceof ICompletionProvider) {
            StringReader stringreader = new StringReader(suggestionsbuilder.getInput());

            stringreader.setCursor(suggestionsbuilder.getStart());
            ICompletionProvider icompletionprovider = (ICompletionProvider) commandcontext.getSource();
            ArgumentParserSelector argumentparserselector = new ArgumentParserSelector(stringreader, icompletionprovider.hasPermission(2));

            try {
                argumentparserselector.parse();
            } catch (CommandSyntaxException commandsyntaxexception) {
                ;
            }

            return argumentparserselector.a(suggestionsbuilder, (suggestionsbuilder1) -> {
                Collection<String> collection = icompletionprovider.l();
                Iterable<String> iterable = this.i ? collection : Iterables.concat(collection, icompletionprovider.r());

                ICompletionProvider.b((Iterable) iterable, suggestionsbuilder1);
            });
        } else {
            return Suggestions.empty();
        }
    }

    public Collection<String> getExamples() {
        return ArgumentEntity.g;
    }

    public static class a implements ArgumentSerializer<ArgumentEntity> {

        public a() {}

        public void a(ArgumentEntity argumententity, PacketDataSerializer packetdataserializer) {
            byte b0 = 0;

            if (argumententity.h) {
                b0 = (byte) (b0 | 1);
            }

            if (argumententity.i) {
                b0 = (byte) (b0 | 2);
            }

            packetdataserializer.writeByte(b0);
        }

        @Override
        public ArgumentEntity b(PacketDataSerializer packetdataserializer) {
            byte b0 = packetdataserializer.readByte();

            return new ArgumentEntity((b0 & 1) != 0, (b0 & 2) != 0);
        }

        public void a(ArgumentEntity argumententity, JsonObject jsonobject) {
            jsonobject.addProperty("amount", argumententity.h ? "single" : "multiple");
            jsonobject.addProperty("type", argumententity.i ? "players" : "entities");
        }
    }
}
