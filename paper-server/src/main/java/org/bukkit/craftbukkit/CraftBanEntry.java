package org.bukkit.craftbukkit;

import java.util.Date;

import net.minecraft.server.BanEntry;
import net.minecraft.server.BanList;

public final class CraftBanEntry implements org.bukkit.BanEntry {
    private final BanList list;
    private final String name;
    private Date created;
    private String source;
    private Date expiration;
    private String reason;

    public CraftBanEntry(BanEntry entry, BanList list) {
        this.list = list;
        this.name = entry.getName();
        this.created = entry.getCreated() != null ? new Date(entry.getCreated().getTime()) : null;
        this.source = entry.getSource();
        this.expiration = entry.getExpires() != null ? new Date(entry.getExpires().getTime()) : null;
        this.reason = entry.getReason();
    }

    @Override
    public String getTarget() {
        return this.name;
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
        if (expiration != null && expiration.getTime() == new Date(0, 0, 0, 0, 0, 0).getTime()) {
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
        BanEntry entry = new BanEntry(this.name);
        entry.setCreated(this.created);
        entry.setSource(this.source);
        entry.setExpires(this.expiration);
        entry.setReason(this.reason);

        this.list.add(entry);
        this.list.save();
    }

}
