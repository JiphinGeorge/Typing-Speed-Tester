package com.project.typingspeed.controller;

import com.project.typingspeed.service.TypingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * ========================================================================================
 * TypingController.java - THE CONTROLLER LAYER (Handles HTTP Requests)
 * ========================================================================================
 *
 * WHAT IS A CONTROLLER?
 * In the Spring MVC (Model-View-Controller) pattern:
 *   - Model      = The data (WPM, accuracy, paragraph text)
 *   - View       = The HTML template (index.html, dashboard.html rendered by Thymeleaf)
 *   - Controller = THIS CLASS — it receives browser requests, processes them using
 *                  the Service layer, and decides which View to show.
 *
 * HOW IT WORKS:
 *   1. User visits http://localhost:8080/ in their browser
 *   2. The browser sends a GET request to "/"
 *   3. Spring routes that request to showTypingPage() (because of @GetMapping("/"))
 *   4. The method adds a random paragraph to the Model and returns "index"
 *   5. Thymeleaf renders index.html, injecting the paragraph into the template
 *   6. The rendered HTML is sent back to the user's browser
 *
 *   7. User types text and clicks "Submit Speed Test"
 *   8. The browser sends a POST request to "/result" with form data
 *   9. Spring routes that request to processTypingResult() (because of @PostMapping("/result"))
 *  10. The method calls TypingService to calculate WPM and accuracy
 *  11. Results are added to the Model and the "dashboard" view is returned
 *  12. Thymeleaf renders dashboard.html with the results
 *
 * WHY @Controller (not @RestController)?
 * @Controller returns VIEW NAMES (like "index" or "dashboard") that Thymeleaf resolves
 * to HTML templates. @RestController would return raw data (JSON), which is not what we want.
 */
@Controller
public class TypingController {

    // =====================================================================================
    // DEPENDENCY INJECTION
    // =====================================================================================
    // The TypingService is injected here by Spring's Dependency Injection (DI).
    // We declare it as 'final' to ensure it's always set and can't be changed later.
    // Spring automatically finds our @Service class and passes it into the constructor.
    private final TypingService typingService;

    // =====================================================================================
    // PREDEFINED PARAGRAPHS
    // =====================================================================================
    // These are the typing test paragraphs. A random one is selected each time the user
    // visits the home page. In a production app, these could come from a database.
    private static final String[] PREDEFINED_PARAGRAPHS = {
        "The quick brown fox jumps over the lazy dog. Programming in Spring Boot is a fantastic way to build robust web applications quickly and efficiently.",
        "To be or not to be, that is the question. We strive to create the best software tools for developers around the world, making the experience seamless.",
        "Success is not final, failure is not fatal: it is the courage to continue that counts. Keep practicing your typing speed and you will see gradual improvements.",
        "Computer science is no more about computers than astronomy is about telescopes. The true essence of computing lies in the problem-solving journey.",
        "A journey of a thousand miles begins with a single step. Take the challenge today and type this text as fast as you possibly can without making errors!"
    };

    /**
     * Constructor-based Dependency Injection.
     *
     * HOW DI WORKS:
     * When Spring starts up, it creates instances of all @Service, @Controller, etc. classes.
     * It sees that TypingController needs a TypingService in its constructor, so it
     * automatically passes the TypingService instance it already created.
     * This is called "constructor injection" — the recommended way to do DI in Spring.
     *
     * @param typingService - The service bean automatically injected by Spring
     */
    public TypingController(TypingService typingService) {
        this.typingService = typingService;
    }

    // =====================================================================================
    // GET "/" — DISPLAY THE LOADING PAGE
    // =====================================================================================
    @GetMapping("/")
    public String showLoadingPage() {
        return "loading";
    }

