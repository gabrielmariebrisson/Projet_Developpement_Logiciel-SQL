package pdl.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;



@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);

		
		
		//sqlConnection conn = new sqlConnection();
		
		try {
			Connection connection = sqlConnection.getConnection();
			System.out.println("Connexion à la base de données réussie !");
			// Faire d'autres opérations sur la base de données ici...


			java.sql.Statement stmt = connection.createStatement();
			//Statement stmt = (Statement) conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT * FROM users");
			

			while (rs.next()) {
				System.out.println(rs.getString("pseudo"));
			}
			
		} catch (SQLException e) {
			System.out.println("Erreur lors de la connexion à la base de données : " + e.getMessage());
		}


}
}