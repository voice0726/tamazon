package jp.akinori.ecsite.repository;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import jp.akinori.ecsite.helper.ReplacementCsvDataSetLoader;
import jp.akinori.ecsite.entity.Address;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.UUID;

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
class AddressRepositoryTest {

    @Autowired
    AddressRepository repository;

    @Test
    public void insert() {
        String s = UUID.randomUUID().toString();
        System.out.println(s);

        Address address = new Address();
        address.setUserId(UUID.randomUUID());
        address.setUuid(UUID.randomUUID());
        address.setPostalCode("255-0003");
        address.setAddress1("大磯715");
        address.setAddress2("中郡大磯町");
        address.setAddress3("神奈川県");
        address.setDefaultAddress(false);
        address.setDeleted(false);

        repository.saveAndFlush(address);

    }
}