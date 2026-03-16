package com.project.typingspeed.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * ========================================================================================
 * TypingServiceTest.java - UNIT TESTS FOR THE SERVICE LAYER
 * ========================================================================================
 *
 * WHAT ARE UNIT TESTS?
 * Unit tests verify that individual methods work correctly in isolation.
 * They run automatically with "mvn test" and catch bugs before the app goes live.
 *
 * WHY TEST THE SERVICE?
 * The Service layer contains critical business logic (WPM and accuracy calculations).
 * If someone accidentally changes the calculation formula, these tests will FAIL,
 * alerting us to the bug immediately.
 *
 * HOW JUNIT 5 WORKS:
 *   - @Test marks a method as a test case
 *   - assertEquals(expected, actual, message) checks if expected == actual
 *   - If the assertion fails, JUnit reports the test as FAILED with the message
 *   - All tests run independently — one test failing doesn't affect others
 *
 * HOW TO RUN:
 *   Command: mvn test
 *   This compiles the test classes and runs all methods annotated with @Test
 */
class TypingServiceTest {

    // Create a fresh TypingService instance for testing
    // Note: We don't need Spring DI here — we just create the object directly
    // because we're testing the pure logic, not the Spring wiring
    private final TypingService typingService = new TypingService();

    /**
     * TEST 1: Verify WPM calculation counts words correctly.
     *
     * SCENARIO:
     *   Input:    "The quick brown fox jumps over the lazy dog." (44 characters)
     *   Time:     60 seconds
     *   Expected: (44 / 5) / (60 / 60) = 8.8 -> 9 WPM
     */
    @Test
    void testCalculateWPM() {
        String typedText = "The quick brown fox jumps over the lazy dog.";
        int wpm = typingService.calculateWPM(typedText, 60);
        assertEquals(9, wpm, "WPM should be the gross words (chars/5) typed per minute");
    }

    /**
     * TEST 2: Verify accuracy calculation detects incorrect words properly.
     *
     * SCENARIO:
     *   Original: "The quick brown fox jumps over the lazy dog."  (9 words)
     *   Typed:    "The quick brown fox jump over the lazy dog."   (9 words)
     *   Difference: "jumps" vs "jump" at index 4 — 1 word wrong
     *   Expected: 8 correct out of 9 = 88.88%
     *
     * The delta of 0.02 allows for floating-point precision differences.
     */
    @Test
    void testCalculateAccuracy() {
        String originalText = "The quick brown fox jumps over the lazy dog.";
        // 1 typo: "jump" instead of "jumps"
        String typedText = "The quick brown fox jump over the lazy dog.";
        
        double accuracy = typingService.calculateAccuracy(typedText, originalText);
        // 8 correct out of 9 = 88.888...%
        assertEquals(88.88, accuracy, 0.02, "Accuracy should be correctly formulated");
    }
}
