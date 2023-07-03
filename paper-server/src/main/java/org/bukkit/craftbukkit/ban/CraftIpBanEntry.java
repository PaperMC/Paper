package org.bukkit.craftbukkit.ban;

import com.google.common.net.InetAddresses;
import java.net.InetAddress;
import java.time.Instant;
import java.util.Date;
import net.minecraft.server.players.IpBanEntry;
import net.minecraft.server.players.IpBanList;
import org.bukkit.BanEntry;

public final class CraftIpBanEntry implements BanEntry<InetAddress> {
    private static final Date minorDate = Date.from(Instant.parse("1899-12-31T04:00:00Z"));
    private final IpBanList list;
    private final String target;
    private Date created;
    private String source;
    private Date expiration;
    private String reason;

    public CraftIpBanEntry(String target, IpBanEntry entry, IpBanList list) {
        this.list = list;
        this.target = target;
        this.created = entry.getCreated() != null ? new Date(entry.getCreated().getTime()) : null;
        this.source = entry.getSource();
        this.expiration = entry.getExpires() != null ? new Date(entry.getExpires().getTime()) : null;
        this.reason = entry.getReason();
    }

    @Override
    public String getTarget() {
        return this.target;
    }

    @Override
    public InetAddress getBanTarget() {
        return InetAddresses.forString(this.target);
    }

    @Override
    public Date getCreated() {
        return this.created == null ? null : (Date) this.created.clone();
    }

    @Override
    public void setCreated(Date created) {
        this.created = created;
    }

    @Override
    public String getSource() {
        return this.source;
    }

    @Override
    public void setSource(String source) {
        this.source = source;
    }

    @Override
    public Date getExpiration() {
        return this.expiration == null ? null : (Date) this.expiration.clone();
    }

    @Override
    public void setExpiration(Date expiration) {
        if (expiration != null && expiration.getTime() == minorDate.getTime()) {
            expiration = null; // Forces "forever"
        }

        this.expiration = expiration;
    }

    @Override
    public String getReason() {
        return this.reason;
    }

    @Override
    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public void save() {
        IpBanEntry entry = new IpBanEntry(this.target, this.created, this.source, this.expiration, this.reason);
        this.list.add(entry);
    }

    @Override
    public void remove() {
        this.list.remove(target);
    }
}
