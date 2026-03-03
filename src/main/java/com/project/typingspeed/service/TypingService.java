package com.project.typingspeed.service;

import org.springframework.stereotype.Service;

@Service
public class TypingService {

    // WPM = number of words typed (assume 1-minute test)
    public int calculateWPM(String typedText) {
        if (typedText == null || typedText.trim().isEmpty()) {
            return 0;
        }
        String[] words = typedText.trim().split("\\s+");
        return words.length;
    }

    // Accuracy = correct words / total original words × 100
    public double calculateAccuracy(String typedText, String originalText) {
        if (typedText == null || typedText.trim().isEmpty() || originalText == null || originalText.isEmpty()) {
            return 0.0;
        }

        String[] typedWords = typedText.trim().split("\\s+");
        String[] originalWords = originalText.trim().split("\\s+");

        int correctWords = 0;
        int minLen = Math.min(typedWords.length, originalWords.length);

        // Compare words one by one
        for (int i = 0; i < minLen; i++) {
            if (typedWords[i].equals(originalWords[i])) {
                correctWords++;
            }
        }

        return ((double) correctWords / originalWords.length) * 100;
    }
}
