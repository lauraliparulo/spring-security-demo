package de.security.demo.config;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import de.security.demo.model.UserAccount;
import de.security.demo.repo.UserAccountRepository;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity
public class SecurityConfig {

  @Bean
  CommandLineRunner initUsers(UserAccountRepository repository) {
    return args -> {
      repository.save(new UserAccount("alice", "pass", "ROLE_USER"));
      repository.save(new UserAccount("bob", "pass", "ROLE_USER"));
      repository.save(new UserAccount("admin", "pass", "ROLE_ADMIN"));
    };
  }
  


  @Bean
  UserDetailsService userService(UserAccountRepository repo) {
	  
	    User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("alice").password("pass").roles("USER").build());
        manager.createUser(users.username("admin").password("pass").roles("USER", "ADMIN").build());
        return manager;
	  
  }

  @Bean
  SecurityFilterChain configureSecurity(HttpSecurity http) throws Exception {
      http.authorizeHttpRequests() //
              .requestMatchers("/login").permitAll() //
              .requestMatchers("/search").authenticated() //
              .requestMatchers(HttpMethod.GET, "/api/**").authenticated()//
              .requestMatchers("/admin").hasRole("ADMIN") //
              .requestMatchers(HttpMethod.POST, "/delete/**", "/new-video").authenticated() //
              .anyRequest().denyAll() //
              .and() //
              .formLogin(withDefaults()) //
              .httpBasic(withDefaults());
    return http.build();
  }
  

}
