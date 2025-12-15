# Muscle-Building Planner — Backend Requirements

## Overview
This document lists the backend modules, responsibilities, non-functional requirements, and domain objects for the "Muscle-Building Planner App". Focus is planning (no detailed per-set tracking). The backend must expose a clear, versioned API usable by a separate Tracker app.

Common backend conventions (apply globally):
- IDs: UUIDs (v4).
- Timestamps: `createdAt`, `updatedAt` (UTC, ISO8601).
- Audit: `createdBy`, `updatedBy` (user id) where applicable.
- Deletion: hard delete only (no `deletedAt` soft-delete field).
- Pagination: cursor-based or offset with `limit`/`cursor` support.
- Responses: consistent envelope with data/meta/errors.
- Validation: strict DTO validation server-side.
- Auth: OAuth2 / JWT. Scopes: `planner.read`, `planner.write`, `planner.admin`.
--
- Search: full-text and tag-based filtering; indexes on name, tags, muscle groups.
- ID references in DTOs (no embedded heavy objects unless specified).

---

## Modules (high-level)
1. Exercises
2. Routines
3. Programs
4. Suggestions
5. Users & Access (Auth & ACL)
6. Media & Attachments
7. Muscles
8. Tags & Taxonomy (search metadata)
8. API & Versioning
9. Audit, Versioning & History
10. Integrations & Events
11. Admin & Maintenance
12. Analytics & Aggregation

For each module below: purpose, backend requirements, domain objects.

---

**1. Exercises**

Purpose: Manage canonical exercise catalog and user-defined exercises. Store metadata for planning and suggestions.

Requirements:
- CRUD endpoints for exercises with validation.
- Search and filter by name, primary/secondary muscles, movement patterns, equipment, tags, rating ranges.
- Support user-defined and system-provided exercises (system exercises indicated by `ownerId == null`). Users may create and edit their own exercises.
- Ratings: `mentalDifficulty` and `physicalDifficulty` numeric (e.g., 1–5) with optional user overrides.
- Attach GIF/image URLs (media stored via Media module).
- Permissions: users can create/edit their own exercises; users cannot delete exercises. Admins (or privileged service accounts) may perform hard deletes.
- Uniqueness: unique (case-insensitive) name per owner scope (global or user).
- Deletion: hard delete only; no soft-delete fields.
- Indexes: name, primaryMuscle, tags, equipment.

Domain objects:
- Exercise
  - id: UUID
  - ownerId: UUID | null (null indicates system/global)
  - name: string
  - shortDescription: string
  - primaryMuscleGroups: [string]
  - secondaryMuscleGroups: [string]
  - movementPatterns: [string] (e.g., "push", "pull", "hinge", "squat", "carry")
  - equipment: [string]
  - tags: [string]
  - gifUrl: string | null
  - mediaIds: [UUID]
  - mentalDifficulty: integer (1-5)
  - physicalDifficulty: integer (1-5)
  - createdAt, updatedAt

Notes:
- Muscle group values should be defined and managed by the `Muscles` module.

---

**2. Routines**

Purpose: Ordered daily routines composed of references to exercises. Support supersets.

Requirements:
- CRUD endpoints for routines.
- Create routine as an ordered list of steps. Each step references an `Exercise` and contains ordering metadata.
- Support supersets: groups of steps that form a superset (superset = contiguous group with special grouping id/type).
- Routine-level computed stats: average difficulty (physical/mental), exercise count, muscle group distribution.
- Templates and duplication: allow copying routines (new id, keep references).
- Permissions: owner-only edit; sharing: read-only shareable links / `public` flag.
- Validation: referenced exercises must exist and be accessible to the user.
-- Versioning: none required at routine level for initial implementation.

Domain objects:
- Routine
  - id: UUID
  - ownerId: UUID
  - name: string
  - description: string
  - public: boolean
  - tags: [string]
  - steps: [RoutineStep] (ordered array)
  - stats: computed object (avgPhysicalDifficulty, avgMentalDifficulty, exerciseCount)
  - createdAt, updatedAt

- RoutineStep
  - id: UUID
  - order: integer
  - exerciseId: UUID
  - note: string | null
  - supersetId: UUID | null (group id if part of superset)
  - repsPreset: string | null (optional text, but trackless)

