import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class PastRoastingSessionWindow {
    private JPanel pastRoastingSessionsListPanel;
    private JPanel pastRoastingSessionsWindow;
    private JFrame frame;

    public JFrame openPastRoastingSessionsWindow(){
        loadList();

        frame = new JFrame();
        frame.setSize(280,300);
        frame.setContentPane(this.pastRoastingSessionsWindow);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setResizable(false);
        return frame;
    }

    private void loadList(){
        ArrayList<RoastSummary> list = new DatabaseConnection().getAllPastRoasts();

        pastRoastingSessionsListPanel.setLayout(new GridLayout(0,1));


        for (int x = 0; x< list.size(); x++) {
            RoastSummary rs = list.get(x);

            System.out.println(rs.getRoastingDate());

            JButton jb = new JButton((x+1) + ") " + rs.getRoastingDate());

            jb.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    new PastSessionInfo().PastSessionInfo(rs);
                }
            });

            pastRoastingSessionsListPanel.add(jb);
            pastRoastingSessionsListPanel.add(Box.createHorizontalGlue());
        }

    }

}
