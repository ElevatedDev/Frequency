package xyz.elevated.frequency.data.impl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.observable.Observable;

@Getter @RequiredArgsConstructor
public final class ActionManager {
    private final PlayerData playerData;

    /*
    We're using observables so we don't reset variables all the time which hogs performance
     */
    private final Observable<Boolean> placing = new Observable<>(false);
    private final Observable<Boolean> attacking = new Observable<>(false);
    private final Observable<Boolean> swinging = new Observable<>(false);
    private final Observable<Boolean> digging = new Observable<>(false);
    private final Observable<Boolean> delayed = new Observable<>(false);
    private final Observable<Boolean> teleported = new Observable<>(false);
    private final Observable<Boolean> steer = new Observable<>(false);

    private int lastAttack, lastDig, lastFlying, lastDelayedFlying, lastTeleport;

    public void onArmAnimation() {
        this.swinging.set(true);
    }

    public void onAttack() {
        this.attacking.set(true);

        this.lastAttack = playerData.getTicks().get();
    }

    public void onPlace() {
        this.placing.set(true);
    }

    public void onDig() {
        this.lastDig = playerData.getTicks().get();
    }

    public void onFlying() {
        final int now = playerData.getTicks().get();

        final boolean delayed = now - lastFlying > 2;
        final boolean digging = now - lastDig < 8;
        final boolean lagging = now - lastDelayedFlying < 2;
        final boolean teleporting = now - lastTeleport < 2;

        this.placing.set(false);
        this.attacking.set(false);
        this.swinging.set(false);
        this.attacking.set(false);
        this.steer.set(false);

        this.digging.set(digging);
        this.delayed.set(lagging);
        this.teleported.set(teleporting);

        this.lastDelayedFlying = delayed ? now : lastDelayedFlying;
        this.lastFlying = now;

        playerData.getTicks().set(now + 1);
    }

    public void onSteerVehicle() {
        this.steer.set(true);
    }

    public void onTeleport() {
        this.lastTeleport = playerData.getTicks().get();
    }

    public void onBukkitDig() {
        this.lastDig = playerData.getTicks().get();
    }
}
