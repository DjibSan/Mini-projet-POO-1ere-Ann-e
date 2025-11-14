package model;

public class GlucometreConnecte extends CapteurConnecte {

    public GlucometreConnecte(String id) {
        super(id, "Glucomètre connecté", "mg/dL",
                70, 140, true);
    }

    @Override
    public double mesurer() {
        // Simule une glycémie entre 50 et 250
        return 50 + Math.random() * 200;
    }
}
