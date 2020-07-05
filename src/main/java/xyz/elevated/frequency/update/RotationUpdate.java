package xyz.elevated.frequency.update;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor @Getter @Setter
public final class RotationUpdate {
    private float deltaYaw, deltaPitch;
}
