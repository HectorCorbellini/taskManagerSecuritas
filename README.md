# Dynamic Agenda Application for Security Guards

This application helps security guards working for Securitas manage their daily work schedules dynamically. It allows for tracking of recurring tasks, handling schedule variations, and querying future assignments.

## Key Features:

- **Dynamic Scheduling**: The application can handle recurring tasks and adapt to variations in the user's schedule.
- **Future Assignments**: Users can query where they will work the following day and get specific details about each job.
- **Rest Day Management**: The application tracks rotating rest days and ensures assignments do not conflict with these days.
- **Default Work Hours**: If no hours are specified, the application defaults to a work schedule from 14:00 to 22:00.
- **Separation of Concerns**: The application follows a clean architecture with separate classes for user input, business logic, and data access.
- **Command-Line Interface**: Run specific commands directly from the terminal without navigating through menus.

## Documentation

For detailed information about recent architectural improvements and implementation details, please refer to the [Last_Improvements.md](Last_Improvements.md) file.

For detailed information about the latest changes, please refer to [Last_Improvements.md](Last_Improvements.md). Please note that this file is regularly updated to reflect the latest changes and improvements made to the application.

## Logging Framework:

The application uses **SLF4J** with **Log4j2** for logging. Ensure you have the necessary dependencies in your `pom.xml`:

```xml
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
    <version>2.0.9</version>
</dependency>
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-simple</artifactId>
    <version>2.0.9</version>
</dependency>
```

Logs will be generated in the `logs` directory, with rotation based on size and time.

## Build and Run Instructions:

To build the application, run:

```bash
mvn clean install
```

To run the application, use:

```bash
mvn exec:java -Dexec.mainClass="com.securitas.TaskManagerApp"
``` 

## Background Information:

- As a security guard for Securitas, assignments vary daily and often require flexibility.
- Some assignments are recurring (e.g., Gasoducto Cruz Del Sur every weekend, Magnolio on Mondays), while others are communicated on short notice (referred to as "retén").

## Weekly Schedule Example (March 2025):

- **Sunday, March 2**: Service at Gasoducto Cruz Del Sur (Industrial enterprise) from 17:00 to 01:00 (8 hours).
- **Monday, March 3**: Service at Magnolio (Restaurant) from 18:00 to 02:00 (8 hours).
- **Tuesday, March 4**: Assigned to Devoto nro 16 (Supermarket) as a "retén."
- **Wednesday, March 5**: Service at Club de Golf from 14:00 to 22:00 (8 hours).
- **Thursday, March 6**: Rest day.
- **Friday, March 7**: Service at Club de Golf from 14:00 to 22:00 (8 hours).

## Upcoming Week:

- The schedule for the next week will differ slightly due to a rotation of free days every two weeks.
- Users will not work at Club de Golf on Wednesday this week because it's a free day, and retén service will be moved to Friday.
- On Tuesday, the user will work at Magnolio (instead of Monday as usual).

## Recent Updates:
- **Command-Line Arguments**: Added support for running specific commands directly from the terminal
- **Helper Scripts**: Added run_sudo.sh script to simplify running commands with elevated privileges
- **Documentation**: Added comprehensive documentation of recent architectural improvements
- **Database Setup**: Added SQL scripts to automatically set up the required database tables
- **Menu System**: Improved menu system with clearer options and better user feedback

## Application Capabilities:

- Recognizes recurring shifts (e.g., weekends at Gasoducto Cruz Del Sur, Mondays at Magnolio).
- Handles dynamic scheduling changes (e.g., switching retén days or changing locations at short notice).
- Keeps track of rest days (e.g., every Thursday and rotating Wednesdays).
- Provides answers when queried about future schedules, such as "Where am I working tomorrow?" or "What are my next assignments?"
- Users can insert new information, such as past assignments on retén days, including details about armed and unarmed shifts.

## Dependencies:
- Java 17 or higher
- Maven for building the project
- MySQL for database management
- SLF4J with Log4j2 for logging

## Features:
- Dynamic scheduling of assignments.
- Error handling for assignment retrieval and addition.
- Dynamic date retrieval from an API.
- Enhanced user interface for easy navigation.

## Usage Examples:
1. **Add an Assignment**: Run the application and select the option to add a new assignment. Follow the prompts to enter the shift ID, date, and notes.
2. **View Tomorrow's Assignment**: Select the option to view tomorrow's assignment to see details about the next scheduled task.
3. **Manage Locations and Shifts**: Use the application to add and manage locations and shifts as needed.

## Installation Instructions:
1. Clone the repository: `git clone https://github.com/HectorCorbellini/taskManagerSecuritas`
2. Navigate to the project directory: `cd taskManagerSecuritas`
3. Make the helper script executable: `chmod +x run_sudo.sh`
4. Set up the database:
   ```bash
   ./run_sudo.sh mysql -u root -pplacita < src/main/resources/db/setup.sql
   ```
5. Build the project using Maven: `mvn clean install`
6. Run the application: `mvn exec:java -Dexec.mainClass="com.securitas.TaskManagerApp"`
7. Alternatively, run with specific commands: `mvn exec:java -Dexec.mainClass="com.securitas.TaskManagerApp" -Dexec.args="tomorrow"`

## Project Structure:

```
src/main/java/com/securitas/
├── command/             # Command pattern implementations for user actions
├── dao/                 # Data Access Objects for database operations
├── model/               # Domain models (Location, Shift, Assignment, etc.)
├── service/             # Business logic services
├── util/                # Utility classes (DatabaseConnection, etc.)
├── InputHandler.java    # Handles user input and displays menus
├── TaskManagerApp.java  # Main application entry point
└── TaskService.java     # Service layer for task-related operations
```

## Architecture:

The application follows a layered architecture:

1. **Presentation Layer**: TaskManagerApp, InputHandler, and Command classes handle user interaction
2. **Service Layer**: TaskService and TaskManagerService contain business logic
3. **Data Access Layer**: DAO classes handle database operations
4. **Domain Layer**: Model classes represent the business entities

## Command-Line Usage

The application supports the following command-line arguments:

- `tomorrow` - Display tomorrow's assignment
- `today` - Display today's assignment
- `week` - Display assignments for the current week
- `rest` - Display upcoming rest days
- `locations` - List all available locations
- `shifts` - List all recurring shifts

Example: `mvn exec:java -Dexec.mainClass="com.securitas.TaskManagerApp" -Dexec.args="tomorrow"`

This application is designed to enhance the scheduling experience for security guards, making it easier to manage their dynamic work environment.
