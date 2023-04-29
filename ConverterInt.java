package mechanics_project;

/**
 * @author aram.azatyan | 4/13/2023 3:11 PM
 */
public class ConverterInt extends Converter {

    @Override
    public String constructUnitSpringSystem(Bit... bits) {
        return constructInParallel(bits);
    }

    @Override
    public int evaluateDecimalValue(double t, double dt, double x0, Bit... bits) {
        double[] positions = computeSpringSystemOscillations(constructUnitSpringSystem(bits), t, dt, x0);
        return computeFrequencyAmplitudes(positions, dt);
    }

    public static void main(String[] args) {
        ConverterInt converter = new ConverterInt();
        String system = converter.constructUnitSpringSystem(Bit.ONE,
                Bit.ONE, Bit.ZERO, Bit.ZERO, Bit.ONE);
        String system2 = converter.constructUnitSpringSystem();
        String system3 = converter.constructUnitSpringSystem(Bit.ONE, Bit.ONE, Bit.ONE, Bit.ONE, Bit.ONE,
                Bit.ONE, Bit.ONE, Bit.ONE, Bit.ZERO, Bit.ONE);
        String system4 = converter.constructUnitSpringSystem(Bit.ONE);
        System.out.println((int) SpringArray.equivalentSpring(system).getK());
        System.out.println((int) SpringArray.equivalentSpring(system2).getK());
        System.out.println((int) SpringArray.equivalentSpring(system3).getK());
        System.out.println((int) SpringArray.equivalentSpring(system4).getK());
        System.out.println(converter.evaluateDecimalValue(10, 0.01, 0.1,
                Bit.ONE,
                Bit.ONE, Bit.ZERO, Bit.ZERO, Bit.ONE));
    }
}
