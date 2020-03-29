package jp.akinori.ecsite.controller;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import jp.akinori.ecsite.entity.Address;
import jp.akinori.ecsite.form.AddressForm;
import jp.akinori.ecsite.helper.ReplacementCsvDataSetLoader;
import jp.akinori.ecsite.repository.AddressRepository;
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
class AddressControllerTest {

    private static final String TEST_DATA_DIR = "/DBUnit/AddressControllerTest/";

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AddressRepository addressRepository;

    /**
     * Indexページを表示できるかテスト
     *
     * @throws Exception 例外
     */
    @Test
    @DatabaseSetup(TEST_DATA_DIR)
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testIndex() throws Exception {

        List<Address> addresses = addressRepository
                .findAllByUserIdAndDeletedFalse(UUID.fromString("d6859a71-5779-4ea4-804d-7216aa958b5a"));

        MockHttpServletRequestBuilder getRequest =
                MockMvcRequestBuilders
                        .get("/accounts/addresses/");
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(view().name("accounts/addresses/index"))
                .andExpect(model().attribute("addressList", addresses))
                .andReturn();
    }

    /**
     * 新規追加ページを表示できるかテスト
     *
     * @throws Exception 例外
     */
    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testAdd() throws Exception {

        MockHttpServletRequestBuilder getRequest =
                MockMvcRequestBuilders
                        .get("/accounts/addresses/add");
        mockMvc.perform(getRequest)
                .andExpect(status().isOk())
                .andExpect(view().name("accounts/addresses/add"))
                .andExpect(model().attribute("addressForm", new AddressForm()))
                .andReturn();
    }

