package gumga.framework.presentation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class GumgaErrorMessage {

    private List<String> errors;

    public GumgaErrorMessage() {
    }

    public GumgaErrorMessage(List<String> errors) {
        this.errors = errors;
    }

    public GumgaErrorMessage(String error) {
        this(Collections.singletonList(error));
    }

    public GumgaErrorMessage(String... errors) {
        this(Arrays.asList(errors));
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

}
