package pdl.backend;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;

import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.*;
import static org.hamcrest.MatcherAssert.*;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jayway.jsonpath.internal.Path;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
public class ImageControllerTests {

	@Autowired
	private MockMvc mockMvc;
	@BeforeAll
	
	public static void reset() {
		// reset Image class static counter
		ReflectionTestUtils.setField(Image.class, "count", Long.valueOf(0));
	}
	

	@Test
	@Order(1)
	public void getImageListShouldReturnSuccess() throws Exception {

		
		this.mockMvc.perform(get("/images")).andDo(print()).andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON));
	}

	
	
	@Test
	@Order(2)
	public void getImageShouldReturnSuccess() throws Exception {
		this.mockMvc.perform(get("/images/0")).andExpect(status().isOk());
	}

	@Test
	@Order(3)
	public void deleteImagesShouldReturnMethodNotAllowed() throws Exception {
	this.mockMvc.perform(delete("/images")).andDo(print()).andExpect(status().isMethodNotAllowed());
	}

	

	@Test
	@Order(4)
	public void deleteImageShouldReturnSuccess() throws Exception {
		this.mockMvc.perform(delete("/images/0")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	@Order(5)
	public void getImageShouldReturnNotFound() throws Exception {
		this.mockMvc.perform(get("/images/0")).andDo(print()).andExpect(status().isNotFound());
	}

	@Test
	@Order(6)
	public void deleteImageShouldReturnNotFound() throws Exception {
		this.mockMvc.perform(delete("/images/0")).andDo(print()).andExpect(status().isNotFound());
	}


	@Test
	@Order(7)
	public void createImageShouldReturnSuccess() throws Exception {
        ClassPathResource imgFile = new ClassPathResource("test.jpg");
        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.jpg", "image/jpeg", imgFile.getInputStream());

        mockMvc.perform(multipart("/images")
                .file(multipartFile)
                .param("monId", "1")
                .param("monStatut", "1"))
                .andDo(print())
                .andExpect(status().isOk());
    }


/* 
	@Test
	@Order(8)
	public void testLoadDirectoryImages() throws Exception
	{
		
		//On vérifie qu'il y ai bien les deux images généré par loadDirectoryImages
		this.mockMvc.perform(get("/images/1")).andExpect(status().isOk());
		this.mockMvc.perform(get("/images/2")).andExpect(status().isOk());

		// Il il existe une image crée par createImageShouldReturnSuccess()  qui odit être d'id 3 si loadDirectoryImages à bine fonctionné. 
		this.mockMvc.perform(get("/images/2")).andExpect(status().isOk());

		// Toute autre id d'image doit renvoyé not Found
		this.mockMvc.perform(get("/images/4")).andExpect(status().isNotFound());
		this.mockMvc.perform(get("/images/6")).andExpect(status().isNotFound());

		
		//on verifie qu'on gère bien  les extension .jpg et .jpeg ...
		MockMultipartFile image1 = new MockMultipartFile("file", "image.jpg", "image/jpeg", "test image data true".getBytes());
		mockMvc.perform(multipart("/images").file(image1))
				.andExpect(status().isOk());
	
		//	et qu'on ne gère pas les autres
		MockMultipartFile image = new MockMultipartFile("file", "image.png", "image/png", "test image data false".getBytes());
    	mockMvc.perform(multipart("/images").file(image)).andExpect(status().isUnsupportedMediaType());
		
	}	*/
}
