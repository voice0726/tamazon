package jp.akinori.ecsite.form.admin;

import lombok.Data;

@Data
public class UserForm {
    private String username;
    private String viewname;
    private String password;
    private byte role;
}
