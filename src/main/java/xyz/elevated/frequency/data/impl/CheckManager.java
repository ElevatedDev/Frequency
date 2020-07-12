package xyz.elevated.frequency.data.impl;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import lombok.Getter;
import xyz.elevated.frequency.check.Check;
import xyz.elevated.frequency.check.impl.aimassist.AimAssistA;
import xyz.elevated.frequency.check.impl.aimassist.AimAssistB;
import xyz.elevated.frequency.check.impl.aimassist.AimAssistC;
import xyz.elevated.frequency.check.impl.aimassist.AimAssistD;
import xyz.elevated.frequency.check.impl.autoclicker.*;
import xyz.elevated.frequency.check.impl.badpackets.*;
import xyz.elevated.frequency.check.impl.fly.*;
import xyz.elevated.frequency.check.impl.hitbox.HitboxA;
import xyz.elevated.frequency.check.impl.invalid.InvalidA;
import xyz.elevated.frequency.check.impl.invalid.InvalidB;
import xyz.elevated.frequency.check.impl.invalid.InvalidC;
import xyz.elevated.frequency.check.impl.invalid.InvalidD;
import xyz.elevated.frequency.check.impl.invaliddirection.InvalidDirection;
import xyz.elevated.frequency.check.impl.invalidposition.InvalidPosition;
import xyz.elevated.frequency.check.impl.inventory.InventoryA;
import xyz.elevated.frequency.check.impl.inventory.InventoryB;
import xyz.elevated.frequency.check.impl.jesus.JesusA;
import xyz.elevated.frequency.check.impl.killaura.*;
import xyz.elevated.frequency.check.impl.speed.SpeedA;
import xyz.elevated.frequency.check.impl.timer.TimerA;
import xyz.elevated.frequency.data.PlayerData;

import java.util.Collection;

@Getter
public final class CheckManager {
    private final ClassToInstanceMap<Check> checks;

    public CheckManager(final PlayerData playerData) {
        checks = new ImmutableClassToInstanceMap.Builder<Check>()
                .put(KillAuraA.class, new KillAuraA(playerData))
                .put(KillAuraB.class, new KillAuraB(playerData))
                .put(KillAuraC.class, new KillAuraC(playerData))
                .put(KillAuraD.class, new KillAuraD(playerData))
                .put(KillAuraE.class, new KillAuraE(playerData))
                .put(KillAuraF.class, new KillAuraF(playerData))
                .put(KillAuraG.class, new KillAuraG(playerData))
                .put(HitboxA.class, new HitboxA(playerData))
                .put(AimAssistA.class, new AimAssistA(playerData))
                .put(AimAssistB.class, new AimAssistB(playerData))
                .put(AimAssistC.class, new AimAssistC(playerData))
                .put(AimAssistD.class, new AimAssistD(playerData))
                .put(BadPacketsA.class, new BadPacketsA(playerData))
                .put(BadPacketsB.class, new BadPacketsB(playerData))
                .put(BadPacketsC.class, new BadPacketsC(playerData))
                .put(BadPacketsD.class, new BadPacketsD(playerData))
                .put(BadPacketsE.class, new BadPacketsE(playerData))
                .put(BadPacketsF.class, new BadPacketsF(playerData))
                .put(BadPacketsG.class, new BadPacketsG(playerData))
                .put(BadPacketsH.class, new BadPacketsH(playerData))
                .put(BadPacketsI.class, new BadPacketsI(playerData))
                .put(InvalidA.class, new InvalidA(playerData))
                .put(InvalidB.class, new InvalidB(playerData))
                .put(InvalidC.class, new InvalidC(playerData))
                .put(InvalidD.class, new InvalidD(playerData))
                .put(FlyA.class, new FlyA(playerData))
                .put(FlyB.class, new FlyB(playerData))
                .put(FlyC.class, new FlyC(playerData))
                .put(FlyD.class, new FlyD(playerData))
                .put(FlyE.class, new FlyE(playerData))
                .put(AutoClickerA.class, new AutoClickerA(playerData))
                .put(AutoClickerB.class, new AutoClickerB(playerData))
                .put(AutoClickerC.class, new AutoClickerC(playerData))
                .put(AutoClickerD.class, new AutoClickerD(playerData))
                .put(AutoClickerE.class, new AutoClickerE(playerData))
                .put(AutoClickerF.class, new AutoClickerF(playerData))
                .put(AutoClickerG.class, new AutoClickerG(playerData))
                .put(InventoryA.class, new InventoryA(playerData))
                .put(InventoryB.class, new InventoryB(playerData))
                .put(TimerA.class, new TimerA(playerData))
                .put(SpeedA.class, new SpeedA(playerData))
                .put(JesusA.class, new JesusA(playerData))
                .put(InvalidPosition.class, new InvalidPosition(playerData))
                .put(InvalidDirection.class, new InvalidDirection(playerData))
                .build();
    }

    public Collection<Check> getChecks() {
        return checks.values();
    }

    public Check<?> getCheck(final Class<? extends Check> clazz) {
        return checks.getInstance(clazz);
    }
}
