import org.apache.commons.dbutils.DbUtils;
import org.apache.derby.shared.common.error.DerbySQLIntegrityConstraintViolationException;

import java.sql.*;


public class DatabaseConnection {
    //created INIT Variables
    String urlOpen = "jdbc:derby:crERP;create=true";
    String urlClose = "jdbc:derby:;shutdown=true";
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
            }

            //creates tables not found
            if(!roastSettings){
                createRoastSettingsTable();
            }
            if(!cacheSession){
                createCacheSession();
            }




        }catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }

    public void recreateCacheTable(){
        try{
            openConnection();

            state.execute("DROP TABLE CACHE_SESSION");

            createCacheSession();
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    private void createCacheSession() throws SQLException {
        state.execute("create table cache_session(roastNumber SMALLINT NOT NULL PRIMARY KEY , gramsUsed DOUBLE, " +
                "gramsProduced DOUBLE, poundsUsed DOUBLE, poundsProduced DOUBLE, bagsWanted SMALLINT)");
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

    public ResultSet getRoastSettings(){
        try{
            openConnection();

            return rs = state.executeQuery("SELECT settingname, value FROM roast_settings");
        }catch(SQLException e){
            e.printStackTrace();
        }

        return null;
    }

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

}
