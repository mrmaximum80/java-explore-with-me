package ewm.main.error;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.info("400 {}", exception.getMessage());
        ApiError error = new ApiError(exception.getMessage(), "Not valid", HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return error;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleConstraintViolationException(ConstraintViolationException exception, HttpStatus status) {
        log.info("400 {}", exception.getMessage());
        return new ApiError(exception.getMessage(), "Integrity constraint has been violated.",
                HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.info("400 {}", exception.getMostSpecificCause().getMessage());
        return new ApiError(exception.getMostSpecificCause().getMessage(), "Required request body is missing.",
                HttpStatus.BAD_REQUEST, LocalDateTime.now());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.info("409 {}", exception.getMostSpecificCause().getMessage());
        return new ApiError(exception.getMostSpecificCause().getMessage(), "Integrity constraint has been violated.",
                HttpStatus.CONFLICT, LocalDateTime.now());
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMissingServletRequestParameterException(MissingServletRequestParameterException exception) {
        log.info("400 {}", exception.getMessage());
        return new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT, LocalDateTime.now());
    }

    @ExceptionHandler(CategoryNotEmptyException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryNotEmptyException(CategoryNotEmptyException exception) {
        log.info("409 {}", exception.getMessage());
        return new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT, LocalDateTime.now());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException exception) {
        log.info("404 {}", exception.getMessage());
        return new ApiError(exception.getMessage(), "The required object was not found.",
                HttpStatus.NOT_FOUND, LocalDateTime.now());
    }

    @ExceptionHandler(NotChangeableEventException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleNotChangeableEventException(NotChangeableEventException exception) {
        log.info("409 {}", exception.getMessage());
        return new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.NOT_FOUND, LocalDateTime.now());
    }

    @ExceptionHandler(WrongDateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleWrongDateException(WrongDateException exception) {
        log.info("400 {}", exception.getMessage());
        ApiError error = new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.BAD_REQUEST, LocalDateTime.now());
        return error;
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleConflictException(ConflictException exception) {
        log.info("409 {}", exception.getMessage());
        return new ApiError(exception.getMessage(), "For the requested operation the conditions are not met.",
                HttpStatus.CONFLICT, LocalDateTime.now());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleException(Exception exception) {
        log.info("Error", exception);
        StringWriter out = new StringWriter();
        exception.printStackTrace(new PrintWriter(out));
        String stackTrace = out.toString();
        return new ApiError(Collections.singletonList(stackTrace),
                exception.getMessage(), "INTERNAL SERVER ERROR",
                HttpStatus.INTERNAL_SERVER_ERROR, LocalDateTime.now());
    }

}
