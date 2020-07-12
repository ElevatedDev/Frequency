package xyz.elevated.frequency;

import org.bukkit.plugin.java.JavaPlugin;

public final class FrequencyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        Frequency.INSTANCE.start(this);
    }

    @Override
    public void onDisable() {
        Frequency.INSTANCE.stop(this);
    }
}
