package VertexData;

public class GCodeData {
    // command
    public String command; // maybe replace with command enum
    // point coordinates
    public float x;
    public float y;
    public float z;
    // extrude amount
    public float e;
    // feedrate
    public float f;
    // temperature value
    public float s;

    public Vertex toVertex(){
        return new Vertex(x, y);
    }
}
