package com.specification.service.response.apires;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse<T> {

	@Schema(description = "Status of the response", example = "success")
	private String status;

	@Schema(description = "HTTP status code", example = "200")
	private Integer httpStatus;

	@Schema(description = "Application or business code", example = "OK")
	private String code;

	@Schema(description = "Response message", example = "Success")
	private String message;

	@Schema(description = "Response data")
	private T data;

	@Schema(description = "Indicates if more data is available", example = "true", nullable = true)
	private Boolean hasMore;

	public static <T> ResponseEntity<APIResponse<T>> successResponse(T res, String message, String code) {
		APIResponse<T> body = APIResponse.<T>builder().status("success").httpStatus(HttpStatus.OK.value()).code(code).message(message).data(res).build();
		return ResponseEntity.ok(body);
	}
}
