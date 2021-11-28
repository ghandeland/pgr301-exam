package com.pgr301.exam;

import com.pgr301.exam.model.Account;
import com.pgr301.exam.model.Transaction;
import io.micrometer.core.annotation.Timed;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static java.util.Optional.ofNullable;

@RestController
public class BankAccountController implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    private BankingCoreSystmeService bankService;

    @Autowired
    private MeterRegistry meterRegistry;

    @Autowired
    public BankAccountController(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @PostMapping(path = "/account/{fromAccount}/transfer/{toAccount}", consumes = "application/json", produces = "application/json")
    public ResponseEntity transfer(@RequestBody Transaction tx, @PathVariable String fromAccount, @PathVariable String toAccount) {
        meterRegistry.counter("http_requests","endpoint", "transfer").increment();

        try {
            bankService.transfer(tx, fromAccount, toAccount);
            return ResponseEntity.ok("Transfer successful");
        } catch(BackEndException e) {
            meterRegistry.counter("backend_exception","endpoint", "transfer").increment();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping(path = "/account", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> updateAccount(@RequestBody Account a) {
        meterRegistry.counter("http_requests","endpoint", "account_update").increment();

        try {
            bankService.updateAccount(a);
            return new ResponseEntity(a, HttpStatus.OK);
        } catch(BackEndException e) {
            meterRegistry.counter("backend_exception", "endpoint", "account_update").increment();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Timed
    @GetMapping(path = "/account/{accountId}", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Account> balance(@PathVariable String accountId) {
        meterRegistry.counter("http_requests", "endpoint", "account_balance").increment();

        try {
            Account account = ofNullable(bankService.getAccount(accountId)).orElseThrow(AccountNotFoundException::new);
            return new ResponseEntity<>(account, HttpStatus.OK);
        } catch(BackEndException e) {
            meterRegistry.counter("backend_exception", "endpoint", "account_balance").increment();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {}

    @ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "video not found")
    public static class AccountNotFoundException extends RuntimeException {}
}

