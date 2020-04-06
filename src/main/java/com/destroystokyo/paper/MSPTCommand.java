package com.destroystokyo.paper;

import net.minecraft.server.MinecraftServer;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MSPTCommand extends Command {
    private static final DecimalFormat DF = new DecimalFormat("########0.0");

    public MSPTCommand(String name) {
        super(name);
        this.description = "View server tick times";
        this.usageMessage = "/mspt";
        this.setPermission("bukkit.command.mspt");
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args, Location location) throws IllegalArgumentException {
        return Collections.emptyList();
    }

    @Override
    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!testPermission(sender)) return true;

        MinecraftServer server = MinecraftServer.getServer();

        List<String> times = new ArrayList<>();
        times.addAll(eval(server.tickTimes5s.getTimes()));
        times.addAll(eval(server.tickTimes10s.getTimes()));
        times.addAll(eval(server.tickTimes60s.getTimes()));

        sender.sendMessage("§6Server tick times §e(§7avg§e/§7min§e/§7max§e)§6 from last 5s§7,§6 10s§7,§6 1m§e:");
        sender.sendMessage(String.format("§6◴ %s§7/%s§7/%s§e, %s§7/%s§7/%s§e, %s§7/%s§7/%s", times.toArray()));
        return true;
    }

    private static List<String> eval(long[] times) {
        long min = Integer.MAX_VALUE;
        long max = 0L;
        long total = 0L;
        for (long value : times) {
            if (value > 0L && value < min) min = value;
            if (value > max) max = value;
            total += value;
        }
        double avgD = ((double) total / (double) times.length) * 1.0E-6D;
        double minD = ((double) min) * 1.0E-6D;
        double maxD = ((double) max) * 1.0E-6D;
        return Arrays.asList(getColor(avgD), getColor(minD), getColor(maxD));
    }

    private static String getColor(double avg) {
        return ChatColor.COLOR_CHAR + (avg >= 50 ? "c" : avg >= 40 ? "e" : "a") + DF.format(avg);
    }
}
