package org.bukkit.material;

import org.bukkit.Material;

public class Diode extends MaterialData {

    public Diode(int type) {
        super(type);
    }

    public Diode(Material type) {
        super(type);
    }

    public Diode(int type, byte data) {
        super(type, data);
    }

    public Diode(Material type, byte data) {
        super(type, data);
    }

    /**
     * Sets the delay of the repeater
     * 
     * @param delay The new delay (1-4)
     */
    public void setDelay(int delay) {
        if (delay > 4) delay = 4;
        if (delay < 1) delay = 1;
        byte newData = (byte) (getData() & 0x3);
        setData((byte) (newData | (delay - 1)));
    }

    /**
     * Gets the delay of the repeater
     * 
     * @return The delay (1-4)
     */
    public int getDelay() {
        return (getData() & 0xC) + 1;
    }
}
