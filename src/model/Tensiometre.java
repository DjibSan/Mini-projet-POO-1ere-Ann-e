package model;

public class Tensiometre extends CapteurConnecte {

    public Tensiometre(String id) {
        super(id, "Tensiomètre connecté", "mmHg",
                90, 140, true); // intervalle normal simplifié
    }

    @Override
    public double mesurer() {
        // Simule une mesure entre 80 et 180
        return 80 + Math.random() * 100;
    }
}
