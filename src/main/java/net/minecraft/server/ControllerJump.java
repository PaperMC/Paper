package net.minecraft.server;

public class ControllerJump {

    private final EntityInsentient b;
    protected boolean a;

    public ControllerJump(EntityInsentient entityinsentient) {
        this.b = entityinsentient;
    }

    public void jump() {
        this.a = true;
    }

    public final void jumpIfSet() { this.b(); } // Paper - OBFHELPER
    public void b() {
        this.b.setJumping(this.a);
        this.a = false;
    }
}
