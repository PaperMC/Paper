package org.bukkit.ban;

import java.net.InetSocketAddress;
import org.bukkit.BanList;

/**
 * A {@link BanList} targeting IP bans.
 */
public interface IpBanList extends BanList<InetSocketAddress> {

}
