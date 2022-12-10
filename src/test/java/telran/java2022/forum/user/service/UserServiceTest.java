package telran.java2022.forum.user.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;

import telran.java2022.forum.user.dao.UserRepository;
import telran.java2022.forum.user.dto.RegisterDto;
import telran.java2022.forum.user.dto.UpdateUserDto;
import telran.java2022.forum.user.dto.UserDto;
import telran.java2022.forum.user.model.User;

@SpringBootTest
@TestPropertySource(locations = "/test.properties")
class UserServiceTest {
	@Autowired
	UserService userService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	List<User> userMock;

	@BeforeEach
	void setUp() throws Exception {
		User trump = User.builder()
				.login("trump")
				.password(passwordEncoder.encode("qwerty"))
				.firstName("Donald")
				.lastName("Trump")
				.build();

		trump.getRoles().add("TEST_ROLE");

		userMock = List.of(trump);

		userRepository.saveAll(userMock);
	}

	@Test
	void testRegister() {
		RegisterDto registerDto = RegisterDto.builder()
				.login("biden")
				.password("123456")
				.firstName("Joe")
				.lastName("Biden")
				.build();

		UserDto userDto = userService.register(registerDto);

		assertEquals(registerDto.getLogin(), userDto.getLogin());
		assertEquals(registerDto.getFirstName(), userDto.getFirstName());
		assertEquals(registerDto.getLastName(), userDto.getLastName());

		Optional<User> optionalUser = userRepository.findById(registerDto.getLogin());

		assertTrue(optionalUser.isPresent());

		User user = optionalUser.get();

		assertEquals(registerDto.getLogin(), user.getLogin());
		assertEquals(registerDto.getFirstName(), user.getFirstName());
		assertEquals(registerDto.getLastName(), user.getLastName());
	}

	@Test
	void testLogin() {
		User mockedUser = userMock.get(0);

		UserDto userDto = userService.login(mockedUser.getLogin());

		assertEquals(mockedUser.getLogin(), userDto.getLogin());
		assertEquals(mockedUser.getFirstName(), userDto.getFirstName());
		assertEquals(mockedUser.getLastName(), userDto.getLastName());
	}

	@Test
	void testDeleteUser() {
		User mockedUser = userMock.get(0);

		assertNotNull(userService.deleteUser(mockedUser.getLogin()));
		assertFalse(userRepository.existsById(mockedUser.getLogin()));
	}

	@Test
	void testUpdateUser() {
		User mockedUser = userMock.get(0);

		UpdateUserDto updateUserDto = UpdateUserDto.builder()
				.firstName("Barack")
				.lastName("Obama")
				.build();

		UserDto userDto = userService.updateUser(mockedUser.getLogin(), updateUserDto);

		assertEquals(updateUserDto.getFirstName(), userDto.getFirstName());
		assertEquals(updateUserDto.getLastName(), userDto.getLastName());

		User user = userRepository.findById(mockedUser.getLogin()).get();

		assertEquals(updateUserDto.getFirstName(), user.getFirstName());
		assertEquals(updateUserDto.getLastName(), user.getLastName());
	}

	@Test
	void testAddRole() {
		User mockedUser = userMock.get(0);

		String role = "ADD_ROLE_TEST";

		UserDto userDto = userService.addRole(mockedUser.getLogin(), role);

		assertTrue(userDto.getRoles().contains(role));

		User user = userRepository.findById(mockedUser.getLogin()).get();

		assertTrue(user.getRoles().contains(role));
	}

	@Test
	void testDeleteRole() {
		User mockedUser = userMock.get(0);

		String role = "TEST_ROLE";

		UserDto userDto = userService.deleteRole(mockedUser.getLogin(), role);

		assertFalse(userDto.getRoles().contains(role));

		User user = userRepository.findById(mockedUser.getLogin()).get();

		assertFalse(user.getRoles().contains(role));
	}

	@Test
	void testChangePassword() {
		User mockedUser = userMock.get(0);

		String newPassword = "NG3LsqEZx3RPEshgS3JX";

		String oldPassword = userRepository.findById(mockedUser.getLogin()).get().getPassword();

		userService.changePassword(mockedUser.getLogin(), newPassword);

		User user = userRepository.findById(mockedUser.getLogin()).get();

		assertNotEquals(user.getPassword(), oldPassword);
	}
}
