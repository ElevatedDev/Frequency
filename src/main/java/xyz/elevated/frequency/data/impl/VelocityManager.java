package xyz.elevated.frequency.data.impl;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class VelocityManager {
    // This is where we will store all the velocity data
    @Getter
    private final List<VelocitySnapshot> velocities;

    public VelocityManager() {
        velocities = new ArrayList<>();
    }

    // Remove any old and unnecessary velocity occurrences
    private final Predicate<VelocitySnapshot> shouldRemoveVelocity = velocity -> velocity.getCreationTime() + 2000L < System.currentTimeMillis();

    // Add the player's velocity to the list
    public void addVelocityEntry(final double x, final double y, final double z) {
        velocities.add(new VelocitySnapshot(x, y, z, x * x + z * z, Math.abs(y)));
    }

    // Get the highest horizontal velocity
    public double getMaxHorizontal() {
        try {
            return Math.sqrt(velocities.stream()
                    .mapToDouble(VelocitySnapshot::getHorizontal)
                    .max()
                    .orElse(0.0));
        } catch (Exception e) {
            return 1.0;
        }
    }

    // Get the highest vertical velocity
    public double getMaxVertical() {
        try {
            return velocities.stream()
                    .mapToDouble(VelocitySnapshot::getVertical)
                    .max()
                    .orElse(0.f);
        } catch (Exception e) {
            return 1.0;
        }
    }

    public void apply() {
        velocities.removeIf(shouldRemoveVelocity);
    }

    // We do not want to put a getter here as we don't want a getter for the entire class including a getter for the class itself
    private class VelocitySnapshot {
        @Getter
        private final double horizontal, vertical;
        @Getter
        private final double x, y, z;
        @Getter
        private final long creationTime = System.currentTimeMillis();

        public VelocitySnapshot(final double x, final double y, final double z, final double horizontal, final double vertical) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.horizontal = horizontal;
            this.vertical = vertical;
        }
    }
}
