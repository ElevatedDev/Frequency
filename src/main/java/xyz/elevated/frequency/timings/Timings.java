package xyz.elevated.frequency.timings;

import jdk.nashorn.internal.runtime.Timing;
import lombok.Getter;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import xyz.elevated.frequency.FrequencyAPI;

@Getter
public enum Timings {
    TICKS(FrequencyAPI.INSTANCE.getTickProcessor().getTicks()),
    TPS(MinecraftServer.getServer().recentTps[0]);

    private final double number;

    Timings(final double number) {
        this.number = number;
    }
}
