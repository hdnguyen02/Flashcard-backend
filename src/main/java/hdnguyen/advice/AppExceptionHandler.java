package hdnguyen.advice;

import hdnguyen.dto.ResponseObject;
import hdnguyen.exception.AddException;
import hdnguyen.exception.RegisterException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AppExceptionHandler {


    @ExceptionHandler(RegisterException.class)
    public ResponseObject handleRegisterException(RegisterException e){
            return ResponseObject.builder()
                    .message(e.getMessage())
                    .status("failure")
                    .data(e.getListError())
                    .build();
    }

    @ExceptionHandler(AddException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseObject handleAddException(AddException e) {
        return ResponseObject.builder()
                .message(e.getMessage())
                .status("failure")
                .data(null)
                .build();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseObject handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseObject.builder()
                .message(e.getMessage())
                .status("failure")
                .data(null)
                .build();
    }
}
