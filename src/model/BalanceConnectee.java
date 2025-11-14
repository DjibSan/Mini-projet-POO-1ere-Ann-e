package model;

public class BalanceConnectee extends CapteurConnecte {

    public BalanceConnectee(String id) {
        super(id, "Balance connectée", "kg",
                40, 120, false);
    }

    @Override
    public double mesurer() {
        // Simule un poids entre 40 et 150
        return 40 + Math.random() * 110;
    }
}
