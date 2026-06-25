# Project Context — GymLibrary

## Overview

GymLibrary is an Android exercise library app that allows users to browse gym exercises filtered by muscle group and equipment, view animated GIFs, muscle activation diagrams, and step-by-step instructions.

The project is a portfolio app built to demonstrate Specification-Driven Development (SDD), Clean Architecture, Jetpack Compose, and REST API consumption skills.

## Goals

- Demonstrate Clean Architecture + MVVM in a real-world context
- Practice SDD workflow (spec → code)
- Produce a polished, publishable portfolio artifact

## Out of Scope

- User authentication
- Workout creation or tracking
- Progress logging
- Push notifications
- Offline-first sync strategy

## API

**ExerciseDB** via RapidAPI  
Base URL: `https://exercisedb.p.rapidapi.com`  
Authentication: `X-RapidAPI-Key` header  
Key storage: `local.properties` → `RAPIDAPI_KEY`

### Endpoints used

| Endpoint | Usage |
|---|---|
| `GET /exercises` | Full exercise list (paginated) |
| `GET /exercises/bodyPart/{bodyPart}` | Filter by muscle group |
| `GET /exercises/equipment/{type}` | Filter by equipment |
| `GET /exercises/exercise/{id}` | Exercise detail |
| `GET /exercises/bodyPartList` | List of muscle groups |
| `GET /exercises/equipmentList` | List of equipment types |

## Package

`com.miranda.gymlibrary`

## Min SDK

26 (Android 8.0)

## Repository

`https://github.com/LucasFalcaoSilva/GymLibrary`
