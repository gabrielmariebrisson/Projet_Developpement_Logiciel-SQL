package pdl.backend;

import java.awt.image.BufferedImage;
import java.sql.Date;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//import org.springframework.boot.autoconfigure.amqp.RabbitProperties.Cache.Connection;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.mysql.cj.xdevapi.Statement;



public class User {

    private int iduser = 1;
    private String pseudo;
    private String password;
    private String mail;
    private Date birthday;
    private String dateInscription = LocalDateTime.now().format( DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    private int coins = 0;
    private int autorisation = 1;
    private int id = iduser;

    private Statut statut;

    public User(String pseudo, String password, String mail, Date birthday) {

        this.pseudo = pseudo;
        this.password = password;
        this.mail = mail;
        this.birthday = birthday;
        this.statut = new Statut(0, Statut.getNameWithIdStatut(0));
    }

 public User(int idUser, String pseudo, String password, String mail, Date birthday, int coins, Statut statut) {
        this.iduser = idUser;
        this.pseudo = pseudo;
        this.password = password;
        this.mail = mail;
        this.birthday = birthday;
        this.coins = coins;
        this.id = idUser;
        this.statut = statut;
    }

    public User( String pseudo, String password, String mail, Date birthday, String dateInscription, int coins, int autorisation) {
   
        this.pseudo = pseudo;
        this.password = password;
        this.mail = mail;
        this.birthday = birthday;
        this.dateInscription = dateInscription;
        this.coins = coins;
        this.autorisation  = autorisation;
        this.statut = new Statut(0, Statut.getNameWithIdStatut(0));
    }

    public User( String pseudo, String password, String mail, Date birthday, String dateInscription, int coins, int autorisation, Statut statut) {
   
        this.pseudo = pseudo;
        this.password = password;
        this.mail = mail;
        this.birthday = birthday;
        this.dateInscription = dateInscription;
        this.coins = coins;
        this.autorisation  = autorisation;
        this.statut = statut;
    }

    public User(int iduser, String pseudo, String password, String mail, Date birthday, String dateInscription, int coins, int autorisation, Statut statut) {
        this.iduser = iduser;
        this.pseudo = pseudo;
        this.password = password;
        this.mail = mail;
        this.birthday = birthday;
        this.dateInscription = dateInscription;
        this.coins = coins;
        this.autorisation  = autorisation;
        this.statut = statut;
        this.id = iduser;






 


    }
    public int getid() {
        return iduser;
    }

    public String getpseudo() {
        return pseudo;
    }

    public void setpseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getmail() {
        return mail;
    }

    public void setmail(String mail) {
        this.mail = mail;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(String dateInscription) {
        this.dateInscription = dateInscription;
    }

    public String getDateInscription() {
        return dateInscription;
    }

    public void setDateInscription(Date birthday) {
        this.birthday = birthday;
    }
    

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }

    public int getAutorisation() {
        return autorisation;
    }

    public void setAutorisation(int autorisation) {
        this.autorisation = autorisation;
    }

    public Statut getStatut() {
        return this.statut;
    }

    public void setStatut_idStatut(Statut statut) {
        this.statut = statut;
    }


    public static User getUser(int idUser) 
    {
        try 
        {
            System.out.println("getUser 1");

            Connection connection2 = sqlConnection.getConnection();
            PreparedStatement statement = connection2.prepareStatement("SELECT * FROM users WHERE idUser = ?");
            statement.setInt(1, idUser);
              ResultSet rs = statement.executeQuery();
              if (rs.next()) {
           
                String pseudo = rs.getString("pseudo");
                String password = rs.getString("password");
                String mail = rs.getString("mail");
                Date birthday = rs.getDate("dateNaissance");
                String dateInscription = rs.getString("dateInscription");
                int coins = rs.getInt("coins");
                int autorisation = rs.getInt("autorisation");

                int statut_idStatut = rs.getInt("statut_idStatut");
                String nameStatut = Statut.getNameWithIdStatut( statut_idStatut);
                Statut statut = new Statut(statut_idStatut, nameStatut); 
                User user = new User(idUser, pseudo, password,  mail, birthday, dateInscription, coins, autorisation, statut);

                System.out.println("getUser 3");
                System.out.println("GetUer : "+ pseudo + " et id : " + idUser);
                return user;
              }
              statement.close();
              connection2.close();
        } catch (SQLException e) 
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            
        }
        return null;

    }


   
}
