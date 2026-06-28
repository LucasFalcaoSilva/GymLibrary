# Spec: Integration Tests — ExerciseRepository + MockWebServer

**ID:** TEST-INTEG-001  
**Layer:** Integration Tests  
**Target specs:** FEAT-001, FEAT-002, FEAT-003  
**Status:** Approved

## Description

Integration tests for `ExerciseRepositoryImpl` + `ExerciseRemoteDataSource` +
`ExerciseDbService` (Retrofit) using `MockWebServer` (OkHttp).

Tests the full data layer stack against a fake HTTP server — no real network,
no MockK for the HTTP layer. Validates JSON parsing, error mapping, and
`Result<T>` propagation from network call to repository output.

## Dependencies

```toml
[versions]
mockwebserver = "4.12.0"   # same version as okhttp

[libraries]
okhttp-mockwebserver = { group = "com.squareup.okhttp3", name = "mockwebserver", version.ref = "mockwebserver" }
```

Add to `app/build.gradle.kts`:
```kotlin
testImplementation(libs.okhttp.mockwebserver)
```

## Test Location

`app/src/test/java/com/miranda/gymlibrary/data/`

---

## ExerciseRepositoryImplTest

**File:** `data/repository/ExerciseRepositoryImplTest.kt`

### Setup

```kotlin
class ExerciseRepositoryImplTest {
    private lateinit var mockWebServer: MockWebServer
    private lateinit var apiService: ExerciseDbService
    private lateinit var dataSource: ExerciseRemoteDataSource
    private lateinit var repository: ExerciseRepositoryImpl

    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()

        val retrofit = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .addConverterFactory(
                Json { ignoreUnknownKeys = true }
                    .asConverterFactory("application/json".toMediaType())
            )
            .build()

        apiService = retrofit.create(ExerciseDbService::class.java)
        dataSource = ExerciseRemoteDataSource(apiService)
        repository = ExerciseRepositoryImpl(dataSource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }
}
```

---

### getBodyParts Tests

| # | Test | MockWebServer response | Expected |
|---|------|----------------------|----------|
| 1 | `returns body part list on 200` | `["back","chest","legs"]` | `Result.success(listOf("back","chest","legs"))` |
| 2 | `returns failure on 401` | HTTP 401 | `Result.failure` with `HttpException(401)` |
| 3 | `returns failure on 500` | HTTP 500 | `Result.failure` with `HttpException(500)` |
| 4 | `returns failure on network timeout` | No response (server closes connection) | `Result.failure` with `SocketTimeoutException` |
| 5 | `returns failure on malformed JSON` | `not_valid_json` | `Result.failure` with serialization exception |

```kotlin
@Test
fun `returns body part list on 200`() = runTest {
    mockWebServer.enqueue(
        MockResponse()
            .setResponseCode(200)
            .setBody("""["back","chest","legs","upper arms"]""")
            .addHeader("Content-Type", "application/json")
    )

    val result = repository.getBodyParts()

    assertTrue(result.isSuccess)
    assertEquals(listOf("back", "chest", "legs", "upper arms"), result.getOrNull())
}
```

---

### getExercisesByBodyPart Tests

| # | Test | MockWebServer response | Expected |
|---|------|----------------------|----------|
| 6 | `returns exercise list on 200` | Valid JSON array with 2 exercises | `Result.success` with 2 `Exercise` objects |
| 7 | `maps all fields correctly` | Single exercise JSON | All domain model fields match JSON values |
| 8 | `returns empty list on empty array` | `[]` | `Result.success(emptyList())` |
| 9 | `returns failure on 403` | HTTP 403 | `Result.failure` with `HttpException(403)` |
| 10 | `ignores unknown fields in JSON` | JSON with extra `unknownField` | `Result.success` — no serialization error |

```kotlin
@Test
fun `maps all fields correctly`() = runTest {
    mockWebServer.enqueue(
        MockResponse()
            .setResponseCode(200)
            .setBody("""
                [{
                    "id": "0007",
                    "name": "alternate lateral pulldown",
                    "bodyPart": "back",
                    "equipment": "cable",
                    "target": "lats",
                    "secondaryMuscles": ["biceps","rhomboids"],
                    "instructions": ["Step 1","Step 2"],
                    "description": "A cable exercise.",
                    "difficulty": "beginner",
                    "category": "strength"
                }]
            """.trimIndent())
            .addHeader("Content-Type", "application/json")
    )

    val result = repository.getExercisesByBodyPart("back", 20, 0)
    val exercise = result.getOrNull()?.first()

    assertNotNull(exercise)
    assertEquals("0007", exercise!!.id)
    assertEquals("alternate lateral pulldown", exercise.name)
    assertEquals("lats", exercise.target)
    assertEquals(listOf("biceps", "rhomboids"), exercise.secondaryMuscles)
    assertEquals("beginner", exercise.difficulty)
    assertEquals("strength", exercise.category)
}
```

---

### getExerciseById Tests

| # | Test | MockWebServer response | Expected |
|---|------|----------------------|----------|
| 11 | `returns exercise on 200` | Single exercise JSON | `Result.success(exercise)` |
| 12 | `returns failure on 404` | HTTP 404 | `Result.failure` with `HttpException(404)` |
| 13 | `maps description field correctly` | JSON with description | `exercise.description` matches JSON value |

---

## Business Rules Validated

| BR | Source | Tests |
|---|---|---|
| BR-05 | error-handling.md | Tests #2–5, #9 — `runCatching` wraps all failures as `Result.failure` |
| BR-01 | error-handling.md | Tests #2,#3,#9 — HttpException propagated, never swallowed |
| JSON contract | FEAT-002 DTO | Tests #7, #10 — fields mapped correctly, unknown fields ignored |

## Files to create

- `data/repository/ExerciseRepositoryImplTest.kt`
- `test/fixtures/ExerciseFixtures.kt` — fake JSON strings and domain objects for reuse across tests

## Notes

- `ignoreUnknownKeys = true` must be set on the test `Json` instance — same as production config (CORE-001 deviation documented in CORE-001 report)
- MockWebServer runs on a real port — tests are slightly slower than pure unit tests but still run on JVM without emulator
