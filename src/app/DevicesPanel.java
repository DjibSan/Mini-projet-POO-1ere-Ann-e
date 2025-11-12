package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Arrays;
import java.util.List;

public class DevicesPanel extends JPanel {
    private final DeviceRepository repo;
    private final DefaultListModel<String> listModel = new DefaultListModel<>();

    public DevicesPanel(DeviceRepository repo) {
        super(new BorderLayout(10, 10));
        this.repo = repo;
        setBorder(new EmptyBorder(16,16,16,16));
        build();
        loadInitial();
    }

    private void build() {
        // Liste
        JList<String> deviceList = new JList<>(listModel);
        deviceList.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(deviceList);

        // Formulaire
        JPanel form = new JPanel(new GridLayout(0, 2, 8, 8));
        JTextField idField = new JTextField();
        JTextField nameField = new JTextField();
        JSpinner limitField = new JSpinner(new SpinnerNumberModel(100.0, -1e9, 1e9, 0.5));

        form.add(new JLabel("ID (unique) :"));
        form.add(idField);
        form.add(new JLabel("Nom :"));
        form.add(nameField);
        form.add(new JLabel("Limite (seuil) :"));
        form.add(limitField);

        JButton addBtn = new JButton("Ajouter l'appareil");
        addBtn.addActionListener((ActionEvent e) -> {
            String id = idField.getText().trim();
            String name = nameField.getText().trim();
            double limit = ((Number) limitField.getValue()).doubleValue();
            if (id.isEmpty() || name.isEmpty()) {
                JOptionPane.showMessageDialog(this, "ID et Nom sont obligatoires.", "Validation", JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {
                Device d = new Device(id, name, limit);
                repo.addDevice(d);
                listModel.addElement(renderLine(d));
                idField.setText("");
                nameField.setText("");
                limitField.setValue(100.0);
                JOptionPane.showMessageDialog(this, "Appareil ajouté.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur ajout appareil : " + ex.getMessage(),
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        JButton seedBtn = new JButton("Ajouter jeux d’essai");
        seedBtn.addActionListener(e -> {
            List<Device> demo = Arrays.asList(
                    new Device("GLUCO-01", "Glucomètre", 1.10),
                    new Device("TENSIO-02", "Tensiomètre", 140.0),
                    new Device("BAL-03", "Balance", 95.0),
                    new Device("PILU-04", "Pilulier", 0.0),
                    new Device("OXY-05", "Oxymètre", 95.0)
            );
            int added = 0;
            for (Device d : demo) {
                try {
                    repo.addDevice(d);
                    listModel.addElement(renderLine(d));
                    added++;
                } catch (Exception ignored) {}
            }
            JOptionPane.showMessageDialog(this, added + " appareil(s) ajouté(s).");
        });

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        actions.add(addBtn);
        actions.add(seedBtn);

        add(new Title("Appareils enregistrés"), BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);

        JPanel south = new JPanel(new BorderLayout(0, 10));
        south.add(new Title("Ajouter un appareil"), BorderLayout.NORTH);
        south.add(form, BorderLayout.CENTER);
        south.add(actions, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);
    }

    private String renderLine(Device d) {
        return String.format("%-12s | %-16s | limite=%.2f", d.id, d.name, d.limit);
    }

    private void loadInitial() {
        try {
            for (Device d : repo.findAllDevices()) {
                listModel.addElement(renderLine(d));
            }
        } catch (Exception ignored) {}
    }
}
