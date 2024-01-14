/* (C)2023 */
package pl.ninecube.oss.cakecdn.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler({BusinessException.class})
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .message(exception.getMessage())
                        .error(exception.getClass().getSimpleName())
                        .path(request.getServletPath())
                        .status(HttpStatus.BAD_REQUEST.value())
                        .build());
    }

    @ResponseBody
    @ExceptionHandler({TechnicalException.class})
    public ResponseEntity<ErrorResponse> handleTechnicalException(TechnicalException exception, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR).
                body(ErrorResponse.builder()
                        .message(exception.getMessage())
                        .error(exception.getClass().getSimpleName())
                        .path(request.getServletPath())
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build());
    }
}
