package jmaster.io.project3.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.UUID;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.util.StringBuilderFormattable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jmaster.io.project3.dto.PageDTO;
import jmaster.io.project3.dto.ResponseDTO;
import jmaster.io.project3.dto.SearchDTO;
import jmaster.io.project3.dto.UserDTO;
import jmaster.io.project3.service.UserService;

@RestController
@RequestMapping("/admin/user")
public class UserController {
	@Autowired
	UserService userService;

	@PostMapping("/new")
	public ResponseDTO<UserDTO> add(@ModelAttribute @Valid UserDTO userDTO) throws IllegalStateException, IOException {
		if (!userDTO.getFile().isEmpty()) {
			String filename = userDTO.getFile().getOriginalFilename();
			File saveFile = new File("D:/project/" + filename);
			userDTO.getFile().transferTo(saveFile);
			userDTO.setAvatar(filename);
		}

		userService.create(userDTO);
		return ResponseDTO.<UserDTO>builder().status(200).data(userDTO).build();
	}
	
	@PutMapping("/")
	public ResponseDTO<Void> update(@ModelAttribute @Valid UserDTO userDTO) throws IllegalStateException, IOException {
		if (!userDTO.getFile().isEmpty()) {
			String filename = userDTO.getFile().getOriginalFilename();
			File saveFile = new File("D:/project/" + filename);
			userDTO.getFile().transferTo(saveFile);
			userDTO.setAvatar(filename);
		}

		userService.update(userDTO);

		return ResponseDTO.<Void>builder().status(200).build();
	}
	@GetMapping("/list")
	public ResponseDTO<List<UserDTO>> lisUsers(){
		List<UserDTO> lisUserDTO = userService.getAll();
		return ResponseDTO.<List<UserDTO>>builder().status(200).data(lisUserDTO).build();
		
	}


	@GetMapping("/download/{filename}")
	public void download(@PathVariable("filename") String filename, HttpServletResponse response) throws IOException {
		File file = new File("D:/project/" + filename);
		Files.copy(file.toPath(), response.getOutputStream());
	}

	@PostMapping("/search")
	public ResponseDTO<PageDTO<UserDTO>> search(
			@RequestBody @Valid SearchDTO searchDTO) {
		return ResponseDTO.<PageDTO<UserDTO>>builder()
				.status(200)
				.data(userService.search(searchDTO))
				.build();
	}

	@GetMapping("/{id}")
	public ResponseDTO<UserDTO> get(@PathVariable("id") int id) {
		UserDTO userDTO = userService.getById(id);
		return ResponseDTO.<UserDTO>builder().status(200).data(userDTO).build();
	}

	@DeleteMapping("/{id}") // /1
	public ResponseDTO<Void> delete(@PathVariable("id") int id) {
		userService.delete(id);
		return ResponseDTO.<Void>builder().status(200).build();
	}


	@PutMapping("/password")
	public ResponseDTO<Void> updatePassword(
			@RequestBody @Valid UserDTO userDTO) {
		userService.updatePassword(userDTO);
		return ResponseDTO.<Void>builder().status(200).build();
	}
	
	
	//quên mật khẩu
	@PutMapping("/forgot-password")
	public ResponseEntity<UserDTO> forgotPassword(@RequestParam String email) throws AddressException, MessagingException, IOException {
		userService.forgotPassword(email);
		return new ResponseEntity<>(userService.forgotPassword(email), HttpStatus.OK);
	}
	
	//đặt lại mật khẩu với email đăng nhập
	@PutMapping("/set-password")
	public ResponseEntity<String> setPassword(@RequestParam String email, @RequestHeader String newPassword){
		return new ResponseEntity<>(userService.setPassword(email, newPassword), HttpStatus.OK);
	}
	
	
}
