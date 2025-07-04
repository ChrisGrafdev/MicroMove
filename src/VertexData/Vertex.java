package VertexData;

public class Vertex {
    float x;
    float y;
    public Vertex(float _x, float _y){
        x=_x;
        y=_y;
    }
    public Vertex CalcVector(Vertex target){
        return new Vertex(target.x - x, target.y - y);
    }
    public float Dist(Vertex other){
        float dx = other.x - x;
        float dy = other.y - y;
        return (float)Math.sqrt(dx*dx + dy*dy);
    }
    public Vertex Normalize(){
        float len = (float)Math.sqrt(x*x + y*y);
        x/=len;
        y/=len;
        return this;
    }
    public Vertex Fac(float fac){
        x*=fac;
        y*=fac;
        return this;
    }
    public Vertex Transpose(){ // rotate at 90° clockwise
        float tmpY = y;
        y = -x;
        x = tmpY;
        return this;
    }
    public Vertex InvTranspose(){ // rotate at 90° counter-clockwise
        float tmpY = -y;
        y = x;
        x = tmpY;
        return this;
    }
    public Vertex Add(Vertex other){
        return new Vertex(x + other.x, y + other.y);
    }
    public Vertex AddIn(Vertex other){
        x += other.x;
        y += other.y;
        return this;
    }
    public float SkalarP(Vertex other){
        return x*other.x + y*other.y;
    }
    public Vertex Invert(){
        x = -x;
        y = -y;
        return this;
    }
    public static VertexSide checkVertexSide(Vertex origPos, Vertex origVec, Vertex testPoint){
        // shift testPoint in relation to the origPos position
        Vertex testVertexShifted = testPoint.Add(origPos.Copy().Invert());
        // scalarproduct -> <0 for >90°/<-90° -> rotate vector around 90° to check if point is on right(>0) or left(<0) side
        Vertex rotVec = origVec.Copy().Transpose(); // rotate current vector
        if(rotVec.SkalarP(testVertexShifted) > 0.0) return VertexSide.right;
        else if(rotVec.SkalarP(testVertexShifted) < 0.0) return VertexSide.left;
        return VertexSide.straight;
    }
    public static VertexSide checkPointInFront(Vertex origPos, Vertex origVec, Vertex testPoint){
        // shift testPoint in relation to the origPos position
        Vertex testVertexShifted = testPoint.Add(origPos.Copy().Invert());
        // Skalarproduct 
        if(origVec.SkalarP(testVertexShifted) > 0.0) return VertexSide.front; // f -> testpoint is in front of origPoint
        else if(origVec.SkalarP(testVertexShifted) < 0.0) return VertexSide.behind; // b -> testpoint is behind the origPoint
        return VertexSide.aligned; // testpoint 90° to origin point&vector
    }
    public Vertex Copy(){
        return new Vertex(x, y);
    }
    public boolean isEqual(Vertex other){
        // checking with slight variation
        return x < (other.x + 1e-10) && x > (other.x - 1e-10) && y < (other.y + 1e-10) && y > (other.y - 1e-10);
    }
    public static Vertex CalcIntersec(Vertex p1, Vertex v1, Vertex p2, Vertex v2) {
        float s = (-v1.x*p1.y + v1.x*p2.y + p1.x*v1.y - p2.x*v1.y)/(v2.x*v1.y - v1.x*v2.y);
        return p2.Add(v2.Copy().Fac(s));
    }
}
