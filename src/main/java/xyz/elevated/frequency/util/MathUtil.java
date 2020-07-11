package xyz.elevated.frequency.util;

import com.google.common.collect.Lists;
import net.minecraft.server.v1_8_R3.Tuple;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class MathUtil {

    // We don't want to initialise a class that has every method declared as static
    public MathUtil() throws Exception {
        throw new Exception("You may not initialise utility classes.");
    }

    public static final double EXPANDER = Math.pow(2, 24);

    /**
     *
     * @param data - The set of data you want to find the variance from
     * @return - The variance of the numbers.
     *
     * @See - https://en.wikipedia.org/wiki/Variance
     */
    public static double getVariance(final Iterable<? extends Number> data) {
        int count = 0;

        double sum = 0.0;
        double variance = 0.0;

        double average;

        // Increase the sum and the count to find the average and the standard deviation
        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;
        }

        average = sum / count;

        // Run the standard deviation formula
        for (final Number number : data) {
            variance += Math.pow(number.doubleValue() - average, 2.0);
        }

        return variance;
    }

    /**
     * @param data - The set of numbers / data you want to find the standard deviation from.
     * @return - The standard deviation using the square root of the variance.
     *
     * @See - https://en.wikipedia.org/wiki/Standard_deviation
     * @See - https://en.wikipedia.org/wiki/Variance
     */
    public static double getStandardDeviation(final Iterable<? extends Number> data) {
        final double variance = getVariance(data);

        // The standard deviation is the square root of variance. (sqrt(s^2))
        return Math.sqrt(variance);
    }

    /**
     *
     * @param data - The set of numbers / data you want to find the skewness from
     * @return - The skewness running the standard skewness formula.
     *
     * @See - https://en.wikipedia.org/wiki/Skewness
     */
    public static double getSkewness(Iterable<? extends Number> data) {
        double sum = 0;
        int count = 0;

        final List<Double> numbers = Lists.newArrayList();

        // Get the sum of all the data and the amount via looping
        for (final Number number : data) {
            sum += number.doubleValue();
            ++count;

            numbers.add(number.doubleValue());
        }

        // Sort the numbers to run the calculations in the next part
        Collections.sort(numbers);

        // Run the formula to get skewness
        final double mean =  sum / count;
        final double median = (count % 2 != 0) ? numbers.get(count / 2) : (numbers.get((count - 1) / 2) + numbers.get(count / 2)) / 2;
        final double variance = getVariance(data);

        return 3 * (mean - median) / variance;
    }

    /**
     * @param - The collection of numbers you want analyze
     * @return - A pair of the high and low outliers
     *
     * @See - https://en.wikipedia.org/wiki/Outlier
     */
    public static Pair<List<Double>, List<Double>> getOutliers(final Collection<? extends Number> collection) {
        final List<Double> values = new ArrayList<>();

        for (final Number number : collection) {
            values.add(number.doubleValue());
        }

        final double q1 = getMedian(values.subList(0, values.size() / 2));
        final double q3 = getMedian(values.subList(values.size() / 2, values.size()));

        final double iqr = Math.abs(q1 - q3);
        final double lowThreshold = q1 - 1.5 * iqr, highThreshold = q3 + 1.5 * iqr;

        final Pair<List<Double>, List<Double>> tuple = new Pair<>(new ArrayList<>(), new ArrayList<>());

        for (final Double value : values) {
            if (value < lowThreshold) {
                tuple.getX().add(value);
            }
            else if (value > highThreshold) {
                tuple.getY().add(value);
            }
        }

        return tuple;
    }

    /**
     *
     * @param data - The set of numbers/data you want to get the kurtosis from
     * @return - The kurtosis using the standard kurtosis formula
     *
     * @See - https://en.wikipedia.org/wiki/Kurtosis
     */
    public static double getKurtosis(final Iterable<? extends Number> data) {
        double sum = 0.0;
        int count = 0;

        for (Number number : data) {
            sum += number.doubleValue();
            ++count;
        }

        if (count < 3.0) {
            return 0.0;
        }

        final double efficiencyFirst = count * (count + 1.0) / ((count - 1.0) * (count - 2.0) * (count - 3.0));
        final double efficiencySecond = 3.0 * Math.pow(count - 1.0, 2.0) / ((count - 2.0) * (count - 3.0));
        final double average = sum / count;

        double variance = 0.0;
        double varianceSquared = 0.0;

        for (final Number number : data) {
            variance += Math.pow(average - number.doubleValue(), 2.0);
            varianceSquared += Math.pow(average - number.doubleValue(), 4.0);
        }

        return efficiencyFirst * (varianceSquared / Math.pow(variance / sum, 2.0)) - efficiencySecond;
    }

    /**
     * @param data - The data you want the median from
     * @return - The middle number of that data
     *
     * @See - https://en.wikipedia.org/wiki/Median
     */
    private static double getMedian(final List<Double> data) {
        if (data.size() % 2 == 0) {
            return (data.get(data.size() / 2) + data.get(data.size() / 2 - 1)) / 2;
        } else {
            return data.get(data.size() / 2);
        }
    }

    /**
     *
     * @param current - The current value
     * @param previous - The previous value
     * @return - The GCD of those two values
     */
    public static long getGcd(long current, long previous) {
        return (previous <= 16384L) ? current : getGcd(previous, current % previous);
    }

    /**
     * @param from - The last location
     * @param to - The current location
     * @return - The horizontal distance using (x^2 + z^2)
     */
    public static double getMagnitude(final Location from, final Location to) {
        if (from.getWorld() != to.getWorld()) return 0.0;

        final double deltaX = to.getX() - from.getX();
        final double deltaZ = to.getZ() - from.getZ();

        return (deltaX * deltaX + deltaZ * deltaZ);
    }

    /**
     * @param player - The player you want to read the effect from
     * @param effect - The potion effect you want to get the amplifier of
     * @return - The amplifier added by one to make things more readable
     */
    public static int getPotionLevel(final Player player, final PotionEffectType effect) {
        final int effectId = effect.getId();

        if (!player.hasPotionEffect(PotionEffectType.SPEED)) return 0;

        return player.getActivePotionEffects().stream().filter(potionEffect -> potionEffect.getType().getId() == effectId).map(PotionEffect::getAmplifier).findAny().orElse(0) + 1;
    }

    /**
     *
     * @param data - The sample of clicks you want to get the cps from
     * @return - The cps using the average as a method of calculation
     */
    public static double getCps(final Iterable<? extends Number> data) {
        double sum = 0;
        int count = 0;

        for (final Number number : data) {
            ++count;
            sum += number.doubleValue();
        }

        final double average = sum / count;

        return 20 / average;
    }
}
