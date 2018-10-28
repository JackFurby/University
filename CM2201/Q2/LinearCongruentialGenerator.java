public class LinearCongruentialGenerator implements RandomInterface {
    // Generates pseudo-random numbers using:
    // X(n+1) = (aX(n) + c) (mod m)
    // for suitable a, c and m. The numbers are "normalised" to the range
    // [0, 1) by computing X(n+1) / m.

    private long a, c, m, seed;
    // Need to be long in order to hold typical values ...

    public LinearCongruentialGenerator(long a_value, long c_value, long m_value, long s_value) {
        a = a_value;
        c = c_value;
        m = m_value;
        seed = s_value;
    }

    public LinearCongruentialGenerator(long seed) {
        // Set a, c and m to values suggested in Press, Teukolsky, et al., "Numberical Recipies"
        this(1664525, 1013904223, 4294967296l, seed);
        // NB "l" on the end is the way that a long integer can be specified. The
        // smaller ones are type-cast silently to longs, but the large number is too
        // big to fit into an ordinary int, so needs to be defined explicitly
    }

    public LinearCongruentialGenerator() {
        // (Re-)set seed to an arbitrary value, having first constructed the object using
        // zero as the seed. The point is that we don't know what m is until after it has
        // been initialised.

        this(0);
        seed = System.currentTimeMillis() % m;

    }

    public static void main(String args[]) {
        // Just a little bit of test code, to illustrate use of this class.
        RandomInterface r = new LinearCongruentialGenerator();
        for (int i = 0; i < 10; i++) {
            System.out.println(r.next());
        }
        // Since RandomInterface doesn't know about the instance variables defined in this
        // particular implementation, LinearCongruentialGenerator, we need to type-cast
        // in order to print out the parameters (primarily for "debugging" purposes).

        LinearCongruentialGenerator temp = (LinearCongruentialGenerator) r;
        System.out.println("a: " + temp.a + "  c: " + temp.c + "  m: " + temp.m + "  seed: " + temp.seed);

    }

    public double next() {
        seed = (a * seed + c) % m;
        return (double) seed / m;
    }
}
