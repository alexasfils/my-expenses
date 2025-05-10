package com.alex.myexpenses.controller.advice;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.alex.myexpenses.controller.exception.BadRequestException;
import com.alex.myexpenses.controller.exception.ResourceNotFoundException;
import com.alex.myexpenses.general.ErrorDTO;

import lombok.extern.slf4j.Slf4j;

/**
 * A global exception handler that catches exceptions thrown from controllers
 * and returns structured error responses.
 * 
 * This class uses @ControllerAdvice to catch and handle exceptions like:
 * - BadRequestException
 * - MethodArgumentNotValidException
 * - ResourceNotFoundException
 * 
 * It returns appropriate error messages in the form of an ErrorDTO, with a suitable HTTP status code.
 * 
 */
@ControllerAdvice
@Slf4j
public class BaseExceptionHandler {

    /**
     * Handles BadRequestException.
     * This exception is typically thrown when the client sends invalid input.
     *
     * @param ex the exception thrown
     * @return a ResponseEntity containing the error details with a 400 BAD REQUEST status
     */
    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> badRequestException(BadRequestException ex) {
        log.info("Received a BadRequestException");
        ErrorDTO errorDetails = new ErrorDTO();
        errorDetails.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MethodArgumentNotValidException.
     * This exception occurs when method argument validation fails, such as missing required fields.
     *
     * @param ex the exception thrown
     * @return a ResponseEntity containing the validation errors with a 400 BAD REQUEST status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        // Collect all field errors and map them to a field name -> error message map
        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream()
                .collect(Collectors.toMap(
                        fieldError -> fieldError.getField(), // field name
                        fieldError -> fieldError.getDefaultMessage(), // custom message from annotations
                        (existing, replacement) -> existing)); // In case of duplicates, choose the first error message

        log.info("Received a MethodArgumentNotValidException");
        ErrorDTO errorDetails = new ErrorDTO();
        errorDetails.setMessage("Following errors have been found!");
        errorDetails.setFieldErrors(errors); // Set the field errors in the error details

        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles ResourceNotFoundException.
     * This exception is thrown when a requested resource is not found (e.g., item not in the database).
     *
     * @param ex the exception thrown
     * @return a ResponseEntity containing the error message with a 400 BAD REQUEST status
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorDTO> resourceNotFoundException(ResourceNotFoundException ex) {
        log.info("Received a ResourceNotFoundException");
        ErrorDTO errorDetails = new ErrorDTO();
        errorDetails.setMessage(ex.getMessage());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<ErrorDTO> PropertyNameExistsException(DuplicateKeyException ex){
    	log.info("Received a PropertyNameExistsException");
    	ErrorDTO errorDetails = new ErrorDTO();
    	errorDetails.setMessage(ex.getMessage());
    	return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles BadRequestException.
     * This exception is typically thrown when the client sends invalid input.
     *
     * @param ex the exception thrown
     * @return a ResponseEntity containing the error details with a 400 BAD REQUEST status
     */
//    @ExceptionHandler(ImageUploadException.class)
//    public ResponseEntity<ErrorDTO> imageUploadException(ImageUploadException ex) {
//        log.info("Received a ImageUploadException");
//        ErrorDTO errorDetails = new ErrorDTO();
//        errorDetails.setMessage(ex.getMessage());
//        return new ResponseEntity<>(errorDetails, HttpStatus.NOT_ACCEPTABLE);
//    }
}
