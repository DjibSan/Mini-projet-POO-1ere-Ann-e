package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.Instant;

public class ReadingsPanel extends JPanel {
    private final DeviceRepository repo;
    private final JComboBox<String> deviceCombo = new JComboBox<>();
    private final DefaultListModel<String> logModel = new DefaultListModel<>();

    public ReadingsPanel(DeviceRepository repo) {
        super(new BorderLayout(10, 10));
        this.repo = repo;
        setBorder(new EmptyBorder(16,16,16,16));
        build();
        refreshDeviceCombo();
    }

    private void build() {
        // Header
        add(new Title("Ajouter des mesures"), BorderLayout.NORTH);

        // Formulaire
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        JButton refreshBtn = new JButton("Rafraîchir la liste");
        refreshBtn.addActionListener(e -> refreshDeviceCombo());

        JPanel comboLine = new JPanel(new BorderLayout(6, 0));
        comboLine.add(deviceCombo, BorderLayout.CENTER);
        comboLine.add(refreshBtn, BorderLayout.EAST);

        JSpinner valueField = new JSpinner(new SpinnerNumberModel(0.0, -1e9, 1e9, 0.5));

        form.add(new JLabel("Appareil :"));
        form.add(comboLine);
        form.add(new JLabel("Valeur mesurée :"));
        form.add(valueField);

        // Log
        add(new Title("Historique rapide"), BorderLayout.CENTER);
        JList<String> logList = new JList<>(logModel);
        logList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane logScroll = new JScrollPane(logList);

        // Actions
        JButton addReadingBtn = new JButton("Ajouter la mesure");
        addReadingBtn.addActionListener((ActionEvent e) -> {
            String deviceId = (String) deviceCombo.getSelectedItem();
            if (deviceId == null) {
                JOptionPane.showMessageDialog(this, "Aucun appareil sélectionné.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            double value = ((Number) valueField.getValue()).doubleValue();
            try {
                repo.addReading(deviceId, value);
                Device d = repo.getDevice(deviceId);
                String line = String.format("%s | %s => %.2f (limite %.2f)",
                        Instant.now(), deviceId, value, d.limit);
                logModel.addElement(line);

                // 🚨 Alerte si dépassement
                if (value > d.limit) {
                    JOptionPane.showMessageDialog(this,
                            "ALERTE — Valeur " + value + " > limite " + d.limit + " pour " + d.name + " (" + d.id + ")",
                            "Seuil dépassé", JOptionPane.WARNING_MESSAGE);
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur ajout mesure : " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel south = new JPanel(new BorderLayout(0, 10));
        south.add(form, BorderLayout.NORTH);
        south.add(logScroll, BorderLayout.CENTER);
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.add(addReadingBtn);
        south.add(actions, BorderLayout.SOUTH);

        add(south, BorderLayout.SOUTH);
    }

    private void refreshDeviceCombo() {
        deviceCombo.removeAllItems();
        try {
            for (Device d : repo.findAllDevices()) {
                deviceCombo.addItem(d.id);
            }
        } catch (Exception ignored) {}
    }
}
