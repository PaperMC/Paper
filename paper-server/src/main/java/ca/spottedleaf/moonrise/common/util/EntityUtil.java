package ca.spottedleaf.moonrise.common.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public final class EntityUtil {

    private static final ThreadLocal<DecimalFormat> THREE_DECIMAL_PLACES = ThreadLocal.withInitial(() -> {
        return new DecimalFormat("#,##0.000");
    });

    private static String formatVec(final Vec3 vec) {
        final DecimalFormat format = THREE_DECIMAL_PLACES.get();

        return "(" + format.format(vec.x) + "," + format.format(vec.y) + "," + format.format(vec.z) + ")";
    }

    private static String dumpEntityWithoutReferences(final Entity entity) {
        if (entity == null) {
            return "{null}";
        }

        return "{type=" + entity.getClass().getSimpleName() + ",id=" + entity.getId() + ",uuid=" + entity.getUUID() + ",pos=" + formatVec(entity.position())
            + ",mot=" + formatVec(entity.getDeltaMovement()) + ",aabb=" + entity.getBoundingBox() + ",removed=" + entity.getRemovalReason() + ",has_vehicle=" + (entity.getVehicle() != null)
            + ",passenger_count=" + entity.getPassengers().size();
    }

    public static String dumpEntity(final Entity entity) {
        final List<Entity> passengers = entity.getPassengers();
        final List<String> passengerStrings = new ArrayList<>(passengers.size());

        for (final Entity passenger : passengers) {
            passengerStrings.add("(" + dumpEntityWithoutReferences(passenger) + ")");
        }

        return "{root=[" + dumpEntityWithoutReferences(entity) + "], vehicle=[" + dumpEntityWithoutReferences(entity.getVehicle())
            + "], passengers=[" + String.join(",", passengerStrings) + "]";
    }

    private EntityUtil() {}
}
