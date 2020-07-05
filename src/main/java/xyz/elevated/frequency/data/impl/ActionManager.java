package xyz.elevated.frequency.data.impl;

import lombok.Getter;
import xyz.elevated.frequency.observable.Observable;

@Getter
public final class ActionManager {

    /*
    We're using observables so we don't reset variables all the time which hogs performance
     */
    private final Observable<Boolean> placing = new Observable<>(false);
    private final Observable<Boolean> attacking = new Observable<>(false);
    private final Observable<Boolean> swinging = new Observable<>(false);
    private final Observable<Boolean> digging = new Observable<>(false);
    private final Observable<Boolean> delayed = new Observable<>(false);

    private long lastAttack, lastDig, lastFlying, lastDelayedFlying;

    public void onArmAnimation() {
        this.swinging.set(true);
    }

    public void onAttack() {
        this.attacking.set(true);

        this.lastAttack = System.currentTimeMillis();
    }

    public void onPlace() {
        this.placing.set(true);
    }

    public void onDig() {
        this.digging.set(true);
    }

    public void onFlying() {
        final long now = System.currentTimeMillis();

        final boolean delayed = now - lastFlying > 120L;
        final boolean digging = now - lastDig < 120;
        final boolean lagging = now - lastDelayedFlying < 120L;

        this.placing.set(false);
        this.attacking.set(false);
        this.swinging.set(false);

        this.digging.set(digging);
        this.delayed.set(lagging);

        this.lastDelayedFlying = delayed ? now : lastDelayedFlying;
        this.lastFlying = now;
    }

    public void onBukkitDig() {
        this.lastDig = System.currentTimeMillis();
    }
}
