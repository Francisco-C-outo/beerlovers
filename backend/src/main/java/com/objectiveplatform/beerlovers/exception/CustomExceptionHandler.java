package com.objectiveplatform.beerlovers.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles the use case where an argument of our API annotated with the @Valid annotation failed the validation
     * useful to be aware of failed constraints we imposed in our DTOs for example
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        logger.info(ex.getClass().getName());

        final List<String> errors = new ArrayList<>();
        for (final FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(error.getField() + ": " + error.getDefaultMessage());
        }
        for (final ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());
        }
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(
            final TypeMismatchException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = format("%s value for %s should be of type %s", ex.getValue(), ex.getPropertyName(), ex.getRequiredType());

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Raised when the part of a "multipart/form-data" request identified by its name cannot be found.
     * This may be because the request is not a multipart/form-data request, because the part is not present in the request,
     * or because the web application is not configured correctly for processing multipart requests, e.g. no MultipartResolver.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            final MissingServletRequestPartException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = format("%s part is missing", ex.getRequestPartName());
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Triggered when a 'required' request parameter is missing.
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            final MissingServletRequestParameterException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = format("%s parameter is missing", ex.getParameterName());
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            final MethodArgumentTypeMismatchException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final String error = String.format("%s should be of type %s", ex.getName(), Objects.requireNonNull(ex.getRequiredType()).getName());

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles javax.validation.ConstraintViolationException and as the name states is thrown when an imposed constraint
     * on a field fails - check @Validated annotations
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(final ConstraintViolationException ex, final WebRequest request) {
        logger.info(ex.getClass().getName());

        final List<String> errors = new ArrayList<>();
        for (final ConstraintViolation<?> violation : ex.getConstraintViolations()) {
            errors.add(violation.getRootBeanClass().getName() + " " + violation.getPropertyPath() + ": " + violation.getMessage());
        }

        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Thrown when our API has no handler for the operation requested
     */
    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            final NoHandlerFoundException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String error = format("No handler found for %s %s", ex.getHttpMethod(), ex.getRequestURL());
        final ApiError apiError = new ApiError(NOT_FOUND, ex.getLocalizedMessage(), error);

        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles the use case where a client sends a request with an unsupported HTTP method
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            final HttpRequestMethodNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(format("%s method is not supported for this request. Supported methods are ", ex.getMethod()));
        for (HttpMethod t : Objects.requireNonNull(ex.getSupportedHttpMethods())) {
            builder.append(t).append(" ");
        }

        final ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Handles the use case where a client sends a request with an unsupported media type
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            final HttpMediaTypeNotSupportedException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(format("%s media type is not supported. Supported media types are ", ex.getContentType()));
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));

        final ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, ex.getLocalizedMessage(), builder.substring(0, builder.length() - 2));
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    @ExceptionHandler(EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(final EntityNotFoundException ex) {
        final ApiError apiError = new ApiError(NOT_FOUND, ex.getLocalizedMessage(), ex.getMessage());
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getStatus());
    }

    /**
     * Fallback handler: Catches every exception without a default handler - as the API is iteratively developed
     * and becomes more robust, fewer exceptions should fall under this handler
     */
    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        final ApiError apiError = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
        return new ResponseEntity<>(
                apiError, new HttpHeaders(), apiError.getStatus());
    }
}
