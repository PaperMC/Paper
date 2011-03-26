package org.bukkit.material;

import org.bukkit.Material;

public class Jukebox extends MaterialData {

    public Jukebox(int type) {
        super(type);
    }

    public Jukebox(Material type) {
        super(type);
    }

    public Jukebox(int type, byte data) {
        super(type, data);
    }

    public Jukebox(Material type, byte data) {
        super(type, data);
    }

    /**
     * Gets the type of record currently playing
     * 
     * @return The type of record (Material.GOLD_RECORD or Material.GREEN_RECORD), or null for none.
     */
    public Material getPlaying() {
        switch ((int) getData()) {
        default:
        case 0x0:
            return null;
        case 0x1:
            return Material.GOLD_RECORD;
        case 0x2:
            return Material.GREEN_RECORD;
        }
    }

    /**
     * Sets the type of record currently playing
     * 
     * @param rec The type of record (Material.GOLD_RECORD or Material.GREEN_RECORD), or null for none.
     */
    public void setPlaying(Material rec) {
        if (rec == null) setData((byte) 0x0);
        else switch (rec) {
        case GOLD_RECORD:
            setData((byte) 0x1);
            break;
        case GREEN_RECORD:
            setData((byte) 0x2);
            break;
        default:
            setData((byte) 0x0);
        }
    }
}
