package com.example.bookstore.controller;

import com.example.bookstore.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/")
public class MainController {
    private StorageService storageService;
    @Autowired
    public void setBookStorage(StorageService storageService) {
        this.storageService = storageService;
    }
    @GetMapping
    public String home(Model model) {
        model.addAttribute("books", storageService.getBooks());
        return "home";
    }
    @GetMapping("book/")
    public String book(Model model, @RequestParam int id) {
        model.addAttribute("book", storageService.getBook(id));
        model.addAttribute("amount", storageService.getCartAmount(id));
        return "book";
    }
    @GetMapping("cart")
    public String cart(Model model) {
        model.addAttribute("cart", storageService.getCartBooks());
        model.addAttribute("total", storageService.getCartTotal());
        return "cart";
    }
    @PostMapping("addToCart")
    public String addToCart(@RequestParam("bookId") int id) {
        storageService.addToCart(id);
        return "redirect:/book/?id="+id;
    }
}