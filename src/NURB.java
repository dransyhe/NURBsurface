public class NURB implements Cloneable{
    protected Knot       u;              // Horizontal Knot Array
    protected Knot       v;              // Vertical   Knot Array
    protected ControlNet controlNet;     // Control Grid

    public NURB(Knot u, Knot v, ControlNet controlNet){
        String errorString = getClass().getName() + ":  " +
                "Number of knots in array must equal " +
                "order + number of control points";

        if (u.N != controlNet.n || v.N != controlNet.m) throw new IllegalArgumentException(errorString);
        this.u = u;
        this.v = v;
        this.controlNet = controlNet;
    }

    public Object clone() {
        try {
            NURB n      = (NURB) super.clone();  // Clone the base class
            n.u          = (Knot) u.clone();       // Clone the u knot
            n.v          = (Knot) v.clone();       // Clone the v knot
            n.controlNet = (ControlNet) controlNet.clone(); // Clone the Control Net
            return n;
        }
        catch (CloneNotSupportedException e) {
            String errorString = getClass().getName() + ":  " +
                    "CloneNotSupportedException " +
                    "for a Cloneable class";
            throw new InternalError(errorString);  // this should never happen
        }
    }

    public int getNumUKnots() { return u.K; }
    public int getNumVKnots() { return v.K; }
    public int getUOrder() { return u.order; }
    public int getVOrder() { return v.order; }
    public int getNumUControlPoints() { return controlNet.n; }
    public int getNumVControlPoints() { return controlNet.m; }
    public Knot getUKnot() { return u; }
    public Knot getVKnot() { return v; }
    public void setUKnot(Knot u) { this.u = u; }
    public void setVKnot(Knot v) { this.v = v; }
    public void setControlNet(ControlNet controlNet) { this.controlNet = controlNet; }

    public void scale(double scale) { controlNet.scale(scale); }
    public void scale(double xscale, double yscale, double zscale) { controlNet.scale(xscale, yscale, zscale); }
    public void translate(double xscale, double yscale, double zscale) { controlNet.translate(xscale, yscale, zscale); }
    public void rotate(double x, double y, double z, double theta) { controlNet.rotate(x, y, z, theta); }
    public void transpose() { Knot temp; temp = u; u = v; v = temp; controlNet.transpose(); }


    public double[] getCoordinates() {

        return controlNet.getCoordinates();
    }

    public int[] getCoordinateIndices()
    {
        return controlNet.getCoordinateIndices();
    }



}
