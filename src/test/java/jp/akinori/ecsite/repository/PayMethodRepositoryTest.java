package jp.akinori.ecsite.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import jp.akinori.ecsite.helper.ReplacementCsvDataSetLoader;
import jp.akinori.ecsite.entity.PayMethod;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.samePropertyValuesAs;

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
class PayMethodRepositoryTest {

    private static final String DATA_FILE_PATH = "/DBUnit/PayMethodRepository/";

    @Autowired
    PayMethodRepository payMethodRepository;

    @Test
    public void testInsertAndGet() {

        UUID uuid = UUID.randomUUID();

        PayMethod payMethod = new PayMethod();
        payMethod.setBrand((short) 1);
        payMethod.setCardHolder("test");
        payMethod.setCardNumber("1234234534551223");
        payMethod.setExpireYear(2021);
        payMethod.setExpireMonth(12);
        payMethod.setUserId(uuid);
        payMethod.setDefaultMethod(false);

        PayMethod saved = payMethodRepository.saveAndFlush(payMethod);
        Optional<PayMethod> optionalPayMethod = payMethodRepository.findByUuid(uuid);
        if (!optionalPayMethod.isPresent()) {
            return;
        }
        PayMethod fetched = optionalPayMethod.get();
        assertThat(fetched, samePropertyValuesAs(saved));
    }

    @DatabaseSetup(value = DATA_FILE_PATH)
    @Test
    public void testFetchByUserId() {
        UUID userId = UUID.fromString("0442161b-8f37-4d75-942d-b546b00d915c");

        List<PayMethod> all = payMethodRepository.findAllByUserIdAndDeletedFalse(userId);
        assertThat(all.size(), equalTo(2));
    }
}