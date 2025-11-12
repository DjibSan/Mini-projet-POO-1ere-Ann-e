package app;

public class Device {
    public final String id;
    public final String name;
    public final double limit;

    public Device(String id, String name, double limit) {
        if (id == null || id.isBlank()) throw new IllegalArgumentException("id obligatoire");
        if (name == null || name.isBlank()) throw new IllegalArgumentException("nom obligatoire");
        this.id = id.trim();
        this.name = name.trim();
        this.limit = limit;
    }
}
