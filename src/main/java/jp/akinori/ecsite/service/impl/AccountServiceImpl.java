package jp.akinori.ecsite.service.impl;

import jp.akinori.ecsite.entity.Address;
import jp.akinori.ecsite.entity.Category;
import jp.akinori.ecsite.entity.PayMethod;
import jp.akinori.ecsite.entity.User;
import jp.akinori.ecsite.exception.RecordNotFoundException;
import jp.akinori.ecsite.form.AddressForm;
import jp.akinori.ecsite.form.PayMethodForm;
import jp.akinori.ecsite.repository.AddressRepository;
import jp.akinori.ecsite.repository.CategoryRepository;
import jp.akinori.ecsite.repository.PayMethodRepository;
import jp.akinori.ecsite.repository.UserRepository;
import jp.akinori.ecsite.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PayMethodRepository payMethodRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public Map<String, String> fetchCategoryNameByUuid() {
        List<Category> all = categoryRepository.findAll();
        Map<String, String> map = new LinkedHashMap<>();
        all.forEach(e->map.put(e.getUuid().toString(), e.getName()));
        return map;
    }

    @Override
    public User fetchUserById(UUID uuid) {
        Optional<User> userOptional = userRepository.findByUuid(uuid);

        if (!userOptional.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }

        return userOptional.get();
    }

    @Override
    public List<Address> fetchAddressesByUserId(UUID userId) {
        return addressRepository.findAllByUserIdAndDeletedFalse(userId);
    }

    @Override
    public Address addAddress(AddressForm form, UUID userId) {
        Address address = form.convertToEntity();
        address.setUserId(userId);
        address.setUuid(UUID.randomUUID());

        updateDefaultAddress(address);

        return addressRepository.saveAndFlush(address);
    }

    @Override
    public Address editAddress(AddressForm form) {
        Optional<Address> optionalAddress = addressRepository.findByUuid(form.getUuid());
        if (!optionalAddress.isPresent()) {
            throw new RecordNotFoundException("Address not found."); //fixme
        }

        Address address = optionalAddress.get();
        address.setPostalCode(form.getPostalCode());
        address.setAddress1(form.getAddress1());
        address.setAddress2(form.getAddress2());
        address.setAddress3(form.getAddress3());
        address.setDefaultAddress(form.isDefaultAddress());

        updateDefaultAddress(address);

        return addressRepository.saveAndFlush(address);
    }

    @Override
    public Address fetchAddressById(UUID uuid) {
        Optional<Address> optionalAddress = addressRepository.findByUuid(uuid);

        if (!optionalAddress.isPresent()) {
            throw new RecordNotFoundException("User not found");
        }

        return optionalAddress.get();
    }

    @Override
    public AddressForm convertToForm(Address address) {
        return new AddressForm(address);
    }

    @Override
    public void deleteAddress(String uuidStr) {
        Optional<Address> optionalAddress = addressRepository.findByUuid(UUID.fromString(uuidStr));
        if (!optionalAddress.isPresent()) {
            throw new RecordNotFoundException("address not found"); // fixme
        }
        Address address = optionalAddress.get();
        address.setDeleted(true);
        addressRepository.saveAndFlush(address);
    }

    @Override
    public List<PayMethod> fetchPayMethodsByUserId(UUID userId) {
        return payMethodRepository.findAllByUserIdAndDeletedFalse(userId);
    }

    @Override
    public PayMethod addPayMethod(PayMethodForm form, UUID userId) {
        PayMethod payMethod = form.convertToEntity();
        payMethod.setUserId(userId);
        payMethod.setUuid(UUID.randomUUID());

        updateDefaultPayMethod(payMethod);

        return payMethodRepository.saveAndFlush(payMethod);
    }

    @Override
    public PayMethod editPayMethod(PayMethodForm form) {
        Optional<PayMethod> optionalPayMethod = payMethodRepository.findByUuid(form.getUuid());
        if (!optionalPayMethod.isPresent()) {
            throw new RecordNotFoundException("Address not found.");
        }

        PayMethod payMethod = optionalPayMethod.get();
        payMethod.setCardHolder(form.getCardHolder());
        payMethod.setBrand(form.getBrand());
        payMethod.setExpireMonth(form.getExpireMonth());
        payMethod.setExpireYear(form.getExpireYear());
        payMethod.setDefaultMethod(form.isDefaultMethod());
        payMethod.setCardNumber(form.getCardNumber());

        updateDefaultPayMethod(payMethod);

        return payMethodRepository.saveAndFlush(payMethod);
    }

    @Override
    public PayMethod fetchPayMethodById(UUID uuid) {
        Optional<PayMethod> optionalPayMethod = payMethodRepository.findByUuid(uuid);

        if (!optionalPayMethod.isPresent()) {
            throw new RecordNotFoundException("User not found");
        }

        return optionalPayMethod.get();
    }

    @Override
    public PayMethodForm convertToForm(PayMethod payMethod) {
        return new PayMethodForm(payMethod);
    }

    @Override
    public void deletePayMethod(String uuidStr) {
        Optional<PayMethod> optional = payMethodRepository.findByUuid(UUID.fromString(uuidStr));
        if (!optional.isPresent()) {
            throw new RecordNotFoundException("Pay method not found"); // fixme
        }
        PayMethod payMethod = optional.get();
        payMethod.setDeleted(true);
        payMethodRepository.saveAndFlush(payMethod);
    }

    private void updateDefaultAddress(Address address) {
        if (address.isDefaultAddress()) {
            Optional<Address> defaultAddressOptional = addressRepository.findByDefaultAddressTrue();
            if (defaultAddressOptional.isPresent()) {
                Address defaultAddress = defaultAddressOptional.get();
                defaultAddress.setDefaultAddress(false);
                addressRepository.save(defaultAddress);
            }
        }
    }

    private void updateDefaultPayMethod(PayMethod payMethod) {
        if (payMethod.isDefaultMethod()) {
            Optional<PayMethod> payMethodOptional = payMethodRepository.findByDefaultMethodTrue();
            if (payMethodOptional.isPresent()) {
                PayMethod defaultMethod = payMethodOptional.get();
                defaultMethod.setDefaultMethod(false);
                payMethodRepository.save(defaultMethod);
            }
        }
    }

}
