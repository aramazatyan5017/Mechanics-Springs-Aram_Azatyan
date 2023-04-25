package mechanics_project;


/**
 * @author aram.azatyan | 4/13/2023 3:10 PM
 */
public class Converter8Bit extends Converter {

    /**
     * If less than 8 bits are provided, heading zeros are assumed
     */
    @Override
    public String constructUnitSpringSystem(Bit... bits) {
        if (bits == null || bits.length > 8) throw new IllegalArgumentException("must provide a sequence of 8 bits");
        return constructInParallel(bits);
    }

    private String constructInParallel(Bit... bits) {
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

    public static void main(String[] args) {
        Converter8Bit converter = new Converter8Bit();
        String system = converter.constructUnitSpringSystem(Bit.ONE,
                Bit.ONE, Bit.ZERO, Bit.ZERO, Bit.ONE);
        String system2 = converter.constructUnitSpringSystem();
        String system3 = converter.constructUnitSpringSystem(Bit.ONE, Bit.ONE, Bit.ONE, Bit.ONE, Bit.ONE,
                Bit.ONE, Bit.ONE, Bit.ONE);
        String system4 = converter.constructUnitSpringSystem(Bit.ONE);
        System.out.println((int) SpringArray.equivalentSpring(system).getK());
        System.out.println((int) SpringArray.equivalentSpring(system2).getK());
        System.out.println((int) SpringArray.equivalentSpring(system3).getK());
        System.out.println((int) SpringArray.equivalentSpring(system4).getK());
    }
}
