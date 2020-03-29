package jp.akinori.ecsite.service.impl;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import jp.akinori.ecsite.helper.ReplacementCsvDataSetLoader;
import jp.akinori.ecsite.service.AccountService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestExecutionListeners({
        DependencyInjectionTestExecutionListener.class,
        TransactionDbUnitTestExecutionListener.class,
        DbUnitTestExecutionListener.class,
        WithSecurityContextTestExecutionListener.class})
@DbUnitConfiguration(
        databaseConnection = "dataSource",
        dataSetLoader = ReplacementCsvDataSetLoader.class
)
class AccountServiceImplTest {

    private static final String TEST_DATA_DIR = "/DBUnit/AccountServiceImplTest/";

    @Autowired
    AccountService accountService;

    @Test
    @DatabaseSetup(TEST_DATA_DIR)
    void fetchCategoryNameByUuid() {
        Map<String, String> map = accountService.fetchCategoryNameByUuid();
    }

    @Test
    void fetchUserById() {
    }

    @Test
    void fetchAddressesByUserId() {
    }

    @Test
    void addAddress() {
    }

    @Test
    void editAddress() {
    }

    @Test
    void fetchAddressById() {
    }

    @Test
    void convertToForm() {
    }

    @Test
    void deleteAddress() {
    }

    @Test
    void fetchPayMethodsByUserId() {
    }

    @Test
    void addPayMethod() {
    }

    @Test
    void editPayMethod() {
    }

    @Test
    void fetchPayMethodById() {
    }

    @Test
    void testConvertToForm() {
    }
}