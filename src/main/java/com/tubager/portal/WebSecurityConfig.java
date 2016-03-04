package com.tubager.portal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.InMemoryTokenRepositoryImpl;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;
	
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
	        .csrf().disable()
	        .authorizeRequests()
            .antMatchers("/img/**", "/js/**", "/css/**", "/fonts/**", "/font/**", "/font-awesome/**", "/bower_components/**", "/application/helper.js").permitAll()
			.anyRequest().authenticated()
        .and()
            .formLogin().loginPage("/login.html").defaultSuccessUrl("/index.html").failureUrl("/login.html?error=1").permitAll()
        .and()
            .logout().logoutUrl("/logout").logoutSuccessUrl("/login.html").permitAll()
		.and().rememberMe().tokenRepository(new InMemoryTokenRepositoryImpl()).tokenValiditySeconds(1209600);
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
            .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
    }
}