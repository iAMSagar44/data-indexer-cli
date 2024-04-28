# Document Indexer Application

A CLI application to interact with an Azure AI Search resource.

## Technologies Used

- Java 21
- Spring Boot 3.2.5
- Maven
- OpenAI
- Azure AI Search

## Dependencies

- Spring AI OpenAI Spring Boot Starter
- Spring AI PDF Document Reader
- Spring AI Azure AI Search Spring Boot Starter
- Spring Shell Starter
- Spring Boot Starter Web
- Spring Boot Devtools (optional)
- Spring Boot Starter Test (test scope)

## Environment Variables

Before running the application, you need to set the following environment variables:

- `AZURE_AI_SEARCH_ENDPOINT`: The endpoint of the Azure AI Search resource.
- `AZURE_AI_SEARCH_API_KEY`: The primary or the secondary key of the Azure AI Search resource.
- `SPRING_AI_OPENAI_API_KEY`: The API key for the Spring AI OpenAI service.

You can set these environment variables in your terminal as follows:

```bash
export AZURE_AI_SEARCH_ENDPOINT=your_Azure_AI_SEARCH_endpoint
export AZURE_AI_SEARCH_API_KEY=your_Azure_AI_SEARCH_api_key
export SPRING_AI_OPENAI_API_KEY=your_Spring_AI_OpenAI_api_key
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
       azais load: Index documents to the Vector Store
       azais list: List existing index names in the Azure AI Search instance
       azais create: Create an index in the Azure AI Search instance
       azais delete: Delete an index from the Azure AI Search instance

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
shell:>azais load -h
NAME
       azais load - Index documents to the Vector Store

SYNOPSIS
       azais load [--path the path of the directory or file] [--index the index name to load the documents] --help

OPTIONS
       --path or -p the path of the directory or file
       [Mandatory]

       --index or -i the index name to load the documents
       [Mandatory]

       --help or -h
       help for azais load
       [Optional]
```
