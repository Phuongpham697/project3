package jmaster.io.project3;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, jsr250Enabled = true)
public class SecurityConfig {

	@Autowired
	UserDetailsService userDetailsService;
	
	@Autowired
	JwtTokenFilter jwtTokenFilter;

	// xac thuc
	@Autowired
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
	}

	@Bean
	public AuthenticationManager 
		authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	// overload
	@Bean
	public SecurityFilterChain configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/admin/**")
				// .hasAnyRole("ADMIN", "SUBADMIN")
				// ROLE_
				.hasAnyAuthority("ROLE_ADMIN", "ROLE_SUBADMIN")

				.antMatchers("/customer/**").authenticated()

				.anyRequest().permitAll()

				.and().csrf().disable()
				//bo session vi lam REST API ko can
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

				.and().httpBasic();

//				.formLogin()
////				
//				.loginPage("/dang-nhap")
////				.usernameParameter(null)
//				.loginProcessingUrl("/login")
////				
//				.failureUrl("/dang-nhap?err")
//				.and().logout()
//				.defaultSuccessUrl("/", false)
//				.successHandler(
//					new SimpleUrlAuthenticationSuccessHandler() {
//						@Override
//						protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response) {
////							if ADMIN return /admin/home
//							
//						}
//				})
//				.and().exceptionHandling().accessDeniedPage("/login");

		// Apply JWT
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
		return http.build();
	}
}
