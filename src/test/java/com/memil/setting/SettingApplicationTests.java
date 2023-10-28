package com.memil.setting;

import com.memil.setting.entity.User;
import com.memil.setting.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootTest
class SettingApplicationTests {
	@Autowired
	BCryptPasswordEncoder encoder;

	@Autowired
	UserRepository userRepository;

	@Test
	void contextLoads() {
	}

	@Test
	void insertUsers(){
		User memil = User.builder()
				.username("memil")
				.password(encoder.encode("loveyoumemil"))
				.name("memil")
				.provider("local")
				.build();

		User dani = User.builder()
				.username("dani")
				.password(encoder.encode("loveyoudani"))
				.name("dani")
				.provider("local")
				.build();

		userRepository.save(memil);
		userRepository.save(dani);
	}

}
