public class ControlNet implements Cloneable{
    protected int n;             // number of control points in the horizontal direction
    protected int m;             // number of control points in the vertical direction
    private Vector4[][] controlPts;

    public ControlNet(int n, int m, Vector4[][] controlPts) {
        this.n = n;
        this.m = m;
        this.controlPts = controlPts;
    }

    public ControlNet(int n, int m) {
        this(n, m, new Vector4[n][m]);
    }

    // transpose controlPts by swapping n, m
    public void transpose() {
        Vector4[][] temp = new Vector4[n][m];
        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < m; j ++) {
                temp[j][i] = controlPts[i][j];
            }
        }
        controlPts = temp;
        int value = n;
        n = m;
        m = value;
    }

    public void scale(double factor) {
        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < m; j ++) {
                controlPts[i][j].scale(factor);
            }
        }
    }

    public void scale(double xfactor, double yfactor, double zfactor) {
        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < m; j ++) {
                controlPts[i][j].scale(xfactor, yfactor, zfactor);
            }
        }
    }

    public void translate(double xfactor, double yfactor, double zfactor) {
        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < m; j ++) {
                controlPts[i][j].translate(xfactor, yfactor, zfactor);
            }
        }
    }

    public void rotate(double xfactor, double yfactor, double zfactor, double angle) {
        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < m; j ++) {
                controlPts[i][j].rotate(xfactor, yfactor, zfactor, angle);
            }
        }
    }

    public Object clone() {
        try {
            ControlNet newnet = (ControlNet) super.clone();
            System.arraycopy(controlPts, 0, newnet.controlPts, 0,  n*m);
            return newnet;
        }
        catch (CloneNotSupportedException e) {
            String errorString = getClass().getName() + ":  " +
                    "CloneNotSupportedException " +
                    "for a Cloneable class";
            throw new InternalError(errorString);  // this should never happen
        }
    }

    public double[] getCoordinates() {
        double[] retVal = new double[n * m * 3];
        /////////////////////////////////////////////////////////////////////////
        // Really, really need to sort through controlPoint
        // list here and eliminate duplicate points.  Otherwise
        // automatic normal generation gets screwed up.
        //
        int index = 0;
        for (int i = 0; i < n; i ++) {
            for (int j = 0; j < m; j ++) {
                retVal[index++] = controlPts[i][j].x / controlPts[i][j].w;
                retVal[index++] = controlPts[i][j].y / controlPts[i][j].w;
                retVal[index++] = controlPts[i][j].z / controlPts[i][j].w;
            }
        }
        return retVal;
    }

    public int[] getCoordinateIndices(){
        int[] indices = null;
        if (m > 1) {
            indices  = new int[6*(n-1)*(m-1)];
        }
        else {
            indices  = new int[n*m+1];
        }
        int position = 0;
        for (int j=0; j<n-1; j++) {
            if (m >1) {
                for (int i=0; i<m-1; i++) {
                    int index = i + j*m;
                    indices[position++] = index;
                    indices[position++] = index+1;
                    indices[position++] = index+m;
                    indices[position++] = index+m;
                    indices[position++] = index+1;
                    indices[position++] = index+1+m;

                }
            }
            else {
                indices[j] = j;
            }
        }
        if (m >1) {
            // Do nothing
        }
        else {
            indices[indices.length-2] = n-1;
            indices[indices.length-1] = -1;
        }
        return indices;
    }
}
