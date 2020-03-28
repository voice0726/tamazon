package jp.akinori.ecsite.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "review", schema = "ec-site")
public class Review extends AbstractEntity {
    private UUID uuid;
    private UUID userId;
    private UUID itemId;
    private short star;
    private String comment;
    private boolean deleted;
    private Item item;
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
    @Column(name = "item_id")
    public UUID getItemId() {
        return itemId;
    }

    public void setItemId(UUID itemId) {
        this.itemId = itemId;
    }

    @Basic
    @Column(name = "star")
    public short getStar() {
        return star;
    }

    public void setStar(short star) {
        this.star = star;
    }

    @Basic
    @Column(name = "comment")
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
        Review review = (Review) o;
        return star == review.star &&
                deleted == review.deleted &&
                Objects.equals(uuid, review.uuid) &&
                Objects.equals(userId, review.userId) &&
                Objects.equals(itemId, review.itemId) &&
                Objects.equals(comment, review.comment) &&
                Objects.equals(createdAt, review.createdAt) &&
                Objects.equals(updatedAt, review.updatedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, userId, itemId, star, comment, deleted, createdAt, updatedAt);
    }

    @ManyToOne
    @JoinColumn(name = "item_id", referencedColumnName = "uuid", nullable = false, insertable = false, updatable = false)
    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
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