Notes:
- Supersets are identified by `supersetId`; a superset is any contiguous group sharing that id.

---

**3. Programs**

Purpose: Compose multiple routines into multi-day programs. Provide stats and muscle-balance analysis.

Requirements:
- CRUD endpoints for programs.
- Program contains an ordered schedule mapping day indices to routine references (support custom week length N days).
-- Program metadata: goal tags, description, name, difficulty, total exercises estimate.
-- Stats: muscle-targeting summary (aggregate primary/secondary hits), muscle balance heatmap across days, average difficulty/day, total distinct exercises.
-- Program-level ordering and re-mapping routines to different days.
-- Validation: routines used must exist and be visible to the owner.
- API to export program structure for Tracker app import (simple JSON contract).
- Transactions: changes that modify multiple entities (e.g., reordering days) must be transactional.

Domain objects:
- Program
  - id: UUID
  - ownerId: UUID
  - name: string
  - description: string
  - goalTags: [string]
  - weekLength: integer (>0)
  - schedule: [ProgramDay] (ordered)
  - createdAt, updatedAt

- ProgramDay
  - dayIndex: integer (1..weekLength)
  - routineId: UUID
  - order: integer (in-day order if multiple routines per day supported)

---

**4. Suggestions**

Purpose: Analytical engine that inspects exercises, routines, and programs to provide actionable recommendations.

- Requirements:
- Provide suggestion endpoints: `GET /programs/{id}/suggestions`, `GET /routines/{id}/suggestions`.
- Suggestions must include: `reason`, `confidence` (0-1), `type` (e.g., "missing-muscle", "balance", "difficulty-adjustment"), and `actions` (e.g., recommended exercise ids or routine reorder suggestions).
- Configurable rulesets: rules-based baseline plus pluggable ML model outputs; keep engine decoupled from core DB (suggestion service or background job).
- Store suggestion runs and results for audit and user review (ability to accept/ignore suggestions).
- Support batch operations and scheduled suggestion jobs for all programs owned by user.
- Explainability: provide concise textual reason and the metrics used.

Domain objects:
- Suggestion
  - id: UUID
  - targetType: enum (Program, Routine)
  - targetId: UUID
  - type: string
  - reason: string
  - confidence: float
  - recommendedExerciseIds: [UUID]
  - recommendedRoutineChanges: JSON (diff-like structure)
  - createdAt, createdBy
  - status: enum (pending, applied, dismissed)

Notes:
- Suggestions should be idempotent and safe; applying a suggestion should produce an explicit change request that the user approves.

---

**5. Users & Access (Auth & ACL)**

Purpose: User identity, roles, and permissions.

Requirements:
- Authentication via OAuth2/JWT; tokens include scopes.
- Roles: user, admin. Optional organization/multi-tenant support.
- Permissions: owner-based access for CRUD; admin overrides.
- Sharing controls: `public`, `shareToken` (expiring), or explicit user grants.
- Optional per-user quotas for program count, media storage (configurable).

Domain objects:
- User (existing if app has auth): id, email, displayName, roles, createdAt
- Grant/Share
  - id: UUID
  - resourceType: string
  - resourceId: UUID
  - granteeId: UUID | null
  - token: string | null
  - expiresAt: timestamp | null

---

**6. Media & Attachments**

Purpose: Store GIFs, images, attachments for exercises and user content.

Requirements:
- Store only metadata in DB; store actual files in S3-compatible store.
- Provide signed upload URLs and signed download URLs.
- Validate MIME types and size limits (e.g., images < 5MB, gifs < 10MB).
- Generate thumbnails and optimized variants (background job).
- Virus/malware scanning hook optional.
- Reference media from `mediaId` in `Exercise` or other objects.

Domain objects:
- Media
  - id: UUID
  - ownerId: UUID
  - filename: string
  - mimeType: string
  - sizeBytes: integer
  - storagePath: string
  - variants: JSON (thumb, webp, etc.)
  - createdAt

---

**7. Muscles**

Purpose: Centralize canonical muscle groups and related metadata used by Exercises, Routines and Programs.

Requirements:
- Admin CRUD for muscle groups and canonical ordering/labels.
- Provide canonical muscle enums/IDs for validation and analytics.
- Support grouping (e.g., upper/lower, pushing/pulling) and synonyms.

