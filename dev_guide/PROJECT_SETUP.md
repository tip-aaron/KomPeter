# Project Setup Guide

## Prerequisites

- Java JDK 22+
- Maven 3.9+
- IDE: IntelliJ IDEA / Eclipse / VS Code / NetBeans (with Java extension)

## Cloning the Repo

```bash
git clone https://github.com/Ragudos/kompeter.git
cd kompeter
```

## Building and Running the Project

### VS Code

Click `Ctrl + Shift + P`, then find `Tasks: Run Task` and run `Maven Build (Dev)` for development and `Maven Build (Prod)` for production. Then, under the same menu, click `Run App`

### Eclipse

1. Import the project:
   - `File > Import > Maven > Existing Maven Projects`
   - Select the root directory of the cloned repository.

2. Launch configurations:
   - Go to `Run > Run Configurations`.
   - Go to `Launch Groups`. There should be configurations ready already.

3. Save these under a **Launch Group** if you want to execute multiple configurations in sequence.

### NetBeans

1. Open NetBeans and choose:
   `File > Open Project` → Select the cloned repo.

2. Configure Maven profiles:
   - Right-click the project → `Properties` → `Build > Maven`.
   - Set an **Action Pre-Build Step** if needed (e.g., `clean install`).

3. Running the app:
   - Right-click the **desktop module** → `Run`.
   - To switch environments, set the profile via:
     `Right-click Project > Properties > Actions > Run > VM Options`
     Add either:
     - `-Dspring.profiles.active=dev`
     - `-Dspring.profiles.active=prod`

### IntelliJ IDEA

1. Import project:
   - `File > New > Project from Existing Sources` → Select the cloned repo → choose **Maven**.

2. Build:
   - IntelliJ will automatically detect the Maven build.
   - Use the **Maven tool window** (`View > Tool Windows > Maven`) to run `clean install`.

3. Run configurations:
   - Go to `Run > Edit Configurations`.
   - Add a new **Application** configuration:
     - Name: `Run Desktop App (Dev)`
       - Main class: `com.github.ragudos.kompeter.app.desktop.KompeterDesktopApp`
       - VM options: `-Dspring.profiles.active=dev`
     - Duplicate for production with `-Dspring.profiles.active=prod`.

4. Run using the play button or `Shift + F10`.
