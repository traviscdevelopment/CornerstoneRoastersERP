import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


@SuppressWarnings("SqlResolve")
public class DatabaseConnection {
    //created INIT Variables
    String urlOpen = "jdbc:derby:crERP;create=true";
    Connection conn;
    Statement state;
    DatabaseMetaData dmbd;
    ResultSet rs;

    //function to open database connection and set variables
    private void openConnection(){
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            conn = DriverManager.getConnection(urlOpen);
            state = conn.createStatement();

        }catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        catch (ClassNotFoundException e){
            e.printStackTrace(System.err);
        }
    }


    //checked if tables exist in database already and if not call their respective create functions
    public void tableCheck(){
        try{
            //sets found tables booleans to false
            boolean roastSettings = false;
            boolean cacheSession = false;
            boolean roastBatches = false;

            openConnection();

            dmbd = conn.getMetaData();
            //pulls list of table names
            rs = dmbd.getTables(null,null,null,null);

            //checks each table name against needed tables and sets variables if found
            while(rs.next()){
                if (rs.getString("TABLE_NAME").equals("ROAST_SETTINGS")){
                    roastSettings = true;
                }
                if (rs.getString("TABLE_NAME").equals("CACHE_SESSION")){
                    cacheSession = true;
                }
                if (rs.getString("TABLE_NAME").equals("ROAST_BATCHES")){
                    roastBatches = true;
                }
            }

            //creates tables not found
            if(!roastSettings){
                createRoastSettingsTable();
            }
            if(!cacheSession){
                createCacheSession();
            }
            if(!roastBatches){
                createRoastBatches();
            }



        }catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }

    //creates and initalizes the roast settings table
    private void createRoastSettingsTable() throws SQLException {
        state.execute("create table roast_settings(settingName varchar(8), value varchar(8))");
        PreparedStatement ps = conn.prepareStatement("insert into roast_settings values(?,?)");

        ps.setString(1,"rpb");//roasts per bag
        ps.setString(2,"2");
        ps.executeUpdate();

        ps.setString(1,"gpb");//grams per bag
        ps.setString(2,"397");
        ps.executeUpdate();

        ps.setString(1,"gpr");//grams per roast
        ps.setString(2,"250");
        ps.executeUpdate();
    }

    //function to get the settings from the roast settings database
    public ResultSet getRoastSettings(){
        try{
            openConnection();

            return rs = state.executeQuery("SELECT settingname, value FROM roast_settings");
        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }

    //function for changing roast settings
    public void setRoastSettings(int rpb, int gpb, int gpr){
        try{
            openConnection();

            PreparedStatement ps = conn.prepareStatement("update roast_settings set value=? where settingname=?");

            ps.setInt(1,rpb);
            ps.setString(2,"rpb");
            ps.executeUpdate();

            ps.setInt(1,gpb);
            ps.setString(2,"gpb");
            ps.executeUpdate();

            ps.setInt(1,gpr);
            ps.setString(2,"gpr");
            ps.executeUpdate();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    //creates Cache Session Table for roasting session saving
    private void createCacheSession() throws SQLException {
        state.execute("create table cache_session(roastNumber SMALLINT NOT NULL PRIMARY KEY , gramsUsed DOUBLE, " +
                "gramsProduced DOUBLE, poundsUsed DOUBLE, poundsProduced DOUBLE, bagsWanted SMALLINT)");
    }

    //recreates cache table when new roasting session is started
    public void recreateCacheTable(){
        try{
            openConnection();

            state.execute("DROP TABLE CACHE_SESSION");

            createCacheSession();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    //function to return all the cache info
    public ResultSet loadCacheSession(){

        try {
            openConnection();
            ResultSet rs = state.executeQuery("SELECT * FROM CACHE_SESSION");
            return rs;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    //function to update the cache session, first it tried an insert and on failure updates the correct rows data
    public void setCacheSession(int roastNumber,double gramsUsed,
                                double gramsProduced,double poundsUsed,double poundsProduced, int bagsWanted){
        try{
            openConnection();

            PreparedStatement ps = conn.prepareStatement("INSERT INTO CACHE_SESSION (ROASTNUMBER, " +
                    "GRAMSUSED, GRAMSPRODUCED, POUNDSUSED, POUNDSPRODUCED, BAGSWANTED) VALUES(?,?,?,?,?,?)");

            ps.setInt(1,roastNumber);
            ps.setDouble(2,gramsUsed);
            ps.setDouble(3,gramsProduced);
            ps.setDouble(4,poundsUsed);
            ps.setDouble(5,poundsProduced);
            ps.setInt(6,bagsWanted);
            ps.executeUpdate();
        }
        catch (DerbySQLIntegrityConstraintViolationException err){
            try{
                PreparedStatement ps = conn.prepareStatement("update CACHE_SESSION SET GRAMSPRODUCED=?, POUNDSPRODUCED=? WHERE ROASTNUMBER=?");

                ps.setInt(3,roastNumber);
                ps.setDouble(2,poundsProduced);
                ps.setDouble(1,gramsProduced);
                ps.executeUpdate();


            }catch(SQLException errr){
                errr.printStackTrace();
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    //creates and initalizes the roast batch summary table
    private void createRoastBatches() throws SQLException {
        state.execute("create table roast_batches(roast_ID INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1),amount_of_roasts SMALLINT, " +
                "grams_used DOUBLE, grams_produced DOUBLE, pounds_used DOUBLE, pounds_produced DOUBLE, bags_produced SMALLINT, " +
                "hry DOUBLE, LRY DOUBLE,roasting_date VARCHAR(10))");
        PreparedStatement ps = conn.prepareStatement("insert into roast_batches (AMOUNT_OF_ROASTS, GRAMS_USED, GRAMS_PRODUCED, POUNDS_USED, POUNDS_PRODUCED, BAGS_PRODUCED, HRY, LRY, ROASTING_DATE) values(?,?,?,?,?,?,?,?,?)");

        ps.setInt(1,4);
        ps.setDouble(2,1000);
        ps.setDouble(3,838);
        ps.setDouble(4,2.20462);
        ps.setDouble(5,1.84747);
        ps.setInt(6,2);
        ps.setDouble(7,210);
        ps.setDouble(8, 208);

        LocalDate date = LocalDate.of(2021,12,2);

        ps.setString(9, date.toString());
        ps.executeUpdate();
    }

    public void insertRoastBatches(RoastSummary roastSummary){
        try{
            openConnection();

            PreparedStatement ps = conn.prepareStatement("insert into roast_batches (AMOUNT_OF_ROASTS, GRAMS_USED, GRAMS_PRODUCED, POUNDS_USED, POUNDS_PRODUCED, BAGS_PRODUCED, HRY, LRY, ROASTING_DATE) values(?,?,?,?,?,?,?,?,?)");

            ps.setInt(1,roastSummary.getAmountOfRoasts());
            ps.setDouble(2,roastSummary.getGramsUsed());
            ps.setDouble(3,roastSummary.getGramsProduced());
            ps.setDouble(4,roastSummary.getPoundsUsed());
            ps.setDouble(5,roastSummary.getPoundsProduced());
            ps.setInt(6,roastSummary.getBagsProduced());
            ps.setDouble(7,roastSummary.getHry());
            ps.setDouble(8, roastSummary.getLry());
            ps.setString(9, roastSummary.getRoastingDate());
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

}
