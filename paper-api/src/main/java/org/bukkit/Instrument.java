package org.bukkit;

import java.util.HashMap;
import java.util.Map;

public enum Instrument {

    PIANO((byte) 0x0), // All other
    BASS_DRUM((byte) 0x1), // Stone
    SNARE_DRUM((byte) 0x2), // Sand
    STICKS((byte) 0x3), // Glass
    BASS_GUITAR((byte) 0x4); // Wood

    private final byte type;
    private final static Map<Byte, Instrument> types = new HashMap<Byte, Instrument>();

    private Instrument(byte type) {
        this.type = type;
    }

    public byte getType() {
        return this.type;
    }

    public static Instrument getByType(final byte type) {
        return types.get(type);
    }

    static {
        for (Instrument instrument : Instrument.values()) {
            types.put(instrument.getType(), instrument);
        }
    }
}
