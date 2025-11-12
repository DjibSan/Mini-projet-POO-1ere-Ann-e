package app;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class Title extends JLabel {
    public Title(String text) {
        super(text);
        setFont(getFont().deriveFont(Font.BOLD, 16f));
        setBorder(new EmptyBorder(0, 0, 8, 0));
    }
}
