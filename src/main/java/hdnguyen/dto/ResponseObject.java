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
    private String status;  // success or failure
    private String message;
    private Object data;
}
