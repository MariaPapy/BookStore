package com.example.bookstore.controllers;

import com.example.bookstore.entities.BookDaO;
import com.example.bookstore.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/admin")
public class AdministratorController {
    private StorageService storageService;
    @Autowired
    public void setStorageService(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/add-book")
    public String bookForm(Model model) {
        model.addAttribute("genres", storageService.getGenres());
        model.addAttribute("languages", storageService.getLanguages());
        return "addBook";
    }
    @PostMapping("/addBook")
    public ResponseEntity<BookDaO> addBook(@RequestBody BookDaO bookDaO) {
        BookDaO newBook = storageService.addBook(bookDaO);
        return new ResponseEntity<>(newBook, HttpStatus.CREATED);
    }
}
