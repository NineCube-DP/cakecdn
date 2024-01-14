package pl.ninecube.oss.cakecdn.exception;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
class ErrorResponse {
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
    private String error;
    private String message;
    private int status;
    private String path;
}
