package com.example.grocerystore.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration //extends WebSecurityConfigurerAdapter 
{
	 @Bean
	    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
	    {
			 http
	         .csrf()
	             .csrfTokenRepository(csrfTokenRepository())
	         .and()
	             .authorizeRequests()
	             .antMatchers("/css/**", "/js/**", "/products/fetch/sale", "/fetch/sales/All").permitAll()
	             .antMatchers("/", "/login", "/register","/forgot_password","/reset_password").anonymous()
	//             .antMatchers("/admin/**").hasAuthority("ADMIN")
	             .anyRequest().authenticated()
	         .and()
	             .formLogin()
	             .loginPage("/login")
	             .usernameParameter("username")
	             .passwordParameter("password")
	             .defaultSuccessUrl("/home", true)
	         .and()
	             .exceptionHandling().accessDeniedPage("/unauthorized")
	         .and()
	             .logout().logoutSuccessUrl("/login?logout").permitAll();
			 
			 return http.build();
	    }
/*
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf()
                    .csrfTokenRepository(csrfTokenRepository())
                .and()
                    .authorizeRequests()
                    .antMatchers("/css/**", "/js/**", "/products/fetch/sale", "/fetch/sales/All").permitAll()
                    .antMatchers("/", "/login", "/register").anonymous()
//                    .antMatchers("/admin/**").hasAuthority("ADMIN")
                    .anyRequest().authenticated()
                .and()
                    .formLogin()
                    .loginPage("/login")
                    .usernameParameter("username")
                    .passwordParameter("password")
                    .defaultSuccessUrl("/home", true)
                .and()
                    .exceptionHandling().accessDeniedPage("/unauthorized")
                .and()
                    .logout().logoutSuccessUrl("/login?logout").permitAll();
    }
*/
    private CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository =
                new HttpSessionCsrfTokenRepository();
        repository.setSessionAttributeName("_csrf");
        return repository;
    }
}