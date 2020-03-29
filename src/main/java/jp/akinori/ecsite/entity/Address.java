package jp.akinori.ecsite.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "address", schema = "ec-site")
public class Address extends AbstractEntity {
    private UUID uuid;
    @Type(type="uuid-char")
    private UUID userId;
    private String postalCode;
    private String address1;
    private String address2;
    private String address3;
    private boolean defaultAddress;
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
    @Column(name = "postal_code")
    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    @Basic
    @Column(name = "address1")
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    @Basic
    @Column(name = "address2")
    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    @Basic
    @Column(name = "address3")
    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    @Basic
    @Column(name = "default_address")
    public boolean isDefaultAddress() {
        return defaultAddress;
    }

    public void setDefaultAddress(boolean defaultAddr) {
        this.defaultAddress = defaultAddr;
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
        Address address = (Address) o;
        return defaultAddress == address.defaultAddress &&
                deleted == address.deleted &&
                Objects.equals(uuid, address.uuid) &&
                Objects.equals(userId, address.userId) &&
                Objects.equals(postalCode, address.postalCode) &&
                Objects.equals(address1, address.address1) &&
                Objects.equals(address2, address.address2) &&
                Objects.equals(address3, address.address3) &&
                Objects.equals(createdAt, address.createdAt) &&
                Objects.equals(updatedAt, address.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, userId, postalCode, address1, address2, address3, defaultAddress, deleted, createdAt, updatedAt);
    }

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uuid", nullable = false, insertable = false, updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
