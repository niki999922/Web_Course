package ru.itmo.wm4.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class NoticeForm {

    @NotEmpty
    @Size(min = 5, message = "expected more 5 letters")
    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}