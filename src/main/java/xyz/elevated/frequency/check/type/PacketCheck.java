package xyz.elevated.frequency.check.type;

import xyz.elevated.frequency.check.Check;
import xyz.elevated.frequency.data.PlayerData;

public class PacketCheck extends Check<Object> {

    public PacketCheck(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final Object object) {
        playerData.getCheckManager().getChecks()
                .stream()
                .filter(PostCheck.class::isInstance)
                .forEach(check -> check.process(object));
    }
}
