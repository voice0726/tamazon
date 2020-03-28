package jp.akinori.ecsite.form;

import jp.akinori.ecsite.entity.PayMethod;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Data
public class PayMethodForm {
    private UUID uuid;
    @NotNull(message = "{paymethod.brand}")
    private Short brand;
    @Pattern(regexp = "\\d{4}-?\\d{4}-?\\d{4}-?\\d{4}", message = "{paymethod.card-number}")
    private String cardNumber;
    @NotBlank(message = "{paymethod.card-holder}")
    private String cardHolder;
    @NotNull(message = "{paymethod.expire-month}")
    private Integer expireMonth;
    @NotNull(message = "{paymethod.expire-year}")
    private  Integer expireYear;
    private boolean defaultMethod;

    public PayMethodForm() {}

    public PayMethodForm(PayMethod payMethod) {
        this.uuid = payMethod.getUuid();
        this.brand = payMethod.getBrand();
        this.cardNumber = payMethod.getCardNumber();
        this.cardHolder = payMethod.getCardHolder();
        this.expireMonth = payMethod.getExpireMonth();
        this.expireYear = payMethod.getExpireYear();
        this.defaultMethod = payMethod.isDefaultMethod();
    }

    public PayMethod convertToEntity() {
        PayMethod payMethod = new PayMethod();
        payMethod.setUuid(this.uuid);
        payMethod.setBrand(this.brand);
        payMethod.setCardHolder(this.cardHolder);
        payMethod.setCardNumber(this.cardNumber);
        payMethod.setExpireMonth(this.expireMonth);
        payMethod.setExpireYear(this.expireYear);
        if (defaultMethod) {
            payMethod.setDefaultMethod(true);
        } else {
            payMethod.setDefaultMethod(false);
        }
        payMethod.setDeleted(false);
        return payMethod;
    }
}
