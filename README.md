Project description: https://drive.google.com/file/d/1UcbgH2Gc05bBbcIkPBTl70ALUhrImbng/view?usp=drive_link

Extension of ClinicProject, adding GUI with javafx and xml files.


## Overview

This project is a JavaFX-based graphical user interface for a clinic appointment management system, developed as part of coursework. It revamps the command-line interface from Project 2 into a full GUI using JavaFX and the **MVC (Model-View-Controller)** design pattern. All core functionality from Project 2 is preserved — only the user interface layer has changed.

## Features

- Schedule, cancel, and manage clinic appointments through a GUI
- Input validation with descriptive error messages displayed in the UI
- No terminal I/O — all interaction happens through the GUI
- Built with JavaFX UI components including:
  - `TextField`, `Button`, `RadioButton`
  - `TextArea`, `TableView`, `TabPane`, `GridPane`

## Technologies

- Java
- JavaFX
- FXML (Scene Builder compatible)
- IntelliJ IDEA

## How to Run

1. Make sure you have **JDK 17+** and **JavaFX SDK** installed and configured.
2. Clone the repository:
   ```bash
   git clone https://github.com/your-username/your-repo-name.git
   ```
3. Open the project in **IntelliJ IDEA**.
4. Add the JavaFX SDK to your project's VM options:
   ```
   --module-path /path/to/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
   ```
5. Run `ClinicManagerMain.java`.
