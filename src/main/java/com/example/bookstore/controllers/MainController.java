package com.example.bookstore.controllers;

import com.example.bookstore.user.ConditionCart;
import com.example.bookstore.user.User;
import com.example.bookstore.service.StorageService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Setter
@Controller
@RequestMapping("/")
public class MainController {
    private StorageService storageService;
    private AuthenticationManager authenticationManager;


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
        return "book";
    }

    @Autowired
    public void setBookStorage(StorageService storageService) {
        this.storageService = storageService;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @GetMapping("cart")
    public String cart(Model model, @AuthenticationPrincipal User user, @RequestParam(required = false) Boolean successful) {
        List<ConditionCart> books = storageService.getCartBooks(user.getId());
        model.addAttribute("cart", books);
        model.addAttribute("total", storageService.getCartTotal(user.getId()));
        model.addAttribute("cartEmpty", books.isEmpty());
        if (successful != null && !successful) {
            model.addAttribute("successful", successful);
        }
        return "cart";
    }

    @PostMapping("addToCart")
    public String addToCart(@RequestParam("bookId") int id, @AuthenticationPrincipal User user) {
        storageService.addToCart(user.getId(), id);
        return "redirect:/book/?id=" + id;
    }

    @PostMapping("subtractFromCart")
    public String subtractFromCart(@RequestParam("bookId") int id, @AuthenticationPrincipal User user) {
        storageService.subtractFromCart(user.getId(), id, 1);
        return "redirect:/book/?id=" + id;
    }

    @PostMapping("subtractInCart")
    public String subtractInCart(@RequestParam("bookId") int id, @AuthenticationPrincipal User user) {
        storageService.subtractFromCart(user.getId(), id, 1);
        return "redirect:/cart";
    }

    @GetMapping("/registration")
    public String register() {
        return "registration";
    }

    @PostMapping("/createAccount")
    public String createAccount(@RequestParam("username") String username, @RequestParam("password") String password) {
        storageService.addUser(username, password);
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication = authenticationManager.authenticate(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return "redirect:/loginSuccess";
    }

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        if (referer != null) {
            request.getSession().setAttribute("previousUrl", referer); // Сохраняем предыдущий URL в сессии
        }
        return "login";
    }
    @GetMapping("/loginSuccess")
    public String loginSuccess(HttpServletRequest request) {
        String previousUrl = (String) request.getSession().getAttribute("previousUrl");
        request.getSession().removeAttribute("previousUrl"); // Удаляем предыдущий URL из сессии
        return previousUrl != null ? "redirect:" + previousUrl : "redirect:/";
    }


    @PostMapping("/clearCart")
    public String clearCart(@AuthenticationPrincipal User user) {
        storageService.clearCart(user.getId());
        return "redirect:/cart";
    }

    @GetMapping("/order")
    public String getOrderPage(Model model, @AuthenticationPrincipal User user, RedirectAttributes redirectAttributes) {
        List<ConditionCart> cart = storageService.getCartBooks(user.getId());
        double total = storageService.getCartTotal(user.getId());

        if (cart.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Нельзя оформить заказ, корзина пуста.");
            return "redirect:/cart";
        }

        model.addAttribute("cart", cart);
        model.addAttribute("total", total);

        LocalDateTime now = LocalDateTime.now();
        String formattedNow = now.format(DateTimeFormatter.ofPattern("dd.MM.yyyy 'в' HH:mm"));
        model.addAttribute("formattedNow", formattedNow);

        LocalDateTime datePlus3Days = now.plus(3, ChronoUnit.DAYS);
        String formattedDatePlus3Days = datePlus3Days.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
        model.addAttribute("formattedDatePlus3Days", formattedDatePlus3Days);

        storageService.clearCart(user.getId());
        return "order";
    }


}
