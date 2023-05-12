package pdl.backend;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Timestamp;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Repository;

import pdl.backend.Dao;
import pdl.backend.Image;

@Repository
public class ImageDao implements Dao<Image> {

    private final Map<Long, Image> images = new HashMap<>();
    int IdAdmin=8;
    int StatutAdmin=1;

    public ImageDao() throws IOException {
        final ClassPathResource imgFile = new ClassPathResource("test.jpg");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        System.out.println(" ");
        java.util.Date dt = new java.util.Date();
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String currentTime = sdf.format(dt);
        System.out.println(currentTime);
        Image img = new Image("test.jpg","tag",currentTime, "src/main/resources/"+imgFile.getFile().toString());
        try {
          System.out.println("   fin new image bis ");
          Connection connection = sqlConnection.getConnection();
          System.out.println("   fin connection ");
          PreparedStatement statement = connection.prepareStatement(
              "INSERT INTO images (nameImage, dateCreation, path, iduser,statut_idStatut)  SELECT ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT path FROM images WHERE path = ?)" );
    statement.setString(1, img.getName());
    statement.setString(2, currentTime);
    statement.setString(3, img.getChemin());
    statement.setInt(4, IdAdmin);
    statement.setInt(5, StatutAdmin);
    statement.setString(6, img.getChemin());
            System.out.println("   fin prepareStatement ");

            System.out.println("   fin images.put ");

            //statement.executeUpdate();
            System.out.println("   fin  statement.executeUpdate(); ");
            statement.close();
            System.out.println("   fin  statement.close();");
            connection.close();
            System.out.println("   fin connection.close(); ");
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println("before img.setId");
        img.setId(img.getName(),img.getChemin(),IdAdmin);
        System.out.println("Before id"+img.getId());
        images.put(img.getId(), img);
        System.out.println("after id"+img.getId());
        System.out.println("debut loadDirectoryImages");
        loadDirectoryImages("images");
        System.out.println("fin loadDirectoryImages");
    }

    private void loadDirectoryImages(String nameDirectory)
    {
      File imagesDir = new File(nameDirectory);
      if (!imagesDir.exists() || !imagesDir.isDirectory()) {
         //boolean res = imagesDir.mkdir(); pour le creer 
         //if(!res){
          throw new RuntimeException("Images directory can not exist or is not a directory");
         //}
      }
      List<File> imageFiles = listImageFiles(imagesDir);
      for (File imageFile : imageFiles) {
        try {
            java.util.Date dt = new java.util.Date();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String currentTime = sdf.format(dt);
            Image img = new Image(imageFile.getName(),"tag",currentTime, imageFile.toPath().toString());
            Connection connection = sqlConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
               // "INSERT INTO images (nom, description, date_creation, chemin)  SELECT ?, ?, ?, ? ");
              // "INSERT INTO images (nameImage, dateCreation, path, iduser,statut_idStatut)  SELECT ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT path FROM images WHERE path = ?)" );
              "INSERT INTO images (nameImage, dateCreation, path, iduser, statut_idStatut) SELECT ?, ?, ?, ?, ? WHERE NOT EXISTS (SELECT 1 FROM images WHERE path = ?) AND NOT EXISTS (SELECT 1 FROM images WHERE nameImage = ?)" );
               statement.setString(1, img.getName());
               statement.setString(2, currentTime);
               statement.setString(3, img.getChemin());
               statement.setInt(4, IdAdmin);
               statement.setInt(5, StatutAdmin);
               statement.setString(6, img.getChemin());
               statement.setString(7, img.getName());

               System.out.println("Before id"+img.getId());
               img.setId(img.getName(),img.getChemin(),IdAdmin);
               System.out.println("after id"+img.getId());
            images.put(img.getId(), img);

            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
      }
    }
    private List<File> listImageFiles(File directory) {
        List<File> imageFiles = new ArrayList<>();
        File[] files = directory.listFiles();
        for (File file : files) {
          if (file.isDirectory()) {
            imageFiles.addAll(listImageFiles(file));
          } else {
            //String extension = getFileExtension(file);
            if (isImageFile(file)) {
              imageFiles.add(file);
            }
          }
        }
        return imageFiles;
      }

    private boolean isImageFile(File file) {
        String fileName = file.getName();
        int lastIndex = fileName.lastIndexOf('.');
        if (lastIndex > 0 && lastIndex < fileName.length() - 1) {
            String extension = fileName.substring(lastIndex + 1).toLowerCase();
            return extension.equals("jpg") || extension.equals("jpeg");
        }
        return false;
    }


    @Override
    public Optional<Image> retrieve(final long id) {
        return Optional.ofNullable(images.get(id));
    }

    @Override
    public List<Image> retrieveAll() {
        return new ArrayList<Image>(images.values());
    }

    @Override
    public void create(final Image img, int getid, int getStatut) {
        try {
            Connection connection = sqlConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                "INSERT INTO images (nameImage, dateCreation, path, iduser,statut_idStatut)  SELECT ?, ?, ?, ? ,? WHERE NOT EXISTS (SELECT path FROM images WHERE path = ?)" );
                statement.setString(1, img.getName());
                statement.setString(2, img.getDateCreation());
                statement.setString(3, img.getChemin());
                statement.setInt(4, getid);
                statement.setInt(5, getStatut);
                statement.setString(6, img.getChemin());
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        img.setId(img.getName(),img.getDateCreation(),img.getChemin(),getid);
        images.put(img.getId(), img);
    }

    @Override
    public void update(final Image img, final String[] params, int getid, int getStatut) {
        img.setName(Objects.requireNonNull(params[0], "Name cannot be null"));
        try {
            Connection connection = sqlConnection.getConnection();
            PreparedStatement statement = connection.prepareStatement(
                    "UPDATE images SET (nameImage, dateCreation, path, iduser,statut_idStatut) VALUES (?, ?, ?, ?, ?)");
                    statement.setString(1, img.getName());
                    statement.setString(2, img.getDateCreation());
                    statement.setString(3, img.getChemin());
                    statement.setInt(4, getid);
                    statement.setInt(5, getStatut);
            statement.executeUpdate();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        img.setId(img.getName(),img.getDateCreation(),img.getChemin(),getid);
        images.put(img.getId(), img);
    }

    @Override
    public void delete(final Image img) {
        try {
            Connection connection = sqlConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE images SET statut_idStatut = 2 WHERE idImage = ?    ");
            stmt.setFloat(1, img.getId());
            stmt.executeUpdate();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        images.remove(img.getId());
    }

    public void AddToHashMap(Long id, Image img) {
        images.put(id, img);
    }
}
