package jp.akinori.ecsite.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import jp.akinori.ecsite.entity.PayMethod;
import jp.akinori.ecsite.form.PayMethodForm;
import jp.akinori.ecsite.helper.ReplacementCsvDataSetLoader;
import jp.akinori.ecsite.repository.PayMethodRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithSecurityContextTestExecutionListener;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED;
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

    /**
     * 新規追加ページを表示できるかテスト
     * @throws Exception 例外
     */
    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testAdd() throws Exception {

        MockHttpServletRequestBuilder getRequest =
                MockMvcRequestBuilders
                        .get("/accounts/paymethods/add");
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(view().name("accounts/paymethods/add"))
                .andExpect(model().attribute("payMethodForm", new PayMethodForm()))
                .andReturn();
    }

    /**
     * 正常なフォームでPOST送信し，追加されているか確認
     * @throws Exception 例外
     */
    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testAddExec() throws Exception {

        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .post("/accounts/paymethods/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("uuid", "d6859a71-5779-4ea4-804d-7216aa958b5a")
                        .param("brand", "1")
                        .param("cardNumber", "1234123412341234")
                        .param("cardHolder", "unitTest")
                        .param("expireMonth", "12")
                        .param("expireYear", "2012");

        mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/accounts/paymethods/"))
                .andReturn();

        List<PayMethod> addresses = payMethodRepository
                .findAllByUserIdAndDeletedFalse(UUID.fromString("d6859a71-5779-4ea4-804d-7216aa958b5a"));

        Optional<PayMethod> first = addresses.stream()
                .filter(e -> e.getBrand() == 1)
                .filter(e -> "1234123412341234".equals(e.getCardNumber()))
                .filter(e -> "unitTest".equals(e.getCardHolder()))
                .filter(e -> e.getExpireMonth() == 12)
                .filter(e -> e.getExpireYear() == 2012)
                .findFirst();

        assert first.isPresent();
        PayMethod address = first.get();

        assertThat(address.getBrand(), is((short) 1));
        assertThat(address.getCardNumber(), is("1234123412341234"));
        assertThat(address.getCardHolder(), is("unitTest"));
        assertThat(address.getExpireMonth(), is(12));
        assertThat(address.getExpireYear(), is(2012));
    }

    /**
     * postalCodeが空欄の状態でPOSTリクエストを送信し，バリデーションで弾くかテスト。
     * @throws Exception
     */
    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testAddExecInvalidBrand() throws Exception {

        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .post("/accounts/paymethods/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("uuid", "d6859a71-5779-4ea4-804d-7216aa958b5a")
                        .param("brand", "")
                        .param("cardNumber", "1234123412341234")
                        .param("cardHolder", "unitTest")
                        .param("expireMonth", "12")
                        .param("expireYear", "2012");

        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("accounts/paymethods/add"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        assert modelAndView != null;
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult = (BindingResult) model
                .get("org.springframework.validation.BindingResult.payMethodForm");
        FieldError fieldError = bindingResult.getFieldError();
        assert fieldError != null;
        assertThat(fieldError.getField(), is("brand"));
        assertThat(fieldError.getDefaultMessage(), is("カードのブランドを指定してください。"));

    }

    /**
     * address1が空欄の状態でPOSTリクエストを送信し，バリデーションで弾くかテスト。
     * @throws Exception
     */
    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testAddExecInvalidCardNumber() throws Exception {

        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .post("/accounts/paymethods/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("uuid", "d6859a71-5779-4ea4-804d-7216aa958b5a")
                        .param("brand", "1")
                        .param("cardNumber", "")
                        .param("cardHolder", "unitTest")
                        .param("expireMonth", "12")
                        .param("expireYear", "2012");

        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("accounts/paymethods/add"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        assert modelAndView != null;
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult = (BindingResult) model
                .get("org.springframework.validation.BindingResult.payMethodForm");
        FieldError fieldError = bindingResult.getFieldError();
        assert fieldError != null;
        assertThat(fieldError.getField(), is("cardNumber"));
        assertThat(fieldError.getDefaultMessage(), is("カード番号が不正です。"));
    }

    /**
     * address2が空欄の状態でPOSTリクエストを送信し，バリデーションで弾くかテスト。
     * @throws Exception
     */
    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testAddExecInvalidCardHolder() throws Exception {

        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .post("/accounts/paymethods/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("uuid", "d6859a71-5779-4ea4-804d-7216aa958b5a")
                        .param("brand", "1")
                        .param("cardNumber", "1234123412341234")
                        .param("cardHolder", "")
                        .param("expireMonth", "12")
                        .param("expireYear", "2012");

        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("accounts/paymethods/add"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        assert modelAndView != null;
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult = (BindingResult) model
                .get("org.springframework.validation.BindingResult.payMethodForm");
        FieldError fieldError = bindingResult.getFieldError();
        assert fieldError != null;
        assertThat(fieldError.getField(), is("cardHolder"));
        assertThat(fieldError.getDefaultMessage(), is("カードの所有者を入力してください。"));
    }

    /**
     * addresss3が空欄の状態でPOSTリクエストを送信し，バリデーションで弾くかテスト。
     * @throws Exception
     */
    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testAddExecInvalidExpireMonth() throws Exception {

        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .post("/accounts/paymethods/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("uuid", "d6859a71-5779-4ea4-804d-7216aa958b5a")
                        .param("brand", "1")
                        .param("cardNumber", "1234123412341234")
                        .param("cardHolder", "unitTest")
                        .param("expireYear", "2012");

        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("accounts/paymethods/add"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        assert modelAndView != null;
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult = (BindingResult) model
                .get("org.springframework.validation.BindingResult.payMethodForm");
        FieldError fieldError = bindingResult.getFieldError();
        assert fieldError != null;
        assertThat(fieldError.getField(), is("expireMonth"));
        assertThat(fieldError.getDefaultMessage(), is("カードの有効期限月を入力してください。"));
    }

    /**
     * expireYearが空欄の状態でPOSTリクエストを送信し，バリデーションで弾くかテスト。
     * @throws Exception
     */
    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testAddExecInvalidExpireYear() throws Exception {

        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .post("/accounts/paymethods/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("uuid", "d6859a71-5779-4ea4-804d-7216aa958b5a")
                        .param("brand", "1")
                        .param("cardNumber", "1234123412341234")
                        .param("cardHolder", "unitTest")
                        .param("expireMonth", "12");

        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("accounts/paymethods/add"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        assert modelAndView != null;
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult = (BindingResult) model
                .get("org.springframework.validation.BindingResult.payMethodForm");
        FieldError fieldError = bindingResult.getFieldError();
        assert fieldError != null;
        assertThat(fieldError.getField(), is("expireYear"));
        assertThat(fieldError.getDefaultMessage(), is("カードの有効期限年を入力してください。"));
    }
}