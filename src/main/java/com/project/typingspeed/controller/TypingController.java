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

    // Define predefined paragraphs
    private static final String[] PREDEFINED_PARAGRAPHS = {
        "The quick brown fox jumps over the lazy dog. Programming in Spring Boot is a fantastic way to build robust web applications quickly and efficiently.",
        "To be or not to be, that is the question. We strive to create the best software tools for developers around the world, making the experience seamless.",
        "Success is not final, failure is not fatal: it is the courage to continue that counts. Keep practicing your typing speed and you will see gradual improvements.",
        "Computer science is no more about computers than astronomy is about telescopes. The true essence of computing lies in the problem-solving journey.",
        "A journey of a thousand miles begins with a single step. Take the challenge today and type this text as fast as you possibly can without making errors!"
    };

    // Dependency injection via constructor
    public TypingController(TypingService typingService) {
        this.typingService = typingService;
    }

    // GET mapping to display typing page
    @GetMapping("/")
    public String showTypingPage(Model model) {
        String randomParagraph = PREDEFINED_PARAGRAPHS[new java.util.Random().nextInt(PREDEFINED_PARAGRAPHS.length)];
        model.addAttribute("paragraph", randomParagraph);
        model.addAttribute("highestWpm", typingService.getHighestWpm());
        return "index";
    }

    // POST mapping to process typing input and return result
    @PostMapping("/result")
    public String processTypingResult(
            @RequestParam("typedText") String typedText,
            @RequestParam("originalText") String originalText,
            @RequestParam(value = "heatmapData", required = false, defaultValue = "{}") String heatmapData,
            Model model) {
        
        int wpm = typingService.calculateWPM(typedText);
        double accuracy = typingService.calculateAccuracy(typedText, originalText);

        // Update highest WPM
        typingService.updateHighestWpm(wpm);

        model.addAttribute("paragraph", originalText);
        model.addAttribute("typedText", typedText);
        model.addAttribute("wpm", wpm);
        // Format accuracy to 2 decimal places
        model.addAttribute("accuracy", String.format("%.2f", accuracy));
        model.addAttribute("highestWpm", typingService.getHighestWpm());
        model.addAttribute("heatmapData", heatmapData);

        return "dashboard";
    }
}
