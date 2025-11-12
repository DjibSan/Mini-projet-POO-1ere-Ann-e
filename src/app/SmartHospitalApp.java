package app;

import javax.swing.*;

public class SmartHospitalApp {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                DeviceRepository repo = new InMemoryRepository(); // <-- mémoire uniquement
                repo.init();

                JFrame f = new JFrame("Smart Hospital — Démo seuils & alertes");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

                JTabbedPane tabs = new JTabbedPane();
                tabs.addTab("Appareils", new DevicesPanel(repo));
                tabs.addTab("Mesures", new ReadingsPanel(repo));

                f.setContentPane(tabs);
                f.setSize(820, 560);
                f.setLocationRelativeTo(null);
                f.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(null, "Erreur au démarrage : " + e.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
