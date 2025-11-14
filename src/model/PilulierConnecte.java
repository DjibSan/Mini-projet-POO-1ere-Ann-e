package model;

public class PilulierConnecte extends CapteurConnecte {

    public PilulierConnecte(String id) {
        super(id, "Pilulier connecté", "prises/jour",
                1, 3, true);
    }

    @Override
    public double mesurer() {
        // Simule le nombre de prises détectées (entier de 0 à 4)
        return (int) (Math.random() * 5);
    }
}