Domain objects:
- Muscle
  - id: UUID
  - key: string (e.g., "chest")
  - label: string (e.g., "Chest")
  - group: string | null (e.g., "upper")
  - synonyms: [string]
  - order: integer

**8. Tags & Taxonomy**

Purpose: Centralize enums and tag taxonomies (movement patterns, equipment, goal tags, and general tags).

Requirements:
- Admin CRUD for taxonomy entries.
- Validation against taxonomy when creating exercises/routines/programs.
- Provide endpoint for client to fetch the full taxonomy.

Domain objects:
- TaxonomyEntry
  - id: UUID
  - category: string (movement, equipment, goal, tag)
  - key: string
  - label: string
  - synonyms: [string]
  - order: integer

---

**8. API & Versioning**

Requirements:
- Versioned API (`/api/v1/...`), be explicit about breaking changes.
- Provide OpenAPI/Swagger spec for all endpoints.
- Consistent error codes and well-documented DTOs for import/export.
- CORS for frontends and Tracker integration.

---

**9. Audit & History**

Requirements:
- Audit logs for critical actions (create/delete/publish program, apply suggestions).
- Optional per-entity history (delta + snapshot) for important objects.

Domain objects:
- AuditLog
  - id: UUID
  - userId: UUID
  - action: string
  - resourceType: string
  - resourceId: UUID
  - details: JSON
  - createdAt

---

**10. Integrations & Events**

Purpose: Integration with Tracker app and external systems.

Requirements:
- Export API for Tracker to import Program/Exercise definitions (lightweight contract focused on ids, names, steps, and media URLs).
- Webhooks or message bus (e.g., Kafka) for key events: `program.created`, `exercise.updated`.
- Event payloads should include resolved state when relevant.

---

**11. Admin & Maintenance**

Requirements:
- Admin endpoints for cleanup, reindex, rebuild stats, and manage system/global exercises.
- Bulk import/export tooling (CSV/JSON) for exercise and program migration.
- Data retention and backup policies for stored data and media.

---

**12. Analytics & Aggregation**

Purpose: Provide aggregated metrics used by Suggestions and program dashboards.

Requirements:
- Periodic jobs to compute program-level aggregates (muscle distribution, difficulty heatmaps).
- Store computed aggregates (cache) for fast API responses.
- Expose endpoints for program dashboards.

Domain objects (computed/cached):
- ProgramStats
  - programId
  - computedAt
  - muscleCoverage: map(muscle -> hits)
  - difficultyByDay: [float]
  - totalExercises

---

## Non-functional requirements
- Scalability: services should scale independently; read-heavy workloads for suggestions/analytics should use read replicas or caches.
- Security: input sanitization, ACL enforcement, media validation, and secure signed URLs.
- Observability: request metrics, traces for long-running suggestion jobs, and error reporting.
- Backups: DB backups and media storage backups with restore tested.
- Tests: unit tests, integration tests for API contracts, and end-to-end tests for program export/import.

---

## Suggested endpoints (examples)
- Exercises: `GET /api/v1/exercises`, `POST /api/v1/exercises`, `GET /api/v1/exercises/{id}`
- Routines: `GET /api/v1/routines`, `POST /api/v1/routines`, `POST /api/v1/routines/{id}/duplicate`
- Programs: `GET /api/v1/programs`, `POST /api/v1/programs`, `GET /api/v1/programs/{id}/export`
- Suggestions: `GET /api/v1/programs/{id}/suggestions`, `POST /api/v1/suggestions/{id}/apply`
- Media: `POST /api/v1/media/upload-url`, `GET /api/v1/media/{id}`

---

## Implementation notes / priorities (first sprint)
1. Basic Exercises CRUD with media support and taxonomy.
2. Routines CRUD with ordered steps and superset support.
3. Programs create/read with simple schedule (no versioning initially), plus export endpoint for Tracker.
4. Authentication/authorization and basic sharing model.
5. Simple Suggestions rules (rule-based missing-muscle detector) as a background job.
6. Add program stats and analytics in the next iteration.

---

## Appendix: example enums
- Muscle groups: chest, back, quads, hamstrings, glutes, shoulders, biceps, triceps, calves, core
- Movement patterns: push, pull, hinge, squat, carry
- Equipment: barbell, dumbbell, kettlebell, machine, bodyweight




