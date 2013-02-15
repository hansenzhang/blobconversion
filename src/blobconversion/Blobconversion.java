/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package blobconversion;
import com.microsoft.sqlserver.jdbc.SQLServerResultSet;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import javax.imageio.ImageIO;

/**
 *
 * @author hzhang
 */
public class Blobconversion {
    private Connection con;
    
    public Blobconversion (String[]args) throws IOException{
        this.con = con;
    }
    
    public void openConnection() throws SQLException{
        try {
            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            String connectionUrl = "jdbc:sqlserver://localhost:1433;" +
            "databaseName=Book_Of_Faces;user=sa;password=Summer2012;";
            con = DriverManager.getConnection(connectionUrl);
            con.setAutoCommit(false);
            System.out.println("Connected.");
        } catch (SQLException e) {
            System.out.println("SQL Exception: "+ e.toString());
        } catch (ClassNotFoundException cE) {
            System.out.println("Class Not Found Exception: "+ cE.toString());
        }
    }
    
    public void closeConnection() throws SQLException {
        try{
            con.close();
            System.out.println("Disconnected.");
        } catch (SQLException e) {
            System.out.println("SQL Exception: "+ e.toString());
        }
    }
    
    public void readBlob() {
        String sqltext = "SELECT BLOBData FROM BLOBTest";
        try {
            Statement st = con.createStatement();
            ResultSet rset = st.executeQuery(sqltext);
            rset.next();
            Blob image = ((SQLServerResultSet)rset).getBlob("BLOBData");
            InputStream binaryStream = image.getBinaryStream();
            BufferedImage imagein = ImageIO.read(binaryStream);
            ImageIO.write(imagein, "jpg", new File("C:/Users/hzhang/test.jpg"));
            System.out.println("Created new file.");
            con.commit();
            rset.close();
            st.close();
        } catch (SQLException | IOException e) {
            System.out.println("caught here" + e.getMessage());
        }
    }
    
    public static void main(String[] args) throws IOException, SQLException {
        Blobconversion blob = new Blobconversion(args);
        try{
            blob.openConnection();
            blob.readBlob();
            blob.closeConnection();
        } catch (Exception e){
            System.out.println("Failed to create file.");
        }
    }
}