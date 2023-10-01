package hdnguyen.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class RegisterException extends Exception {
    public RegisterException(String message, Map<String, String> listError ) {
        super(message);
        this.listError = listError;
    }
    Map<String, String> listError;
}
