package com.project.typingspeed.controller;

import com.project.typingspeed.service.TypingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TypingController {

    private final TypingService typingService;

    // Define a predefined paragraph
    private static final String PREDEFINED_PARAGRAPH = "The quick brown fox jumps over the lazy dog. Programming in Spring Boot is a fantastic way to build robust web applications quickly and efficiently.";

    // Dependency injection via constructor
    public TypingController(TypingService typingService) {
        this.typingService = typingService;
    }

    // GET mapping to display typing page
    @GetMapping("/")
    public String showTypingPage(Model model) {
        model.addAttribute("paragraph", PREDEFINED_PARAGRAPH);
        return "index";
    }

    // POST mapping to process typing input and return result
    @PostMapping("/result")
    public String processTypingResult(
            @RequestParam("typedText") String typedText,
            @RequestParam(value = "heatmapData", required = false, defaultValue = "{}") String heatmapData,
            Model model) {
        
        int wpm = typingService.calculateWPM(typedText);
        double accuracy = typingService.calculateAccuracy(typedText, PREDEFINED_PARAGRAPH);

        // Update highest WPM
        typingService.updateHighestWpm(wpm);

        model.addAttribute("paragraph", PREDEFINED_PARAGRAPH);
        model.addAttribute("typedText", typedText);
        model.addAttribute("wpm", wpm);
        // Format accuracy to 2 decimal places
        model.addAttribute("accuracy", String.format("%.2f", accuracy));
        model.addAttribute("highestWpm", typingService.getHighestWpm());
        model.addAttribute("heatmapData", heatmapData);

        return "dashboard";
    }
}
