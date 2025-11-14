package service;

import model.CapteurConnecte;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ServiceAlerte {

    private static final String FICHIER_ALERTES = "alertes.log";

    public void traiterMesure(CapteurConnecte capteur, double valeur) {
        if (capteur.estEnAlerte(valeur)) {
            String message = "!!! ALERTE !!! "
                    + capteur.getNom()
                    + " (" + capteur.getId() + ") : "
                    + valeur + " " + capteur.getUnite()
                    + " hors des seuils ["
                    + capteur.getSeuilMin() + " ; "
                    + capteur.getSeuilMax() + "]";

            System.out.println(message);
            logAlerte(message);
        } else {
            System.out.println("OK - " + capteur.getNom()
                    + " : " + valeur + " " + capteur.getUnite());
        }
    }

    private void logAlerte(String message) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FICHIER_ALERTES, true))) {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            pw.println("[" + timestamp + "] " + message);
        } catch (IOException e) {
            System.err.println("Erreur écriture alertes : " + e.getMessage());
        }
    }
}
