package PathGeneration;

import VertexData.*;

import java.util.ArrayList;

public class GenerateMakroVertexList {
    public GenerateMakroVertexList(float boundingRadius){
        _makroVertexList = new ArrayList<MakroVertex>();
        _boundingRadius = boundingRadius;
    }

    public ArrayList<MakroVertex> getMakroVertexList(){
        return _makroVertexList;
    }

    public void createMakroVertex(Vertex nextVertex) {
        _makroVertexList.add(new MakroVertex(MakroVertexMark.center_point, nextVertex));
        _makroVertexList.getLast().corner = VertexSide.straight;
        if(_makroVertexList.size() == 1) return;
        Vertex direction = _makroVertexList.get(_makroVertexList.size()-2).vertex.CalcVector(nextVertex);
        direction.Normalize();
        // handle every new vertex as if it would be the last one
        _makroVertexList.getLast().leftPoint1 = new VertexVec(direction.Copy().InvTranspose().Fac(_boundingRadius));
        _makroVertexList.getLast().rightPoint1 = new VertexVec(direction.Copy().Transpose().Fac(_boundingRadius));
        // first point individual
        if(_makroVertexList.size() == 2) {
            _makroVertexList.getFirst().leftPoint1 = new VertexVec(direction.Copy().InvTranspose().Fac(_boundingRadius));
            _makroVertexList.getFirst().rightPoint1 = new VertexVec(direction.Copy().Transpose().Fac(_boundingRadius));
            return;
        }
        // update previous (pre-last) MakroVertex due to not being the last point anymore (>2 vertices)
        int preLastIndex = _makroVertexList.size() - 2;
        Vertex prevDirection = _makroVertexList.get(preLastIndex-1).vertex.CalcVector(_makroVertexList.get(preLastIndex).vertex).Normalize();
        VertexSide corner = Vertex.checkVertexSide(
                _makroVertexList.get(preLastIndex-1).vertex,
                prevDirection,
                _makroVertexList.getLast().vertex
            );
        _makroVertexList.get(preLastIndex).corner = corner;
        // calculate inside bounding point
        if(corner == VertexSide.straight) return; // no changes to be made
        Vertex invPrevDirection = prevDirection.Copy().Invert();
        Vertex insideVec = invPrevDirection.Add(direction).Normalize();
        if(corner == VertexSide.left){
            _makroVertexList.get(preLastIndex).leftPoint1 = new VertexVec(_makroVertexList.get(preLastIndex).vertex.Add(insideVec.Fac(_boundingRadius)));
            _makroVertexList.get(preLastIndex).leftPoint1.vec = _makroVertexList.get(preLastIndex).leftPoint1.pos.CalcVector(_makroVertexList.getLast().leftPoint1.pos);
            // update also vector of previous pre-last bounding point due to possible changes of the new bounding point, in case of a curve.
            if(_makroVertexList.get(preLastIndex-1).corner == VertexSide.left || _makroVertexList.get(preLastIndex-1).corner == VertexSide.straight) {
                _makroVertexList.get(preLastIndex - 1).leftPoint1.vec = _makroVertexList.get(preLastIndex - 1).leftPoint1.pos.CalcVector(_makroVertexList.get(preLastIndex).leftPoint1.pos);
            }
            else {
                _makroVertexList.get(preLastIndex - 1).leftPoint2.vec = _makroVertexList.get(preLastIndex - 1).leftPoint2.pos.CalcVector(_makroVertexList.get(preLastIndex).leftPoint1.pos);
            }
            /**
             * Important note:
             * Currently the "vec" variable only contains the direction to the next bounding point
             * on the same side. This should simplify the later executed calculation of the
             * stabilization points of the Bezier curve. (More specific the calculation of the
             * vector-vector intersection vs vector bounding border intersection) Later some of these
             * "vec" parameters will be overwritten with the transition vector between the bezier
             * sections to achieve a smooth transition.
             */
            // outside Points
            Vertex outsideVec1 = prevDirection.Copy().Transpose().Fac(_boundingRadius);
            Vertex outsideVec2 = direction.Copy().Transpose().Fac(_boundingRadius);
            _makroVertexList.get(preLastIndex).rightPoint1 = new VertexVec(_makroVertexList.get(preLastIndex).vertex.Add(outsideVec1));
            _makroVertexList.get(preLastIndex).rightPoint2 = new VertexVec(_makroVertexList.get(preLastIndex).vertex.Add(outsideVec2));
            _makroVertexList.get(preLastIndex).rightPoint1.vec = _makroVertexList.get(preLastIndex).rightPoint1.pos.CalcVector(_makroVertexList.get(preLastIndex).rightPoint2.pos); // vector inside that point
            _makroVertexList.get(preLastIndex).rightPoint2.vec = _makroVertexList.get(preLastIndex).rightPoint2.pos.CalcVector(_makroVertexList.getLast().rightPoint1.pos); // vector to next point
        }
        else if (corner == VertexSide.right) {
            _makroVertexList.get(preLastIndex).rightPoint1 = new VertexVec(_makroVertexList.get(preLastIndex).vertex.Add(insideVec.Fac(_boundingRadius)));
            _makroVertexList.get(preLastIndex).rightPoint1.vec = insideVec.Copy().InvTranspose();
            // modification of previous pre-last bounding vector
            if(_makroVertexList.get(preLastIndex-1).corner == VertexSide.right || _makroVertexList.get(preLastIndex-1).corner == VertexSide.straight) {
                _makroVertexList.get(preLastIndex - 1).rightPoint1.vec = _makroVertexList.get(preLastIndex - 1).rightPoint1.pos.CalcVector(_makroVertexList.get(preLastIndex).rightPoint1.pos);
            }
            else {
                _makroVertexList.get(preLastIndex - 1).rightPoint2.vec = _makroVertexList.get(preLastIndex - 1).rightPoint2.pos.CalcVector(_makroVertexList.get(preLastIndex).rightPoint1.pos);
            }
            // outside Points
            Vertex outsideVec1 = prevDirection.Copy().InvTranspose().Fac(_boundingRadius);
            Vertex outsideVec2 = direction.Copy().InvTranspose().Fac(_boundingRadius);
            _makroVertexList.get(preLastIndex).leftPoint1 = new VertexVec(_makroVertexList.get(preLastIndex).vertex.Add(outsideVec1));
            _makroVertexList.get(preLastIndex).leftPoint2 = new VertexVec(_makroVertexList.get(preLastIndex).vertex.Add(outsideVec2));
            _makroVertexList.get(preLastIndex).leftPoint1.vec = _makroVertexList.get(preLastIndex).leftPoint1.pos.CalcVector(_makroVertexList.get(preLastIndex).leftPoint2.pos); // vector inside that point
            _makroVertexList.get(preLastIndex).leftPoint2.vec = _makroVertexList.get(preLastIndex).leftPoint2.pos.CalcVector(_makroVertexList.getLast().rightPoint1.pos); // vector to next point
        }
    }

    private ArrayList<MakroVertex> _makroVertexList;
    private float _boundingRadius;
}
