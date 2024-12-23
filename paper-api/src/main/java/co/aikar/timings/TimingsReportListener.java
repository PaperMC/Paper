package co.aikar.timings;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.MessageCommandSender;
import org.bukkit.command.RemoteConsoleCommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * @deprecated Timings will be removed in the future
 */
@Deprecated(forRemoval = true)
@SuppressWarnings("WeakerAccess")
public class TimingsReportListener implements net.kyori.adventure.audience.ForwardingAudience, MessageCommandSender {
    private final List<CommandSender> senders;
    private final Runnable onDone;
    private String timingsURL;

    public TimingsReportListener(@NotNull CommandSender senders) {
        this(senders, null);
    }
    public TimingsReportListener(@NotNull CommandSender sender, @Nullable Runnable onDone) {
        this(Lists.newArrayList(sender), onDone);
    }
    public TimingsReportListener(@NotNull List<CommandSender> senders) {
        this(senders, null);
    }
    public TimingsReportListener(@NotNull List<CommandSender> senders, @Nullable Runnable onDone) {
        Preconditions.checkNotNull(senders);
        Preconditions.checkArgument(!senders.isEmpty(), "senders is empty");

        this.senders = Lists.newArrayList(senders);
        this.onDone = onDone;
    }

    @Nullable
    public String getTimingsURL() {
        return timingsURL;
    }

    public void done() {
        done(null);
    }

    public void done(@Nullable String url) {
        this.timingsURL = url;
        if (onDone != null) {
            onDone.run();
        }
        for (CommandSender sender : senders) {
            if (sender instanceof TimingsReportListener) {
                ((TimingsReportListener) sender).done();
            }
        }
    }

    @Override
    public void sendMessage(final @NotNull net.kyori.adventure.identity.Identity source, final @NotNull net.kyori.adventure.text.Component message, final @NotNull net.kyori.adventure.audience.MessageType type) {
        net.kyori.adventure.audience.ForwardingAudience.super.sendMessage(source, message, type);
    }

    @NotNull
    @Override
    public Iterable<? extends net.kyori.adventure.audience.Audience> audiences() {
        return this.senders;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        senders.forEach((sender) -> sender.sendMessage(message));
    }

    public void addConsoleIfNeeded() {
        boolean hasConsole = false;
        for (CommandSender sender : this.senders) {
            if (sender instanceof ConsoleCommandSender || sender instanceof RemoteConsoleCommandSender) {
                hasConsole = true;
            }
        }
        if (!hasConsole) {
            this.senders.add(Bukkit.getConsoleSender());
        }
    }
}
