# Document Indexer Application

A CLI application to interact with a locally running PGVector store 

## Technologies Used

- Java 21
- Spring Boot 3.2.5
- Maven
- OpenAI
- PGVector Store

## Dependencies

- Spring AI OpenAI Spring Boot Starter
- Spring AI PDF Document Reader
- Spring AI PGVector Starter
- Spring Shell Starter
- Spring Boot Starter Web
- Spring Boot Devtools (optional)
- Spring Boot Starter Test (test scope)

## Environment Variables

Before running the application, you need to set the following environment variables:

- `SPRING_AI_OPENAI_API_KEY`: The API key for the Spring AI OpenAI service.

You can set these environment variables in your terminal as follows:

```bash
export SPRING_AI_OPENAI_API_KEY=your_Spring_AI_OpenAI_api_key
```

## Vector Store

This project uses PGvector store to store the document vectors. PGvector is an open-source extension for PostgreSQL that enables storing and searching over machine learning-generated embeddings.

### Running the VectorStore

The Vector Store is a docker container that can be started with the following command:

```
docker run -it --rm --name postgres -p 5432:5432 -e POSTGRES_USER=postgres -e POSTGRES_PASSWORD=postgres ankane/pgvector
```

Then you can connect to the database (password: `postgres`) and inspect or alter the `vector_store` table content:

```
psql -U postgres -h localhost -p 5432

\l
\c vector_store
\dt

select count(*) from vector_store;
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

Built-In Commands
       help: Display help about available commands
       stacktrace: Display the full stacktrace of the last error.
       clear: Clear the shell screen.
       quit, exit: Exit the shell.
       history: Display or save the history of previously run commands
       version: Show version info
       script: Read and execute commands from a file.

Default
       data load: Index documents to the Vector Store
       data delete: Deletes all the documents from the Vector Store
```
You can know more about what each command does by typing ```help <command_name>``` or by typing ```<command_name> -h```.

Example -
```
shell:>data load -h
NAME
       data load - Index documents to the Vector Store

SYNOPSIS
       data load [--path the path of the directory or file] --help

OPTIONS
       --path or -p the path of the directory or file
       [Mandatory]

       --help or -h
       help for data load
       [Optional]
```