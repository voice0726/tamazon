package jp.akinori.ecsite.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "cart_item", schema = "ec-site", catalog = "")
public class CartItem extends AbstractEntity {
    private UUID uuid;
    private UUID cartId;
    private UUID itemId;
    private int count;
    private boolean deleted;
    private Cart cart;
    private Item item;

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
    @Column(name = "cart_id")
    public UUID getCartId() {
        return cartId;
    }

    public void setCartId(UUID cartId) {
        this.cartId = cartId;
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
        CartItem cartItem = (CartItem) o;
        return count == cartItem.count &&
                deleted == cartItem.deleted &&
                Objects.equals(uuid, cartItem.uuid) &&
                Objects.equals(cartId, cartItem.cartId) &&
                Objects.equals(itemId, cartItem.itemId) &&
                Objects.equals(createdAt, cartItem.createdAt) &&
                Objects.equals(updatedAt, cartItem.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, cartId, itemId, count, deleted, createdAt, updatedAt);
    }

    @ManyToOne
    @JoinColumn(name = "cart_id", referencedColumnName = "uuid", nullable = false, insertable = false, updatable = false)
    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    @OneToOne
    @JoinColumn(name = "item_id", referencedColumnName = "uuid", nullable = false, insertable = false, updatable = false)
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }
}
