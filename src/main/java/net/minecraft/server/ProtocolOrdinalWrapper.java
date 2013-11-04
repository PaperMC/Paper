package net.minecraft.server;

// CraftBukkit - import package private class
class ProtocolOrdinalWrapper {

    static final int[] a = new int[EnumProtocol.values().length];

    static {
        try {
            a[EnumProtocol.LOGIN.ordinal()] = 1;
        } catch (NoSuchFieldError nosuchfielderror) {
            ;
        }

        try {
            a[EnumProtocol.STATUS.ordinal()] = 2;
        } catch (NoSuchFieldError nosuchfielderror1) {
            ;
        }
    }
}
