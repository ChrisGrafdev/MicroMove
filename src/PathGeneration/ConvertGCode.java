package PathGeneration;

import VertexData.GCodeData;

import java.util.ArrayList;

public class ConvertGCode {
    // Constructor with default readLen = 0
    public ConvertGCode(String gCodeFilePath){
        this(gCodeFilePath, 0); // Call the main constructor with default value
    }
    // Constructor with default readLen = 0
    public ConvertGCode(String gCodeFilePath){
        this(gCodeFilePath, 0); // Call the main constructor with default value
    }

    // Main constructor with optional readLen parameter
    public ConvertGCode(String gCodeFilePath, int readLen){
        _gCodeFilePath = gCodeFilePath;
        _readLen = readLen;
        // open gcode file...
        // read lines into gCodeData arraylist
    }

    private String _gCodeFilePath;
    private int _readLen;
    private ArrayList<GCodeData> _gCodeData;
}
