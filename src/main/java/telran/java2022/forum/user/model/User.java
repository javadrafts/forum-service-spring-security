package telran.java2022.forum.user.model;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of = { "login" })
@Document(collection = "users")
public class User {
	@Id
	String login;
	String password;
	LocalDateTime passwordExpiration;
	String firstName;
	String lastName;
	Set<String> roles = new HashSet<String>(List.of("USER"));
}
