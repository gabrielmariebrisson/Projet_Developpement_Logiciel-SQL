package pdl.backend;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;



public class Image {
    private static Long count = Long.valueOf(0);
    private Long id;
    private String name;
    private String tag;
    private String dateCreation;
    private String chemin;
    private byte[] data;

    public Image(String name,String tag, String dateCreation,String path) {
        this.id = count;
        this.name = name;
        this.tag = tag;
        this.dateCreation = dateCreation;
        this.chemin=path;
        setData(path);
        /* 
        try {
            System.out.println("before");
            ImageTagger.tagImage();
            System.out.println("label");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }*/
        System.out.println("fin new image ");
    }
    public void setId(String name,String path, int iduser) {
        try {
          Connection connection = sqlConnection.getConnection();
          PreparedStatement statement = connection.prepareStatement(
            "SELECT idImage FROM images WHERE nameImage = ?  AND path = ? AND iduser = ? and statut_idStatut != 2");

            statement.setString(1, name);
            statement.setString(2, path);
            statement.setInt(3, iduser);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int idImage = rs.getInt("idImage");
                this.id = (long) idImage;
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public void setId(String name, String dateCreation,String path, int iduser) {
        try {
          Connection connection = sqlConnection.getConnection();
          PreparedStatement statement = connection.prepareStatement(
            "SELECT idImage FROM images WHERE nameImage = ? AND dateCreation = ? AND path = ? AND iduser = ? and statut_idStatut != 2");
            statement.setString(1, name);
            statement.setString(2, dateCreation);
            statement.setString(3, path);
            statement.setInt(4, iduser);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                int idImage = rs.getInt("idImage");
                this.id = (long) idImage;
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public long getId() {
        return id;
    }

    public String getTag() {
        return tag;
    }

    public String getName() {
        return name;
      }

    public void setName(final String name) {
        this.name = name;
    }
    
    
    public String getDateCreation() {
        return dateCreation;
    }

    public String getChemin() {
        return chemin;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, dateCreation, chemin);
    }
    
    public void updateDatabase(int getid,int getIdStatut) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = formatter.format(getDateCreation());
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = sqlConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "UPDATE images SET (nameImage, dateCreation, path, iduser,statut_idStatut) VALUES (?, ?, ?, ?, ?) WHERE NOT EXISTS (SELECT path FROM images WHERE path = ?) " );
            statement.setString(1, getName());
            statement.setString(2, dateString);
            statement.setString(3, getChemin());
            statement.setInt(4, getid);
            statement.setInt(5, getIdStatut);
            statement.setString(6, getChemin());
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            ((Throwable) e).printStackTrace();
        }
    }

    public byte[] getData() {
        return data;
    }

    private void setData(final String chemin){
        File imageFile = new File(chemin);
        FileInputStream fis;
    
        try {
            fis = new FileInputStream(imageFile);
            byte[] data = new byte[(int) imageFile.length()]; // créer un tableau de la taille de l'image
            fis.read(data); // lire les données de l'image dans le tableau
            fis.close();
            this.data=data;
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
