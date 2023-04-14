package mechanics_project;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * @author aram.azatyan | 4/12/2023 8:23 PM
 */
public class SpringArray {

    private enum OperationMode {
        PARALLEL, SERIES
    }

    /**
     * It is given the springExpr is valid, so no need for checking for validity
     */
    public static Spring equivalentSpring(String springExpr) {
        Spring[] arr = constructUnitSpringArray(springExpr);
        return equivalentSpring(springExpr, arr);
    }


    /**
     * It is given the springExpr is valid, so no need for checking for validity
     */
    @SuppressWarnings("all")
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

    /**
     * It is given the springExpr is valid, so no need for checking for validity
     */
    public static Spring equivalentSpringTreeSolution(String springExpr) {
        return equivalentSpringTreeSolution(springExpr, constructUnitSpringArray(springExpr));
    }

    /**
     * It is given the springExpr is valid, so no need for checking for validity
     */
    public static Spring equivalentSpringTreeSolution(String springExpr, Spring[] springs) {
        if (isOpening(springExpr.charAt(0)) && isClosing(springExpr.charAt(1))) return springs[0]; // in case that a single spring is given
        TreeNode<Double> tree = constructSpringTree(springExpr, springs);
        return new Spring(compressSpringTree(tree));
    }

    @SuppressWarnings("all")
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

    // SERIES --- (-1), PARALLEL --- (-2)
    @SuppressWarnings("all")
    private static TreeNode<Double> constructSpringTree(String springExpr, Spring[] springs) {
        int curIndex = 0;
        String firstOperation = springExpr.charAt(0) + "";
        TreeNode<Double> root = new TreeNode<>(firstOperation.equals("{") ? -1.0 : -2.0);
        Deque<TreeNode<Double>> treeNodeStack = new ArrayDeque<>();
        treeNodeStack.push(root);
        for (int i = 1; i < springExpr.length(); i++) {
            char currentChar = springExpr.charAt(i);
            if (isOpening(currentChar) && isOpening(springExpr.charAt(i + 1))) {
                TreeNode<Double> node = new TreeNode<>(currentChar == '{' ? -1.0 : -2.0);
                treeNodeStack.peek().children.add(node);
                treeNodeStack.push(node);
            } else if (isOpening(currentChar) && isClosing(springExpr.charAt(i + 1))) {
                TreeNode<Double> node = new TreeNode<>(springs[curIndex++].getK());
                treeNodeStack.peek().children.add(node);
                i++;
            } else {
                treeNodeStack.poll();
            }
        }
        return root;
    }

    private static double compressSpringTree(TreeNode<Double> root) {
        springTreeCompressHelper(root);
        return root.val;
    }

    private static void springTreeCompressHelper(TreeNode<Double> root) {
        if (root == null || root.children.isEmpty()) return;

        if (isTerminalOperation(root)) {
            root.val = performOperation(root.children, root.val == -1 ? OperationMode.SERIES : OperationMode.PARALLEL);
            return;
        }

        for (TreeNode<Double> node : root.children) {
            if (node.val < 0) {
                springTreeCompressHelper(node);
                if (isTerminalOperation(root)) {
                    root.val = performOperation(root.children, root.val == -1
                            ? OperationMode.SERIES
                            : OperationMode.PARALLEL);
                }
            }
        }
    }

    private static boolean isTerminalOperation(TreeNode<Double> root) {
        if (root.val > 0) return false;
        for (TreeNode<Double> node : root.children) {
            if (node.val < 0) return false;
        }
        return true;
    }

    private static double performOperation(List<TreeNode<Double>> springs, OperationMode mode) {
        if (springs.size() == 1) return springs.get(0).val;
        Spring spring = new Spring(springs.get(0).val);
        for (int i = 1; i < springs.size(); i++) {
            spring = mode == OperationMode.PARALLEL
                    ? spring.inParallel(new Spring(springs.get(i).val))
                    : spring.inSeries(new Spring(springs.get(i).val));
        }
        return spring.getK();
    }

    private static boolean isOpening(char c) {
        return c == '{' || c == '[';
    }

    private static boolean isClosing(char c) {
        return c == '}' || c == ']';
    }

    private static Spring[] constructUnitSpringArray(String springExpr) {
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
        return arr;
    }

    public static void main(String[] args) {
        Spring s1 = new Spring(5);
        Spring s2 = new Spring();
        Spring s3 = new Spring(6);
        Spring s4 = new Spring(2);
        Spring s5 = new Spring(8);
        Spring s6 = new Spring(9);
        Spring s7 = new Spring();
        Spring s8 = new Spring();
        Spring s9 = new Spring(1);
        Spring s10 = new Spring(2);

        System.out.println(equivalentSpring("[]").getK());
        System.out.println(equivalentSpringTreeSolution("[]").getK());

        System.out.println(equivalentSpring("[{[]}[]{[]}]").getK());
        System.out.println(equivalentSpringTreeSolution("[{[]}[]{[]}]").getK());


        System.out.println(equivalentSpring("[{[]{[][][]}}[{[[{[][[][]]}]]}[][[][]]]]", new Spring[] {
                s1, s2, s3, s4, s5, s6, s7, s8, s9, s10
        }).getK());
        System.out.println(equivalentSpringTreeSolution("[{[]{[][][]}}[{[[{[][[][]]}]]}[][[][]]]]", new Spring[] {
                s1, s2, s3, s4, s5, s6, s7, s8, s9, s10
        }).getK());
    }
}

class TreeNode<T> {
    public T val;
    public List<TreeNode<T>> children = new ArrayList<>();

    public TreeNode(T val) {
        this.val = val;
    }

    public TreeNode(T val, List<TreeNode<T>> children) {
        this.val = val;
        this.children = children == null ? new ArrayList<>() : children;
    }
};