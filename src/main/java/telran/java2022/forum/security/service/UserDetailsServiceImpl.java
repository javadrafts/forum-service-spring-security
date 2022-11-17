package telran.java2022.forum.security.service;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import telran.java2022.forum.user.dao.UserRepository;
import telran.java2022.forum.user.model.User;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
	final UserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findById(username).orElseThrow(() -> new UsernameNotFoundException(username));

		String[] roles = user.getRoles().stream().map(r -> "ROLE_" + r).toArray(String[]::new);

		return new org.springframework.security.core.userdetails.User(username, user.getPassword(),
				AuthorityUtils.createAuthorityList(roles));
	}
}
