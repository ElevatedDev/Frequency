package xyz.elevated.frequency.timings;

import lombok.Getter;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import xyz.elevated.frequency.Frequency;

@Getter
public enum Timings {
    TICKS(Frequency.INSTANCE.getTickProcessor().getTicks()),
    TPS(MinecraftServer.getServer().recentTps[0]);

    private final double number;

    Timings(final double number) {
        this.number = number;
    }
}
