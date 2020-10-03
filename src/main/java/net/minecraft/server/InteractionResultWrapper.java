package net.minecraft.server;

public class InteractionResultWrapper<T> {

    private final EnumInteractionResult a;
    private final T b;

    public InteractionResultWrapper(EnumInteractionResult enuminteractionresult, T t0) {
        this.a = enuminteractionresult;
        this.b = t0;
    }

    public EnumInteractionResult a() {
        return this.a;
    }

    public T b() {
        return this.b;
    }

    public static <T> InteractionResultWrapper<T> success(T t0) {
        return new InteractionResultWrapper<>(EnumInteractionResult.SUCCESS, t0);
    }

    public static <T> InteractionResultWrapper<T> consume(T t0) {
        return new InteractionResultWrapper<>(EnumInteractionResult.CONSUME, t0);
    }

    public static <T> InteractionResultWrapper<T> pass(T t0) {
        return new InteractionResultWrapper<>(EnumInteractionResult.PASS, t0);
    }

    public static <T> InteractionResultWrapper<T> fail(T t0) {
        return new InteractionResultWrapper<>(EnumInteractionResult.FAIL, t0);
    }

    public static <T> InteractionResultWrapper<T> a(T t0, boolean flag) {
        return flag ? success(t0) : consume(t0);
    }
}
