package io.papermc.paper.command.brigadier;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import io.papermc.paper.command.brigadier.bukkit.BukkitBrigForwardingMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.support.RegistryHelper;
import org.bukkit.support.environment.AllFeatures;
import org.bukkit.support.environment.Normal;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

@Normal
public class BukkitCommandConversionTest {

    private CommandSender getSender() {
        return Mockito.mock(CommandSender.class);
    }

    @Test
    public void test() throws CommandSyntaxException {
        CommandSender sender = this.getSender();
        CommandSourceStack object = Mockito.mock(CommandSourceStack.class);
        Mockito.when(object.getLocation()).thenReturn(new Location(null, 0, 0, 0));;

        CommandDispatcher dispatcher = RegistryHelper.getDataPack().commands.getDispatcher();
        dispatcher.setConsumer((context, success, result) -> {});
        CommandMap commandMap = new SimpleCommandMap(Bukkit.getServer(), new BukkitBrigForwardingMap());
        Map<String, Command> stringCommandMap = commandMap.getKnownCommands();
        // All commands should be mirrored -- or equal
        int commandMapSize = stringCommandMap.values().size();
        ExampleCommand exampleCommand = new ExampleCommand();

        Assertions.assertEquals(commandMapSize, dispatcher.getRoot().getChildren().size());

        // Register a new command
        commandMap.register("test", exampleCommand);
        Assertions.assertEquals(commandMapSize + (3 * 2), stringCommandMap.values().size()); // Make sure commands are accounted for, including those with namespaced keys

        // Test Registration
        for (String alias : exampleCommand.getAliases()) {
            Assertions.assertEquals(stringCommandMap.get(alias), exampleCommand);
            Assertions.assertEquals(stringCommandMap.get("test:" + alias), exampleCommand);
        }
        // Test command instance equality
        Assertions.assertEquals(stringCommandMap.get(exampleCommand.getName()), exampleCommand);
        Assertions.assertEquals(stringCommandMap.get("test:" + exampleCommand.getName()), exampleCommand);

        // Test command map execution
        commandMap.dispatch(sender, "main-example example");
        Assertions.assertEquals(exampleCommand.invocations, 1);
        Assertions.assertEquals(commandMap.tabComplete(sender, "main-example 1 2"), List.of("complete"));

        // Test dispatcher execution
        dispatcher.execute("main-example example", object);
        Assertions.assertEquals(exampleCommand.invocations, 2);

        dispatcher.execute("test:example2 example", object);
        Assertions.assertEquals(exampleCommand.invocations, 3);

        Suggestions suggestions = (Suggestions) dispatcher.getCompletionSuggestions(dispatcher.parse("main-example 1 2", object)).join();
        Assertions.assertEquals(suggestions.getList().get(0).getText(), "complete");


        // Test command map removal
        commandMap.getKnownCommands().remove("test");
        Assertions.assertNull(commandMap.getCommand("test"));
        Assertions.assertNull(dispatcher.getRoot().getChild("test"));
    }

    private static class ExampleCommand extends Command {

        int invocations;

        protected ExampleCommand() {
            super("main-example", "This is an example.", "", List.of("example", "example2"));
        }

        @Override
        public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
            Assertions.assertEquals(args[0], "example");
            this.invocations++;
            return true;
        }

        @Override
        public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
            Assertions.assertEquals(args.length, 2);
            return List.of("complete");
        }
    }
}
