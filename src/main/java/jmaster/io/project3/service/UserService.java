package jmaster.io.project3.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.persistence.NoResultException;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jmaster.io.project3.dto.PageDTO;
import jmaster.io.project3.dto.RoleDTO;
import jmaster.io.project3.dto.SearchDTO;
import jmaster.io.project3.dto.UserDTO;
import jmaster.io.project3.entity.Role;
import jmaster.io.project3.entity.User;
import jmaster.io.project3.repo.RoleRepo;
import jmaster.io.project3.repo.UserRepo;

@Service
public class UserService {

	@Autowired
	UserRepo userRepo;

	@Autowired
	RoleRepo roleRepo;
	
	@Autowired
	MailService mailService;

	@Transactional
	public void create(UserDTO userDTO) {
		User user = new ModelMapper().map(userDTO, User.class);

		user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
		userRepo.save(user);

		// tra ve idsau khi tao
		userDTO.setId(user.getId());
	}

	@Transactional
	public void update(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);
		user.setName(userDTO.getName());
		user.setBirthdate(userDTO.getBirthdate());

		user.setRoles(userDTO.getRoles().stream().map(role -> roleRepo.findById(role.getId()).orElse(null))
				.filter(r -> r != null).collect(Collectors.toList()));

		user.setEmail(userDTO.getEmail());

		if (userDTO.getAvatar() != null)
			user.setAvatar(userDTO.getAvatar());

		userRepo.save(user);
	}

	@Transactional
	public void update2(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);

		ModelMapper mapper = new ModelMapper();
		mapper.createTypeMap(UserDTO.class, User.class).addMappings(map -> {
			map.skip(User::setPassword);
			if (userDTO.getAvatar() == null)
				map.skip(User::setAvatar);
		}).setProvider(p -> user);

		User saveUser = mapper.map(userDTO, User.class);

		userRepo.save(saveUser);
	}

	@Transactional
	public void updatePassword(UserDTO userDTO) {
		User user = userRepo.findById(userDTO.getId()).orElseThrow(NoResultException::new);

		user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));

		userRepo.save(user);
	}

	@Transactional
	public void delete(int id) {
		userRepo.deleteById(id);
	}

	public PageDTO<UserDTO> search(SearchDTO searchDTO) {
		Pageable pageable = PageRequest.of(searchDTO.getCurrentPage(), 
				searchDTO.getSize());

		Page<User> pageRS = userRepo.searchByName("%" + searchDTO.getKeyword() + "%", pageable);

		PageDTO<UserDTO> pageDTO = new PageDTO<>();
		pageDTO.setTotalPages(pageRS.getTotalPages());
		pageDTO.setTotalElements(pageRS.getTotalElements());


		List<UserDTO> userDTOs = pageRS.get().map(user -> new ModelMapper().map(user, UserDTO.class))
				.collect(Collectors.toList());

		pageDTO.setContents(userDTOs);
		return pageDTO;
	}

	public UserDTO getById(int id) {
		User user = userRepo.findById(id).orElseThrow(NoResultException::new);
		return new ModelMapper().map(user, UserDTO.class);
	}
	
	public List<UserDTO> getAll() { 
		List<User> userList = userRepo.findAll();
		return userList.stream().map(u -> convert(u))
				.collect(Collectors.toList());
	}

	public UserDTO findByUsername(String username) { // java8, optinal
		User user = userRepo.findByUsername(username);
		if (user == null)
			throw new NoResultException();
		return new ModelMapper().map(user, UserDTO.class);
	}
	
	private UserDTO convert(User user) {
		return new ModelMapper().map(user, UserDTO.class);
	}
	
	public UserDTO forgotPassword(String email) throws AddressException, MessagingException, IOException {
		User user=userRepo.findByEmail(email);
		if (user == null) 
			throw new NoResultException();
		mailService.sendSetPasswordEmail(email);
		return new ModelMapper().map(user, UserDTO.class);
	
	}
	

	public String setPassword(String email, String newPassword){
		User user=userRepo.findByEmail(email);
		if (user == null) 
			throw new NoResultException();
		user.setPassword(new BCryptPasswordEncoder().encode(newPassword));
		userRepo.save(user);
		return " new password set successfully with new password";
	}

	
}
