package com.patientms.JWT;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtUtil jwtUtil;
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		log.info("Incoming to Patient :{}",request.getRequestURI());

		final String requestHeader = request.getHeader("Authorization");
		if(requestHeader==null || !requestHeader.startsWith("Bearer ")){
			filterChain.doFilter(request,response);
			return;
		}

		// Token will split in two parts we will take the 1st index -> means second part
		String token = requestHeader.substring(7);
		String userId = jwtUtil.extractUserId(token);
		log.info("Authenticated userId from JWT: {}", userId);
		UsernamePasswordAuthenticationToken authentication =
				new UsernamePasswordAuthenticationToken(
						userId,
						null,
						List.of()
				);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}
}
