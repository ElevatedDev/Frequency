package xyz.elevated.frequency.check.impl.aimassist.cinematic;

import com.google.common.collect.Lists;
import xyz.elevated.frequency.check.CheckData;
import xyz.elevated.frequency.check.type.RotationCheck;
import xyz.elevated.frequency.data.PlayerData;
import xyz.elevated.frequency.update.RotationUpdate;
import xyz.elevated.frequency.util.GraphUtil;

import java.util.ArrayList;
import java.util.List;

@CheckData(name = "Cinematic")
public final class Cinematic extends RotationCheck {
    private long lastSmooth, lastHighRate;
    private double lastDeltaYaw, lastDeltaPitch;

    private final List<Double> yawSamples = Lists.newArrayList();
    private final List<Double> pitchSamples = Lists.newArrayList();

    public Cinematic(final PlayerData playerData) {
        super(playerData);
    }

    @Override
    public void process(final RotationUpdate rotationUpdate) {
        final long now = System.currentTimeMillis();

        final double deltaYaw = rotationUpdate.getDeltaYaw();
        final double deltaPitch = rotationUpdate.getDeltaPitch();

        final double differenceYaw = Math.abs(deltaYaw - lastDeltaYaw);
        final double differencePitch = Math.abs(deltaPitch - lastDeltaPitch);

        final double joltYaw = Math.abs(differenceYaw - deltaYaw);
        final double joltPitch = Math.abs(differencePitch - deltaPitch);

        final boolean cinematic = (now - lastHighRate > 250L) || now - lastSmooth < 9000L;

        if (joltYaw > 1.0 && joltPitch > 1.0) {
            this.lastHighRate = now;
        }

        if (deltaPitch > 0.0 && deltaPitch > 0.0) {
            yawSamples.add(deltaYaw);
            pitchSamples.add(deltaPitch);
        }

        if (yawSamples.size() == 20 && pitchSamples.size() == 20) {
            // Get the cerberus/positive graph of the sample-lists
            final GraphUtil.GraphResult resultsYaw = GraphUtil.getGraph(yawSamples);
            final GraphUtil.GraphResult resultsPitch = GraphUtil.getGraph(pitchSamples);

            // Negative values
            final int negativesYaw = resultsYaw.getNegatives();
            final int negativesPitch = resultsPitch.getNegatives();

            // Positive values
            final int positivesYaw = resultsYaw.getPositives();
            final int positivesPitch = resultsPitch.getPositives();

            // Cinematic camera usually does this on *most* speeds and is accurate for the most part.
            if (positivesYaw > negativesYaw || positivesPitch > negativesPitch) {
                this.lastSmooth = now;
            }

            yawSamples.clear();
            pitchSamples.clear();
        }

        playerData.getCinematic().set(cinematic);

        this.lastDeltaYaw = deltaYaw;
        this.lastDeltaPitch = deltaPitch;
    }
}
