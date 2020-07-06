package xyz.elevated.frequency.util;

public final class MathUtil {

    // We don't want to initialise a class that has every method declared as static
    public MathUtil() throws Exception {
        throw new Exception("You may not initialise utility classes.");
    }

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
}
