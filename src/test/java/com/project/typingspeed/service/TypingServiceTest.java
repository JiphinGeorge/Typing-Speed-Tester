package com.project.typingspeed.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class TypingServiceTest {

    private final TypingService typingService = new TypingService();

    @Test
    void testCalculateWPM() {
        String typedText = "The quick brown fox jumps over the lazy dog.";
        int wpm = typingService.calculateWPM(typedText);
        assertEquals(9, wpm, "WPM should be the number of words typed");
    }

    @Test
    void testCalculateAccuracy() {
        String originalText = "The quick brown fox jumps over the lazy dog.";
        // 1 typo: "jump" instead of "jumps"
        String typedText = "The quick brown fox jump over the lazy dog.";
        
        double accuracy = typingService.calculateAccuracy(typedText, originalText);
        // 8 correct out of 9
        assertEquals(88.88, accuracy, 0.02, "Accuracy should be correctly formulated");
    }
}
