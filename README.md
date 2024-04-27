# Document Indexer Application

A CLI application to interact with a Qdrant Vector Store.

## Technologies Used

- Java 21
- Spring Boot 3.2.5
- Maven
- OpenAI
- Qdrant Vector Store

## Dependencies

- Spring AI OpenAI Spring Boot Starter
- Spring AI PDF Document Reader
- Spring AI Qdrant Store Spring Boot Starter
- Spring Shell Starter
- Spring Boot Starter Web
- Spring Boot Devtools (optional)
- Spring Boot Starter Test (test scope)

## Environment Variables

Before running the application, you need to set the following environment variables:

- `QDRANT_COLLECTION_NAME`: The name of the Qdrant collection.
- `QDRANT_HOST`: The host address of the Qdrant service.
- `QDRANT_API_KEY`: The API key for the Qdrant service.
- `SPRING_AI_OPENAI_API_KEY`: The API key for the Spring AI OpenAI service.

You can set these environment variables in your terminal as follows:

```bash
export QDRANT_COLLECTION_NAME=your_collection_name
export QDRANT_HOST=your_qdrant_host
export QDRANT_API_KEY=your_qdrant_api_key
export SPRING_AI_OPENAI_API_KEY=your_spring_ai_openai_api_key
```

## Building the Project

This project uses Maven for dependency management and build automation. You can build the project using the following command:

```bash
./mvnw clean package -DskipTests
```

## Running the Application
After building the project, you can run the application using the following command:

```bash
java -jar target/data-loader-0.0.1-SNAPSHOT.jar
```
Upon running the application, you will see a shell prompt where you can type commands to interact with the application using the Terminal. Typing 'help' in the shell prompt will provide you with the available commands.

```
shell:>help
AVAILABLE COMMANDS

Application Commands
       qdrant delete: Delete a collection from Qdrant Vector Store
       qdrant list: List existing collections in Qdrant Vector Store
       qdrant load: Index documents to the Vector Store

Built-In Commands
       help: Display help about available commands
       stacktrace: Display the full stacktrace of the last error.
       clear: Clear the shell screen.
       quit, exit: Exit the shell.
       history: Display or save the history of previously run commands
       version: Show version info
       script: Read and execute commands from a file.
```
You can know more about what each command does by typing ```help <command_name>``` or by typing ```<command_name> -h```.

Example - 
```
shell:>qdrant load -h
NAME
       qdrant load - Index documents to the Vector Store

SYNOPSIS
       qdrant load [--path the path of the directory or file] --help

OPTIONS
       --path or -p the path of the directory or file
       [Mandatory]

       --help or -h
       help for qdrant load
       [Optional]
```
