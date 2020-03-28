package jp.akinori.ecsite.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import jp.akinori.ecsite.helper.ReplacementCsvDataSetLoader;
import jp.akinori.ecsite.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@DbUnitConfiguration(
        databaseConnection = "dataSource",
        dataSetLoader = ReplacementCsvDataSetLoader.class
)
class UserRepositoryTest {

    private static final String DATA_FILE_PATH = "/DBUnit/UserRepository/";

    @Autowired
    UserRepository repository;

    @Test
    public void testInsert() {
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setUsername("username");
        user.setPassword("password");
        user.setViewName("viewname");
        user.setDeleted(false);
        user.setRoleId(1);

        // without setting createdAt and updatedAt because of testing prepersist

        User user1 = repository.saveAndFlush(user);

        Optional<User> fetched = repository.findByUuid(user1.getUuid());
        if (!fetched.isPresent()) {
            return;
        }
        User fetchedUser = fetched.get();

        assertThat(fetchedUser.getUuid(), equalTo(user1.getUuid()));
        assertThat(fetchedUser.isDeleted(), equalTo(user1.isDeleted()));
        assertThat(fetchedUser.getPassword(), equalTo(user1.getPassword()));
        assertThat(fetchedUser.getUsername(), equalTo(user1.getUsername()));
        assertThat(fetchedUser.getViewName(), equalTo(user1.getViewName()));
        assertThat(fetchedUser.getRoleId(), equalTo(user1.getRoleId()));
        assertThat(fetchedUser.getCreatedAt(), equalTo(user1.getCreatedAt().truncatedTo(ChronoUnit.SECONDS)));
        assertThat(fetchedUser.getUpdatedAt(), equalTo(user1.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS)));

    }

    @DatabaseSetup(value = DATA_FILE_PATH)
    @Test
    public void findById() {
        Optional<User> fetched = repository.findByUuid(UUID.fromString("d6859a71-5779-4ea4-804d-7216aa958b5a"));
        if (!fetched.isPresent()) {
            return;
        }
        User user = fetched.get();
        assertThat(user.isDeleted(), equalTo(false));
        assertThat(user.getPassword(), equalTo("test"));
        assertThat(user.getUsername(), equalTo("test"));
        assertThat(user.getViewName(), equalTo("test"));
        assertThat(user.getCreatedAt(), equalTo(LocalDateTime.of(2020,3,15,9,9,35)));
        assertThat(user.getUpdatedAt(), equalTo(LocalDateTime.of(2020,3,15,9,9,35)));
    }
}