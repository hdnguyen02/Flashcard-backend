package hdnguyen.advice;

import hdnguyen.dto.ResponseObject;
import hdnguyen.exception.AddException;
import hdnguyen.exception.ForbiddenException;
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseObject handleException(Exception e){
        return ResponseObject.builder()
                .message(e.getMessage())
                .status("failure")
                .data(null)
                .build();
    }

    @ExceptionHandler(RegisterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
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
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseObject handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseObject.builder()
                .message(e.getMessage())
                .status("failure")
                .data(null)
                .build();
    }

    @ExceptionHandler(StorageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseObject handleStorageException(StorageException e) {
        return ResponseObject.builder()
                .message(e.getMessage())
                .status("failure")
                .data(null)
                .build();
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseObject handleForbiddenException(ForbiddenException e) {
        return ResponseObject.builder()
                .message(e.getMessage())
                .status("failure")
                .data(null)
                .build();
    }
}
