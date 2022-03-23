package com.yunussen.mobilappws.security;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.yunussen.mobilappws.io.repository.UserRepository;
import com.yunussen.mobilappws.service.UserService;

//method üzerinde  security saglamak icin ekledim eger EnableWebSecurity olmasaydı dikkate alınması icin @Configuration yazmalıydım.
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final UserService userDetailsService;
	private final UserRepository userRepository;
	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	public WebSecurity(UserService userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder,UserRepository userRepository) {
		this.userDetailsService = userDetailsService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
		this.userRepository= userRepository;
	}

	/**
	 * izin verecegi istekleri yazdım.
	 */
	protected void configure(HttpSecurity http) throws Exception {

		http
	        .cors().and()//cors a izin verdim
	        .csrf().disable().authorizeRequests()
	        .antMatchers(HttpMethod.POST, SecurityConstants.SIGN_UP_URL)
	        .permitAll()
	        .antMatchers(HttpMethod.GET, SecurityConstants.VERIFICATION_EMAIL_URL)
	        .permitAll()
	        .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_REQUEST_URL)
	        .permitAll()
	        .antMatchers(HttpMethod.POST, SecurityConstants.PASSWORD_RESET_URL)
	        .permitAll()
	        .antMatchers(SecurityConstants.H2_CONSOLE)
	        .permitAll()
	        .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")//swagger izin vedim.
	        .permitAll()
	        //method üzerinde annotation ile saglayacagım icin lkapattım.
	        //.antMatchers(HttpMethod.DELETE, "/users/**").hasRole("ADMIN")
	        .anyRequest().authenticated().and()
	        .addFilter(getAuthenticationFilter())
	        .addFilter(new AuthorizationFilter(authenticationManager(),userRepository))
	        .sessionManagement()
	        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	        
	        http.headers().frameOptions().disable();

	}

	protected void configure(AuthenticationManagerBuilder auth) throws Exception {

		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
	}

	public AuthenticationFilter getAuthenticationFilter() throws Exception {
		final AuthenticationFilter filter = new AuthenticationFilter(authenticationManager());
		filter.setFilterProcessesUrl("/users/login");
		return filter;
	}
	
	 	@Bean
	    public CorsConfigurationSource corsConfigurationSource()
	    {
	    	final CorsConfiguration configuration = new CorsConfiguration();
	    	   
	    	configuration.setAllowedOrigins(Arrays.asList("*"));// tüm domainlere izin verdim.
	    	configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE","OPTIONS"));
	    	configuration.setAllowCredentials(true);
	    	configuration.setAllowedHeaders(Arrays.asList("*"));//tüm davranışlar auth gibi.
	    	
	    	final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	    	source.registerCorsConfiguration("/**", configuration);
	    	
	    	return source;
	    }

}
