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
            dmbd = conn.getMetaData();
        }catch (SQLException e) {
            e.printStackTrace(System.err);
        }catch (ClassNotFoundException e){
            e.printStackTrace(System.err);
        }
    }

    //function to close database connection
    private void connectionClose(){
        try{DriverManager.getConnection(urlClose);}
        catch (java.sql.SQLException e){
            if((e.getErrorCode() == 50000) && ("XJ015".equals(e.getSQLState()))){
                System.out.println("Derby Shut Down Normal");
            }else{
                System.out.println("Derby did not shutdown correctly");
                e.printStackTrace();
            }
        }
    }

    //checked if tables exist in database already and if not call their respective create functions
    public void tableCheck(){
        try{
            //sets found tables booleans to false
            boolean roastSettings = false;

            openConnection();

            //pulls list of table names
            rs = dmbd.getTables(null,null,null,null);

            //checks each table name against needed tables and sets variables if found
            while(rs.next()){
                if (rs.getString("TABLE_NAME").equals("ROAST_SETTINGS")){
                    roastSettings = true;
                }
            }

            //creates tables not found
            if(!roastSettings){
                createRoastSettingsTable();
            }


        }catch(SQLException e){
            e.printStackTrace(System.err);
        }finally {
            connectionClose();
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
    }

}
