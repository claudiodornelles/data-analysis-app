package com.claudiodornelles.data.impl.models;

import java.io.File;
import java.util.List;

public interface Report {
    
    void setSource(File file);
    
    List<String> readSource();
    
    void writeReport();
}
