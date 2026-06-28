# /commit — Commit Following Project Standards

Stage and commit changes following GymLibrary conventions.

## Usage

```
/commit CORE-001
```

## Steps

1. Check `reports/[SPEC-ID].md` for unresolved 🔴 findings:
   - Read the latest `## Review Round N` section in the report
   - If any finding has severity 🔴 and status other than "Fixed" → **abort immediately** with:
     ```
     ❌ Commit bloqueado — [N] finding(s) 🔴 pendentes em reports/[SPEC-ID].md.
     Corrija todos os findings 🔴 antes de rodar /commit [SPEC-ID].
     ```
   - If no 🔴 findings pending → proceed to step 2
2. Check `reports/[SPEC-ID].md` for QA sign-off (feature specs only):
   - If the report contains `## QA Sign-off` with status **Blocked** → **abort** with:
     ```
     ❌ Commit bloqueado — QA reprovado em reports/[SPEC-ID].md.
     Corrija os TCs com falha e rode /qa [SPEC-ID] novamente.
     ```
   - If the report does not contain `## QA Sign-off` and the spec is a FEAT → **abort** with:
     ```
     ❌ Commit bloqueado — QA não executado.
     Rode /qa [SPEC-ID] antes de commitar.
     ```
   - If QA Approved or spec is CORE → proceed to step 3
3. Run `./gradlew assembleDebug` — abort if build fails
4. List all modified and new files
5. Stage all relevant files with `git add`
6. Commit using the Conventional Commits format:
   - `feat:` for new spec implementation
   - `fix:` for bug fixes
   - `docs:` for spec or documentation updates
   - `chore:` for Gradle or config changes
   - `refactor:` for refactoring without behavior change
   - `test:` for test additions
7. Use the spec ID in the commit message body

## Commit Message Format

```
feat: implement network setup

Implements CORE-001 — configures Retrofit + OkHttp with
AuthInterceptor, HttpLoggingInterceptor (debug only), and
60s timeouts. Koin networkModule wired with ExerciseDbService.
```

## Rules

- Never commit if any 🔴 finding is unresolved in the report
- Never commit if `assembleDebug` fails
- Never commit `local.properties`, `.idea/`, or `*.jks`
- Never use `git add .` without reviewing what is staged first
- One commit per spec — do not bundle multiple specs in one commit
