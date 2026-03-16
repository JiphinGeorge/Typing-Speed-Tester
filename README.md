# XcelerType

![XcelerType Heatmap](file:///C:/Users/asus/.gemini/antigravity/brain/82700e11-b46c-45cd-9232-6388413607ea/screenshot_dashboard_1772566908859.png)

A dynamic, minimalist Spring Boot web application designed to track and improve your keyboard typing speed and accuracy in real-time. Built specifically around a clean MVC architecture under Java 21, the application requires absolutely no database setup to operate out of the box.

## Features

- **Character-Level Accuracy Tracking:** The application tracks your typing live, character by character. Correct letters glow a vibrant green, while typos immediately flag a bold red indicator.
- **Dynamic Text Generation:** You will never type the exact same text sequentially! XcelerType selects an unpredictable paragraph from a core rotation of classic pangrams and challenging texts on each refresh.
- **60-Second Auto-Submit Timer:** Built-in auto-counter triggers as soon as you type the first character.
- **Enter Key Quick Submit:** Press Enter at any time to instantly submit your test.
- **Night Mode Support:** Integrated working Light/Dark mode toggle caching preferences to local storage so styles persist effectively over sessions.
- **Animated 3D Loading Overlay:** A premium CSS 3D rotating cube animation with a progress bar plays every time the page loads or is refreshed.
- **Random Pro Tips:** The loading screen shows a different, meaningful typing pro tip on each page load.
- **Keypress Frequency Heatmap:** The application actively logs every single letter you press during your test. When you submit your results, XcelerType dynamically generates a gorgeous frequency heatmap, adjusting color intensity seamlessly based on your most-used (and abused) keys!
- **Highest Score Tracking:** Your top Words Per Minute (WPM) score is preserved in application memory. The system tracks your peak performance and displays it prominently on your main page dashboard.
- **Post/Redirect/Get Pattern:** Refreshing the results dashboard cleanly redirects back to the home page without form resubmission warnings.

## Demonstration

### The Typing Interface
![Typing Interface](file:///C:/Users/asus/.gemini/antigravity/brain/82700e11-b46c-45cd-9232-6388413607ea/screenshot_home_1772566870118.png)

## Technology Stack

- **Backend Architecture:** Spring Boot (Java 21)
- **MVC Templating:** Thymeleaf
- **Frontend UI:** Tailwind CSS (via CDN) & Vanilla JavaScript
- **Build Tool:** Maven

## Getting Started

Because XcelerType intentionally omits a database configuration, launching the application is straightforward and immediate.

### Prerequisites

You will need the following installed on your machine:
- Java JDK 21
- Apache Maven

### Installation & Run

1. Clone or download this repository to your local machine.
2. Navigate to the root directory `Typing Speed Tester` inside your terminal.
3. Execute the Maven Spring Boot plugin command:

```bash
mvn spring-boot:run
```

4. Open your preferred web browser and navigate directly to: `http://localhost:8080/`

That's it! The application is instantly ready for use.

## Project Structure

```text
src/main/
├── java/com/project/typingspeed/
│   ├── TypingSpeedApplication.java     # The main Spring Boot bootstrap entry point
│   ├── controller/TypingController.java # Handles all HTTP Request mapping for the UI and POST results
│   └── service/TypingService.java      # Application-scoped business logic (WPM, Accuracy, Highest Score algorithms)
└── resources/
    ├── application.properties
    └── templates/
        ├── index.html                   # The primary interactive typing interface
        └── dashboard.html               # The result page and visual heatmap builder
```

## Contributing

Feel free to open an issue or submit a pull request if you want to expand the feature set—such as tying the application memory into an actual PostgreSQL instance or adding further complex statistics.

## License

This project is licensed under the MIT License - see the `LICENSE` file for details.
