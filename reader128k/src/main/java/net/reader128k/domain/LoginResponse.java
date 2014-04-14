package net.reader128k.domain;

import java.util.List;

public class LoginResponse {
    public boolean success;
    public List<String> form_errors;
    public List<String> username_errors;
    public List<String> password_errors;
}
