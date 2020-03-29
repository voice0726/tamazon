package jp.akinori.ecsite.service;

import jp.akinori.ecsite.entity.Address;
import jp.akinori.ecsite.entity.PayMethod;
import jp.akinori.ecsite.entity.User;
import jp.akinori.ecsite.form.AddressForm;
import jp.akinori.ecsite.form.PayMethodForm;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public interface AccountService {
    Map<String, String> fetchCategoryNameByUuid(); // todo: rename function

    User fetchUserById(UUID uuid);

    List<Address> fetchAddressesByUserId(UUID userId);

    Address addAddress(AddressForm form, UUID userId);

    Address editAddress(AddressForm form) ;

    Address fetchAddressById(UUID uuid);

    AddressForm convertToForm(Address address);

    void deleteAddress(String uuidStr);

    List<PayMethod> fetchPayMethodsByUserId(UUID userId);

    PayMethod addPayMethod(PayMethodForm form, UUID userId);

    PayMethod editPayMethod(PayMethodForm form);

    PayMethod fetchPayMethodById(UUID uuid);

    PayMethodForm convertToForm(PayMethod payMethod);

    void deletePayMethod(String uuidStr);
}
