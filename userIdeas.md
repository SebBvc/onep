


# Health

## Muscle-Building Planner App
Features and architecture summary:

The Muscle-Building Planner App focuses solely on planning (not tracking). Features are organised into four main modules to keep responsibilities clear and extensible:

- Exercises: manage the exercise catalog and metadata.
- Routines: build ordered daily routines composed of exercise references.
- Programs: assemble routines into multi-day programs, version and analyze programs.
- Suggestions: analytical engines that inspect exercises/routines/programs and provide recommendations.


Shared principles:

- Plans are for sets per day (no reps/weights) — tracking is handled in the separate Tracker app.
- No reminders/notifications; no export UI (data accessible via backend API).

Exercises (module responsibilities):

- CRUD for exercises: name, main + secondary muscle groups, GIF, short description.
- Ratings: mental and physical difficulty.
- Tags and searchable metadata (movement patterns, equipment, primary muscle focus).
- Allow user-defined exercises and images/GIF attachments.

Routines (module responsibilities):

- Create a routine as an ordered group of exercise references (no per-exercise tracking fields).
- Support supersets inside a routine (not circuits) and explicit exercise ordering.
- Routine-level stats: average difficulty (mental/physical), average exercises count, most/least targeted muscle groups.
- Routine templates and duplication.

Programs (module responsibilities):

- Compose multiple routines into a program with a custom week length (not limited to 7 days).
- Program metadata: name, description, goal tags (strength, hypertrophy, etc.).
- Save and switch between multiple programs; program versioning to save and compare different program iterations.
- Program-level stats and dashboards: muscle-targeting summary, muscle balance heatmap across the program, average difficulty, total exercises.
- Ordering: order routines within the program; order routines defines daily plan flow.

Suggestions (module responsibilities):

- Analyze exercises, routines, and programs to provide actionable suggestions:
  - Flag under-developed or missing muscle groups across a program or routine.
  - Recommend balancing changes when certain days are too easy or too hard.
  - Suggest exercises to fill gaps based on muscle group coverage and training model (e.g., full-body checks).
- Expose suggestion reasons and confidence so users can accept or ignore recommendations.

Integration notes:

- The Planner exposes program and exercise data via the backend API for the Tracker app to import and use for workout logging and PR tracking.
- Keep planning responsibilities isolated so the Suggestions engine can be iterated independently (different models or rulesets).

## Training Tracker App
Features:
- Integrates with the Muscle-Building Planner App (import training plans)
- Log workouts with weights, sets, and reps
- Calendar view to mark gym attendance
- Track personal records (PRs) for each exercise
- Fun stats: total weight lifted, total hours trained, most frequent exercises, longest streak, etc.
- Progress charts for volume, frequency, and PRs
- Notes and comments for each workout
- Auto-suggest missed or upcoming planned sessions
- Export data for analysis
- Inspired by features from the Heavy app

# Finance

## Budget App
Features:
- Track income and expenses
- Set and monitor savings goals
- Visualize spending with charts
- Get alerts for overspending

# Personal Development

## Habit Reflection Assistant
Features (morning boost — categorized facts + image of the day):
- Morning boost feed mixing short, categorized fun facts and micro-quotes to start the day with perspective and motivation.
  Categories include: Psychology, Neuroscience, Productivity, Fitness, and General Trivia (user-selectable).
- Image of the day paired with the fact/quote (photography or illustration) with attribution and optional source link.
- Optional 1–2 sentence reflection prompt for the day (lightweight morning reflection, not end-of-day journaling).
- Personalization: choose preferred categories and set the content mix; the feed adapts based on recent engagement.
