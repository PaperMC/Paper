package org.bukkit.craftbukkit.profile;

import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.SystemUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

public final class CraftGameProfile extends GameProfile {

    private final boolean nullId;
    private final boolean nullName;

    public CraftGameProfile(UUID id, String name) {
        super((id == null) ? SystemUtils.NIL_UUID : id, (name == null) ? "" : name);

        this.nullId = (id == null);
        this.nullName = (name == null);
    }

    @Override
    public UUID getId() {
        return (nullId) ? null : super.getId();
    }

    @Override
    public String getName() {
        return (nullName) ? null : super.getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        GameProfile that = (GameProfile) o;
        if ((this.getId() != null) ? !this.getId().equals(that.getId()) : (that.getId() != null)) {
            return false;
        }
        if ((this.getName() != null) ? !this.getName().equals(that.getName()) : (that.getName() != null)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = (this.getId() != null) ? this.getId().hashCode() : 0;
        result = 31 * result + ((this.getName() != null) ? this.getName().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("id", this.getId())
                .append("name", this.getName())
                .append("properties", this.getProperties())
                .toString();
    }
}
