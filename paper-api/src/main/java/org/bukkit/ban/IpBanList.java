package org.bukkit.ban;

import java.net.InetAddress;
import org.bukkit.BanList;

/**
 * A {@link BanList} targeting IP bans.
 *
 * @since 1.20.1
 */
public interface IpBanList extends BanList<InetAddress> {

}
