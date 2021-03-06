package com.pruebas.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.pruebas.app.auth.handler.LoginSuccessHandler;
 
//Habilita el uso de anotaciones en los controladores para la visibilidad de roles
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled=true)
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private LoginSuccessHandler successHandler;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests() //
				.antMatchers("/", "/css/**", "/js/**", "/images/**", "/listar").permitAll() //
//				.antMatchers("/ver/**").hasAnyRole("USER") //
//				.antMatchers("/uploads/**").hasAnyRole("USER") //
//				.antMatchers("/form/**").hasAnyRole("ADMIN") //
//				.antMatchers("/eliminar/**").hasAnyRole("ADMIN") //
//				.antMatchers("/factura/**").hasAnyRole("ADMIN") //
				.anyRequest().authenticated() //
				.and().formLogin().successHandler(successHandler).loginPage("/login").permitAll() //
				.and().logout().permitAll() //
				.and().exceptionHandling().accessDeniedPage("/error_403");

	}

	@Autowired
	public void configurerGlobal(AuthenticationManagerBuilder build) throws Exception {
		PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		UserBuilder users = User.builder().passwordEncoder(encoder::encode);

		build.inMemoryAuthentication() // Concat default users
				.withUser(users.username("admin").password("123456").roles("ADMIN", "USER"))
				.withUser(users.username("andres").password("123456").roles("USER"));
	}
}
