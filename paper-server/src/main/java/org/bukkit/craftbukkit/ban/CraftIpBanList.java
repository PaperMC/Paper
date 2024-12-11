package org.bukkit.craftbukkit.ban;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.net.InetAddresses;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Set;
import net.minecraft.server.players.IpBanList;
import net.minecraft.server.players.IpBanListEntry;
import org.bukkit.BanEntry;

public class CraftIpBanList implements org.bukkit.ban.IpBanList {
    private final IpBanList list;

    public CraftIpBanList(IpBanList list) {
        this.list = list;
    }

    @Override
    public BanEntry<InetAddress> getBanEntry(String target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");

        IpBanListEntry entry = this.list.get(target);
        if (entry == null) {
            return null;
        }

        return new CraftIpBanEntry(target, entry, this.list);
    }

    @Override
    public BanEntry<InetAddress> getBanEntry(InetAddress target) {
        return this.getBanEntry(this.getIpFromAddress(target));
    }

    @Override
    public BanEntry<InetAddress> addBan(String target, String reason, Date expires, String source) {
        Preconditions.checkArgument(target != null, "Ban target cannot be null");

        IpBanListEntry entry = new IpBanListEntry(target, new Date(),
                (source == null || source.isBlank()) ? null : source, expires,
                (reason == null || reason.isBlank()) ? null : reason);

        this.list.add(entry);

        return new CraftIpBanEntry(target, entry, this.list);
    }

    @Override
    public BanEntry<InetAddress> addBan(InetAddress target, String reason, Date expires, String source) {
        return this.addBan(this.getIpFromAddress(target), reason, expires, source);
    }

    @Override
    public BanEntry<InetAddress> addBan(InetAddress target, String reason, Instant expires, String source) {
        Date date = expires != null ? Date.from(expires) : null;
        return this.addBan(target, reason, date, source);
    }

    @Override
    public BanEntry<InetAddress> addBan(InetAddress target, String reason, Duration duration, String source) {
        Instant instant = duration != null ? Instant.now().plus(duration) : null;
        return this.addBan(target, reason, instant, source);
    }

    @Override
    public Set<BanEntry> getBanEntries() {
        ImmutableSet.Builder<BanEntry> builder = ImmutableSet.builder();
        for (String target : this.list.getUserList()) {
            IpBanListEntry ipBanEntry = this.list.get(target);
            if (ipBanEntry != null) {
                builder.add(new CraftIpBanEntry(target, ipBanEntry, this.list));
            }
        }
        return builder.build();
    }

    @Override
    public Set<BanEntry<InetAddress>> getEntries() {
        ImmutableSet.Builder<BanEntry<InetAddress>> builder = ImmutableSet.builder();
        for (String target : this.list.getUserList()) {
            IpBanListEntry ipBanEntry = this.list.get(target);
            if (ipBanEntry != null) {
                builder.add(new CraftIpBanEntry(target, ipBanEntry, this.list));
            }
        }
        return builder.build();
    }

    @Override
    public boolean isBanned(String target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");
        return this.list.isBanned(target);
    }

    @Override
    public boolean isBanned(InetAddress target) {
        return this.isBanned(this.getIpFromAddress(target));
    }

    @Override
    public void pardon(String target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");
        this.list.remove(target);
    }

    @Override
    public void pardon(InetAddress target) {
        this.pardon(this.getIpFromAddress(target));
    }

    private String getIpFromAddress(InetAddress address) {
        if (address == null) {
            return null;
        }
        return InetAddresses.toAddrString(address);
    }
}
