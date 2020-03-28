package jp.akinori.ecsite.form.admin;

import lombok.Data;

import java.util.UUID;

@Data
public class ItemForm {
    private String name;
    private UUID categoryId;
    private String description;
    private Integer price;
    private Long stock;
}
