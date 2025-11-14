package service;

import model.Abonnement;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceAbonnementFichier {

    private String cheminFichier;

    public ServiceAbonnementFichier(String cheminFichier) {
        this.cheminFichier = cheminFichier;
    }

    public void sauvegarder(List<Abonnement> abonnements) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(cheminFichier))) {
            for (Abonnement a : abonnements) {
                pw.println(a.getIdPatient() + ";" +
                        a.getIdCapteur() + ";" +
                        a.isActif());
            }
        } catch (IOException e) {
            System.err.println("Erreur sauvegarde : " + e.getMessage());
        }
    }

    public List<Abonnement> charger() {
        List<Abonnement> result = new ArrayList<>();
        File f = new File(cheminFichier);
        if (!f.exists()) {
            return result;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] t = line.split(";");
                if (t.length == 3) {
                    String idPatient = t[0];
                    String idCapteur = t[1];
                    boolean actif = Boolean.parseBoolean(t[2]);
                    result.add(new Abonnement(idPatient, idCapteur, actif));
                }
            }
        } catch (IOException e) {
            System.err.println("Erreur lecture : " + e.getMessage());
        }

        return result;
    }
}
