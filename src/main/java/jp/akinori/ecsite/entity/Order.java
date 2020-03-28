package jp.akinori.ecsite.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order", schema = "ec-site")
public class Order extends AbstractEntity {
    private UUID uuid;
    private UUID userId;
    private String payMethodId;
    private short shipStatus;
    private User user;
    private List<OrderItem> orderItems;

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
    @Column(name = "pay_method_id")
    public String getPayMethodId() {
        return payMethodId;
    }

    public void setPayMethodId(String payMethodId) {
        this.payMethodId = payMethodId;
    }

    @Basic
    @Column(name = "ship_status")
    public short getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(short shipStatus) {
        this.shipStatus = shipStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return shipStatus == order.shipStatus &&
                Objects.equals(uuid, order.uuid) &&
                Objects.equals(userId, order.userId) &&
                Objects.equals(payMethodId, order.payMethodId) &&
                Objects.equals(createdAt, order.createdAt) &&
                Objects.equals(updatedAt, order.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, userId, payMethodId, shipStatus, createdAt, updatedAt);
    }

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "uuid", nullable = false, insertable = false, updatable = false)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @OneToMany(mappedBy = "order")
    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
        this.orderItems = orderItems;
    }
}
