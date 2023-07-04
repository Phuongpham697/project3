package jmaster.io.project3.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jmaster.io.project3.dto.LoginUserDTO;
import jmaster.io.project3.dto.UserDTO;
import jmaster.io.project3.service.JwtTokenService;
import jmaster.io.project3.service.UserService;

//su dung http://localhost:8080/swagger-ui/index.html  de xem api doc

@RestController
@RequestMapping("/")
public class LoginAPI {
	@Autowired
	AuthenticationManager authenticationManager;

	@Autowired
	JwtTokenService jwtTokenService;

	@Autowired
	UserService userService;

	@GetMapping("/current")
	@PreAuthorize("isAuthenticated()")
	public UserDTO current(Principal p) {
		String userName= p.getName();
		UserDTO user= userService.findByUsername(userName);
		return user;
	}
	/// java -jar <ten_file.jar>
//	@PreAuthorize("isAuthenticated()")
//	@PostMapping("/refresh-token")
//	public String login(
//			@RequestParam("refresh-token") String refresh) {
	// select from fresh_token
	// check expire time
	// lay username -> tao token moi
//		return jwtTokenService.createToken(username);
//	}

	@PostMapping("/login")
	public String login(@RequestParam("username") String username, @RequestParam("password") String password) {
		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		// refreshToken = UUID.randomUUID().toString();
		// save refresh token - table(refresh_token)(username, refreshtoken,expired)
		// class TokenDTO(refreshToken,accesstoken)
		return jwtTokenService.createToken(username);
	}
}
