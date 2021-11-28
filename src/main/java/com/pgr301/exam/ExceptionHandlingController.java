package com.pgr301.exam;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
/*
@Controller
public class ExceptionHandlingController {

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    public ExceptionHandlingController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR,
            reason="Internal server error")
    @ExceptionHandler(BackEndException.class)
    public void handleBackendException() {
        meterRegistry.counter("http", "backend_exception");
    }

}
*/
