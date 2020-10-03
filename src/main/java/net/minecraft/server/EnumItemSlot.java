package net.minecraft.server;

public enum EnumItemSlot {

    MAINHAND(EnumItemSlot.Function.HAND, 0, 0, "mainhand"), OFFHAND(EnumItemSlot.Function.HAND, 1, 5, "offhand"), FEET(EnumItemSlot.Function.ARMOR, 0, 1, "feet"), LEGS(EnumItemSlot.Function.ARMOR, 1, 2, "legs"), CHEST(EnumItemSlot.Function.ARMOR, 2, 3, "chest"), HEAD(EnumItemSlot.Function.ARMOR, 3, 4, "head");

    private final EnumItemSlot.Function g;
    private final int h;
    private final int i;
    private final String j;

    private EnumItemSlot(EnumItemSlot.Function enumitemslot_function, int i, int j, String s) {
        this.g = enumitemslot_function;
        this.h = i;
        this.i = j;
        this.j = s;
    }

    public EnumItemSlot.Function a() {
        return this.g;
    }

    public int b() {
        return this.h;
    }

    public int getSlotFlag() {
        return this.i;
    }

    public String getSlotName() {
        return this.j;
    }

    public static EnumItemSlot fromName(String s) {
        EnumItemSlot[] aenumitemslot = values();
        int i = aenumitemslot.length;

        for (int j = 0; j < i; ++j) {
            EnumItemSlot enumitemslot = aenumitemslot[j];

            if (enumitemslot.getSlotName().equals(s)) {
                return enumitemslot;
            }
        }

        throw new IllegalArgumentException("Invalid slot '" + s + "'");
    }

    public static EnumItemSlot a(EnumItemSlot.Function enumitemslot_function, int i) {
        EnumItemSlot[] aenumitemslot = values();
        int j = aenumitemslot.length;

        for (int k = 0; k < j; ++k) {
            EnumItemSlot enumitemslot = aenumitemslot[k];

            if (enumitemslot.a() == enumitemslot_function && enumitemslot.b() == i) {
                return enumitemslot;
            }
        }

        throw new IllegalArgumentException("Invalid slot '" + enumitemslot_function + "': " + i);
    }

    public static enum Function {

        HAND, ARMOR;

        private Function() {}
    }
}
