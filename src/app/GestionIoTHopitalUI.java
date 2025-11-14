package app;

import model.*;
import service.ServiceAbonnementFichier;
import service.ServiceAlerte;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class GestionIoTHopitalUI extends JFrame {

    private List<Patient> patients = new ArrayList<>();
    private List<CapteurConnecte> capteurs = new ArrayList<>();
    private List<Abonnement> abonnements = new ArrayList<>();

    private ServiceAbonnementFichier serviceAbonnement;
    private ServiceAlerte serviceAlerte = new ServiceAlerte();

    // Modèles pour afficher les listes
    private DefaultListModel<Patient> modelPatients = new DefaultListModel<>();
    private DefaultListModel<CapteurConnecte> modelCapteurs = new DefaultListModel<>();

    private JList<Patient> listePatients = new JList<>(modelPatients);
    private JList<CapteurConnecte> listeCapteurs = new JList<>(modelCapteurs);

    // Combos pour abonnements / mesures
    private JComboBox<Patient> comboPatientsAbonnement;
    private JComboBox<CapteurConnecte> comboCapteursAbonnement;

    private JComboBox<Patient> comboPatientsMesures;
    private JComboBox<CapteurConnecte> comboCapteursMesures;

    // Zones de texte
    private JTextArea zoneAbonnements;
    private JTextArea zoneMesures;

    public GestionIoTHopitalUI() {
        super("Gestion IoT Hôpital");

        // Gestion des fichiers d'abonnement
        serviceAbonnement = new ServiceAbonnementFichier("abonnements.csv");
        abonnements.addAll(serviceAbonnement.charger());

        initialiserCapteurs();
        initialiserPatientsDemo();

        construireInterface();
        rafraichirModeles();

        setSize(900, 600);
        setLocationRelativeTo(null); // centre l'écran
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Sauvegarde automatique des abonnements à la fermeture
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                serviceAbonnement.sauvegarder(abonnements);
            }
        });
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

    private void rafraichirModeles() {
        modelPatients.clear();
        for (Patient p : patients) {
            modelPatients.addElement(p);
        }

        modelCapteurs.clear();
        for (CapteurConnecte c : capteurs) {
            modelCapteurs.addElement(c);
        }

        // Réinitialisation des combos
        if (comboPatientsAbonnement != null) {
            comboPatientsAbonnement.removeAllItems();
            for (Patient p : patients) comboPatientsAbonnement.addItem(p);
        }

        if (comboPatientsMesures != null) {
            comboPatientsMesures.removeAllItems();
            for (Patient p : patients) comboPatientsMesures.addItem(p);
        }

        if (comboCapteursAbonnement != null) {
            comboCapteursAbonnement.removeAllItems();
            for (CapteurConnecte c : capteurs) comboCapteursAbonnement.addItem(c);
        }

        if (comboCapteursMesures != null) {
            comboCapteursMesures.removeAllItems();
            for (CapteurConnecte c : capteurs) comboCapteursMesures.addItem(c);
        }

        rafraichirZoneAbonnements();
    }

    private void construireInterface() {
        JTabbedPane onglets = new JTabbedPane();

        // Onglet Patients
        JPanel panelPatients = new JPanel(new BorderLayout());
        panelPatients.add(new JScrollPane(listePatients), BorderLayout.CENTER);
        panelPatients.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        onglets.addTab("Patients", panelPatients);

        // Onglet Capteurs
        JPanel panelCapteurs = new JPanel(new BorderLayout());
        panelCapteurs.add(new JScrollPane(listeCapteurs), BorderLayout.CENTER);
        panelCapteurs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        onglets.addTab("Capteurs", panelCapteurs);

        // Onglet Abonnements
        JPanel panelAbonnements = construirePanelAbonnements();
        onglets.addTab("Abonnements", panelAbonnements);

        // Onglet Mesures
        JPanel panelMesures = construirePanelMesures();
        onglets.addTab("Mesures", panelMesures);

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(onglets, BorderLayout.CENTER);
    }

    private JPanel construirePanelAbonnements() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel haut = new JPanel();
        haut.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboPatientsAbonnement = new JComboBox<>();
        comboCapteursAbonnement = new JComboBox<>();

        // Ligne patient
        gbc.gridx = 0; gbc.gridy = 0;
        haut.add(new JLabel("Patient :"), gbc);
        gbc.gridx = 1;
        haut.add(comboPatientsAbonnement, gbc);

        // Ligne capteur
        gbc.gridx = 0; gbc.gridy = 1;
        haut.add(new JLabel("Capteur :"), gbc);
        gbc.gridx = 1;
        haut.add(comboCapteursAbonnement, gbc);

        // Bouton abonner
        JButton boutonAbonner = new JButton("Créer abonnement");
        boutonAbonner.addActionListener(e -> abonnerSelection());
        gbc.gridx = 0; gbc.gridy = 2;
        gbc.gridwidth = 2;
        haut.add(boutonAbonner, gbc);

        panel.add(haut, BorderLayout.NORTH);

        // Zone affichage abonnements
        zoneAbonnements = new JTextArea();
        zoneAbonnements.setEditable(false);
        zoneAbonnements.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        panel.add(new JScrollPane(zoneAbonnements), BorderLayout.CENTER);

        return panel;
    }

    private JPanel construirePanelMesures() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel haut = new JPanel();
        haut.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        comboPatientsMesures = new JComboBox<>();
        comboCapteursMesures = new JComboBox<>();

        // Ligne patient
        gbc.gridx = 0; gbc.gridy = 0;
        haut.add(new JLabel("Patient :"), gbc);
        gbc.gridx = 1;
        haut.add(comboPatientsMesures, gbc);

        // Ligne capteur
        gbc.gridx = 0; gbc.gridy = 1;
        haut.add(new JLabel("Capteur :"), gbc);
        gbc.gridx = 1;
        haut.add(comboCapteursMesures, gbc);

        // Boutons
        JButton boutonMesureCapteur = new JButton("Mesurer ce capteur");
        boutonMesureCapteur.addActionListener(e -> mesurerCapteurSelectionne());

        JButton boutonMesureTous = new JButton("Mesurer tous les capteurs du patient");
        boutonMesureTous.addActionListener(e -> mesurerTousCapteursPatientSelectionne());

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2;
        haut.add(boutonMesureCapteur, gbc);
        gbc.gridy = 3;
        haut.add(boutonMesureTous, gbc);

        panel.add(haut, BorderLayout.NORTH);

        // Zone de log des mesures
        zoneMesures = new JTextArea();
        zoneMesures.setEditable(false);
        zoneMesures.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        panel.add(new JScrollPane(zoneMesures), BorderLayout.CENTER);

        return panel;
    }

    private void abonnerSelection() {
        Patient p = (Patient) comboPatientsAbonnement.getSelectedItem();
        CapteurConnecte c = (CapteurConnecte) comboCapteursAbonnement.getSelectedItem();

        if (p == null || c == null) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un patient et un capteur.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Éviter les doublons
        for (Abonnement a : abonnements) {
            if (a.getIdPatient().equals(p.getId())
                    && a.getIdCapteur().equals(c.getId())
                    && a.isActif()) {
                JOptionPane.showMessageDialog(this,
                        "Ce patient est déjà abonné à ce capteur.",
                        "Information",
                        JOptionPane.INFORMATION_MESSAGE);
                return;
            }
        }

        abonnements.add(new Abonnement(p.getId(), c.getId(), true));
        rafraichirZoneAbonnements();
    }

    private void rafraichirZoneAbonnements() {
        StringBuilder sb = new StringBuilder();
        if (abonnements.isEmpty()) {
            sb.append("Aucun abonnement.\n");
        } else {
            for (Abonnement a : abonnements) {
                Patient p = trouverPatientParId(a.getIdPatient());
                CapteurConnecte c = trouverCapteurParId(a.getIdCapteur());
                sb.append("- ");
                if (p != null) sb.append(p.getNom().toUpperCase()).append(" ");
                if (p != null) sb.append(p.getPrenom());
                sb.append(" -> ");
                if (c != null) sb.append(c.getNom()).append(" (").append(c.getId()).append(")");
                sb.append(" [actif=").append(a.isActif()).append("]\n");
            }
        }
        if (zoneAbonnements != null) {
            zoneAbonnements.setText(sb.toString());
        }
    }

    private Patient trouverPatientParId(String id) {
        for (Patient p : patients) {
            if (p.getId().equals(id)) return p;
        }
        return null;
    }

    private CapteurConnecte trouverCapteurParId(String id) {
        for (CapteurConnecte c : capteurs) {
            if (c.getId().equals(id)) return c;
        }
        return null;
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

    private void mesurerCapteurSelectionne() {
        CapteurConnecte c = (CapteurConnecte) comboCapteursMesures.getSelectedItem();
        if (c == null) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un capteur.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        double valeur = c.mesurer();
        String message = serviceAlerte.traiterMesure(c, valeur);
        zoneMesures.append(message + "\n");
    }

    private void mesurerTousCapteursPatientSelectionne() {
        Patient p = (Patient) comboPatientsMesures.getSelectedItem();
        if (p == null) {
            JOptionPane.showMessageDialog(this,
                    "Veuillez sélectionner un patient.",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<CapteurConnecte> liste = trouverCapteursPourPatient(p.getId());
        if (liste.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Ce patient n'a aucun capteur abonné.",
                    "Information",
                    JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        zoneMesures.append("Mesures pour " + p + " :\n");
        for (CapteurConnecte c : liste) {
            double valeur = c.mesurer();
            String message = serviceAlerte.traiterMesure(c, valeur);
            zoneMesures.append("  " + message + "\n");
        }
        zoneMesures.append("\n");
    }

    public static void main(String[] args) {
        // Look & feel sympa (optionnel)
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            GestionIoTHopitalUI ui = new GestionIoTHopitalUI();
            ui.setVisible(true);
            ui.rafraichirModeles();
        });
    }
}
