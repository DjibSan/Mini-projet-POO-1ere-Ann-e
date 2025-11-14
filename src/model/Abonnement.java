package model;

public class Abonnement {
    private String idPatient;
    private String idCapteur;
    private boolean actif;

    public Abonnement(String idPatient, String idCapteur, boolean actif) {
        this.idPatient = idPatient;
        this.idCapteur = idCapteur;
        this.actif = actif;
    }

    public String getIdPatient() {
        return idPatient;
    }

    public String getIdCapteur() {
        return idCapteur;
    }

    public boolean isActif() {
        return actif;
    }

    public void setActif(boolean actif) {
        this.actif = actif;
    }

    @Override
    public String toString() {
        return "Abonnement{" +
                "patient='" + idPatient + '\'' +
                ", capteur='" + idCapteur + '\'' +
                ", actif=" + actif +
                '}';
    }
}
