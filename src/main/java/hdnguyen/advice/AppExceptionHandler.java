package hdnguyen.advice;

import hdnguyen.dto.ResponseObject;
import hdnguyen.exception.RegisterException;
import hdnguyen.exception.StorageException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class AppExceptionHandler {
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // các lỗi đều trả ra 401
    public ResponseObject handleException(Exception e) {
        return ResponseObject.builder()
                .status("failure")
                .message(e.getMessage())
                .data(null)
                .build();
    }


    @ExceptionHandler(RegisterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseObject handleRegisterException(RegisterException e){
            return ResponseObject.builder()
                    .status("failure")
                    .message(e.getMessage())
                    .data(e.getListError())
                    .build();
    }


    @ExceptionHandler(StorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseObject handleStorageException(StorageException e) {
        return ResponseObject.builder()
                .status("failure")
                .message(e.getMessage())
                .data(null)
                .build();
    }
}
