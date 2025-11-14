package model;

public abstract class CapteurConnecte {
    private String id;
    private String nom;
    private String unite;
    private double seuilMin;
    private double seuilMax;
    private boolean abonnementRequis;

    public CapteurConnecte(String id, String nom, String unite,
                           double seuilMin, double seuilMax,
                           boolean abonnementRequis) {
        this.id = id;
        this.nom = nom;
        this.unite = unite;
        this.seuilMin = seuilMin;
        this.seuilMax = seuilMax;
        this.abonnementRequis = abonnementRequis;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getUnite() {
        return unite;
    }

    public double getSeuilMin() {
        return seuilMin;
    }

    public double getSeuilMax() {
        return seuilMax;
    }

    public boolean isAbonnementRequis() {
        return abonnementRequis;
    }

    /** Simulation d'une mesure prise par le capteur */
    public abstract double mesurer();

    /** Vérifie si la valeur déclenche une alerte */
    public boolean estEnAlerte(double valeur) {
        return valeur < seuilMin || valeur > seuilMax;
    }

    @Override
    public String toString() {
        return id + " - " + nom + " [" + seuilMin + " ; " +
                seuilMax + " " + unite + "]"
                + (abonnementRequis ? " (abonnement requis)" : "");
    }
}
