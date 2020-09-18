public class Knot {
    public static final double EPSILON = 1.0E-7f;

    protected int order;
    protected int N;          // number of control points
    protected int K;          // number of knots
    protected double[] knots;

    public Knot(int order, int N){
        this.order = order;
        this.N = N;
        this.K = order + N;
    }

    public Knot(int order, int N, double[] knots) throws IllegalArgumentException{
        if (knots.length != order + N) throw new IllegalArgumentException("wrong length of knots");
        this.order = order;
        this.N = N;
        this.K = knots.length;
        this.knots = new double[this.K];
        for (int i = 0; i < this.K; i ++) this.knots[i] = knots[i];
    }

    public int getOrder(){ return this.order;}

    public int getN(){ return this.N;}

    public int getK(){ return this.K;}

    public double[] getKnots(){return knots;}

    public void setKnots(double[] knots){
        if (knots.length != order + N) throw new IllegalArgumentException("wrong length of knots");
        this.K = knots.length;
        this.knots = new double[this.K];
        for (int i = 0; i < this.K; i ++) this.knots[i] = knots[i];
    }

    // determine where in the knot sequence t lies
    protected static int interval(double[] knots, double t) throws IllegalArgumentException{
        if (knots[0] <= t) {
            for (int i = 0; i < knots.length - 1; i ++)
                if (t < knots[i+1]) return i;
        }
        throw new IllegalArgumentException("t not in knot sequence");
    }

    // produce knots between tmin and tmax
    // with tmin and tmax appear K times at two ends, then distribute the rest uniformly
    protected void makeKnots(double tmin, double tmax){
        knots = new double[K];
        for (int i = 0; i < order; i++) {
            knots[i] = tmin;
            knots[i + N] = tmax;
        }
        double interval = (tmax - tmin) / (K - 2 * order + 1);
        for (int i = order; i < N; i++)
            knots[i] = tmin + (i - order + 1) * interval;
    }

    private boolean equal(double one, double two) {
        return ((float) Math.abs(one - two) <= EPSILON);
    }

    // union of two knots
    protected Knot unionKnots(Knot knot2) {
        boolean done = false;
        int numNewKnots = 0;
        int i1  = 0;
        int i2  = 0;
        double t;

        double[] newknot = new double[this.K + knot2.K];

        while (!done) {
            if (equal(this.knots[i1], knot2.knots[i2])) {
                t = this.knots[i1];
                i1++;
                i2++;
            }
            else {
                if (this.knots[i1] < knot2.knots[i2]) {
                    t = this.knots[i1];
                    i1++;
                }
                else {
                    t = knot2.knots[i2];
                    i2++;
                }
            }
            newknot[numNewKnots] = t;   // store knot parameter
            numNewKnots++;
            done = (i1 >= this.K || i2 >= knot2.K);
        }

        if (i1 < this.K) {
            for (int i=i1; i<this.K; i++) {
                newknot[numNewKnots] = this.knots[i];
                numNewKnots++;
            }
        }
        else {
            for (int i=i2; i<knot2.K; i++) {
                newknot[numNewKnots] = knot2.knots[i];
                numNewKnots++;
            }
        }
        double[] temp = new double[numNewKnots];
        System.arraycopy(newknot, 0, temp, 0, numNewKnots);
        return new Knot(order, numNewKnots - order, temp);
    }

    public Object clone() {
        try {
            Knot klone = (Knot) super.clone();
            System.arraycopy(knots, 0,   klone.knots, 0, K);
            return klone;
        }
        catch (CloneNotSupportedException e) {
            String errorString = getClass().getName() + ":  " +
                    "CloneNotSupportedException " +
                    "for a Cloneable class";
            throw new InternalError(errorString);  // this should never happen
        }
    }


}
