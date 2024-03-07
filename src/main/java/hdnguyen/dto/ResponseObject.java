package hdnguyen.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResponseObject {
    private Boolean isSuccess;
    private String message;
    private Object data;

    public ResponseObject(Object data) {
        this.isSuccess = true;
        this.message = "Success";
        this.data = data;
    }






}
