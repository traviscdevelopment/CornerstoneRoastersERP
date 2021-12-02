import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CurrentRoastingSession {
    private JPanel crs;
    private JPanel roastBatchPanel;
    private JButton closeRoastingSessionButton;
    private JTextField bagsProducedInput;
    private JLabel dateLabel;
    private JScrollPane roastBatchPanel1;
    private int bagsWanted;
    private int roastsPerBag = 2;
    private String date;
    private ArrayList roasts;
    private int roastsNeeded;


    //creates new roasting session window
    public JFrame OpenCurrentRoastingSession(int bagsWanted){
        //sets bags wanted and calls initilzation method
        this.bagsWanted = bagsWanted;

        //new roasting intialization
        newRoastingSessionInti();
        commonIntialization();
        newRoastingSessionInputInti();

        return openForm();
    }

    public JFrame OpenCurrentRoastingSession(ArrayList <RoastingBatch> roasts){
        this.roasts = roasts;
        bagsWanted = roasts.get(1).getBagsWanted();

        commonIntialization();
        cacheRoastingSessionInputInti();

        return openForm();
    }

    private JFrame openForm(){
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

    private void newRoastingSessionInti(){
        //recreate Cache Table since starting a new roast
        new DatabaseConnection().recreateCacheTable();

        //initalizing roasts arraylist for input
        roasts = new ArrayList<RoastingBatch>();
    }

    private void newRoastingSessionInputInti(){
        //Creating Roast Input Form
        for(int x=0; x< roastsNeeded; x++){
            //Batch Numbers
            JLabel label = new JLabel((x + 1) + ")     ");
            label.setHorizontalAlignment(JLabel.RIGHT);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Calibri",Font.PLAIN,18));
            roastBatchPanel.add(label);

            //Batch Input fields and initializing Roasting Batch Objects
            JTextField input = new JTextField(4);
            RoastingBatch rb = new RoastingBatch(x+1,getGramsPerRoast(),bagsWanted);

            //making it so if the Batch input is ever changed it automatically updates the RB object
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

            //adding input field to the panel and control array list
            roastBatchPanel.add(input);
            roasts.add(rb);
        }
    }

    private void cacheRoastingSessionInputInti(){
        //Creating Roast Input Form
        for(int x=0; x< roastsNeeded; x++){
            //Batch Numbers
            JLabel label = new JLabel((x + 1) + ")     ");
            label.setHorizontalAlignment(JLabel.RIGHT);
            label.setForeground(Color.WHITE);
            label.setFont(new Font("Calibri",Font.PLAIN,18));
            roastBatchPanel.add(label);

            //Batch Input fields and initializing Roasting Batch Objects
            JTextField input = new JTextField(4);

            RoastingBatch rb = (RoastingBatch) roasts.get(x);
            input.setText(Double.toString(rb.getGramsProduced()));

            //making it so if the Batch input is ever changed it automatically updates the RB object
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

            //adding input field to the panel and control array list
            roastBatchPanel.add(input);
        }
    }
    //method for setting up and initalizing the current roasting session
    private void commonIntialization(){
        //setting current date into roasting module
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        date = LocalDate.now().format(formatter);
        dateLabel.setText(date);

        //setting bags produced and caclulating roasts needed based on the bags needed
        bagsProducedInput.setText(Integer.toString(bagsWanted));
        roastsNeeded = bagsWanted * roastsPerBag;

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
    }

    //function to get the grams per roast setting from the database
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
