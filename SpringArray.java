package mechanics_project;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * @author aram.azatyan | 4/12/2023 8:23 PM
 */
public class SpringArray {

    private enum OperationMode {
        PARALLEL, SERIES
    }

    public SpringArray() {}

    //It is given the springExpr is valid, so no need for checking for validity
    public static Spring equivalentSpring(String springExpr) {
        int arrayLength = 0;
        for (int i = 0; i < springExpr.length() - 1; i++) {
            if (isOpening(springExpr.charAt(i)) && isClosing(springExpr.charAt(i + 1))) {
                    arrayLength++;
                    i++;
            }
        }
        Spring[] arr = new Spring[arrayLength];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Spring();
        }
        return equivalentSpring(springExpr, arr);
    }

    //It is given the springExpr and springs are valid, so no need for checking for validity
    public static Spring equivalentSpring(String springExpr, Spring[] springs) {
        Deque<Double> stiffnessStack = new ArrayDeque<>();
        Deque<Character> operationModeStack = new ArrayDeque<>();
        boolean isNewGroup = false;
        int currentSpring = 0;
        for (int i = 0; i < springExpr.length(); i++) {
            char currentChar = springExpr.charAt(i);
            if (isOpening(currentChar)) {
                operationModeStack.push(currentChar);
                if (isOpening(springExpr.charAt(i + 1))) {
                    isNewGroup = true;
                }
            } else if (isClosing(currentChar) && isOpening(springExpr.charAt(i - 1))) {
                operationModeStack.poll();
                if (isNewGroup) {
                    if (!stiffnessStack.isEmpty()) {
                        stiffnessStack.push(-1.0);
                    }
                    isNewGroup = false;
                }
                stiffnessStack.push(springs[currentSpring++].getK());
            } else {
                OperationMode mode = operationModeStack.poll() == '{' ? OperationMode.SERIES : OperationMode.PARALLEL;
                mergeGroup(stiffnessStack, mode);
            }
        }
        return new Spring(stiffnessStack.poll());
    }

    private static void mergeGroup(Deque<Double> stiffnessStack, OperationMode mode) {
        double currentSpringStiffness = stiffnessStack.poll();
        while (!stiffnessStack.isEmpty()) {
            double secondSpringStiffness = stiffnessStack.poll();
            if (secondSpringStiffness == -1.0) {
                stiffnessStack.push(currentSpringStiffness);
                return;
            } else {
                currentSpringStiffness = mode == OperationMode.PARALLEL
                        ? new Spring(currentSpringStiffness).inParallel(new Spring(secondSpringStiffness)).getK()
                        : new Spring(currentSpringStiffness).inSeries(new Spring(secondSpringStiffness)).getK();
            }
        }
        stiffnessStack.push(currentSpringStiffness);
    }

    private static boolean isOpening(char c) {
        return c == '{' || c == '[';
    }

    private static boolean isClosing(char c) {
        return c == '}' || c == ']';
    }

    public static void main(String[] args) {
        Spring spring = equivalentSpring("[{[[{[]}]]}[[][]]]");
        System.out.print(spring.getK());
    }
}