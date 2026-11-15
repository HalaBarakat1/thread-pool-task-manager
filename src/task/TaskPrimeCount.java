package task;

/**
 * New task: counts the prime numbers inside a range [start, end].
 *
 * Request format (POST body):  TaskPrimeCount&start,end
 * Example:                      TaskPrimeCount&1,5000000
 *
 * It is a CPU-heavy task, so it is a good example for testing
 * how the thread pool runs several requests in parallel.
 */
public class TaskPrimeCount extends TaskImpl {

    @Override
    public void execute() {
        try {
            // 1) Read the range from the input "start,end"
            String[] parts = input.split(",");
            if (parts.length != 2) {
                result = "Exception: input must be in the form start,end";
                return;
            }
            long start = Long.parseLong(parts[0].trim());
            long end   = Long.parseLong(parts[1].trim());

            if (start > end) {
                result = "Exception: start must be less than or equal to end";
                return;
            }

            // 2) Count the primes in the range
            long count = countPrimes(start, end);

            // 3) Save the result
            result = "Primes in [" + start + ", " + end + "] = " + count;

        } catch (NumberFormatException e) {
            result = "Exception: invalid number -> " + e.getMessage();
        }
    }

    /**
     * Counts how many prime numbers exist between start and end (inclusive).
     */
    private long countPrimes(long start, long end) {
        long count = 0;
        for (long n = Math.max(2, start); n <= end; n++) {
            if (isPrime(n)) {
                count++;
            }
        }
        return count;
    }

    /**
     * A number is prime if it has no divisor between 2 and its square root.
     */
    private boolean isPrime(long n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;          // even numbers (except 2) are not prime
        for (long i = 3; i * i <= n; i += 2) { // check odd divisors only
            if (n % i == 0) return false;
        }
        return true;
    }
}
