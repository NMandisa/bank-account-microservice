package za.co.mkhungo.bank.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import za.co.mkhungo.bank.exception.AccountNotFoundException;
import za.co.mkhungo.bank.exception.InsufficientFundsException;
import za.co.mkhungo.bank.exception.InvalidAmountException;
import za.co.mkhungo.bank.exception.TransactionFailedException;
import za.co.mkhungo.bank.response.ApiResponse;
import za.co.mkhungo.bank.response.CustomErrorResponse;
import java.time.Instant;
import java.util.Map;

/**
 * @author Noxolo.Mkhungo
 */
@Slf4j
@RestControllerAdvice(annotations ={RestController.class})
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<?> handleException(Exception ex) {
        //TODO
        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);//500
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<?> handleDatabaseError(DataAccessException ex) {
        //Typical DB errors hence in try catch
        //TODO
        return new ResponseEntity<>(ex, HttpStatus.INTERNAL_SERVER_ERROR);//500
    }

    @ExceptionHandler(AccountNotFoundException.class)
    public ResponseEntity<ApiResponse<CustomErrorResponse>> handleAccountNotFound(
            AccountNotFoundException ex) {
        //LOG ERROR ECT
        //TODO
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponse.error(new CustomErrorResponse(Instant.now(),
                HttpStatus.NOT_FOUND.value(),ex.getCause().getLocalizedMessage(),ex.getLocalizedMessage(),"", Map.of())));
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiResponse<CustomErrorResponse>> handleInsufficientFunds(
            InsufficientFundsException ex) {
        //LOG ERROR ECT
        //TODO
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponse.error(new CustomErrorResponse(Instant.now(),
                HttpStatus.BAD_REQUEST.value(),ex.getCause().getMessage(),ex.getLocalizedMessage(),"", Map.of())));
    }

    @ExceptionHandler(InvalidAmountException.class)
    public ResponseEntity<ApiResponse<CustomErrorResponse>> handleInvalidAmount(
            InvalidAmountException ex) {
        //LOG ERROR ECT
        //TODO
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ApiResponse.error(new CustomErrorResponse(Instant.now(),
                HttpStatus.UNPROCESSABLE_ENTITY.value(),ex.getCause().getLocalizedMessage(),ex.getLocalizedMessage(),"", Map.of())));
    }

    @ExceptionHandler(TransactionFailedException.class)
    public ResponseEntity<ApiResponse<CustomErrorResponse>> handleTransactionFailed(
            TransactionFailedException ex) {
        //LOG ERROR ECT
        //TODO
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponse.error(new CustomErrorResponse(Instant.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),ex.getCause().getLocalizedMessage(),ex.getLocalizedMessage(),"", Map.of())));
    }
}
