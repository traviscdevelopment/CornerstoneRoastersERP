import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
        loadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ResultSet rs = new DatabaseConnection().loadCacheSession();

                ArrayList<RoastingBatch> roasts = new ArrayList<RoastingBatch>();
                try {
                    while (rs.next()) {
                        roasts.add(new RoastingBatch(rs.getInt(1),rs.getDouble(2),
                                rs.getInt(6),rs.getDouble(3)));
                    }
                }catch(SQLException exception){exception.printStackTrace();}


                JFrame crs = new CurrentRoastingSession().OpenCurrentRoastingSession(roasts);
                crs.setVisible(true);
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
