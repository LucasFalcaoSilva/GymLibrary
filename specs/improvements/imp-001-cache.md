# Spec: Improvement — Cache Local com Room

**ID:** IMP-001  
**Priority:** 🟡 High  
**Type:** Performance + UX  
**Status:** Approved

## Description

Implementar cache local com Room para evitar requisições desnecessárias à API.
O usuário não deve precisar aguardar o carregamento toda vez que revisitar
uma tela já carregada anteriormente na mesma sessão.

## Scope

Cache em memória **não** é suficiente — Room persiste entre sessões, evitando
requisições à API mesmo após fechar e reabrir o app.

| Dado | TTL sugerido | Justificativa |
|---|---|---|
| Body parts list | 24h | Raramente muda |
| Exercises by body part | 1h | Pode ter novos exercícios |
| Exercise detail | 1h | Detalhes estáveis |

## Architecture Change

Adiciona uma camada de cache entre `RemoteDataSource` e `RepositoryImpl`:

```
ExerciseRepositoryImpl
    ├── ExerciseRemoteDataSource  (API)
    └── ExerciseLocalDataSource   (Room) ← novo
```

**Estratégia: Cache-first**
1. Verificar cache local
2. Se válido (dentro do TTL) → retornar dados do cache
3. Se expirado ou ausente → buscar na API → salvar no cache → retornar

## Room Schema

```kotlin
@Entity(tableName = "body_parts")
data class BodyPartEntity(
    @PrimaryKey val name: String,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "exercises")
data class ExerciseEntity(
    @PrimaryKey val id: String,
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val target: String,
    val secondaryMuscles: String,  // JSON serialized List<String>
    val instructions: String,       // JSON serialized List<String>
    val description: String,
    val difficulty: String,
    val category: String,
    val cachedAt: Long = System.currentTimeMillis()
)

@Database(
    entities = [BodyPartEntity::class, ExerciseEntity::class],
    version = 1
)
abstract class GymLibraryDatabase : RoomDatabase() {
    abstract fun bodyPartDao(): BodyPartDao
    abstract fun exerciseDao(): ExerciseDao
}
```

## DAOs

```kotlin
@Dao
interface BodyPartDao {
    @Query("SELECT * FROM body_parts")
    suspend fun getAll(): List<BodyPartEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(bodyParts: List<BodyPartEntity>)

    @Query("DELETE FROM body_parts")
    suspend fun deleteAll()
}

@Dao
interface ExerciseDao {
    @Query("SELECT * FROM exercises WHERE bodyPart = :bodyPart LIMIT :limit OFFSET :offset")
    suspend fun getByBodyPart(bodyPart: String, limit: Int, offset: Int): List<ExerciseEntity>

    @Query("SELECT * FROM exercises WHERE id = :id")
    suspend fun getById(id: String): ExerciseEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(exercises: List<ExerciseEntity>)
}
```

## Dependencies

```toml
[versions]
room = "2.6.1"

[libraries]
room-runtime = { group = "androidx.room", name = "room-runtime", version.ref = "room" }
room-ktx = { group = "androidx.room", name = "room-ktx", version.ref = "room" }
room-compiler = { group = "androidx.room", name = "room-compiler", version.ref = "room" }
```

```kotlin
// app/build.gradle.kts
implementation(libs.room.runtime)
implementation(libs.room.ktx)
kapt(libs.room.compiler)
```

## Business Rules

- BR-01: Cache TTL para body parts = 24 horas
- BR-02: Cache TTL para exercises = 1 hora
- BR-03: Se o cache está expirado, buscar na API e atualizar o cache
- BR-04: Se a API falhar e o cache existir (mesmo expirado), retornar cache com flag `isStale = true`
- BR-05: Cache deve ser transparente para a camada de domínio — UseCase e ViewModel não sabem da existência do cache

## Files to create

- `data/local/database/GymLibraryDatabase.kt`
- `data/local/entity/BodyPartEntity.kt`
- `data/local/entity/ExerciseEntity.kt`
- `data/local/dao/BodyPartDao.kt`
- `data/local/dao/ExerciseDao.kt`
- `data/local/datasource/ExerciseLocalDataSource.kt`
- `data/local/mapper/EntityMapper.kt`

## Files to modify

- `data/repository/ExerciseRepositoryImpl.kt` — adicionar lógica cache-first
- `core/di/DataModule.kt` — adicionar Room database e LocalDataSource
- `app/build.gradle.kts` — adicionar Room dependencies
- `gradle/libs.versions.toml` — adicionar Room versions

## Acceptance Criteria

- [ ] Body parts carregam do cache na segunda visita sem requisição à API
- [ ] Exercícios de um body part carregam do cache na segunda visita
- [ ] Cache expira corretamente após TTL
- [ ] App funciona offline se dados estiverem em cache (mesmo expirado)
- [ ] Nenhum dado de cache é visível para as camadas de domain e presentation
