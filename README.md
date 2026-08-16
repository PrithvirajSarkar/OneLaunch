# OneLaunch

> Launch your workspace with one click.

OneLaunch is a Java desktop application built with JavaFX that lets users group applications, files, folders, and websites into reusable workspaces.

Instead of opening the same resources individually every time, users can save them as a workspace and launch them together with one click.

## ✨ Features

### Workspace Management

- Create and edit workspaces
- Rename and delete workspaces
- Search workspaces
- Pin and unpin workspaces
- Prevent duplicate workspace names

### Launch Items

A workspace can contain:

- 🖥️ Applications
- 📄 Files
- 📁 Folders
- 🌐 Websites

Items can be added, edited, removed, and checked for duplicates.

### User Experience

- Light and dark themes
- Persistent dark-mode preference
- Unsaved changes protection
- Confirmation dialogs
- User-friendly error handling
- Item-specific icons
- Launch animations

### Reliable Storage

Workspace data is stored locally as JSON using Gson.

OneLaunch also handles corrupted workspace data:

```text
Invalid JSON
     ↓
Detect corruption
     ↓
Create backup
     ↓
Recover with empty workspace list
     ↓
Continue running
```

## 🖼️ Screenshots

Screenshots will be added here after the final UI presentation is complete.

Planned screenshots:

- Home screen
- Workspace creation
- Adding items
- Editing a workspace
- Dark mode
- About dialog

## 🛠️ Technology Stack

| Technology | Purpose |
|---|---|
| Java | Core application language |
| JavaFX | Desktop user interface |
| Maven | Build and dependency management |
| Gson | JSON serialization and deserialization |
| Ikonli | UI icons |
| CSS | Application themes |
| Git | Version control |

## 🏗️ Architecture

OneLaunch uses a simple layered structure:

```text
OneLaunch
│
├── model
│   ├── Workspace
│   ├── LaunchItem
│   └── ItemType
│
├── service
│   ├── StorageManager
│   └── SettingsManager
│
├── ui
│   ├── Main
│   ├── HomeScreen
│   ├── AddWorkspaceScreen
│   └── AddItemsScreen
│
└── util
    ├── DialogUtil
    ├── DisplayNameUtil
    └── IconUtil
```

### Model

Represents the core application data.

### UI

Handles JavaFX screens and user interaction.

### Service

Handles persistent application data and settings.

### Utility

Contains reusable dialog, display-name, and icon functionality.

More details are available in [`docs/Architecture.md`](docs/Architecture.md).

## 💾 Data Storage

Runtime data is stored locally:

```text
data/
├── settings.json
├── workspaces.json
└── workspaces.json.backup
```

`workspaces.json` stores workspace information.

`settings.json` stores application settings such as the dark-mode preference.

The `data/` directory is generated at runtime and is excluded from Git so personal workspace data is not committed to the repository.

## 🛡️ Error Handling

OneLaunch handles common problems such as:

- Empty workspace names
- Duplicate workspace names
- Duplicate launch items
- Invalid website URLs
- Empty workspace launches
- Failed application/file/folder/website launches
- Corrupted workspace JSON

When workspace JSON is corrupted, OneLaunch creates a backup before continuing.

## 🔄 How It Works

### Create a Workspace

```text
Home
 ↓
Add Workspace
 ↓
Enter Name
 ↓
Add Items
 ↓
Save
```

### Launch a Workspace

```text
Home
 ↓
Launch
 ↓
Applications / Files / Folders / Websites
```

### Edit a Workspace

```text
Home
 ↓
Workspace Menu
 ↓
Edit
 ↓
Modify Workspace
 ↓
Save
```

## 📂 Project Structure

```text
OneLaunch/
│
├── docs/
│   ├── Architecture.md
│   ├── FutureIdeas.md
│   └── UserGuide.md
│
├── src/
│   └── main/
│       ├── java/onelaunch/
│       │   ├── model/
│       │   ├── service/
│       │   ├── ui/
│       │   └── util/
│       │
│       └── resources/
│
├── .gitignore
├── pom.xml
└── README.md
```

Runtime-generated data under `data/` is intentionally excluded from the repository.

## ⚙️ Requirements

To build OneLaunch from source:

- Java JDK 24
- Maven
- Git

Check your installation:

```bash
java -version
javac -version
mvn -version
```

## ▶️ Running from Source

Clone the repository:

```bash
git clone <repository-url>
cd OneLaunch
```

Build the project:

```bash
mvn clean package
```

Run the application:

```bash
mvn javafx:run
```

The exact runtime configuration is defined in `pom.xml`.

## 🧪 Testing

The current project has been manually tested across the core workflows, including:

- Workspace creation
- Workspace editing
- Workspace deletion
- Workspace search
- Workspace pinning
- Application launching
- File launching
- Folder launching
- Website launching
- Duplicate detection
- Invalid URL handling
- Unsaved changes
- Dark-mode persistence
- Corrupted JSON recovery
- Backup creation

## 🎯 Design Philosophy

OneLaunch intentionally keeps its architecture simple.

The project uses:

- JavaFX for the desktop interface
- JSON for local persistence
- Separate service classes for storage
- Utility classes for reusable functionality
- Git for version control

The goal is to maintain clear separation of responsibilities without introducing unnecessary frameworks or abstractions for a small desktop application.

## 📚 Documentation

- [Architecture](docs/Architecture.md)
- [User Guide](docs/UserGuide.md)
- [Future Ideas](docs/FutureIdeas.md)

## 👨‍💻 Project

OneLaunch is a Java desktop software engineering project focused on applying practical development concepts including:

- Object-oriented programming
- JavaFX application development
- File-based persistence
- JSON serialization
- Input validation
- Error handling
- Version control with Git
- Software architecture
- Technical documentation