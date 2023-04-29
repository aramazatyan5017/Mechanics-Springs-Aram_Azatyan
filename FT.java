package mechanics_project;

import java.util.HashMap;
import java.util.Map;

/**
 * @author aram.azatyan | 4/13/2023 3:12 PM
 */
public class FT {

    public static int transform(double[] positions, double deltaT) {
        int N = positions.length;
        Map<Integer, double[]> resultMap = new HashMap<>();
        for (int j = 0; j < N; j++) {
            resultMap.put(j, new double[]
                    {computeCoefficient(N, positions, j, deltaT, true),
                     computeCoefficient(N, positions, j, deltaT, false)});
        }
        return (int) Math.pow(determineMaxJ(resultMap), 2);
    }

    private static double computeCoefficient(int N, double[] positions, int index, double deltaT, boolean isA) {
        double coefficient = 0;
        for (int i = 0; i < positions.length; i++) {
            coefficient += positions[i] * (isA
                    ? Math.cos(index * i * deltaT)
                    : Math.sin(index * i * deltaT));
        }
        return (2.0 / N) * coefficient;
    }

    private static int determineMaxJ(Map<Integer, double[]> map) {
        int currentJ = 0;
        double currentVal = 0;
        for (Map.Entry<Integer, double[]> entry : map.entrySet()) {
            double a = entry.getValue()[0];
            double b = entry.getValue()[1];
            int j = entry.getKey();
            double val = Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
            if (val > currentVal) {
                currentVal = val;
                currentJ = j;
            }
        }
        return currentJ;
    }

    public static void main(String[] args) {
        System.out.println(transform(new Spring(25).move(10, 0.1, 0.1), 0.1));
        System.out.println(transform(new Spring(9).move(10, 0.01, 0.1), 0.01));
        System.out.println(transform(new Spring(4).move(10, 0.02, 0.1), 0.02));
        //for complete square stiffness, the program works correctly
    }
}
