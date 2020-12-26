package guru.springframework.controlles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ControllerAdvice
public class ControllerExcepitonHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NumberFormatException.class)
    public String handNumberFormatException(Model model, Exception exception){
        log.error("Handling number format exception");
        log.error(exception.getMessage());

        model.addAttribute("message",exception.getMessage());
        return "400error";
    }
}
