package pdl.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.mysql.cj.xdevapi.Statement;

public class sqlConnection {

    public static Connection getConnection() throws SQLException {
        System.out.println("Connexion à SQL PDL début");
        String url = "jdbc:mysql://localhost:3306/pdl";
        String username = "root";
        //String password = "PDLpassword";
        String password = "";
        Connection conn = DriverManager.getConnection(url, username, password);
        System.out.println("Connexion à SQL PDL success fin");
        return conn;
    }

    public Statement createStatement() {
        return null;
    }
}
