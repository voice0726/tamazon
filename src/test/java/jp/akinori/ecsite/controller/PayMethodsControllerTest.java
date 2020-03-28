package jp.akinori.ecsite.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import jp.akinori.ecsite.entity.PayMethod;
import jp.akinori.ecsite.helper.ReplacementCsvDataSetLoader;
import jp.akinori.ecsite.repository.PayMethodRepository;
import jp.akinori.ecsite.service.AccountService;
import jp.akinori.ecsite.service.impl.AccountServiceImpl;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
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

/**
 * PayMethodsController Functional Test
 */
class PayMethodsControllerTest {

    private static final String TEST_DATA_DIR = "/DBUnit/PayMethodControllerTest/";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    PayMethodRepository payMethodRepository;

    @Test
    @DatabaseSetup(TEST_DATA_DIR)
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testIndex() throws Exception {

        List<PayMethod> payMethods = payMethodRepository
                .findAllByUserIdAndDeletedFalse(UUID.fromString("d6859a71-5779-4ea4-804d-7216aa958b5a"));

        assertThat(payMethods.size(), Matchers.equalTo(2));

        MockHttpServletRequestBuilder getRequest =
                MockMvcRequestBuilders
                        .get("/accounts/paymethods/");
        MvcResult result = mockMvc.perform(getRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("accounts/paymethods/index"))
                .andExpect(model().attribute("payMethodList", payMethods))
                .andReturn();
    }
}