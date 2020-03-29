package jp.akinori.ecsite.form;

import jp.akinori.ecsite.entity.Address;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.UUID;

@Data
public class AddressForm {
    private UUID uuid;
    @NotBlank
    private String postalCode;
    @NotBlank
    private String address1;
    @NotBlank
    private String address2;
    @NotBlank
    private String address3;
    private boolean defaultAddress;

    public AddressForm() {

    }

    public AddressForm(Address address) {
        this.uuid = address.getUuid();
        this.postalCode = address.getPostalCode();
        this.address1 = address.getAddress1();
        this.address2 = address.getAddress2();
        this.address3 = address.getAddress3();
        this.defaultAddress = address.isDefaultAddress();
    }

    public Address convertToEntity() {
        Address address = new Address();
        address.setUuid(this.uuid);
        address.setAddress1(this.address1);
        address.setAddress2(this.address2);
        address.setAddress3(this.address3);
        address.setPostalCode(this.postalCode);
        if (defaultAddress) {
            address.setDefaultAddress(true);
        } else {
            address.setDefaultAddress(false);
        }
        address.setDeleted(false);
        return address;
    }
}
