package pdl.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Liker {

    @RequestMapping(value = "/like", method = RequestMethod.POST)
    public ResponseEntity<?> likeImage(HttpServletRequest request,
                        @RequestParam(value = "imageId") Integer imageId ,
                        @RequestParam(value = "userId") Integer userId) throws SQLException {
        int userId_int=(int) userId;
        int imageId_int=(int) imageId;
        String query = "INSERT INTO liker (users_idUser, images_idImage) VALUES (?, ?)";
        Connection connection = sqlConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1,  userId_int);
        preparedStatement.setInt(2,  imageId_int);
        preparedStatement.executeUpdate();
        System.out.println(" likeImage");
        return new ResponseEntity<>("Image id=" + imageId + " like by "+ userId +".", HttpStatus.OK);

    }

    @RequestMapping(value = "/like", method = RequestMethod.DELETE)
    public ResponseEntity<?> unlikeImage(HttpServletRequest request,
                        @RequestParam(value = "imageId") Integer imageId ,
                        @RequestParam(value = "userId") Integer userId) throws SQLException {
        int userId_int=(int) userId;
        int imageId_int=(int) imageId;
        String query = "DELETE FROM liker WHERE users_idUser = ? AND images_idImage = ?";
        Connection connection = sqlConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId_int);
        preparedStatement.setInt(2, imageId_int);
        preparedStatement.executeUpdate();
        System.out.println(" unlikeImage");
        return new ResponseEntity<>("Image id=" + imageId + " like by "+ userId +".", HttpStatus.OK);
    }

    @RequestMapping(value = "/like", method = RequestMethod.GET)
    public ResponseEntity<?> checkIfUserLikedImage(HttpServletRequest request,
                        @RequestParam(value = "imageId") Integer imageId ,
                        @RequestParam(value = "userId") Integer userId) throws SQLException {
        int userId_int=(int) userId;
        int imageId_int=(int) imageId;
        String query = "SELECT COUNT(*) FROM liker WHERE users_idUser = ? AND images_idImage = ?";
        Connection connection = sqlConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId_int);
        preparedStatement.setInt(2, imageId_int);
        System.out.println(" checkIfUserLikedImage");
        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return new ResponseEntity<>(resultSet.getInt(1) > 0, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(-1, HttpStatus.NOT_FOUND);
    }

    @RequestMapping(value = "/like/count", method = RequestMethod.GET)
    public ResponseEntity<?> getImageLikeCount(
                        @RequestParam(value = "imageId") Integer imageId) throws SQLException {
        int imageId_int=(int) imageId;
        String query = "SELECT COUNT(*) FROM liker WHERE images_idImage = ?";
        Connection connection = sqlConnection.getConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, imageId_int);

        try (ResultSet resultSet = preparedStatement.executeQuery()) {
            if (resultSet.next()) {
                return new ResponseEntity<>(resultSet.getInt(1), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(0, HttpStatus.NOT_FOUND);
    }
}
