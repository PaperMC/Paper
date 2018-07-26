package io.papermc.paper.math;

import org.jspecify.annotations.NullMarked;

@NullMarked
record RotationsImpl(double x, double y, double z) implements Rotations {

    @Override
    public RotationsImpl withX(final double x) {
        return new RotationsImpl(x, this.y, this.z);
    }

    @Override
    public RotationsImpl withY(final double y) {
        return new RotationsImpl(this.x, y, this.z);
    }

    @Override
    public RotationsImpl withZ(final double z) {
        return new RotationsImpl(this.x, this.y, z);
    }

    @Override
    public RotationsImpl add(final double x, final double y, final double z) {
        return new RotationsImpl(this.x + x, this.y + y, this.z + z);
    }

}
