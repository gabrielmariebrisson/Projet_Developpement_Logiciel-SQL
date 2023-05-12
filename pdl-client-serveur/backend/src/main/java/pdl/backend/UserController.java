package pdl.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.text.SimpleDateFormat;


@Controller
public class UserController {

    @Autowired
    private DataSource dataSource;

    

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(HttpServletRequest request,
                        @RequestParam(value = "pseudo") String pseudo,
                        @RequestParam(value = "password") String password, 
                        @RequestParam(value = "mail") String mail,  
                        @RequestParam(value = "birthday") Date birthday) {

        System.out.println("register 1");
        User user = new User(pseudo, password, mail, birthday);
        
        // // Obtention de la date et l'heure actuelles
        // LocalDateTime now = LocalDateTime.now();
        // // Définition du format d'affichage de la date et l'heure
        // DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // // Formatage de la date et l'heure en utilisant le format spécifié
        // String formattedDateTime = now.format(formatter);
        
        String dateInscription = user.getDateInscription();
            int coins = user.getCoins();
            int autorisation = user.getAutorisation();
            Statut statut = user.getStatut();
           int idStatut = statut.getIdStatut();
        try {
            

            Connection connection;
            connection = sqlConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement
("INSERT INTO users (pseudo, password, mail, dateNaissance, dateInscription, coins, autorisation, statut_idStatut) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
            stmt.setString(1, pseudo);
            stmt.setString(2, password);
            stmt.setString(3, mail);
            stmt.setDate(4, birthday);
            stmt.setString(5, dateInscription);
            stmt.setInt(6, coins);
            stmt.setInt(7, autorisation);
            stmt.setInt(8, idStatut);
            stmt.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
            return "error";
        }

        return "success";
    }



    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public List<User> getAllUsers() 
    {
        System.out.println("getAllUser 1");

        List<User> users = new ArrayList<>();
        try {
            Connection connection = sqlConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int iduser = rs.getInt("idUser");
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

                

                User user = new User(iduser, pseudo, password,  mail, birthday, dateInscription, coins, autorisation, statut);
                users.add(user);
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public User postLogin(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "mail") String mail,
                            @RequestParam(value = "password") String password) throws IOException 
                            
    {
        System.out.println("login 1 ");
        //List<User> users = new ArrayList<>();
        try 
        {

            Connection connection = sqlConnection.getConnection();


            //HttpSession session = request.getSession();
            //System.out.println("end 2 ");
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE mail = ? AND password = ? AND statut_idStatut != 2");
            stmt.setString(1, mail);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            //System.out.println("end 3 ");
            if (rs.next()) 
            {    
                // Successful login

                System.out.println("end 4 ");

                int iduser = rs.getInt("idUser");
                String pseudo = rs.getString("pseudo");
                String password2 = rs.getString("password");
                String mail2 = rs.getString("mail");
                Date birthday = rs.getDate("dateNaissance");
                String dateInscription = rs.getString("dateInscription");
                int coins = rs.getInt("coins");
                int autorisation = rs.getInt("autorisation");

                int statut_idStatut = rs.getInt("statut_idStatut");
                String nameStatut = Statut.getNameWithIdStatut( statut_idStatut);
                Statut statut = new Statut(statut_idStatut, nameStatut); 
          

                System.out.println("Session open de : "+ pseudo + "et id : " + iduser);
                
               

                User user = new User(iduser, pseudo, password2,  mail2, birthday, dateInscription, coins, autorisation, statut);
                

                // response.sendRedirect("/");

                connection.close();

                return user;
                
            } 
            

            
        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }

        return null;
    }




    @RequestMapping(value = "/modify", method = RequestMethod.POST)
    public void modify(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "pseudo") String pseudo,
                        @RequestParam(value = "password") String password, 
                        @RequestParam(value = "mail") String mail,  
                        @RequestParam(value = "birthday") Date birthday,
                        @RequestParam(value = "statut") String statut, 
                        @RequestParam(value = "monId") Number id) throws IOException
    {
        System.out.println("modify 1");
        try 
        {
            int idStatut;
            System.out.println("statut est : " +statut);
            if (statut.equals("private"))
            {
                System.out.println("le statut est maitenent private " );
                idStatut = 0;
            }
            else if(statut.equals("public"))
            {
                System.out.println("le statut est maitenent public ");
                idStatut = 1;
            }
            else
            {
                System.out.println("le statut est maitenent corrupted");
                idStatut = -1;
            }
            Connection connection = sqlConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE users SET pseudo = ?, password = ?, mail = ?, dateNaissance = ?, statut_idStatut = ? WHERE idUser = ?" );
            stmt.setString(1, pseudo);
            stmt.setString(2,password);
            stmt.setString(3, mail);
            stmt.setDate(4, birthday);
            stmt.setInt(5, idStatut);
            stmt.setInt(6, id.intValue());
            stmt.executeUpdate();

            connection.close();

        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }




    @RequestMapping(value = "/winCoins", method = RequestMethod.POST)
    public void winCoins(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "monId") Number id,
                        @RequestParam(value = "coins") Number coins)  throws IOException
    {
        System.out.println("coins 1");
        try 
        {
            System.out.println("coins 2");
            Connection connection;
            connection = sqlConnection.getConnection();

            PreparedStatement stmt = connection.prepareStatement("UPDATE users SET coins = coins + ? WHERE idUser = ?" );
            stmt.setInt(1, coins.intValue());
            stmt.setInt(2, id.intValue());
            stmt.executeUpdate();
            System.out.println("coins 3");

            connection.close();

        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    }
    
    
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public void delete(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "monId") Number id) throws IOException
    {
        System.out.println("delete 1");
        try 
        {

            Connection connection;
            connection = sqlConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE users SET statut_idStatut = 2 WHERE idUser = ?" );
            stmt.setInt(1, id.intValue());
            stmt.executeUpdate();

            connection.close();

        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    } 

    @RequestMapping(value = "/restore", method = RequestMethod.POST)
    public void restore(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "idUser") Number id) throws IOException
    {
        System.out.println("restore 1");
        try 
        {

            Connection connection;
            connection = sqlConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE users SET statut_idStatut = 0 WHERE idUser = ?" );
            stmt.setInt(1, id.intValue());
            stmt.executeUpdate();

            connection.close();

        }
        catch (SQLException e) 
        {
            e.printStackTrace();
        }
    } 


    @RequestMapping(value = "/getUsersCoins", method = RequestMethod.POST)
    @ResponseBody
    public List<User> getUsersCoins() 
    {
        System.out.println("get3User 1");

      List<User> users = new ArrayList<>();
           try {
               Connection connection = sqlConnection.getConnection();
               PreparedStatement stmt = connection.prepareStatement("SELECT idUser, pseudo, coins, statut_idStatut FROM users ORDER BY coins DESC LIMIT 3;");
               ResultSet rs = stmt.executeQuery();
                while (rs.next()) {
                    int iduser = rs.getInt("idUser");
                    String pseudo = rs.getString("pseudo");
                    int coins = rs.getInt("coins");
                    

                    int statut_idStatut = rs.getInt("statut_idStatut");
                    String nameStatut = Statut.getNameWithIdStatut( statut_idStatut);
                    Statut statut = new Statut(statut_idStatut, nameStatut); 

                    String password = "";
                    String mail = "";
                   
                    Date birthday = null;

                    
                     User user = new User(iduser, pseudo, password,  mail, birthday, coins, statut) ;
                     users.add(user);
               }
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
             return users;
    }


   


}