package BezierSmoothing;
import VertexData.Vertex;

import javax.lang.model.type.NullType;
import java.util.ArrayList;

public class BezierSegment {
    /*public BezierSegment(){
    }*/

    public ArrayList<Vertex> createSegment(Vertex p1, Vertex p2, Vertex p3, Vertex p4){
        float pathLen = pathLength(p1, p2, p3, p4); //mm
        int interSteps = (int)(pathLen / 10.0f / _bezierResolution);
        if(interSteps <= 2) interSteps = 3;
        _bezSegment = new ArrayList<Vertex>();

        for(int i = 0; i < interSteps; i++) {
            float fac = (float)i / (float)interSteps;
            if(p4 == null)
                _bezSegment.add(_interpolateBezier3P(p1, p2, p3, fac));
            else
                _bezSegment.add(_interpolateBezier4P(p1, p2, p3, p4, fac));
        }
        return _bezSegment;
    }

    public void setResolution(float resPerCM){
        _bezierResolution = resPerCM;
    }

    public float pathLength(Vertex p1, Vertex p2, Vertex p3, Vertex p4){
        if(p4 == null)
            return p1.Dist(p2) + p2.Dist(p3);
        return p1.Dist(p2) + p2.Dist(p3) + p3.Dist(p4);
    }

    private Vertex _interpolateBezier3P(Vertex p1, Vertex p2, Vertex p3, float fac){
        Vertex vecP12 = p1.CalcVector(p2);
        Vertex vecP23 = p2.CalcVector(p3);
        Vertex m1 = p1.Add(vecP12.Fac(fac));
        Vertex m2 = p2.Add(vecP23.Fac(fac));
        Vertex vecM12 = m1.CalcVector(m2);
        return m1.Add(vecM12.Fac(fac));
    }
    
    private Vertex _interpolateBezier4P(Vertex p1, Vertex p2, Vertex p3, Vertex p4, float fac){
        Vertex vecP12 = p1.CalcVector(p2);
        Vertex vecP23 = p2.CalcVector(p3);
        Vertex vecP34 = p3.CalcVector(p4);
        Vertex m1 = p1.Add(vecP12.Fac(fac));
        Vertex m2 = p2.Add(vecP23.Fac(fac));
        Vertex m3 = p3.Add(vecP34.Fac(fac));
        Vertex vecM12 = m1.CalcVector(m2);
        Vertex vecM23 = m2.CalcVector(m3);
        Vertex n1 = m1.Add(vecM12.Fac(fac));
        Vertex n2 = m2.Add(vecM23.Fac(fac));
        Vertex vecN12 = n1.CalcVector(n2);
        return n1.Add(vecN12.Fac(fac));
    }

    private float _bezierResolution = 3; // Points per 10mm control path
    private ArrayList<Vertex> _bezSegment;
}
