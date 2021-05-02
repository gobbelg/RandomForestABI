package src.main.gov.va.vha09.grecc.raptat.gg.oneoffs.io;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import src.main.gov.va.vha09.grecc.raptat.gg.helpers.GeneralHelper;

public class SQLTextExtractor {
  private static final int REPORT_ID_COLUMN = 1, TEXT_COLUMN = 2;


  String storageDirectory = "";

  public SQLTextExtractor() {
    this.storageDirectory =
        GeneralHelper.getDirectory("Select directory" + " for storing files and concept mappings")
            .getAbsolutePath();
  }


  private void writeFile(String recordID, String textOfFile) {
    PrintWriter pw = null;
    String textPath = this.storageDirectory + File.separator + recordID + ".txt";
    File outFile = new File(textPath);
    try {
      pw = new PrintWriter(outFile);
      pw.write(recordID + "\n\n");
      // pw.write(textOfFile);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } finally {
      if (pw != null) {
        pw.close();
      }
    }
  }


  public static void main(String[] args) {
    SQLTextExtractor extractor = new SQLTextExtractor();
    String connectionUrl = "jdbc:sqlserver://vhaechcartan01.v19.med.va.gov;"
        + "databaseName=NLP;integratedsecurity=true;user=VHA09\\vhatvhgobbeg;password=fota$*_@!hsev32;";
    String dbTitle = "StressTest554Temp";
    String queryStart = "Select top 10 " + "reportID, text FROM " + dbTitle;
    Connection con = null;
    ResultSet rs;

    try {
      Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      con = DriverManager.getConnection(connectionUrl);
      Statement stmt = con.createStatement();

      GeneralHelper.tic("Starting SQL queries");
      rs = stmt.executeQuery(queryStart);
      while (rs.next()) {
        String curID = rs.getString(SQLTextExtractor.REPORT_ID_COLUMN);
        System.out.println("Current ID:" + curID);
        extractor.writeFile(curID, rs.getString(SQLTextExtractor.TEXT_COLUMN));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();

    } finally {

      try {
        con.close();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
}
