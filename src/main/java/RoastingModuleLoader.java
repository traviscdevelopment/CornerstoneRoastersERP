import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RoastingModuleLoader {
    private JPanel rml;
    private JButton roastingListButton;
    private JTextField amountOfBagsWantedTextField;
    private JButton submitButton;
    private JButton loadButton;
    private JButton loadSettingsButton;


    //action buttons for the loader
    public RoastingModuleLoader() {
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //submits the bags wanted and creates the roasting session window
                JFrame crs = new CurrentRoastingSession().OpenCurrentRoastingSession(Integer.valueOf(amountOfBagsWantedTextField.getText()));
                crs.setVisible(true);
            }
        });

        //loads the roasting settings window
        loadSettingsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame rs = new RoastingSettings().OpenRoastingSettings();
                rs.setVisible(true);
            }
        });
    }

    //method to open the roasting module loader window
    public JFrame OpenRoastingModuleLoader(){
        JFrame frame = new JFrame();
        frame.setSize(500,300);
        frame.setContentPane(this.rml);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        return frame;
    }
}
