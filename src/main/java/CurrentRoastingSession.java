import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;

public class CurrentRoastingSession {
    private JPanel crs;
    private JPanel roastBatchPanel;
    private JButton closeRoastingSessionButton;
    private JTextField bagsProducedInput;
    private JLabel dateLabel;
    private JScrollPane roastBatchPanel1;
    private int bagsWanted;
    private int roastsPerBag = 2;
    private ArrayList roasts;
    private String date;
    private ArrayList roasts2;


    //creates roasting session window
    public JFrame OpenCurrentRoastingSession(int bagsWanted){
        //sets bags wanted and calls initilzation method
        this.bagsWanted = bagsWanted;
        intialization();

        //creates window and sets default size based on amount of bags being requested on default
        JFrame frame = new JFrame();
        if(bagsWanted < 4 ){
            frame.setSize(500,350);
        }
        else{
            frame.setSize(500,600);
        }
        frame.setContentPane(this.crs);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        return frame;
    }

    //method for setting up and initalizing the current roasting session
    private void intialization(){
        new DatabaseConnection().recreateCacheTable();

        //setting current date into roasting module
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        date = LocalDate.now().format(formatter);
        dateLabel.setText(date);

        //setting bags produced and caclulating roasts needed based on the bags needed
        bagsProducedInput.setText(Integer.toString(bagsWanted));
        int roastsNeeded = bagsWanted * roastsPerBag;

        //setting up the roasting batch pannel
        roastBatchPanel.setLayout(new GridLayout(0,2));
        roastBatchPanel.setBackground(Color.black);

        //creating titles for roasting session
        JLabel[] titles = new JLabel[]{new JLabel("Roast"),new JLabel("Roast Yield"),new JLabel("Number"),new JLabel("(In Grams)")};
        for (int y = 0; y<titles.length;y++){
            //formatting titles
            titles[y].setHorizontalAlignment(JLabel.CENTER);
            titles[y].setForeground(Color.WHITE);
            titles[y].setFont(new Font("Calibri",Font.BOLD,25));

            if(y == titles.length - 1){
                titles[y].setFont(new Font("Calibri",Font.ITALIC,15)); //making "In grams" different
            }
            roastBatchPanel.add(titles[y]);
        }

        //initalizing roasts arraylist for input
        roasts = new ArrayList<JTextField>();
        roasts2 = new ArrayList<RoastingBatch>();

        //creating roasts input form based on roasts needed
        for(int x=0; x< roastsNeeded; x++){
            JLabel label = new JLabel((x + 1) + ")     ");//roast title and formating
            label.setHorizontalAlignment(JLabel.RIGHT);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Calibri",Font.PLAIN,18));
            roastBatchPanel.add(label);

            JTextField input = new JTextField(4);



            RoastingBatch rb = new RoastingBatch(x+1,getGramsPerRoast(),bagsWanted);



            input.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                public void insertUpdate(DocumentEvent e) {
                    try{
                        rb.setgramsProduced(Double.valueOf(input.getText()));}
                    catch (NumberFormatException err){
                        rb.setgramsProduced(0);
                    }
                }

                @Override
                public void removeUpdate(DocumentEvent e) {
                    try{
                        rb.setgramsProduced(Double.valueOf(input.getText()));}
                    catch (NumberFormatException err){
                        rb.setgramsProduced(0);
                    }

                }

                @Override
                public void changedUpdate(DocumentEvent e) {
                    try{
                        rb.setgramsProduced(Double.valueOf(input.getText()));}
                    catch (NumberFormatException err){
                        rb.setgramsProduced(0);
                    }
                }
            });


            roastBatchPanel.add(input);
            roasts2.add(rb);
        }

    }

    private double getGramsPerRoast(){
        try{
        ResultSet rs = new DatabaseConnection().getRoastSettings();
        while(rs.next()){
            if (rs.getString(1).equals("gpr")){
                return rs.getDouble(2);
            }
        }}catch(SQLException e){};
        return 0;
    }


}
