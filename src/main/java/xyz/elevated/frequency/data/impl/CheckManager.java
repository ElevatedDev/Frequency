package xyz.elevated.frequency.data.impl;

import com.google.common.collect.ClassToInstanceMap;
import com.google.common.collect.ImmutableClassToInstanceMap;
import lombok.Getter;
import xyz.elevated.frequency.check.Check;
import xyz.elevated.frequency.check.impl.aimassist.AimAssistA;
import xyz.elevated.frequency.check.impl.killaura.*;
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
                .put(AimAssistA.class, new AimAssistA(playerData))
                .build();
    }

    public Collection<Check> getChecks() {
        return checks.values();
    }

    public Check<?> getCheck(final Class<? extends Check> clazz) {
        return checks.getInstance(clazz);
    }
}
