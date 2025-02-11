package site.thaiky.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice // anotation danh dấu là class xu ly exception cua api
public class APIHandleException {

    // mỗi khi có lỗi validation thì chay cái dòng xuẻ lý này


    // bắt lỗi MethodArgumentNotValidException do thư viện quăng ra
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleBadRequestException(MethodArgumentNotValidException exception) {
        String messages = "";

        for(FieldError error: exception.getBindingResult().getFieldErrors())
            messages += error.getDefaultMessage() + "\n";

        return new ResponseEntity(messages, HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity handleDuplicate(SQLIntegrityConstraintViolationException exception) {
        return new ResponseEntity("Duplicate", HttpStatus.BAD_REQUEST);
    }



}

