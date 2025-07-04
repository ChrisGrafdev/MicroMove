package VertexData;

public class MakroVertex {
    public MakroVertex(MakroVertexMark marking, Vertex vertex){
        this.marking = marking;
        this.vertex = vertex;
    }
    public Vertex vertex;
    public MakroVertexMark marking;
    public VertexVec rightPoint1; // could be inner or outer point ; vec defines the angle halfing to the path
    public VertexVec rightPoint2; // defines second outer point or empty if inner point
    public VertexVec leftPoint1;
    public VertexVec leftPoint2; // empty if no corner point
    public VertexSide corner;
}
