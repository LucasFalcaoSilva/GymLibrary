# /commit — Commit Following Project Standards

Stage and commit changes following GymLibrary conventions.

## Usage

```
/commit CORE-001
```

## Steps

1. Run `./gradlew assembleDebug` — abort if build fails
2. List all modified and new files
3. Stage all relevant files with `git add`
4. Commit using the Conventional Commits format:
   - `feat:` for new spec implementation
   - `fix:` for bug fixes
   - `docs:` for spec or documentation updates
   - `chore:` for Gradle or config changes
   - `refactor:` for refactoring without behavior change
   - `test:` for test additions
5. Use the spec ID in the commit message body

## Commit Message Format

```
feat: implement network setup

Implements CORE-001 — configures Retrofit + OkHttp with
AuthInterceptor, HttpLoggingInterceptor (debug only), and
60s timeouts. Koin networkModule wired with ExerciseDbService.
```

## Rules

- Never commit if `assembleDebug` fails
- Never commit `local.properties`, `.idea/`, or `*.jks`
- Never use `git add .` without reviewing what is staged first
- One commit per spec — do not bundle multiple specs in one commit
