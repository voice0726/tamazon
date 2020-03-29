package jp.akinori.ecsite.form.admin;

import lombok.Data;

import java.util.UUID;

@Data
public class CategoryForm {
    private UUID uuid;
    private String name;
}
