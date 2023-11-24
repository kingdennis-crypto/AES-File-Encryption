# File Encryption

This Java Maven project provides a file encryption and decryption utility using the AES algorithm. The application includes a command-line interface (CLI) to facilitate key management for users, allowing them to create, delete, select, and retrieve keys for encryption and decryption processes.

## Key Features

- AES Encryption/Decryption: Utilizes the AES algorithm for secure file encryption and decryption.
- Command-Line Interface: Provides a user-friendly CLI for managing cryptographic keys.
- Key Creation and Deletion: Users can create and delete cryptographic keys as needed.
- Key Selection: Supports the selection of a specific key for use in encryption and decryption.

## Usage

To use this application, follow these steps:

1. **Build the Project:**
   - Ensure that you have Maven installed on your system.
   - Open the project in IntelliJ or your preferred Java IDE.
   - Build the project using Maven. You can do this by running the Maven `clean install` command or using the IDE's build tools.

2. **Run the Application:**
    - **Via IntelliJ:**
      - Open the project in IntelliJ.
      - Locate the `Main` class.
      - Right-click on the main class and choose "Run" from the context menu.
      - The CLI will guide you through the available key management commands.

## Project Structure

The project structure follows Maven conventions:

- `src/main/java/org/zenith`: Contains the Java source code.
- `src/test/java/org/zenith`: Contains the test classes for unit testing.
- `target`: The default output directory for compiled classes and generated JAR files.
- `pom.xml`: The Maven Project Object Model file, defining project configuration.

## Commands

### Key Commands

The key commands available in the CLI include:

- `create [Key]`: Creates a new cryptographic key with the specified name.
- `delete [Key]`: Deletes an existing cryptographic key with the specified name.
- `get [Key]`: Retrieves information about an existing cryptographic key.
- `list`: Lists all generated cryptographic keys.
- `select [Key]`: Selects a cryptographic key for encryption and decryption.

### Encryption Commands

The encryption commands available in the CLI include:

- `encrypt [path]`: Encrypts the file located at the specified path.
- `decrypt [path]`: Decrypts the file located at the specified path.
  
## Contributing

Contributions to this project are welcome! If you encounter issues or have suggestions for improvements, please open a issue or submit a pull request.

## License

This project is licensed under the MIT License. Feel free to use, modify, and distribute the code