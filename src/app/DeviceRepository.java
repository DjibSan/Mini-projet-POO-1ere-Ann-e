package app;

import java.util.List;

public interface DeviceRepository {
    void init() throws Exception;
    void addDevice(Device device) throws Exception;
    List<Device> findAllDevices() throws Exception;
    Device getDevice(String id) throws Exception;
    void addReading(String deviceId, double value) throws Exception;
}
