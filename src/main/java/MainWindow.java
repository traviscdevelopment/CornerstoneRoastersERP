import javax.swing.*;
import javax.xml.crypto.Data;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import static javax.swing.JOptionPane.showMessageDialog;


public class MainWindow {
    //variable declaration
    private JPanel MainPanel;
    private JButton roastingModuleButton;
    private JButton inventoryModuleButton;
    private JButton cuppingModuleButton;

    //action listeners for main window buttons
    public MainWindow() {
        inventoryModuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainPanel,"This Module is Currently Not Implemented.");
                //new DatabaseConnection().test2();
            }
        });
        cuppingModuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainPanel,"This Module is Currently Not Implemented.");

                //new DatabaseConnection().build();

            }
        });
        roastingModuleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //creating roasting module loader window
                JFrame rml = new RoastingModuleLoader().OpenRoastingModuleLoader();
                rml.setVisible(true);
            }
        });
    }

    public static void main(String[] args) {
        new DatabaseConnection().tableCheck();

        //creating main window on software launch
        JFrame frame = new JFrame("MainWindow");
        frame.setContentPane(new MainWindow().MainPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }

}