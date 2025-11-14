package model;

public class Patient {
    private String id;
    private String nom;
    private String prenom;

    public Patient(String id, String nom, String prenom) {
        this.id = id;
        this.nom = nom;
        this.prenom = prenom;
    }

    public String getId() {
        return id;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    @Override
    public String toString() {
        return id + " - " + nom.toUpperCase() + " " + prenom;
    }
}
