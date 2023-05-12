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



public class ImageClement {

    private int idImage;
    private String name;
    //private String tag;
    private String dateCreation;
    private String path;

    private User user; 
    private Statut statutI;

    private int idImg;






    public ImageClement(int idImage, String name, String dateCreation, String path, User user, Statut statut) 
    {
        this.idImage = idImage;
        this.name = name;
        this.dateCreation = dateCreation;
        this.path = path;
        this.user = user;
        this.statutI = statut;
        this.idImg = idImage;
        
    }

     // Getters
     public int getIdImage() {
        return idImage;
    }

    public String getName() {
        return name;
    }

    public String getDateCreation() {
        return dateCreation;
    }

    public String getPath() {
        return path;
    }

    public User getUser() {
        return user;
    }

    public Statut getStatutI() {
        return statutI;
    }

    public int getIdImg() {
        return idImg;
    }

    // Setters
    public void setIdImage(int idImage) {
        this.idImage = idImage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDateCreation(String dateCreation) {
        this.dateCreation = dateCreation;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setUser(User user) {
        this.user = user;
    }   

    public void setStatutI(Statut statutI) {
        this.statutI = statutI;
    }

    public void setIdImg(int idImg) {
        this.idImg = idImg;
    }
}




