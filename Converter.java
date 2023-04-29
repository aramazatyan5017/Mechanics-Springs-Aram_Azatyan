package mechanics_project;


/**
 * @author aram.azatyan | 4/13/2023 3:09 PM
 */
public abstract class Converter {

    public abstract String constructUnitSpringSystem(Bit... bits);

    public double[] computeSpringSystemOscillations(String springSystem, double t, double dt, double x0) {
        Spring effSpring = SpringArray.equivalentSpringTreeSolution(springSystem);
        return effSpring.move(t, dt, x0);
    }

    public int computeFrequencyAmplitudes(double[] positions, double dt) {
        return FT.transform(positions, dt);
    }

    public abstract int evaluateDecimalValue(double t, double dt, double x0, Bit... bits);

    protected String constructInParallel(Bit... bits) {
        int rank = 0;
        StringBuilder system = new StringBuilder();
        system.append('[');
        for (int i = bits.length - 1; i >= 0; i--) {
            if (bits[i] == Bit.ZERO) {
                rank++;
                continue;
            }
            system.append('[');
            int quantity = (int) Math.pow(2, rank);
            while (quantity-- > 0) {
                system.append("[]");
            }
            system.append(']');
            rank++;
        }
        system.append(']');
        return system.toString();
    }

}
