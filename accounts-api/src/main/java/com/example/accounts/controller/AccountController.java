package com.example.accounts.controller;

import com.example.accounts.model.Account;
import com.example.accounts.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController @RequestMapping("/accounts/v1")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account acct){
        Account created = accountService.create(acct);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> get(@PathVariable String id){
        return accountService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Account>> list(){
        return ResponseEntity.ok(accountService.listAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Account> update(@PathVariable String id, @RequestBody Account acct){
        return accountService.update(id,acct).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id){
        if (accountService.delete(id))
            return ResponseEntity.noContent().build();
        return ResponseEntity.notFound().build();
    }

    static class InspectRequest {
        public String accountNumber;
    }

    static class InspectResponse {
        public String accountNumber;
        public boolean masked;
        public String reason;
    }

    @PostMapping("/inspect-accounts")
    public ResponseEntity<InspectResponse> inspectAccount(@RequestBody InspectRequest req){
        if (req == null || req.accountNumber == null || req.accountNumber.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Path sdnPath = Path.of("..","prompts","SDNList.txt");
        try {
            List<String> lines = Files.readAllLines(sdnPath);
            if (lines.isEmpty()) {
                InspectResponse r = new InspectResponse();
                r.accountNumber = req.accountNumber;
                r.masked = false;
                r.reason = "SDN list empty";
                return ResponseEntity.ok(r);
            }
            // assume CSV header present
            List<String> dataLines = lines.stream().filter(l -> !l.isBlank()).collect(Collectors.toList());
            if (dataLines.size() == 1 && !dataLines.get(0).contains(",")) {
                // legacy single-column file: treat as plain account list
                boolean found = dataLines.stream().anyMatch(l -> l.trim().equals(req.accountNumber));
                InspectResponse r = new InspectResponse();
                if (!found) {
                    r.accountNumber = req.accountNumber;
                    r.masked = false;
                    r.reason = "not in SDN";
                } else {
                    r.accountNumber = mask(req.accountNumber);
                    r.masked = true;
                    r.reason = "in SDN (no dates available)";
                }
                return ResponseEntity.ok(r);
            }

            // parse CSV with header
            String header = dataLines.get(0);
            int idxAcct = -1, idxDateAdded = -1, idxLastTx = -1;
            String[] cols = header.split(",");
            for (int i = 0; i < cols.length; i++) {
                String c = cols[i].trim();
                if (c.equalsIgnoreCase("AccountNumber"))
                    idxAcct = i;
                if (c.equalsIgnoreCase("DateAddedToSDNList"))
                    idxDateAdded = i;
                if (c.equalsIgnoreCase("LastTransactionDate"))
                    idxLastTx = i;
            }

            DateTimeFormatter fmt = DateTimeFormatter.ISO_LOCAL_DATE;
            for (int i = 1; i < dataLines.size(); i++) {
                String[] parts = dataLines.get(i).split(",");
                if (parts.length <= Math.max(idxAcct,Math.max(idxDateAdded,idxLastTx)))
                    continue;
                String acct = parts[idxAcct].trim();
                if (!acct.equals(req.accountNumber))
                    continue;
                String added = idxDateAdded >= 0 ? parts[idxDateAdded].trim() : null;
                String lastTx = idxLastTx >= 0 ? parts[idxLastTx].trim() : null;

                if (added == null || added.isEmpty()) {
                    InspectResponse r = new InspectResponse();
                    r.accountNumber = mask(req.accountNumber);
                    r.masked = true;
                    r.reason = "in SDN (no added date)";
                    return ResponseEntity.ok(r);
                }

                LocalDate addedDate = LocalDate.parse(added,fmt);
                LocalDate now = LocalDate.now();
                if (addedDate.isBefore(now.minusWeeks(1))) {
                    if (lastTx == null || lastTx.isEmpty()) {
                        InspectResponse r = new InspectResponse();
                        r.accountNumber = mask(req.accountNumber);
                        r.masked = true;
                        r.reason = "no last transaction date";
                        return ResponseEntity.ok(r);
                    }
                    LocalDate lastTxDate = LocalDate.parse(lastTx,fmt);
                    if (lastTxDate.isAfter(addedDate.plusWeeks(1))) {
                        InspectResponse r = new InspectResponse();
                        r.accountNumber = req.accountNumber;
                        r.masked = false;
                        r.reason = "allowed";
                        return ResponseEntity.ok(r);
                    } else {
                        InspectResponse r = new InspectResponse();
                        r.accountNumber = mask(req.accountNumber);
                        r.masked = true;
                        r.reason = "masked: last transaction too old";
                        return ResponseEntity.ok(r);
                    }
                } else {
                    InspectResponse r = new InspectResponse();
                    r.accountNumber = mask(req.accountNumber);
                    r.masked = true;
                    r.reason = "masked: recently added to SDN";
                    return ResponseEntity.ok(r);
                }
            }

            // not found in SDN
            InspectResponse r = new InspectResponse();
            r.accountNumber = req.accountNumber;
            r.masked = false;
            r.reason = "not in SDN";
            return ResponseEntity.ok(r);

        } catch (IOException e) {
            InspectResponse r = new InspectResponse();
            r.accountNumber = mask(req.accountNumber);
            r.masked = true;
            r.reason = "error reading SDN list";
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(r);
        }
    }

    private static String mask(String acct){
        if (acct == null)
            return null;
        int len = acct.length();
        if (len <= 4)
            return "****";
        String last4 = acct.substring(len - 4);
        String stars = "";
        for (int i = 0; i < len - 4; i++)
            stars += "*";
        return stars + last4;
    }
}
