package com.example.bookstore.controller;

import com.example.bookstore.cart.User;
import com.example.bookstore.service.CacheService;
import com.example.bookstore.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/")
public class MainController {
    private StorageService storageService;
    private CacheService cacheService;
    @Autowired
    public void setBookStorage(StorageService storageService) {
        this.storageService = storageService;
    }
    @Autowired
    public void setCacheService(CacheService cacheService) {
        this.cacheService = cacheService;
    }
    @GetMapping
    public String home(Model model) {
        model.addAttribute("books", storageService.getBooks());
        return "home";
    }
    @GetMapping("book/")
    public String book(Model model, @RequestParam int id, @AuthenticationPrincipal User user) {
        model.addAttribute("book", storageService.getBook(id));
        int amount = 0;
        if (user != null) {
            amount = storageService.getCartAmount(user.getId(), id);
        }
        model.addAttribute("amount", amount);
        cacheService.setPage("/book/?id="+id);
        return "book";
    }
    @GetMapping("cart")
    public String cart(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("cart", storageService.getCartBooks(user.getId()));
        model.addAttribute("total", storageService.getCartTotal(user.getId()));
        cacheService.setPage("/cart");
        return "cart";
    }
    @PostMapping("addToCart")
    public String addToCart(@RequestParam("bookId") int id, @AuthenticationPrincipal User user) {
        storageService.addToCart(user.getId(), id);
        cacheService.setPage("/book/?id="+id);
        return "redirect:/book/?id="+id;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}