    // =====================================================================================
    // GET "/test" — DISPLAY THE TYPING TEST PAGE
    // =====================================================================================
    /**
     * Handles GET requests to /test.
     * FLOW:
     *   1. Pick a random paragraph from the PREDEFINED_PARAGRAPHS array
     *   2. Add it to the Model (so Thymeleaf can access it as ${paragraph})
     *   3. Add the highest WPM score to the Model (for the footer stats bar)
     *   4. Return "index" → Thymeleaf resolves this to templates/index.html
     */
    @GetMapping("/test")
    public String showTypingPage(Model model) {
        // Select a random paragraph using java.util.Random
        String randomParagraph = PREDEFINED_PARAGRAPHS[new java.util.Random().nextInt(PREDEFINED_PARAGRAPHS.length)];

        // Add data to the Model — these become Thymeleaf variables in the HTML
        // In index.html, ${paragraph} will be replaced with the actual paragraph text
        model.addAttribute("paragraph", randomParagraph);

        // Pass the highest WPM to display in the stats footer bar
        model.addAttribute("highestWpm", typingService.getHighestWpm());

        // Return the template name. Spring looks for: src/main/resources/templates/index.html
        return "index";
    }

    // =====================================================================================
    // POST "/result" — PROCESS THE TYPING TEST AND SHOW RESULTS
    // =====================================================================================
    /**
     * Handles POST requests to /result (when the user submits the typing test form).
     *
     * FLOW:
     *   1. Receive form data: typedText, originalText, and heatmapData (keyboard usage stats)
     *   2. Call TypingService.calculateWPM() to count words typed
     *   3. Call TypingService.calculateAccuracy() to compare typed vs original text
     *   4. Update the highest WPM score if this attempt is a new record
     *   5. Add all results to the Model for the dashboard template
     *   6. Return "dashboard" → renders templates/dashboard.html with results
     *
     * @param typedText    - The text the user typed (from the textarea, name="typedText")
     * @param originalText - The original paragraph (from a hidden input, name="originalText")
     * @param heatmapData  - JSON string of keypress frequencies (from hidden input, name="heatmapData")
     * @param model        - Spring's Model object to pass results to the template
     * @return "dashboard" - the name of the results Thymeleaf template
     */
    @PostMapping("/result")
    public String processTypingResult(
            @RequestParam("typedText") String typedText,        // Comes from the <textarea> in the form
            @RequestParam("originalText") String originalText,   // Comes from a hidden <input> in the form
            @RequestParam(value = "heatmapData", required = false, defaultValue = "{}") String heatmapData, // Optional JSON data
            @RequestParam(value = "timeElapsed", required = false, defaultValue = "60") int timeElapsed, // Early submit time
            RedirectAttributes redirectAttributes) {             // Flash attributes for Post/Redirect/Get pattern
        
        // STEP 1: Calculate actual WPM based on characters typed and time elapsed
        int wpm = typingService.calculateWPM(typedText, timeElapsed);

        // STEP 2: Calculate accuracy by comparing typed text with original text
        double accuracy = typingService.calculateAccuracy(typedText, originalText);

        // STEP 3: Update highest WPM if this is a new personal best
        typingService.updateHighestWpm(wpm);

        // STEP 4: Add all results to Flash Attributes so they survive the redirect
        redirectAttributes.addFlashAttribute("paragraph", originalText);
        redirectAttributes.addFlashAttribute("typedText", typedText);
        redirectAttributes.addFlashAttribute("wpm", wpm);
        redirectAttributes.addFlashAttribute("accuracy", String.format("%.2f", accuracy));
        redirectAttributes.addFlashAttribute("highestWpm", typingService.getHighestWpm());
        redirectAttributes.addFlashAttribute("heatmapData", heatmapData);

        // Redirect to a clean GET route to prevent form resubmission popups
        return "redirect:/dashboard";
    }

    // =====================================================================================
    // GET "/dashboard" — DISPLAY THE RESULTS PAGE CLEANLY
    // =====================================================================================
    /**
     * Handles GET requests to /dashboard.
     * This is the 'Get' part of the Post/Redirect/Get pattern.
     */
    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        // If there is no WPM attribute, it means the user refreshed the page directly
        // or navigated here without taking the test. Let's redirect them back home!
        if (!model.containsAttribute("wpm")) {
            return "redirect:/test";
        }
        
        // Return the dashboard template to render the flash attributes.
        return "dashboard";
    }
}
