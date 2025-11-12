package app;

import java.time.Instant;
import java.util.*;

public class InMemoryRepository implements DeviceRepository {
    private final Map<String, Device> devices = new LinkedHashMap<>();
    private final List<String> readings = new ArrayList<>();

    @Override public void init() { /* rien */ }

    @Override public void addDevice(Device device) {
        if (devices.containsKey(device.id))
            throw new IllegalArgumentException("ID déjà existant: " + device.id);
        devices.put(device.id, device);
    }

    @Override public List<Device> findAllDevices() {
        return new ArrayList<>(devices.values());
    }

    @Override public Device getDevice(String id) {
        Device d = devices.get(id);
        if (d == null) throw new IllegalArgumentException("Appareil introuvable: " + id);
        return d;
    }

    @Override public void addReading(String deviceId, double value) {
        if (!devices.containsKey(deviceId))
            throw new IllegalArgumentException("Appareil introuvable: " + deviceId);
        readings.add(deviceId + "|" + value + "|" + Instant.now());
    }
}
