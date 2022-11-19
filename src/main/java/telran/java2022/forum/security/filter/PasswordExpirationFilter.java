package telran.java2022.forum.security.filter;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Base64;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import telran.java2022.forum.user.dao.UserRepository;
import telran.java2022.forum.user.model.User;

@Component
@RequiredArgsConstructor
public class PasswordExpirationFilter implements Filter {
	final UserRepository userRepository;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String token = httpRequest.getHeader(HttpHeaders.AUTHORIZATION);

		if (token != null && checkEndPoint(httpRequest.getServletPath())) {

			String[] credentials;
			try {
				credentials = getCredentialsFromToken(token);
			} catch (Exception e) {
				httpResponse.sendError(400, "Invalid token.");
				return;
			}

			User user = userRepository.findById(credentials[0]).orElse(null);

			if (user.getPasswordExpiration().compareTo(LocalDateTime.now()) < 0) {
				httpResponse.sendError(403, "Password expired.");
				return;
			}
		}

		chain.doFilter(httpRequest, httpResponse);
	}

	private String[] getCredentialsFromToken(String token) {
		return new String(Base64.getDecoder().decode(token.split(" ")[1])).split(":");
	}

	private boolean checkEndPoint(String servletPath) {
		return !servletPath.matches("/account/password/?");
	}
}
