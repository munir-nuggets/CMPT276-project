package cmpt276.project.marketmimic;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import cmpt276.project.marketmimic.controllers.loginController;
import cmpt276.project.marketmimic.model.*;

@WebMvcTest(loginController.class)
class MarketmimicApplicationTests {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@MockBean
	UserRepository userRepo;

	@Autowired
	private MockMvc mockMvc;

	@Test
	void contextLoads() {
	}

	@Test
	void testLogin() throws Exception {
		List<User> users = new ArrayList<>();

        User user1 = new User("alice", "alice@example.com", "password1");
        users.add(user1);

        User user2 = new User("bob", "bob@example.com", "password2");
        users.add(user2);

		for (User user : users) {
			when(userRepo.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
			when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		}


		mockMvc.perform(MockMvcRequestBuilders.post("/userlogin")
            .param("usernameOrEmail", "alice")
            .param("password", "password1"))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/api/stocks/"));

    mockMvc.perform(MockMvcRequestBuilders.post("/userlogin")
            .param("usernameOrEmail", "bob@example.com")
            .param("password", "password2"))
        .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
        .andExpect(MockMvcResultMatchers.redirectedUrl("/api/stocks/"));
	}

	@Test
	void testLoginFail() throws Exception {
		List<User> users = new ArrayList<>();

		User user1 = new User("alice", "alice@example.com", "password1");
        users.add(user1);

        User user2 = new User("bob", "bob@example.com", "password2");
        users.add(user2);

		for (User user : users) {
			when(userRepo.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
			when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		}

		mockMvc.perform(MockMvcRequestBuilders.post("/userlogin")
				.param("usernameOrEmail", "alice")
				.param("password", "password2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("invalidLogin"));

		mockMvc.perform(MockMvcRequestBuilders.post("/userlogin")
		.param("usernameOrEmail", "bob@gmail.com")
				.param("password", "password1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("invalidLogin"));

		mockMvc.perform(MockMvcRequestBuilders.post("/userlogin")
		.param("usernameOrEmail", "random")
				.param("password", "password1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("invalidLogin"));
	}

	@Test
	void testLoginAdmin() throws Exception {
		List<User> users = new ArrayList<>();

		User user1 = new User("alice", "alice@example.com", "password1", true);
        users.add(user1);

        User user2 = new User("bob", "bob@example.com", "password2");
        users.add(user2);

		for (User user : users) {
			when(userRepo.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
			when(userRepo.findByEmail(user.getEmail())).thenReturn(Optional.of(user));
		}

		mockMvc.perform(MockMvcRequestBuilders.post("/userlogin")
				.param("usernameOrEmail", "alice")
				.param("password", "password1"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.redirectedUrl("/admin/dashboard"));
	}
		

	@Test
	void testSignup() throws Exception {
		 mockMvc.perform(MockMvcRequestBuilders.post("/usersignup")
                .param("username", "testuser")
                .param("email", "test@example.com")
                .param("password", "password"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("homepage"));
		
		verify(userRepo, times(1)).save(argThat(user ->
            "testuser".equals(user.getUsername()) &&
            "test@example.com".equals(user.getEmail()) &&
            "password".equals(user.getPassword())
   		));
	}

	@Test
	void testSignupFail() throws Exception {
		List<User> users = new ArrayList<>();

		User user1 = new User("alice", "alice@gmail.com", "password1");
		User user2 = new User("bob", "bob@gmail.com", "password2");
		users.add(user1);
		users.add(user2);

		when(userRepo.findAll()).thenReturn(users);
		when(userRepo.findByUsername("alice")).thenReturn(Optional.of(user1));
		when(userRepo.findByEmail("bob@gmail.com")).thenReturn(Optional.of(user2));

		mockMvc.perform(MockMvcRequestBuilders.post("/usersignup")
				.param("username", "alice")
				.param("email", "a@gmail.com")
				.param("password", "password1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("usernameIsTaken"));

		mockMvc.perform(MockMvcRequestBuilders.post("/usersignup")
				.param("username", "david")
				.param("email", "bob@gmail.com")
				.param("password", "password2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("emailIsTaken"));
	}
}
