package com.example.bookstore.service;

import org.springframework.stereotype.Service;

@Service
public class CacheService {
    private String cachedPage;

    public void setCachedPage(String page) {
        this.cachedPage = page;
    }

    public String getCachedPage() {
        return cachedPage;
    }
}