    /**
     * 正常なフォームでPOST送信し，追加されているか確認
     *
     * @throws Exception 例外
     */
    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testAddExec() throws Exception {

        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .post("/accounts/addresses/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("postalCode", "111-1111")
                        .param("address1", "unitTest")
                        .param("address2", "unitTest")
                        .param("address3", "unitTest");

        mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(model().hasNoErrors())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/accounts/addresses/"))
                .andReturn();

        List<Address> addresses = addressRepository
                .findAllByUserIdAndDeletedFalse(UUID.fromString("d6859a71-5779-4ea4-804d-7216aa958b5a"));

        Optional<Address> first = addresses.stream()
                .filter(e -> "unitTest".equals(e.getAddress1()))
                .filter(e -> "unitTest".equals(e.getAddress2()))
                .filter(e -> "unitTest".equals(e.getAddress3()))
                .findFirst();


        assert first.isPresent();
        Address address = first.get();

        assertThat(address.getPostalCode(), is("111-1111"));
        assertThat(address.getAddress1(), is("unitTest"));
        assertThat(address.getAddress2(), is("unitTest"));
        assertThat(address.getAddress3(), is("unitTest"));
    }

    /**
     * postalCodeが空欄の状態でPOSTリクエストを送信し，バリデーションで弾くかテスト。
     *
     * @throws Exception
     */
    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testAddExecInvalidPostalCode() throws Exception {

        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .post("/accounts/addresses/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("postalCode", "")
                        .param("address1", "unitTest")
                        .param("address2", "unitTest")
                        .param("address3", "unitTest");

        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("accounts/addresses/add"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        assert modelAndView != null;
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult = (BindingResult) model
                .get("org.springframework.validation.BindingResult.addressForm");
        FieldError fieldError = bindingResult.getFieldError();
        assert fieldError != null;
        assertThat(fieldError.getField(), is("postalCode"));
    }

    /**
     * address1が空欄の状態でPOSTリクエストを送信し，バリデーションで弾くかテスト。
     *
     * @throws Exception
     */
    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testAddExecInvalidAddress1() throws Exception {

        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .post("/accounts/addresses/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("postalCode", "111-1111")
                        .param("address1", "")
                        .param("address2", "unitTest")
                        .param("address3", "unitTest");

        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("accounts/addresses/add"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        assert modelAndView != null;
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult = (BindingResult) model
                .get("org.springframework.validation.BindingResult.addressForm");
        FieldError fieldError = bindingResult.getFieldError();
        assert fieldError != null;
        assertThat(fieldError.getField(), is("address1"));
    }

    /**
     * address2が空欄の状態でPOSTリクエストを送信し，バリデーションで弾くかテスト。
     *
     * @throws Exception
     */
    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testAddExecInvalidAddress2() throws Exception {

        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .post("/accounts/addresses/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("postalCode", "111-1111")
                        .param("address1", "unitTest")
                        .param("address2", "")
                        .param("address3", "unitTest");

        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("accounts/addresses/add"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        assert modelAndView != null;
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult = (BindingResult) model
                .get("org.springframework.validation.BindingResult.addressForm");
        FieldError fieldError = bindingResult.getFieldError();
        assert fieldError != null;
        assertThat(fieldError.getField(), is("address2"));
    }

    /**
     * addresss3が空欄の状態でPOSTリクエストを送信し，バリデーションで弾くかテスト。
     *
     * @throws Exception
     */
    @Test
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testAddExecInvalidAddress3() throws Exception {

        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .post("/accounts/addresses/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("postalCode", "111-1111")
                        .param("address1", "unitTest")
                        .param("address2", "unitTest")
                        .param("address3", "");

        MvcResult mvcResult = mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(model().hasErrors())
                .andExpect(view().name("accounts/addresses/add"))
                .andReturn();

        ModelAndView modelAndView = mvcResult.getModelAndView();
        assert modelAndView != null;
        Map<String, Object> model = modelAndView.getModel();
        BindingResult bindingResult = (BindingResult) model
                .get("org.springframework.validation.BindingResult.addressForm");
        FieldError fieldError = bindingResult.getFieldError();
        assert fieldError != null;
        assertThat(fieldError.getField(), is("address3"));
    }

    @Test
    @DatabaseSetup(TEST_DATA_DIR)
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testEditIndex() throws Exception {

        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .get("/accounts/addresses/edit/040a2300-5bc0-4a3b-ac89-bbaeae71a8ad");

        mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("accounts/addresses/edit"))
                .andReturn();
    }

    @Test
    @DatabaseSetup(TEST_DATA_DIR)
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testEditExec() throws Exception {

        // すべての項目を編集（既定をfalse）
        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .post("/accounts/addresses/edit")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("uuid", "040a2300-5bc0-4a3b-ac89-bbaeae71a8ad")
                        .param("postalCode", "222-2222")
                        .param("address1", "edited")
                        .param("address2", "edited")
                        .param("address3", "edited")
                        .param("defaultAddress", "false");

        mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/accounts/addresses/"))
                .andReturn();

        Optional<Address> addressOptional = addressRepository
                .findByUuid(UUID.fromString("040a2300-5bc0-4a3b-ac89-bbaeae71a8ad"));

        assert addressOptional.isPresent();

        Address address = addressOptional.get();
        assertThat(address.getUuid(), is(UUID.fromString("040a2300-5bc0-4a3b-ac89-bbaeae71a8ad")));
        assertThat(address.getPostalCode(), is("222-2222"));
        assertThat(address.getAddress1(), is("edited"));
        assertThat(address.getAddress2(), is("edited"));
        assertThat(address.getAddress3(), is("edited"));
        assertThat(address.isDefaultAddress(), is(false));

        // 既定をtrueにするテスト
        MockHttpServletRequestBuilder postRequest2 =
                MockMvcRequestBuilders
                        .post("/accounts/addresses/edit")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(APPLICATION_FORM_URLENCODED)
                        .param("uuid", "040a2300-5bc0-4a3b-ac89-bbaeae71a8ad")
                        .param("postalCode", "222-2222")
                        .param("address1", "edited")
                        .param("address2", "edited")
                        .param("address3", "edited")
                        .param("defaultAddress", "true");

        mockMvc.perform(postRequest2);
        Optional<Address> addressOptional2 = addressRepository
                .findByUuid(UUID.fromString("040a2300-5bc0-4a3b-ac89-bbaeae71a8ad"));

        assert addressOptional2.isPresent();

        Address address2 = addressOptional2.get();
        assertThat(address2.getUuid(), is(UUID.fromString("040a2300-5bc0-4a3b-ac89-bbaeae71a8ad")));
        assertThat(address2.isDefaultAddress(), is(true));
    }

    @Test
    @DatabaseSetup(TEST_DATA_DIR)
    @WithUserDetails(value = "test", userDetailsServiceBeanName = "userDetailsServiceImpl")
    public void testDelete() throws Exception {
        MockHttpServletRequestBuilder postRequest =
                MockMvcRequestBuilders
                        .get("/accounts/addresses/delete/040a2300-5bc0-4a3b-ac89-bbaeae71a8ad");

        mockMvc.perform(postRequest)
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/accounts/addresses/"))
                .andReturn();

        Optional<Address> addressOptional = addressRepository
                .findByUuid(UUID.fromString("040a2300-5bc0-4a3b-ac89-bbaeae71a8ad"));

        assert addressOptional.isPresent();

        Address address = addressOptional.get();
        assertThat(address.isDeleted(), is(true));
    }


}