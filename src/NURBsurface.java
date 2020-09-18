public class NURBsurface {
    public NURBsurface(int uSegments , int vSegments , float[] uKnotSequence ,
                       float[]  vKnotSequence , int numUControlPoints , int numVControlPoints , Vector4[] controlPoints ,
                       Appearance app ){

        // First create the Knots for the U Knot
        int numControlPoints = numUControlPoints;
        int numKnots         = uKnotSequence.length;
        int order            = numKnots - numControlPoints;

        double[] knot = new double[numKnots];
        for (int i=0; i<numKnots; i++) {
            knot[i] = uKnotSequence[i];
        }
        Knot u = new Knot(order, numControlPoints);
        u.setKnots(knot);



        //  Create the Knots for the V Knot
        numControlPoints = numVControlPoints;
        numKnots         = vKnotSequence.length;
        order            = numKnots - numControlPoints;

        knot = new double[numKnots];
        for (int i=0; i<numKnots; i++)
        {
            knot[i] = vKnotSequence[i];
        }
        Knot v = new Knot(order, numControlPoints);
        v.setKnots(knot);

        // Second create the ControlNet
        Vector4[][] points = new Vector4[u.getN()][v.getN()];

        for (int i=0; i<u.getN(); i++)
        {
            for (int j=0; j<v.getN(); j++)
            {
                int index = (j + i*v.getN());
                points[i][j] = new Vector4(controlPoints[index].x,
                        controlPoints[index].y,
                        controlPoints[index].z,
                        controlPoints[index].w);
            }
        }

        ControlNet controlNet = new ControlNet(u.getN(), v.getN(), points);


        // construct Nurbs
        NURB shape = new NURB(u, v, controlNet);


        // Now tessellate to desired smoothness
        /*
        Nurbs  temp = shape.tessellate(uSegments, vSegments );


        float[] coord = temp.getCoordinates();
        int[] indices = temp.getCoordinateIndices();

        IndexedTriangleArray Ita  = new IndexedTriangleArray( coord.length/3 , TriangleArray.COORDINATES , indices.length );

        Ita.setCoordinates(0,coord );
        Ita.setCoordinateIndices(0,indices);

        this.setGeometry(Ita);
        this.setAppearance(app);*/
    }
}
