package com.filespark;

import com.filespark.os.OperatingSystems;
import com.filespark.os.OperatingSystemUtil;
public final class OperatingSystem {

    private OperatingSystem(){}
    public static final OperatingSystems OS = OperatingSystemUtil.getOperatingSystem();
    
}
