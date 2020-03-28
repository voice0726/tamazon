package jp.akinori.ecsite.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "pay_method", schema = "ec-site", catalog = "")
public class PayMethod extends AbstractEntity {
    private UUID uuid;
    private UUID userId;
    private short brand;
    private String cardNumber;
    private String cardHolder;
    private int expireMonth;
    private int expireYear;
    private boolean defaultMethod;
    private boolean deleted;
    private User user;

    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "org.hibernate.id.UUIDGenerator")
    @Type(type="uuid-char")
    @Column(name = "uuid")
    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    @Basic
    @Column(name = "user_id")
    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "brand")
    public short getBrand() {
        return brand;
    }

    public void setBrand(short brand) {
        this.brand = brand;
    }

    @Basic
    @Column(name = "card_number")
    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Basic
    @Column(name = "card_holder")
    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    @Basic
    @Column(name = "expire_month")
    public int getExpireMonth() {
        return expireMonth;
    }

    public void setExpireMonth(int expireMonth) {
        this.expireMonth = expireMonth;
    }

    @Basic
    @Column(name = "expire_year")
    public int getExpireYear() {
        return expireYear;
    }

    public void setExpireYear(int expireYear) {
        this.expireYear = expireYear;
    }

    @Basic
    @Column(name = "default_method")
    public boolean isDefaultMethod() {
        return defaultMethod;
    }

    public void setDefaultMethod(boolean defaultMethod) {
        this.defaultMethod = defaultMethod;
    }

    @Basic
    @Column(name = "deleted")
    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PayMethod payMethod = (PayMethod) o;
        return brand == payMethod.brand &&
                expireMonth == payMethod.expireMonth &&
                expireYear == payMethod.expireYear &&
                defaultMethod == payMethod.defaultMethod &&
                deleted == payMethod.deleted &&
                Objects.equals(uuid, payMethod.uuid) &&
                Objects.equals(userId, payMethod.userId) &&
                Objects.equals(cardNumber, payMethod.cardNumber) &&
                Objects.equals(cardHolder, payMethod.cardHolder) &&
                Objects.equals(createdAt, payMethod.createdAt) &&
                Objects.equals(updatedAt, payMethod.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, userId, brand, cardNumber, cardHolder, expireMonth, expireYear, defaultMethod, deleted, createdAt, updatedAt);
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uuid", nullable = false, insertable = false, updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
