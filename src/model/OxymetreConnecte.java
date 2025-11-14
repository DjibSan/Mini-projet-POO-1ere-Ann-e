package model;

public class OxymetreConnecte extends CapteurConnecte {

    public OxymetreConnecte(String id) {
        super(id, "Oxymètre connecté", "% SpO2",
                92, 100, true);
    }

    @Override
    public double mesurer() {
        // Simule une saturation entre 85 et 100
        return 85 + Math.random() * 15;
    }
}
