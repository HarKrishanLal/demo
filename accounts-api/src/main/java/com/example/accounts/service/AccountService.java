package com.example.accounts.service;

import com.example.accounts.model.Account;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class AccountService {
    private final Map<String, Account> store = new ConcurrentHashMap<>();

    public Account create(Account acct) {
        if (acct.getId() == null || acct.getId().isEmpty()) {
            // id already set in model constructor
        }
        store.put(acct.getId(), acct);
        return acct;
    }

    public Optional<Account> getById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public List<Account> listAll() {
        return new ArrayList<>(store.values());
    }

    public Optional<Account> update(String id, Account updated) {
        if (!store.containsKey(id)) return Optional.empty();
        updated.setId(id);
        store.put(id, updated);
        return Optional.of(updated);
    }

    public boolean delete(String id) {
        return store.remove(id) != null;
    }
}
