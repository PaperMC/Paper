package net.minecraft.server;

import com.google.common.collect.Lists;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Collection;
import java.util.Iterator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CommandReload {

    private static final Logger LOGGER = LogManager.getLogger();

    public static void a(Collection<String> collection, CommandListenerWrapper commandlistenerwrapper) {
        commandlistenerwrapper.getServer().a(collection).exceptionally((throwable) -> {
            CommandReload.LOGGER.warn("Failed to execute reload", throwable);
            commandlistenerwrapper.sendFailureMessage(new ChatMessage("commands.reload.failure"));
            return null;
        });
    }

    private static Collection<String> a(ResourcePackRepository resourcepackrepository, SaveData savedata, Collection<String> collection) {
        resourcepackrepository.a();
        Collection<String> collection1 = Lists.newArrayList(collection);
        Collection<String> collection2 = savedata.D().b();
        Iterator iterator = resourcepackrepository.b().iterator();

        while (iterator.hasNext()) {
            String s = (String) iterator.next();

            if (!collection2.contains(s) && !collection1.contains(s)) {
                collection1.add(s);
            }
        }

        return collection1;
    }

    // CraftBukkit start
    public static void reload(MinecraftServer minecraftserver) {
        ResourcePackRepository resourcepackrepository = minecraftserver.getResourcePackRepository();
        SaveData savedata = minecraftserver.getSaveData();
        Collection<String> collection = resourcepackrepository.d();
        Collection<String> collection1 = a(resourcepackrepository, savedata, collection);
        minecraftserver.a(collection1);
    }
    // CraftBukkit end

    public static void a(com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> com_mojang_brigadier_commanddispatcher) {
        com_mojang_brigadier_commanddispatcher.register((LiteralArgumentBuilder) ((LiteralArgumentBuilder) CommandDispatcher.a("reload").requires((commandlistenerwrapper) -> {
            return commandlistenerwrapper.hasPermission(2);
        })).executes((commandcontext) -> {
            CommandListenerWrapper commandlistenerwrapper = (CommandListenerWrapper) commandcontext.getSource();
            MinecraftServer minecraftserver = commandlistenerwrapper.getServer();
            ResourcePackRepository resourcepackrepository = minecraftserver.getResourcePackRepository();
            SaveData savedata = minecraftserver.getSaveData();
            Collection<String> collection = resourcepackrepository.d();
            Collection<String> collection1 = a(resourcepackrepository, savedata, collection);

            commandlistenerwrapper.sendMessage(new ChatMessage("commands.reload.success"), true);
            a(collection1, commandlistenerwrapper);
            return 0;
        }));
    }
}
