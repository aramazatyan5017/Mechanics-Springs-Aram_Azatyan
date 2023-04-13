package mechanics_project;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;

/**
 * @author aram.azatyan | 4/12/2023 8:00 PM
 */
public class Spring {

    private double k;

    public Spring() {
        k = 1;
    }

    public Spring(double k) throws IllegalArgumentException {
        if (k <= 0) throw new IllegalArgumentException("Unobtainable Stiffness");
        this.k = k;
    }

    public double[] move(double t, double dt, double x0, double v0) {
        if (t < 0 || dt <= 0) throw new IllegalArgumentException("invalid time argument(s)");
        int arrayLength = ((int) (t / dt)) + 1; // initial position x0 will also be included in the returned array
        t = 0;
        return getPositionArray(arrayLength, x0, v0, t, dt, 1);
    }

    public double[] move(double t, double dt, double x0) {
        return move(t, dt, x0, 0);
    }

    public double[] move(double t0, double t1, double dt, double x0, double v0, double m) {
        if (t0 < 0 || t1 < 0 || dt <= 0 || t0 > t1) throw new IllegalArgumentException("negative time");
        int arrayLength = ((int) ((t1 - t0) / dt)) + 1; // initial position x0 will also be included in the returned array
        double t = t0;
        return getPositionArray(arrayLength, x0, v0, t, dt, m);
    }

    public double[] move(double t0, double t1, double dt, double x0, double v0) {
        return move(t0, t1, dt, x0, v0, 1);
    }

    private double[] getPositionArray(int arrayLength, double x0, double v0, double t, double dt, double m) {
        double[] positions = new double[arrayLength];

        double omega = calculateOmega(this.getK(), m);
        double amplitude = v0 == 0 ? x0 : calculateAmplitude(x0, v0 / (omega * -1));
        double phaseShift = v0 == 0 ? 0 : calculatePhaseShift(x0, v0 / (omega * -1));

        for (int i = 0; i < arrayLength; i++) {
            positions[i] = positionOfHarmonicMotion(t, amplitude, omega, phaseShift);
            t += dt;
        }

        return positions;
    }

    private double positionOfHarmonicMotion(double t, double amplitude, double omega, double phaseShift) {
        return amplitude * Math.cos(omega * t + phaseShift);
    }

    private double calculateOmega(double k, double m) {
        return Math.sqrt(k / m);
    }

    private double calculateAmplitude(double AcosPhi, double AsinPhi) {
        return Math.sqrt(Math.pow(AcosPhi, 2) + Math.pow(AsinPhi, 2));
    }

    private double calculatePhaseShift(double AcosPhi, double AsinPhi) {
        return Math.atan(AsinPhi / AcosPhi);
    }

    public Spring inSeries(Spring that) {
        if (that == null) return null;
        BigDecimal k1 = new BigDecimal(this.k + "");
        BigDecimal k2 = new BigDecimal(that.k + "");
        double keq = k1.multiply(k2).divide(new BigDecimal(this.k + that.k + ""),
                new MathContext(3, RoundingMode.FLOOR)).doubleValue();
        return new Spring(keq);
    }

    public Spring inParallel(Spring that) {
        if (that == null) return null;
        BigDecimal k1 = new BigDecimal(this.k + "");
        BigDecimal k2 = new BigDecimal(that.k + "");
        return new Spring(k1.add(k2).doubleValue());
    }

    public double getK() {
        return k;
    }

    private void setK(double k) {
        this.k = k;
    }

    public static void main(String[] args) {
        System.out.println(Arrays.toString(new Spring(9).move(13, 23, 2, 0.1, 0.85, 0.2)));
        System.out.println(Arrays.toString(new Spring(9).move(10, 2.5, 0.1, 0.85)));
        System.out.println(Arrays.toString(new Spring(9).move(10, 2.5, 0.1)));
        System.out.println(Arrays.toString(new Spring(9).move(13, 23, 2, 0.1, 0.85)));
    }
}
