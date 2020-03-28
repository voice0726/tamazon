package jp.akinori.ecsite.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "order_item", schema = "ec-site", catalog = "")
public class OrderItem extends AbstractEntity {
    private UUID uuid;
    private UUID orderId;
    private UUID itemId;
    private int count;
    private Item item;
    private Order order;

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
    @Column(name = "order_id")
    public UUID getOrderId() {
        return orderId;
    }

    public void setOrderId(UUID orderId) {
        this.orderId = orderId;
    }

    @Basic
    @Column(name = "item_id")
    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "count")
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderItem orderItem = (OrderItem) o;
        return count == orderItem.count &&
                Objects.equals(uuid, orderItem.uuid) &&
                Objects.equals(orderId, orderItem.orderId) &&
                Objects.equals(itemId, orderItem.itemId) &&
                Objects.equals(createdAt, orderItem.createdAt) &&
                Objects.equals(updatedAt, orderItem.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, orderId, itemId, count);
    }

    @OneToOne
    @JoinColumn(name = "item_id", referencedColumnName = "uuid", nullable = false, insertable = false, updatable = false)
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "uuid", nullable = false, insertable = false, updatable = false)
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
