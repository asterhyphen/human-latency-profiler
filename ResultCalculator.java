import java.util.List;

/**
 * Utility class for calculating statistics from reaction time data.
 */
public class ResultCalculator {

    /**
     * Calculates the average reaction time in milliseconds.
     */
    public static double average(List<Long> times) {
        if (times.isEmpty()) return 0.0;
        return times.stream().mapToLong(Long::longValue).average().orElse(0.0);
    }

    /**
     * Finds the minimum (fastest) reaction time in milliseconds.
     */
    public static long min(List<Long> times) {
        if (times.isEmpty()) return 0;
        return times.stream().mapToLong(Long::longValue).min().orElse(0);
    }

    /**
     * Finds the maximum (slowest) reaction time in milliseconds.
     */
    public static long max(List<Long> times) {
        if (times.isEmpty()) return 0;
        return times.stream().mapToLong(Long::longValue).max().orElse(0);
    }
}