# Date-Time Utility Library + Event Scheduler

[![Build Status](https://img.shields.io/badge/build-passing-brightgreen.svg)](#)
[![Java Version](https://img.shields.io/badge/Java-17-blue.svg)](https://www.oracle.com/java/)
[![Maven Build](https://img.shields.io/badge/Maven-3.x-C71A22.svg)](https://maven.apache.org/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

> A modern desktop application built with Java Swing for managing events and performing various date/time calculations.

The Date-Time Utility Library is a comprehensive desktop application that combines time calculation utilities (like age calculation and time format conversions) with a robust event scheduler. It features a modern, clean UI with sidebar navigation, theme support, and a background daemon for real-time event reminders.

---

## Preview

<!-- Application preview image -->
![App Preview](docs/images/preview.gif)  
*(Note: Ensure `docs/images/preview.gif` is present to display the preview).*

---

## Features

- **Event Scheduler**
  - Add, edit, and delete events.
  - Search and filter events by title, description, location, or date.
  - Priority levels for events (Low, Medium, High).
- **Background Reminders**
  - Configurable push notifications (`JOptionPane`) via a background daemon thread.
  - Reminder offsets: At time of event, 15 min, 30 min, 1 hour, or 1 day before.
- **Time Utilities**
  - View current time in customizable formats.
  - Calculate time differences between two times.
  - Convert between 12-hour (AM/PM) and 24-hour formats.
  - Add or subtract minutes from a given time.
- **Age Calculator**
  - Calculate exact age based on Date of Birth (Years, Months, Days).
  - Detailed age statistics (total weeks, days, hours, minutes, seconds).
  - Countdown to the next birthday.
- **Data Persistence**
  - Saves all events locally to a flat-file (`events_data.txt`), requiring no database setup.
- **Modern User Interface**
  - Swing-based UI with CardLayout for smooth navigation.
  - Sidebar with hover states and active-tab highlights.
  - (Planned) Dark/Light mode theme switching.

*(Note: Date Utilities are currently planned/under construction).*

---

## Tech Stack

- **Programming Language**: Java 17
- **UI Framework**: Java Swing
- **Build Tool**: Maven
- **Database**: Flat-file storage (`events_data.txt`) - No external SQL/NoSQL required.
- **Dependencies**: None (Core Java only)
- **IDE**: Any Java IDE (IntelliJ IDEA, Eclipse, VS Code)

---

## Project Structure

```text
Date-Time-Utility-Library
├── docs/                   # Documentation and images
├── src/main/java/
│   ├── app/
│   │   └── Main.java
│   ├── model/
│   │   └── Event.java
│   ├── scheduler/
│   │   └── EventScheduler.java
│   ├── storage/
│   │   └── FileManager.java
│   ├── ui/
│   │   ├── AddEvent.java
│   │   ├── CalendarView.java
│   │   ├── Dashboard.java
│   │   ├── ThemeManager.java
│   │   ├── custom/
│   │   │   ├── ModernButton.java
│   │   │   └── RoundedPanel.java
│   │   └── panels/
│   │       ├── AboutPanel.java
│   │       ├── AgeCalculatorPanel.java
│   │       ├── AllEventsPanel.java
│   │       ├── CalendarPanel.java
│   │       ├── DashboardPanel.java
│   │       ├── DateUtilityPanel.java
│   │       ├── EventSchedulerPanel.java
│   │       ├── SettingsPanel.java
│   │       └── TimeUtilityPanel.java
│   └── utility/
│       ├── AgeCalculator.java
│       ├── DateUtility.java
│       └── TimeUtility.java
├── .gitignore
├── LICENSE
├── pom.xml                 # Maven build configuration
└── README.md
```

---

## Installation

Follow these steps to run the project from scratch on your local machine:

1. **Clone the repository**
   ```bash
   git clone https://github.com/ashmitraj0344/Date-Time-Utility-Library.git
   ```

2. **Navigate to the project folder**
   ```bash
   cd Date-Time-Utility-Library
   ```

3. **Install dependencies and Build project**
   Since this is a Maven project, compile the source files using:
   ```bash
   mvn clean install
   ```

4. **Run the project**
   You can run the application directly through Maven:
   ```bash
   mvn exec:java -Dexec.mainClass="app.Main"
   ```
   *Alternatively, open the project in your favorite IDE and run `app.Main.java`.*

---

## Requirements

- **Java Version**: JDK 17 or higher.
- **Maven**: Version 3.6.0 or higher.
- **Database**: None required.
- **OS**: Windows, macOS, or Linux (requires GUI environment).

---

## Usage

1. **Dashboard**: Upon launching, the dashboard gives you a quick overview of your events. Use the left sidebar to navigate.
2. **Adding an Event**: Click on the "Event Scheduler" tab or use the "Add Event" dialog to create a new task. Fill out the mandatory Date (`yyyy-MM-dd`) and Time (`HH:mm`) fields.
3. **Setting Reminders**: When adding an event, check "Set Reminder" and choose your offset. Keep the app open; a popup will appear when the time arrives.
4. **Time & Age Calculations**: Navigate to the respective tabs in the sidebar, input your date/time, and see instant calculated results.

---

## Screenshots

Place the screenshots of your application in the `docs/images/` folder to display them here:

| Dashboard | Add Event | Time Utilities |
| :---: | :---: | :---: |
| ![Dashboard](docs/images/dashboard.png) | ![Add Event](docs/images/add_event.png) | ![Time Utilities](docs/images/time_utility.png) |

---

## Configuration

This project requires minimal configuration. Data is saved locally in the root directory as `events_data.txt`. 
- No environment variables are necessary.
- The `pom.xml` handles the compiler configuration (Source/Target set to Java 17).

---

## Dependencies

The project relies entirely on **Core Java (Standard Library)**. 
- No external libraries (like Hibernate, Spring, or Apache Commons) are used, making it incredibly lightweight and easy to run anywhere.

---

## Architecture

The application follows a standard monolithic **MVC-like (Model-View-Controller)** pattern:
- **Model**: Represents the core data structures (e.g., `Event.java`).
- **Storage Layer**: `FileManager.java` intercepts model data and handles I/O operations (Serialization/Deserialization using a custom `|||` delimiter).
- **Scheduler/Service Layer**: `EventScheduler.java` acts as the controller, maintaining the state of all events in memory and running a daemon Thread (`startReminderCheckLoop()`) to trigger UI callbacks.
- **View (UI)**: Swing JFrame and JPanels that listen to the `EventScheduler` and update dynamically.

---

## Modules / Packages

- **`app`**: Contains the `Main` class which bootstraps the Swing UI and applies the cross-platform Look and Feel.
- **`model`**: Contains POJOs (Plain Old Java Objects) like `Event` representing the data.
- **`scheduler`**: Contains the core logic for filtering, searching, and managing the reminder thread.
- **`storage`**: Contains logic for reading/writing data to the filesystem safely.
- **`ui`**: Contains the main `Dashboard` frame, custom dialogs, and individual `panels` mapped to the sidebar navigation.
- **`utility`**: Contains static helper methods for pure calculations (`AgeCalculator`, `TimeUtility`).

---

## Future Improvements

- Fully implement the `DateUtility` functionality.
- Add recurring events (daily, weekly, monthly).
- Integrate an SQLite or H2 embedded database for better querying and indexing.
- Allow exporting events to `.ics` (iCalendar) format.
- Add drag-and-drop support in the Calendar View.

---

## Contributing

Contributions are welcome! To contribute:

1. Fork the repository.
2. Create a new branch: `git checkout -b feature/YourFeatureName`
3. Commit your changes: `git commit -m 'Add some feature'`
4. Push to the branch: `git push origin feature/YourFeatureName`
5. Open a Pull Request.

---

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## Contributors

This project is collaboratively developed by:
- Aman Dwivedi
- Ashmit Raj
- Bhumika Suryawanshi
- Soumili Mandal
- Shubham Kumar
- Dinesh Bagariya
- Aniket / Aditya
- And other contributors

---

## Acknowledgements

- Built entirely with **Java Swing** and standard `java.time` APIs.
- UI inspiration and design principles adapted for modern desktop constraints.
