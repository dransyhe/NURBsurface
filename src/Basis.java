public class Basis {

    public double ComputeCoefficient(double[] knots, int iInterval, int i, int p, int k){
        double fResult = 0.;

        if( p == 0 ) {
            if( i == iInterval )
                fResult = 1.0f;
        }
        else if( k == 0 ) {
            if( knots[i+p] != knots[i])
                fResult -= knots[i] * ComputeCoefficient( knots, iInterval, i, p-1, 0 ) / (knots[i+p] - knots[i]);
            if( knots[i+p+1] != knots[i+1] )
                fResult += knots[i+p+1] * ComputeCoefficient( knots, iInterval, i+1, p-1, 0 ) / (knots[i+p+1] - knots[i+1]);
        }
        else if( k == p ) {
            if( knots[i+p] != knots[i] )
                fResult += ComputeCoefficient( knots, iInterval, i, p-1, p-1 ) / (knots[i+p] - knots[i]);
            if( knots[i+p+1] != knots[i+1] )
                fResult -= ComputeCoefficient( knots, iInterval, i+1, p-1, p-1 ) / (knots[i+p+1] - knots[i+1]);
        }
        else if( k > p ) {
            fResult = 0.0f;
        }
        else {
            double C1, C2;
            if( knots[i+p] != knots[i] ) {
                C1 = ComputeCoefficient( knots, iInterval, i, p-1, k-1 );
                C2 = ComputeCoefficient( knots, iInterval, i, p-1, k );
                fResult += (C1 - knots[i] * C2 ) / (knots[i+p] - knots[i] );
            }
            if( knots[i+p+1] != knots[i+1] ) {
                C1 = ComputeCoefficient( knots, iInterval, i+1, p-1, k-1 );
                C2 = ComputeCoefficient( knots, iInterval, i+1, p-1, k );
                fResult -= (C1 - knots[i+p+1] * C2 ) / (knots[i+p+1] - knots[i+1] );
            }

        }
        return fResult;
    }

    public void ComputeBasisCoefficients(){
        int i, j, k;

        //
        // Start with U.  For each Basis span calculate coefficients
        // for m_iUOrder polynomials each having m_iUOrder coefficients
        //

        for( i = 0; i < m_iUBasisSpans; i ++) {
            for( j = 0; j < m_iUOrder; j ++) {
                for( k = 0; k < m_iUOrder; k ++) {
                    m_UBasisCoefficients[ (i * m_iUOrder + j) * m_iUOrder + k ] =
                            ComputeCoefficient( m_UKnots, i + m_iUDegree, i + j, m_iUDegree, k );
                }
            }
        }

        for( i=0; i<m_iVBasisSpans; i++) {
            for( j=0; j<m_iVOrder; j++) {
                for( k=0; k<m_iVOrder; k++) {
                    m_VBasisCoefficients[ (i * m_iVOrder + j) * m_iVOrder + k ] =
                            ComputeCoefficient( m_VKnots, i + m_iVDegree, i + j, m_iVDegree, k );
                }
            }
        }
    }

    public void EvaluateBasisFunctions(){
        int i, j, k, idx;
        float u, uinc;
        float v, vinc;

        //
        // First evaluate the U basis functions and derivitives at uniformly spaced u values
        //
        idx = 0;
        u = m_UKnots[idx+m_iUDegree];
        uinc = (m_UKnots[m_iUKnots-m_iUOrder] - m_UKnots[m_iUDegree])/(m_iUTessellations);

        for( i=0; i<=m_iUTessellations; i++ ) {
            while( (idx < m_iUKnots - m_iUDegree*2 - 2) && (u >= m_UKnots[idx+m_iUDegree+1] ) )
                idx++;

            m_TessUKnotSpan[i] = idx+m_iUDegree;

            //
            // Evaluate using Horner's method
            //
            for( j=0; j<m_iUOrder; j++) {
                m_UBasis[(i*m_iUOrder+j) * SIMD_SIZE] = m_UBasisCoefficients[ (idx * m_iUOrder + j) * m_iUOrder + m_iUDegree ];
                m_dUBasis[(i*m_iUOrder+j) * SIMD_SIZE] = m_UBasis[(i*m_iUOrder+j) * SIMD_SIZE] * m_iUDegree;
                for( k=m_iUDegree-1; k>=0; k-- ) {
                    m_UBasis[(i*m_iUOrder+j)*SIMD_SIZE] = m_UBasis[ (i*m_iUOrder+j)*SIMD_SIZE ] * u +
                            m_UBasisCoefficients[ (idx * m_iUOrder + j) * m_iUOrder + k ];
                    if( k>0) {
                        m_dUBasis[(i*m_iUOrder+j)*SIMD_SIZE] = m_dUBasis[(i * m_iUOrder+j)*SIMD_SIZE] * u +
                                m_UBasisCoefficients[ (idx * m_iUOrder + j) * m_iUOrder + k ] * k;
                    }
                }
                //
                // Make three copies.  This isn't necessary if we're using straight C
                // code but for the Pentium III optimizations, it is.
                //
            }

            u += uinc;
        }

        //
        // Finally evaluate the V basis functions at uniformly spaced v values
        //
        idx = 0;
        v = m_VKnots[idx+m_iVDegree];
        vinc = (m_VKnots[m_iVKnots-m_iVOrder] - m_VKnots[m_iVDegree])/(m_iVTessellations);

        for( i=0; i<=m_iVTessellations; i++ ) {
            while( (idx < m_iVKnots - m_iVDegree*2 - 2) && (v >= m_VKnots[idx+m_iVDegree+1] ) )
                idx++;

            m_TessVKnotSpan[i] = idx+m_iVDegree;

            //
            // Evaluate using Horner's method
            //
            for( j=0; j<m_iVOrder; j++) {
                m_VBasis[(i*m_iVOrder+j)*SIMD_SIZE] = m_VBasisCoefficients[ (idx * m_iVOrder + j) * m_iVOrder + m_iVDegree ];
                m_dVBasis[(i*m_iVOrder+j)*SIMD_SIZE] = m_VBasis[(i*m_iVOrder+j)*SIMD_SIZE] * m_iVDegree;
                for( k=m_iVDegree-1; k>=0; k--) {
                    m_VBasis[(i*m_iVOrder+j)*SIMD_SIZE] = m_VBasis[ (i*m_iVOrder+j)*SIMD_SIZE ] * v +
                            m_VBasisCoefficients[ (idx * m_iVOrder + j) * m_iVOrder + k ];
                    if( k>0) {
                        m_dVBasis[(i*m_iVOrder+j)*SIMD_SIZE] = m_dVBasis[(i * m_iVOrder+j)*SIMD_SIZE] * v +
                                m_VBasisCoefficients[ (idx * m_iVOrder + j) * m_iVOrder + k ] * k;
                    }
                }
            }
            v += vinc;
        }
    }
}
