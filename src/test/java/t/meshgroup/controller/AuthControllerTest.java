package t.meshgroup.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import t.meshgroup.database.repo.UserRepository;
import t.meshgroup.models.requests.SignInRequest;
import t.meshgroup.utils.auth.JwtUtils;
import t.meshgroup.utils.autoloaders.databases.DBInitPostgreSQL;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Not best Test ) Need normal Repository handler + some env.
     */
    @Test
    void signIn() throws Exception {
        DBInitPostgreSQL.initDomain("application-test.properties");
        String encodedPassword = JwtUtils.passwordEncoder.encode("password1");
        jdbcTemplate.execute("insert into users (id, date_of_birth, name, password) VALUES (1, '1970-01-03', 'test', '" + encodedPassword + "')");
        jdbcTemplate.execute("insert into phones (id, phone, user_id) VALUES (1, '79000000000', 1)");
        jdbcTemplate.execute("insert into emails (id, email, user_id) VALUES (1, 'email1', 1)");

        SignInRequest signInRequest = new SignInRequest();
        signInRequest.setEmail("email1");
        signInRequest.setPassword("password1");

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
        ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();

        String requestJson = ow.writeValueAsString(signInRequest);

        this.mockMvc.perform(
                post("/auth/sign-in").content(requestJson).contentType("application/json")
        ).andDo(print()).andExpect(status().isOk());
    }
}