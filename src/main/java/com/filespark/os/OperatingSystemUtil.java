package com.filespark.os;

public final class OperatingSystemUtil {

    private OperatingSystemUtil(){}

    public static OperatingSystems getOperatingSystem(){

        String operatingSystem = System.getProperty("os.name").toLowerCase();

        if (operatingSystem.contains("windows")) return OperatingSystems.WINDOWS;
        if (operatingSystem.contains("mac")) return OperatingSystems.MAC;
        if (operatingSystem.contains("nix") || operatingSystem.contains("nux")) return OperatingSystems.LINUX;
        return OperatingSystems.OTHER;

    }
    
}
