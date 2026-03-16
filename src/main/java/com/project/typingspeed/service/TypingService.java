package com.project.typingspeed.service;

import org.springframework.stereotype.Service;

/**
 * ========================================================================================
 * TypingService.java - THE BUSINESS LOGIC LAYER (SERVICE)
 * ========================================================================================
 *
 * WHAT IS A SERVICE?
 * In Spring MVC architecture, the Service layer contains the core business logic.
 * It sits between the Controller (which handles HTTP requests) and any data layer.
 * The Controller delegates all calculations and logic to this Service class.
 *
 * WHY @Service?
 * The @Service annotation tells Spring: "This is a service class. Create one instance of it
 * (a 'bean') and manage its lifecycle. When any other class (like TypingController) needs
 * this service, inject it automatically via Dependency Injection."
 *
 * WHAT THIS SERVICE DOES:
 *   1. calculateWPM()        - Counts how many words the user typed (assumes 1-minute test)
 *   2. calculateAccuracy()   - Compares typed words vs original words to compute accuracy %
 *   3. updateHighestWpm()    - Tracks the user's personal best WPM score (in-memory)
 *   4. getHighestWpm()       - Returns the current highest WPM
 *
 * NOTE: Since we have no database, the highestWpm is stored in-memory and resets when
 * the server restarts. This is by design for the current version.
 */
@Service
public class TypingService {

    // =====================================================================================
    // IN-MEMORY STATE: Tracks the highest WPM score across sessions
    // This variable persists as long as the server is running because Spring creates
    // only ONE instance of this Service (singleton scope by default).
    // =====================================================================================
    private int highestWpm = 0;

    /**
     * Updates the highest WPM if the current score beats the previous record.
     * Called by the Controller after every typing test submission.
     *
     * @param currentWpm - The WPM calculated from the user's latest test
     */
    public void updateHighestWpm(int currentWpm) {
        if (currentWpm > highestWpm) {
            highestWpm = currentWpm;
        }
    }

    /**
     * Returns the current highest WPM score.
     * Used by the Controller to display the "Highest Score" badge on the UI.
     *
     * @return the highest WPM recorded so far
     */
    public int getHighestWpm() {
        return highestWpm;
    }

    // =====================================================================================
    // WPM CALCULATION
    // =====================================================================================
    /**
     * Calculates Words Per Minute (WPM).
     *
     * HOW IT WORKS:
     *   1. Takes the user's typed text as input
     *   2. Splits it by whitespace (spaces, tabs, newlines) using regex "\\s+"
     *   3. Counts the resulting words
     *   4. Since we assume a 1-minute test, WPM = total word count
     *
     * EXAMPLE:
     *   Input:  "The quick brown fox"
     *   Split:  ["The", "quick", "brown", "fox"]
     *   Result: 4 WPM
     *
     * @param typedText - The text the user typed in the textarea
     * @return the number of words typed (WPM for a 1-minute test)
     */
    public int calculateWPM(String typedText) {
        // Guard clause: return 0 if input is null or empty
        if (typedText == null || typedText.trim().isEmpty()) {
            return 0;
        }
        // Split by one or more whitespace characters and count the words
        String[] words = typedText.trim().split("\\s+");
        return words.length;
    }

    // =====================================================================================
    // ACCURACY CALCULATION
    // =====================================================================================
    /**
     * Calculates typing accuracy as a percentage.
     *
     * HOW IT WORKS:
     *   1. Splits both the typed text and original text into word arrays
     *   2. Compares words at each position (index 0 vs 0, index 1 vs 1, etc.)
     *   3. Counts how many words match exactly
     *   4. Accuracy = (correct words / total original words) × 100
     *
     * EXAMPLE:
     *   Original: "The quick brown fox"  → ["The", "quick", "brown", "fox"]
     *   Typed:    "The quik  brown fox"  → ["The", "quik", "brown", "fox"]
     *   Comparison:
     *     Index 0: "The"   == "The"   ✓ correct
     *     Index 1: "quik"  != "quick" ✗ wrong
     *     Index 2: "brown" == "brown" ✓ correct
     *     Index 3: "fox"   == "fox"   ✓ correct
     *   Result: 3 correct / 4 total = 75.0%
     *
     * @param typedText    - The text the user typed
     * @param originalText - The original paragraph that was displayed
     * @return accuracy percentage (0.0 to 100.0)
     */
    public double calculateAccuracy(String typedText, String originalText) {
        // Guard clause: return 0 if either input is null or empty
        if (typedText == null || typedText.trim().isEmpty() || originalText == null || originalText.isEmpty()) {
            return 0.0;
        }

        // Split both texts into arrays of individual words
        String[] typedWords = typedText.trim().split("\\s+");
        String[] originalWords = originalText.trim().split("\\s+");

        int correctWords = 0;
        // Only compare up to the shorter array length to avoid ArrayIndexOutOfBoundsException
        int minLen = Math.min(typedWords.length, originalWords.length);

        // Compare words one by one at matching positions
        for (int i = 0; i < minLen; i++) {
            if (typedWords[i].equals(originalWords[i])) {
                correctWords++;
            }
        }

        // Calculate and return accuracy as a percentage
        // Note: We divide by originalWords.length (not typedWords.length) because
        // accuracy is measured against the ORIGINAL text the user was supposed to type
        return ((double) correctWords / originalWords.length) * 100;
    }
}
