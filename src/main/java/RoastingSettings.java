import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RoastingSettings {
    //intializing variables
    JFrame frame;
    private JButton submitChangesButton;
    private JTextField rpbInput;
    private JTextField gpbInput;
    private JPanel rsMain;
    private JTextField gprInput;

    public RoastingSettings() {
        //save button for changes for Roasting Settings
        submitChangesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //System.out.println(Integer.valueOf(rpbInput.getText()));

                try{
                    //converts input to ints
                    int rpb = Integer.valueOf(rpbInput.getText());
                    int gpb = Integer.valueOf(gpbInput.getText());
                    int gpr = Integer.valueOf(gprInput.getText());

                    //input validation
                    if(rpb < 1 || gpb < 1 || rpb > 30 || gpb > 600 || gpr < 1 || gpr > 600){
                        JOptionPane.showMessageDialog(frame,"Numbers not in a valid range.");
                        throw new IllegalArgumentException();
                    }
                    //saving the input into the database confirming the save and shutting the window down
                    new DatabaseConnection().setRoastSettings(rpb,gpb,gpr);
                    JOptionPane.showMessageDialog(frame,"Settings Saved");
                    frame.dispose();
                }
                catch(Throwable x){
                    JOptionPane.showMessageDialog(frame,"Settings Not Saved. Check Input and try again.");
                }
            }
        });
    }

    //opens roasting settings window and validates the input from the database
    public JFrame OpenRoastingSettings(){
        try{
            //pulls the settings from the database and sets up the window
            ResultSet rs = new DatabaseConnection().getRoastSettings();
            if(rs != null){
                while(rs.next()){
                    switch (rs.getString(1)){
                        case "rpb":
                            rpbInput.setText(String.valueOf(rs.getInt(2)));
                            break;
                        case "gpb":
                            gpbInput.setText(String.valueOf(rs.getInt(2)));
                            break;
                        case "gpr":
                            gprInput.setText(String.valueOf(rs.getInt(2)));
                            break;
                        default:
                            System.out.println("No match found");
                    }
                }
            }}catch(SQLException e){
            e.printStackTrace();
        }

        //creates the settings window
        frame = new JFrame();
        frame.setSize(340,200);
        frame.setContentPane(this.rsMain);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setResizable(false);
        return frame;
    }
}
