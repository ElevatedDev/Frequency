package xyz.elevated.frequency;

import org.bukkit.plugin.java.JavaPlugin;

public final class FrequencyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        FrequencyAPI.INSTANCE.start(this);
    }

    @Override
    public void onDisable() {
        FrequencyAPI.INSTANCE.stop(this);
    }
}
