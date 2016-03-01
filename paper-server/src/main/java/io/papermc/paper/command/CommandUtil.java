package io.papermc.paper.command;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.framework.qual.DefaultQualifier;

@DefaultQualifier(NonNull.class)
public final class CommandUtil {
    private CommandUtil() {
    }

    // Code from Mojang - copyright them
    public static List<String> getListMatchingLast(
        final CommandSender sender,
        final String[] args,
        final String... matches
    ) {
        return getListMatchingLast(sender, args, Arrays.asList(matches));
    }

    public static boolean matches(final String s, final String s1) {
        return s1.regionMatches(true, 0, s, 0, s.length());
    }

    public static List<String> getListMatchingLast(
        final CommandSender sender,
        final String[] strings,
        final Collection<?> collection
    ) {
        String last = strings[strings.length - 1];
        ArrayList<String> results = Lists.newArrayList();

        if (!collection.isEmpty()) {
            Iterator iterator = Iterables.transform(collection, Functions.toStringFunction()).iterator();

            while (iterator.hasNext()) {
                String s1 = (String) iterator.next();

                if (matches(last, s1) && (sender.hasPermission(PaperCommand.BASE_PERM + s1) || sender.hasPermission("bukkit.command.paper"))) {
                    results.add(s1);
                }
            }

            if (results.isEmpty()) {
                iterator = collection.iterator();

                while (iterator.hasNext()) {
                    Object object = iterator.next();

                    if (object instanceof ResourceLocation && matches(last, ((ResourceLocation) object).getPath())) {
                        results.add(String.valueOf(object));
                    }
                }
            }
        }

        return results;
    }
    // end copy stuff
}
