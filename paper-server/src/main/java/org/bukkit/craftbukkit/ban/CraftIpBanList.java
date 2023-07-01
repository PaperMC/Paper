package org.bukkit.craftbukkit.ban;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.net.InetAddresses;
import java.net.InetSocketAddress;
import java.util.Date;
import java.util.Set;
import net.minecraft.server.players.IpBanEntry;
import net.minecraft.server.players.IpBanList;
import org.bukkit.BanEntry;

public class CraftIpBanList implements org.bukkit.ban.IpBanList {
    private final IpBanList list;

    public CraftIpBanList(IpBanList list) {
        this.list = list;
    }

    @Override
    public BanEntry<InetSocketAddress> getBanEntry(String target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");

        IpBanEntry entry = this.list.get(target);
        if (entry == null) {
            return null;
        }

        return new CraftIpBanEntry(target, entry, list);
    }

    @Override
    public BanEntry<InetSocketAddress> getBanEntry(InetSocketAddress target) {
        return this.getBanEntry(this.getIpFromAddress(target));
    }

    @Override
    public BanEntry<InetSocketAddress> addBan(String target, String reason, Date expires, String source) {
        Preconditions.checkArgument(target != null, "Ban target cannot be null");

        IpBanEntry entry = new IpBanEntry(target, new Date(),
                (source == null || source.isBlank()) ? null : source, expires,
                (reason == null || reason.isBlank()) ? null : reason);

        this.list.add(entry);

        return new CraftIpBanEntry(target, entry, list);
    }

    @Override
    public BanEntry<InetSocketAddress> addBan(InetSocketAddress target, String reason, Date expires, String source) {
        return this.addBan(this.getIpFromAddress(target), reason, expires, source);
    }

    @Override
    public Set<BanEntry<InetSocketAddress>> getBanEntries() {
        ImmutableSet.Builder<BanEntry<InetSocketAddress>> builder = ImmutableSet.builder();
        for (String target : list.getUserList()) {
            IpBanEntry ipBanEntry = list.get(target);
            if (ipBanEntry != null) {
                builder.add(new CraftIpBanEntry(target, ipBanEntry, list));
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
    public boolean isBanned(InetSocketAddress target) {
        return this.isBanned(getIpFromAddress(target));
    }

    @Override
    public void pardon(String target) {
        Preconditions.checkArgument(target != null, "Target cannot be null");
        this.list.remove(target);
    }

    @Override
    public void pardon(InetSocketAddress target) {
        this.pardon(getIpFromAddress(target));
    }

    private String getIpFromAddress(InetSocketAddress address) {
        if (address == null) {
            return null;
        }
        Preconditions.checkArgument(!address.isUnresolved(), "%s its not a valid address", address);
        return InetAddresses.toAddrString(address.getAddress());
    }
}
