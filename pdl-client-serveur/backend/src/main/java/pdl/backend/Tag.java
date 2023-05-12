package pdl.backend;


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

public class Tag 
{
    private int idTag = -1;
    private String nameTag;

    public Tag (int idTag, String nameTag) 
    {
        this.idTag = idTag;
        this.nameTag = nameTag;
    }

    public int getIdTag() {
        return idTag;
    }

  

    public String getNameTag() {
        return nameTag;
    }

    public void setNameTag(String nameTag) {
        this.nameTag = nameTag;
    }

    public static String getNameWithIdTag(int idTag) 
    {
        String nameTag = "idUndifined";
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = sqlConnection.getConnection();
    
            PreparedStatement stmt = connection.prepareStatement("SELECT nameTag FROM tags WHERE idTag = ?");
            stmt.setInt(1, idTag);

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) 
            {
                nameTag = rs.getString("nameTag");
            }
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            ((Throwable) e).printStackTrace();
        }

        return nameTag;
    }
    
}



