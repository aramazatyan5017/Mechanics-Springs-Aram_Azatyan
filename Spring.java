package mechanics_project;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

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

    public double[] move(double t, double dt, double x0, double v0) { return null;}

    public double[] move(double t, double dt, double x0) {
        return move(t, dt, x0, 0);
    }

    public double[] move(double t0, double t1, double dt, double x0, double v0) {
        return move(t0, t1, dt, x0, v0, 1);
    }

    public double[] move(double t0, double t1, double dt, double x0, double v0, double m) { return null;}

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
}
