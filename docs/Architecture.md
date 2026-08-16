# OneLaunch Architecture

## Overview

OneLaunch is a Java desktop application built with JavaFX.

The application is organized into four main areas:

```text
OneLaunch
│
├── model
├── service
├── ui
└── util
```

The architecture intentionally stays simple so that each part of the application has a clear responsibility.

## Project Structure

```text
src/main/java/onelaunch/
│
├── model/
│   ├── ItemType.java
│   ├── LaunchItem.java
│   └── Workspace.java
│
├── service/
│   ├── SettingsManager.java
│   └── StorageManager.java
│
├── ui/
│   ├── Main.java
│   ├── HomeScreen.java
│   ├── AddWorkspaceScreen.java
│   └── AddItemsScreen.java
│
└── util/
    ├── DialogUtil.java
    ├── DisplayNameUtil.java
    └── IconUtil.java
```

## Model Layer

The model layer represents application data.

### Workspace

A workspace contains:

- Name
- Launch items
- Pinned state

### LaunchItem

A launch item contains:

- Name
- Path
- Type

### ItemType

Supported item types are:

```text
APPLICATION
FILE
FOLDER
WEBSITE
```

## UI Layer

The UI layer contains the JavaFX screens.

### Main

Responsible for:

- Starting the application
- Managing the main JavaFX scene
- Theme switching
- Navigation between screens
- Launching workspace items
- Displaying the About dialog

### HomeScreen

Responsible for:

- Displaying workspaces
- Searching workspaces
- Pinning and unpinning
- Deleting workspaces
- Launching workspaces

### AddWorkspaceScreen

Responsible for:

- Creating a workspace name
- Basic workspace-name validation
- Moving the user into workspace item setup

### AddItemsScreen

Responsible for:

- Adding launch items
- Editing launch items
- Removing launch items
- Editing workspace names
- Duplicate detection
- Unsaved-change handling
- Saving workspaces

## Service Layer

The service layer handles persistent data.

### StorageManager

Responsible for:

- Saving workspaces
- Loading workspaces
- Creating the runtime data directory
- Serializing workspaces to JSON
- Detecting corrupted workspace data
- Creating backups of corrupted data

Workspace storage:

```text
data/workspaces.json
```

### SettingsManager

Responsible for:

- Saving application settings
- Loading application settings
- Persisting the dark-mode preference

Settings storage:

```text
data/settings.json
```

## Utility Layer

### DialogUtil

Provides reusable dialogs for:

- Confirmation
- Warning
- Information
- Adding items
- Selecting files or folders
- Website input

It also handles dialog styling and the OneLaunch application icon.

### DisplayNameUtil

Creates user-friendly display names for launch items.

It handles:

- Website name formatting
- Application name cleanup
- Long-name truncation

### IconUtil

Centralizes creation of application and interface icons.

## Workspace Creation Flow

```text
User
 ↓
AddWorkspaceScreen
 ↓
Enter workspace name
 ↓
AddItemsScreen
 ↓
Add / edit / remove items
 ↓
Save Workspace
 ↓
StorageManager
 ↓
workspaces.json
```

## Launch Flow

When a workspace is launched:

```text
Workspace
 ↓
LaunchItem
 ↓
ItemType
```

The item type determines how the operating system is asked to open it:

```text
APPLICATION → ProcessBuilder
FILE        → Desktop.open()
FOLDER      → Desktop.open()
WEBSITE     → Desktop.browse()
```

If an item fails to launch, the user can choose whether to continue with the remaining items or stop.

## Editing Flow

Existing workspaces enter `AddItemsScreen` in edit mode.

```text
Existing Workspace
 ↓
Edit Mode
 ↓
Modify Name / Items
 ↓
Save
 ↓
Updated Workspace
```

The existing pinned state is preserved during editing.

## Duplicate Protection

OneLaunch checks for duplicate workspace names and duplicate launch items.

Workspace names are compared case-insensitively.

Launch-item paths and website URLs are checked before an item is added or replaced.

## Unsaved Changes

The editing screen tracks whether changes have been made.

Changes include:

- Adding an item
- Editing an item
- Removing an item
- Renaming a workspace

If the user attempts to leave with unsaved changes, OneLaunch asks for confirmation before discarding them.

## Storage and Recovery

Workspace data is stored as JSON using Gson.

```text
data/
├── settings.json
├── workspaces.json
└── workspaces.json.backup
```

If `workspaces.json` contains invalid JSON:

```text
Invalid JSON
     ↓
Detect corruption
     ↓
Create backup
     ↓
Return empty workspace list
     ↓
Continue application
```

This prevents corrupted workspace data from stopping the application from starting.

## Theme Persistence

Dark mode is controlled by `Main` and persisted through `SettingsManager`.

```text
User changes theme
       ↓
Main.toggleDarkMode()
       ↓
SettingsManager
       ↓
settings.json
```

When the application starts, the saved setting is loaded and the appropriate stylesheet is applied.

## Design Decisions

OneLaunch deliberately avoids unnecessary complexity.

For the current project scope:

- JavaFX is sufficient for the desktop UI.
- JSON is sufficient for local persistence.
- Gson handles serialization.
- Service classes separate persistence from UI code.
- Utility classes provide reusable functionality.
- Git manages source-code history.

The architecture focuses on clarity, maintainability, and practical separation of responsibilities rather than introducing unnecessary frameworks or abstractions.