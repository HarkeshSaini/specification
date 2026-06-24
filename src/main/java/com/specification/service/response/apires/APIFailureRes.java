package com.specification.service.response.apires;

import com.fasterxml.jackson.annotation.JsonInclude;
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
public class APIFailureRes<T> {

	private String status;
	private Integer code;
	private String message;
	private T data;
	private Boolean hasMore;

	public static <T> ResponseEntity<APIFailureRes<T>> failureResponse(T res, String message, String code) {
        APIFailureRes<T> body = APIFailureRes.<T>builder().status("failure").code(Integer.valueOf(code)).message(message).data(res).build();
		return ResponseEntity.ok(body);
	}
}
