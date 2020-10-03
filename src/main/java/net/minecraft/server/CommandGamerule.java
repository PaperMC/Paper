package net.minecraft.server;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

public class CommandGamerule {

    public static void a(com.mojang.brigadier.CommandDispatcher<CommandListenerWrapper> com_mojang_brigadier_commanddispatcher) {
        final LiteralArgumentBuilder<CommandListenerWrapper> literalargumentbuilder = (LiteralArgumentBuilder) CommandDispatcher.a("gamerule").requires((commandlistenerwrapper) -> {
            return commandlistenerwrapper.hasPermission(2);
        });

        GameRules.a(new GameRules.GameRuleVisitor() {
            @Override
            public <T extends GameRules.GameRuleValue<T>> void a(GameRules.GameRuleKey<T> gamerules_gamerulekey, GameRules.GameRuleDefinition<T> gamerules_gameruledefinition) {
                literalargumentbuilder.then(((LiteralArgumentBuilder) CommandDispatcher.a(gamerules_gamerulekey.a()).executes((commandcontext) -> {
                    return CommandGamerule.b((CommandListenerWrapper) commandcontext.getSource(), gamerules_gamerulekey);
                })).then(gamerules_gameruledefinition.a("value").executes((commandcontext) -> {
                    return CommandGamerule.b(commandcontext, gamerules_gamerulekey);
                })));
            }
        });
        com_mojang_brigadier_commanddispatcher.register(literalargumentbuilder);
    }

    private static <T extends GameRules.GameRuleValue<T>> int b(CommandContext<CommandListenerWrapper> commandcontext, GameRules.GameRuleKey<T> gamerules_gamerulekey) {
        CommandListenerWrapper commandlistenerwrapper = (CommandListenerWrapper) commandcontext.getSource();
        T t0 = commandlistenerwrapper.getWorld().getGameRules().get(gamerules_gamerulekey); // CraftBukkit

        t0.b(commandcontext, "value");
        commandlistenerwrapper.sendMessage(new ChatMessage("commands.gamerule.set", new Object[]{gamerules_gamerulekey.a(), t0.toString()}), true);
        return t0.getIntValue();
    }

    private static <T extends GameRules.GameRuleValue<T>> int b(CommandListenerWrapper commandlistenerwrapper, GameRules.GameRuleKey<T> gamerules_gamerulekey) {
        T t0 = commandlistenerwrapper.getWorld().getGameRules().get(gamerules_gamerulekey); // CraftBukkit

        commandlistenerwrapper.sendMessage(new ChatMessage("commands.gamerule.query", new Object[]{gamerules_gamerulekey.a(), t0.toString()}), false);
        return t0.getIntValue();
    }
}
