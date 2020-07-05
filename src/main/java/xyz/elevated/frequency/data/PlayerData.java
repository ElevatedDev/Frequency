package xyz.elevated.frequency.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.elevated.frequency.check.type.PositionCheck;
import xyz.elevated.frequency.data.impl.CheckManager;
import xyz.elevated.frequency.data.impl.PositionManager;
import xyz.elevated.frequency.data.impl.RotationManager;
import xyz.elevated.frequency.exempt.ExemptManager;
import xyz.elevated.frequency.observable.Observable;
import xyz.elevated.frequency.update.PositionUpdate;
import xyz.elevated.frequency.update.RotationUpdate;
import xyz.elevated.frequency.util.EvictingList;

@Getter @Setter
public final class PlayerData {
    private final Player bukkitPlayer;

    private final EvictingList<BoundingBox> boundingBoxes = new EvictingList<>(10);
    private final EvictingList<Location> locationsSent = new EvictingList<>(10);

    private final Observable<BoundingBox> boundingBox = new Observable<>(new BoundingBox(0, 0, 0));

    private final RotationUpdate rotationUpdate = new RotationUpdate(0, 0);
    private final PositionUpdate positionUpdate = new PositionUpdate(null, null);

    private final PositionManager positionManager = new PositionManager(this);
    private final RotationManager rotationManager = new RotationManager(this);
    private final CheckManager checkManager = new CheckManager(this);
    private final ExemptManager exceptManager = new ExemptManager(this);

    public PlayerData(final Player bukkitPlayer) {
        this.bukkitPlayer = bukkitPlayer;
    }
}
