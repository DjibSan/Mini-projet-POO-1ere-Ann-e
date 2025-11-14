package app;

import model.*;
import service.ServiceAbonnementFichier;
import service.ServiceAlerte;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GestionIoTHopital {

    private List<Patient> patients = new ArrayList<>();
    private List<CapteurConnecte> capteurs = new ArrayList<>();
    private List<Abonnement> abonnements = new ArrayList<>();

    private ServiceAbonnementFichier serviceAbonnement;
    private ServiceAlerte serviceAlerte = new ServiceAlerte();
    private Scanner scanner = new Scanner(System.in);

    public GestionIoTHopital() {
        serviceAbonnement = new ServiceAbonnementFichier("abonnements.csv");
        abonnements.addAll(serviceAbonnement.charger());
        initialiserCapteurs();
        initialiserPatientsDemo();
    }

    private void initialiserCapteurs() {
        capteurs.add(new Tensiometre("C1"));
        capteurs.add(new BalanceConnectee("C2"));
        capteurs.add(new PilulierConnecte("C3"));
        capteurs.add(new OxymetreConnecte("C4"));
        capteurs.add(new GlucometreConnecte("C5"));
    }

    private void initialiserPatientsDemo() {
        patients.add(new Patient("P1", "Dupont", "Alice"));
        patients.add(new Patient("P2", "Martin", "Bob"));
        patients.add(new Patient("P3", "Durand", "Chloé"));
    }

    public void menu() {
        int choix;

        do {
            System.out.println("\n=== Gestion IoT Hôpital ===");
            System.out.println("1. Lister les capteurs");
            System.out.println("2. Lister les patients");
            System.out.println("3. Abonner un patient à un capteur");
            System.out.println("4. Afficher les abonnements");
            System.out.println("5. Prendre une mesure sur un capteur");
            System.out.println("6. Prendre des mesures pour tous les capteurs d'un patient");
            System.out.println("0. Quitter");
            System.out.print("Votre choix : ");
            choix = lireInt();

            switch (choix) {
                case 1: listerCapteurs(); break;
                case 2: listerPatients(); break;
                case 3: abonnerPatient(); break;
                case 4: afficherAbonnements(); break;
                case 5: mesurerCapteur(); break;
                case 6: mesurerTousCapteursPourPatient(); break;
                case 0:
                    serviceAbonnement.sauvegarder(abonnements);
                    System.out.println("Sauvegarde terminée. Au revoir.");
                    break;
                default:
                    System.out.println("Choix invalide.");
            }

        } while (choix != 0);
    }

    private int lireInt() {
        while (!scanner.hasNextInt()) {
            scanner.next(); // on jette ce qui n'est pas un int
            System.out.print("Entrez un entier : ");
        }
        int v = scanner.nextInt();
        scanner.nextLine(); // consommer fin de ligne
        return v;
    }

    private void listerCapteurs() {
        System.out.println("\nCapteurs disponibles :");
        for (CapteurConnecte c : capteurs) {
            System.out.println(" - " + c);
        }
    }

    private void listerPatients() {
        System.out.println("\nPatients :");
        for (Patient p : patients) {
            System.out.println(" - " + p);
        }
    }

    private CapteurConnecte trouverCapteurParId(String id) {
        for (CapteurConnecte c : capteurs) {
            if (c.getId().equals(id)) return c;
        }
        return null;
    }

    private Patient trouverPatientParId(String id) {
        for (Patient p : patients) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    private void abonnerPatient() {
        System.out.print("Id patient : ");
        String idP = scanner.nextLine();
        Patient p = trouverPatientParId(idP);
        if (p == null) {
            System.out.println("Patient inconnu.");
            return;
        }

        System.out.print("Id capteur : ");
        String idC = scanner.nextLine();
        CapteurConnecte c = trouverCapteurParId(idC);
        if (c == null) {
            System.out.println("Capteur inconnu.");
            return;
        }

        abonnements.add(new Abonnement(idP, idC, true));
        System.out.println("Abonnement créé.");
    }

    private void afficherAbonnements() {
        System.out.println("\nAbonnements :");
        if (abonnements.isEmpty()) {
            System.out.println("Aucun abonnement enregistré.");
            return;
        }
        for (Abonnement a : abonnements) {
            System.out.println(" - " + a);
        }
    }

    private void mesurerCapteur() {
        System.out.print("Id capteur : ");
        String idC = scanner.nextLine();
        CapteurConnecte c = trouverCapteurParId(idC);
        if (c == null) {
            System.out.println("Capteur inconnu.");
            return;
        }

        double valeur = c.mesurer();
        serviceAlerte.traiterMesure(c, valeur);
    }

    private void mesurerTousCapteursPourPatient() {
        System.out.print("Id patient : ");
        String idP = scanner.nextLine();
        Patient p = trouverPatientParId(idP);
        if (p == null) {
            System.out.println("Patient inconnu.");
            return;
        }

        List<CapteurConnecte> capteursPatient = trouverCapteursPourPatient(idP);
        if (capteursPatient.isEmpty()) {
            System.out.println("Ce patient n'a aucun capteur abonné.");
            return;
        }

        System.out.println("Mesures pour " + p + " :");
        for (CapteurConnecte c : capteursPatient) {
            double valeur = c.mesurer();
            serviceAlerte.traiterMesure(c, valeur);
        }
    }

    private List<CapteurConnecte> trouverCapteursPourPatient(String idPatient) {
        List<CapteurConnecte> res = new ArrayList<>();
        for (Abonnement a : abonnements) {
            if (a.getIdPatient().equals(idPatient) && a.isActif()) {
                CapteurConnecte c = trouverCapteurParId(a.getIdCapteur());
                if (c != null && !res.contains(c)) {
                    res.add(c);
                }
            }
        }
        return res;
    }

    public static void main(String[] args) {
        new GestionIoTHopital().menu();
    }
}
