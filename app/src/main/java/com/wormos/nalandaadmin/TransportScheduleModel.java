package com.wormos.nalandaadmin;

public class TransportScheduleModel {
    String driverName,time, driverContactNumber;

    public TransportScheduleModel() {
    }

    public TransportScheduleModel(String driverName, String time, String driverContactNumber) {
        this.driverName = driverName;
        this.time = time;
        this.driverContactNumber = driverContactNumber;
    }

    public String getDriverName() {
        return driverName;
    }

    public String getTime() {
        return time;
    }

    public String getDriverContactNumber() {
        return driverContactNumber;
    }
}