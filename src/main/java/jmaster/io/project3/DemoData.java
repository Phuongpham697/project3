package jmaster.io.project3;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import jmaster.io.project3.entity.Role;
import jmaster.io.project3.entity.User;
import jmaster.io.project3.repo.RoleRepo;
import jmaster.io.project3.repo.UserRepo;

@Component
public class DemoData implements ApplicationRunner {
	@Autowired
	UserRepo userRepo;
	
	@Autowired
	RoleRepo roleRepo;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		Role role= new Role();
		role.setName("ROLE_ADMIN");
		if(roleRepo.findByName(role.getName())==null) {
		try {
			roleRepo.save(role);
			User user= new User();
			user.setUsername("sysadmin");
			user.setName("SYS ADMIN");
			user.setEmail("phuongphamdinh@gmail.com");
			user.setBirthdate(new Date());
			user.setPassword(new BCryptPasswordEncoder().encode("123456"));
			user.setRoles(Arrays.asList(role));
			
			userRepo.save(user);
		}catch (Exception e) {
			
		}
			
		}
	}

}
