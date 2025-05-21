package za.co.mkhungo.bank.response;

import lombok.Builder;

import java.time.Instant;
import java.util.Map;

/**
 * @author Noxolo.Mkhungo
 */
@Builder
public record CustomErrorResponse(Instant timestamp,
                                  int status,
                                  String error,
                                  String message,
                                  String path,
                                  Map<String, Object> details) {
    public CustomErrorResponse {
        if (status < 400 || status >= 600) {
            throw new IllegalArgumentException("Status code must be 4xx or 5xx");
        }
    }
}