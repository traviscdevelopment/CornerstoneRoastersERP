import javax.swing.*;

public class PastRoastingSessionWindow {
    private JScrollPane pastRoastingSessionsListPanel;
    private JPanel pastRoastingSessionsWindow;
    private JFrame frame;

    public JFrame openPastRoastingSessionsWindow(){
        frame = new JFrame();
        frame.setSize(500,300);
        frame.setContentPane(this.pastRoastingSessionsWindow);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setResizable(false);
        return frame;



    }

}
