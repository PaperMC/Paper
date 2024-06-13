package org.bukkit.craftbukkit;

import com.google.common.base.Preconditions;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.server.dedicated.DedicatedServer;
import org.bukkit.ServerLinks;
import org.bukkit.craftbukkit.util.CraftChatMessage;

public class CraftServerLinks implements ServerLinks {

    private final DedicatedServer server;
    private net.minecraft.server.ServerLinks serverLinks;

    public CraftServerLinks(DedicatedServer server) {
        this(server, null);
    }

    public CraftServerLinks(net.minecraft.server.ServerLinks serverLinks) {
        this(null, serverLinks);
    }

    private CraftServerLinks(DedicatedServer server, net.minecraft.server.ServerLinks serverLinks) {
        this.server = server;
        this.serverLinks = serverLinks;
    }

    @Override
    public ServerLink getLink(Type type) {
        Preconditions.checkArgument(type != null, "type cannot be null");

        return getServerLinks().findKnownType(fromBukkit(type)).map(CraftServerLink::new).orElse(null);
    }

    @Override
    public List<ServerLink> getLinks() {
        return getServerLinks().entries().stream().map(nms -> (ServerLink) new CraftServerLink(nms)).toList();
    }

    @Override
    public ServerLink setLink(Type type, URI url) {
        Preconditions.checkArgument(type != null, "type cannot be null");
        Preconditions.checkArgument(url != null, "url cannot be null");

        ServerLink existing = getLink(type);
        if (existing != null) {
            removeLink(existing);
        }
        return addLink(type, url);
    }

    @Override
    public ServerLink addLink(Type type, URI url) {
        Preconditions.checkArgument(type != null, "type cannot be null");
        Preconditions.checkArgument(url != null, "url cannot be null");

        CraftServerLink link = new CraftServerLink(net.minecraft.server.ServerLinks.Entry.knownType(fromBukkit(type), url));
        addLink(link);

        return link;
    }

    @Override
    public ServerLink addLink(String displayName, URI url) {
        Preconditions.checkArgument(displayName != null, "displayName cannot be null");
        Preconditions.checkArgument(url != null, "url cannot be null");

        CraftServerLink link = new CraftServerLink(net.minecraft.server.ServerLinks.Entry.custom(CraftChatMessage.fromStringOrNull(displayName), url));
        addLink(link);

        return link;
    }

    private void addLink(CraftServerLink link) {
        List<net.minecraft.server.ServerLinks.Entry> lst = new ArrayList<>(getServerLinks().entries());
        lst.add(link.handle);

        setLinks(new net.minecraft.server.ServerLinks(lst));
    }

    @Override
    public boolean removeLink(ServerLink link) {
        Preconditions.checkArgument(link != null, "link cannot be null");

        List<net.minecraft.server.ServerLinks.Entry> lst = new ArrayList<>(getServerLinks().entries());
        boolean result = lst.remove(((CraftServerLink) link).handle);

        setLinks(new net.minecraft.server.ServerLinks(lst));

        return result;
    }

    @Override
    public ServerLinks copy() {
        return new CraftServerLinks(getServerLinks());
    }

    public net.minecraft.server.ServerLinks getServerLinks() {
        return (server != null) ? server.serverLinks() : serverLinks;
    }

    private void setLinks(net.minecraft.server.ServerLinks links) {
        if (server != null) {
            server.serverLinks = links;
        } else {
            this.serverLinks = links;
        }
    }

    private static net.minecraft.server.ServerLinks.KnownLinkType fromBukkit(Type type) {
        return net.minecraft.server.ServerLinks.KnownLinkType.values()[type.ordinal()];
    }

    private static ServerLinks.Type fromNMS(net.minecraft.server.ServerLinks.KnownLinkType nms) {
        return ServerLinks.Type.values()[nms.ordinal()];
    }

    public static class CraftServerLink implements ServerLink {

        private final net.minecraft.server.ServerLinks.Entry handle;

        public CraftServerLink(net.minecraft.server.ServerLinks.Entry handle) {
            this.handle = handle;
        }

        @Override
        public Type getType() {
            return handle.type().left().map(CraftServerLinks::fromNMS).orElse(null);
        }

        @Override
        public String getDisplayName() {
            return CraftChatMessage.fromComponent(handle.displayName());
        }

        @Override
        public URI getUrl() {
            return handle.link();
        }
    }
}
