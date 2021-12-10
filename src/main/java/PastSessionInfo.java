import javax.swing.*;

public class PastSessionInfo {
    private JPanel pastSessionWindow;
    private JLabel roastDateLabel;
    private JLabel amrLabel;
    private JLabel gramsUsedLabel;
    private JLabel gramsProducedLabel;
    private JLabel poundsUsedLabel;
    private JLabel poundsProducedLabel;
    private JLabel bagsProducedLabel;
    private JLabel hryLabel;
    private JLabel lryLabel;

    public void PastSessionInfo(RoastSummary rs){
        roastDateLabel.setText(rs.getRoastingDate());
        amrLabel.setText(String.valueOf(rs.getAmountOfRoasts()));
        gramsUsedLabel.setText(String.valueOf(rs.getGramsUsed()));
        gramsProducedLabel.setText(String.valueOf(rs.getGramsProduced()));
        poundsUsedLabel.setText(String.valueOf(rs.getPoundsUsed()));
        poundsProducedLabel.setText(String.valueOf(rs.getPoundsProduced()));
        bagsProducedLabel.setText(String.valueOf(rs.getBagsProduced()));
        hryLabel.setText(String.valueOf(rs.getHry()));
        lryLabel.setText(String.valueOf(rs.getLry()));

        JFrame frame = new JFrame();
        frame.setSize(280,300);
        frame.setContentPane(this.pastSessionWindow);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //frame.setResizable(false);
        frame.setVisible(true);
    }

}
