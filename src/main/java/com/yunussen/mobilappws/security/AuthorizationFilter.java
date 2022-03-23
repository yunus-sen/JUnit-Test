package com.yunussen.mobilappws.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.yunussen.mobilappws.SpringApplicationContext;
import com.yunussen.mobilappws.io.entity.UserEntity;
import com.yunussen.mobilappws.io.repository.UserRepository;

import io.jsonwebtoken.Jwts;

public class AuthorizationFilter extends BasicAuthenticationFilter {

	private final UserRepository userRepository;
	public AuthorizationFilter(AuthenticationManager authManager,UserRepository userRepository) {
		super(authManager);
		this.userRepository= userRepository;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		String header = req.getHeader(SecurityConstants.HEADER_STRING);

		if (header == null || !header.startsWith(SecurityConstants.TOKEN_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}

		UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(SecurityConstants.HEADER_STRING);

		if (token != null) {

			// burda tokenin başına ekledigim 'Bear' anahtar kelimesini silip saf token elde ettim.
			token = token.replace(SecurityConstants.TOKEN_PREFIX, "");

			// token verify edip userda kullanıcının email adresinin tuttum.
			String user = Jwts.parser().setSigningKey(SecurityConstants.getTokenSecret()).parseClaimsJws(token)
					.getBody().getSubject();

			if (user != null) {
				UserEntity userEntity = userRepository.findByEmail(user);
				if(userEntity==null) return null;
				UserPrincipal userPrincipal = new UserPrincipal(userEntity);

				// ardından kullanıcı rolleri ve email ekleyip return yaptım //principal yerine user göndere bilirdim fakat preAuthorize da Principal dedimde user  mail erişirdim.
				return new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
			}

			return null;
		}

		return null;
	}

}