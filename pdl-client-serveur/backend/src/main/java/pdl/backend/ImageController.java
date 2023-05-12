package pdl.backend;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.nio.ByteBuffer;
import java.lang.System;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.protobuf.Timestamp;
import com.mysql.cj.xdevapi.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

import boofcv.io.image.ConvertBufferedImage;
import boofcv.io.image.UtilImageIO;
import boofcv.struct.border.BorderType;
import boofcv.struct.image.GrayU8;
import boofcv.struct.image.Planar;
import static java.lang.System.*;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.ByteArrayOutputStream;



@RestController
public class ImageController {

  @Autowired
  private ObjectMapper mapper;

  private final ImageDao imageDao;

  @Autowired
  public ImageController(ImageDao imageDao) {
    this.imageDao = imageDao;
  }

 @RequestMapping(value = "/images/{id}", method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE)
  public ResponseEntity<?> getImage(@PathVariable("id") long id,
                                    @RequestParam(value = "algorithm", required = false) String algorithm,
                                    @RequestParam(value = "p1", required = false) Integer p1,
                                    @RequestParam(value = "p2", required = false) Integer p2)  {

          System.out.println("get image");

    Optional<Image> image = imageDao.retrieve(id);
    System.out.println("image controleur");
    if (image.isPresent()) {
      if(algorithm==null)
      {
          InputStream inputStream = new ByteArrayInputStream(image.get().getData());
          return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(inputStream));
      }
      else
      {
        try 
        {
            InputStream inputStream1 = new ByteArrayInputStream(image.get().getData());
            BufferedImage input = ImageIO.read(inputStream1);
            if (input==null)
            {
              return new ResponseEntity<>("Image id=" + id + " not found.", HttpStatus.NO_CONTENT);
            }

            Planar<GrayU8> image_planar=  ConvertBufferedImage.convertFromPlanar(input, null, true, GrayU8.class);
            

            switch(algorithm)
            {
              case "thresholdBrightness":
                if(p1 == null || p1<0 || p1>255){
                  return new ResponseEntity<>("Parameter set value is invalid", HttpStatus.BAD_REQUEST);
                }Couleur.thresholdBrightness(image_planar,p1);
                break;

              case"Histogram_equalization":
                
                boolean s=false; if(p1==0){ s=true; }
                boolean v=false; if(p1==0){ v=true; }
                
                Couleur.Histogram_equalization(image_planar,s,v);
                break;

              case"colorizeImage":
                if( p1 == null || p1<0 || p1>360){
                  return new ResponseEntity<>("the parameter p1 does not exist for the program chosen", HttpStatus.BAD_REQUEST);
                }Couleur.colorizeImage(image_planar,p1);
                break;

              case"meanFilterSimple":
                if(p1 == null || p1<0 || p1>=input.getWidth() || p1>=input.getHeight()){
                  return new ResponseEntity<>("Parameter set value is invalid", HttpStatus.BAD_REQUEST);
                }
                switch(p2){
                  case 0:Couleur.meanFilterWithBorders(image_planar,p1,BorderType.SKIP);
                  break;
                  case 1:Couleur.meanFilterWithBorders(image_planar,p1,BorderType.EXTENDED);
                  break;
                  case 2:Couleur.meanFilterWithBorders(image_planar,p1,BorderType.REFLECT);
                  break;
                  case 3:Couleur.meanFilterWithBorders(image_planar,p1,BorderType.NORMALIZED);
                  break;
                  default: Couleur.meanFilterWithBorders(image_planar,p1,BorderType.SKIP);

                }
                break;

              case "gradientImageSobel": 
                Planar<GrayU8> Copy = image_planar.clone();
                color.gradientImageSobel(Copy, image_planar);
                break;
              default:
                return new ResponseEntity<>("algorithm does not exist", HttpStatus.BAD_REQUEST);

            }
            BufferedImage input_planar = ConvertBufferedImage.convertTo(image_planar,null,true);
            
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(input_planar, "jpg", baos);
            byte[] byteArrray = baos.toByteArray();
            InputStream inputStream = new ByteArrayInputStream(byteArrray);
            return  ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(new InputStreamResource(inputStream));
          }catch (IOException e) {
            return new ResponseEntity<>("Algorithm execution failed for an internal reason", HttpStatus.INTERNAL_SERVER_ERROR);
          }
      }
  }
    return new ResponseEntity<>("Image id=" + id + " not found.", HttpStatus.NOT_FOUND);
  }

  @RequestMapping(value = "/images/{id}", method = RequestMethod.DELETE)
  public ResponseEntity<?> deleteImage(@PathVariable("id") long id) {

    System.out.println("delete image");

    Optional<Image> image = imageDao.retrieve(id);

    if (image.isPresent()) {
      imageDao.delete(image.get());
      return new ResponseEntity<>("Image id=" + id + " deleted.", HttpStatus.OK);
    }
    return new ResponseEntity<>("Image id=" + id + " not found.", HttpStatus.NOT_FOUND);
  }


  @RequestMapping(value = "/images", method = RequestMethod.POST)
  public ResponseEntity<?> addImage( @RequestParam("file") MultipartFile file,
                                     @RequestParam(value = "monId") Integer monId, 
                                     @RequestParam(value = "monStatut") Integer monStatut, 
                                     RedirectAttributes redirectAttributes) {

     System.out.println("post image");

    System.out.println("ceci s'affiche quand on post une image ");
    String contentType = file.getContentType();


    if (!contentType.equals(MediaType.IMAGE_JPEG.toString()) && !contentType.equals(MediaType.IMAGE_PNG.toString())) {
      return new ResponseEntity<>("Only JPEG and PNG file format supported", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    System.out.println("before trying to");
    try {
      byte[] bytes = file.getBytes();
      System.out.println("bytes");
      ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
      System.out.println("bytes read");
      BufferedImage image = ImageIO.read(inputStream);
      System.out.println(("image read"));
      File new_file = new File("images/" + file.getOriginalFilename());
      if(new_file.length()> 1048576){
        return new ResponseEntity<>("File is too large", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
      }
      
      if(contentType.equals(MediaType.IMAGE_JPEG.toString())){
        ImageIO.write(image, "jpg", new_file);
        System.out.println("jpg");
      }if(contentType.equals(MediaType.IMAGE_PNG.toString())){
        System.out.println(("png"));
        ImageIO.write(image, "png", new_file);
      }
      java.util.Date dt = new java.util.Date();
      java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      String currentTime = sdf.format(dt);
      imageDao.create(new Image(file.getOriginalFilename(),"tag" ,currentTime,"images/"+file.getOriginalFilename()), monId, monStatut);
    
    } catch (IOException e) {
      return new ResponseEntity<>("Failure to read file", HttpStatus.NO_CONTENT);
    }
    return new ResponseEntity<>("Image uploaded", HttpStatus.OK);
  }

  @RequestMapping(value = "/images", method = RequestMethod.GET, produces = "application/json")
  @ResponseBody
  public ArrayNode getImageList(@RequestParam(value = "monId", required = false) Integer userId )  {
    ArrayNode nodes = mapper.createArrayNode();
    try {
      Class.forName("com.mysql.jdbc.Driver");
      int userId_int=-1;
      if(userId !=null){
        userId_int= (int) userId;
      }
      Connection connection;
      connection = sqlConnection.getConnection();
      PreparedStatement statement=null;
      if(userId_int==-1){
        statement = connection.prepareStatement(
          "SELECT * FROM images WHERE statut_idStatut = 1");
      }else{
        statement = connection.prepareStatement(
          "SELECT * FROM images WHERE iduser = ? AND statut_idStatut != 2");
        statement.setInt(1,userId_int);
      }
      ResultSet resultSet = statement.executeQuery();
      while (resultSet.next()) {
        java.sql.Timestamp timestamp = resultSet.getTimestamp("dateCreation");
        String dateCreationStr = timestamp.toString();
        ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("id", resultSet.getInt("idImage"));
        objectNode.put("name", resultSet.getString("nameImage"));
        String extension = resultSet.getString("nameImage").substring(resultSet.getString("nameImage").lastIndexOf(".") + 1);
        MediaType mediaType = MediaType.parseMediaType("image/" + extension);
        objectNode.put("type", mediaType.toString());
        try {
            BufferedImage bufferedImage = ImageIO.read(new File(resultSet.getString("path")));
            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();
            int channels = bufferedImage.getColorModel().getNumComponents();
            objectNode.put("size", width + "*" + height + "*" + channels);
        } catch (IOException e) {
            objectNode.putNull("size");
        }
        objectNode.put("tag", "tag");
        objectNode.put("dateCreation", dateCreationStr);
        objectNode.put("chemin", resultSet.getString("path"));
        objectNode.put("iduser", userId_int);
        nodes.add(objectNode);
        Optional<Image> image = imageDao.retrieve(resultSet.getInt("idImage"));
        if (!image.isPresent()) {
          Image img = new Image(resultSet.getString("nameImage"),"tag",dateCreationStr, resultSet.getString("path"));
          imageDao.AddToHashMap((long) resultSet.getInt("idImage"), img);
        }
        
    }
    } catch (SQLException | ClassNotFoundException  e  ) {
      e.printStackTrace();
    }
    return nodes;
  }

  @RequestMapping(value = "/selectImageWithIdUser", method = RequestMethod.POST)
    public List<ImageClement> selectImageWithIdUser(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "idUser") Number idUser) throws IOException
    {
      System.out.println("select image with id user 1");

      List<ImageClement> images = new ArrayList<>();




      
       
            
    
            
           
        try 
        {
          System.out.println("select image with id user 2");
            Connection connection = sqlConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM images WHERE idUser = ?");
            stmt.setInt(1, idUser.intValue());
            ResultSet rs = stmt.executeQuery();
            System.out.println("select image with id user 3 avec idUser = " + idUser );
            while (rs.next()) 
            {    
              // Successful login

                
              System.out.println("select image with id user 4");

              int idImage = rs.getInt("idImage");
              String name = rs.getString("nameImage");
              String dateCreation = rs.getString("dateCreation");
              String path = rs.getString("path");
              int iduser = rs.getInt("iduser");
              int statut_idStatut = rs.getInt("statut_idStatut");


              String nameStatut = Statut.getNameWithIdStatut( statut_idStatut);
              Statut statutI = new Statut(statut_idStatut, nameStatut); 
        

              System.out.println("idImage : " + idImage + " nameImage : " + name + " dateCreation : " + dateCreation + " path :  "+ path + " iduser : " + iduser + " namestatut : " + nameStatut);
              
              User user = User.getUser(iduser);
              

              ImageClement image = new ImageClement(idImage, name, dateCreation, path, user, statutI);


              images.add(image);
             

              System.out.println("select image with id user 5");
                
                
            }
            connection.close(); 
            return images;

          }
          catch (SQLException e) 
          {
              e.printStackTrace();
          }
  
          return null;
    }



    @RequestMapping(value = "/changeImgStatut", method = RequestMethod.POST)
    public void changeImgStatut(HttpServletRequest request, HttpServletResponse response,
                        @RequestParam(value = "ImgId") Number ImgId,
                        @RequestParam(value = "statutId") Number statutId) throws IOException
    {
      System.out.println("update img statut 1");

           
        try 
        {
          System.out.println("id staut : "+ statutId + "id img : " + ImgId);
            Connection connection = sqlConnection.getConnection();
            PreparedStatement stmt = connection.prepareStatement("UPDATE images SET statut_idStatut = ? WHERE idImage = ?");
            
            stmt.setInt(1, statutId.intValue());
            stmt.setInt(2, ImgId.intValue());
            stmt.executeUpdate();
            
            connection.close(); 
        

          }
          catch (SQLException e) 
          {
              e.printStackTrace();
          }
  
    }

    
  

}

