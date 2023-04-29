package mechanics_project;

/**
 * @author aram.azatyan | 4/13/2023 3:11 PM
 */
public class ConverterFloat extends Converter {

    // need to use another method for floating point calculations, hence artificially invalidating these methods
    @Override
    public String constructUnitSpringSystem(Bit... bits) {
        throw new UnsupportedOperationException();
    }

    // need to use another method for floating point calculations, hence artificially invalidating these methods
    @Override
    public int evaluateDecimalValue(double t, double dt, double x0, Bit... bits) {
        throw new UnsupportedOperationException();
    }

    public int evaluateDecimalValueFloat(double t, double dt, double x0, Bit[] wholePart, Bit[] floatingPointPart) {
        double[] positions = computeSpringSystemOscillations(
                constructUnitSpringSystem(wholePart, floatingPointPart), t, dt, x0);
        return computeFrequencyAmplitudes(positions, dt);
    }

    public String constructUnitSpringSystem(Bit[] wholePart, Bit[] floatingPointPart) {
        return constructInParallelFloat(wholePart, floatingPointPart);
    }

    // can't use varargs here, since java won't allow more than 1 vararg arguments in a method, hence arrays
    /* since the other utility methods work for unit springs, I must take the integer part of the
    *  floating point number, hence I print the obtained floating point number in the method,
    *  just to show that it is possible to convert given bit sequences into a floating point number.
    */
    private String constructInParallelFloat(Bit[] wholePart, Bit[] floatingPointPart) {
        double floatingPointNumber = 0;
        int rank = 0;
        for (int i = wholePart.length - 1; i >= 0; i--) {
            if (wholePart[i] == Bit.ONE) {
                floatingPointNumber += Math.pow(2, rank++);
            } else {
                rank++;
            }
        }
        rank = 1;
        for (Bit bit : floatingPointPart) {
            if (bit == Bit.ONE) {
                floatingPointNumber += Math.pow(2, -rank);
            } else {
                rank++;
            }
        }
        System.out.println("The decimal value of the floating point number is " + floatingPointNumber);
        return constructInParallel(wholePart);
    }

    public static void main(String[] args) {
        ConverterFloat converter = new ConverterFloat();
        Bit[] wholePart = {Bit.ONE, Bit.ONE, Bit.ZERO, Bit.ZERO, Bit.ONE};
        Bit[] floatingPart = {Bit.ZERO, Bit.ONE};
        System.out.println(converter.evaluateDecimalValueFloat(10, 0.01, 0.1, wholePart, floatingPart));
    }
}
