# Dynamic Agenda Application for Security Guards

This application helps security guards working for Securitas manage their daily work schedules dynamically. It allows for tracking of recurring tasks, handling schedule variations, and querying future assignments.

## Key Features:

- **Dynamic Scheduling**: The application can handle recurring tasks and adapt to variations in the user's schedule.
- **Future Assignments**: Users can query where they will work the following day and get specific details about each job.
- **Rest Day Management**: The application tracks rotating rest days and ensures assignments do not conflict with these days.
- **Default Work Hours**: If no hours are specified, the application defaults to a work schedule from 14:00 to 22:00.

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

## Application Capabilities:

- Recognizes recurring shifts (e.g., weekends at Gasoducto Cruz Del Sur, Mondays at Magnolio).
- Handles dynamic scheduling changes (e.g., switching retén days or changing locations at short notice).
- Keeps track of rest days (e.g., every Thursday and rotating Wednesdays).
- Provides answers when queried about future schedules, such as "Where am I working tomorrow?" or "What are my next assignments?"
- Users can insert new information, such as past assignments on retén days, including details about armed and unarmed shifts.

This application is designed to enhance the scheduling experience for security guards, making it easier to manage their dynamic work environment.
