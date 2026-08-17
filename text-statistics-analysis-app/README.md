# Text Analysis Tool
A web application for searching and analysing large collections of text. It supports text search, frequency analysis, n-gram analysis, and document/project management.
Built with React, TypeScript, Java, Spring Boot.
This project was made mainly as an exercise to familiarise myself with React and TypeScript.


## Features
-Create and manage projects
-Upload and manage .txt files
-Files can be assigned to projects
-Use regular expressions to search for setences in project files
-Highlight search matches in sentences
-Character, word, bigram and trigram counts for search results


## Screenshots

### Project management page

### Project page

### Search page


## Architecture

### Front end
React and TypeScript

### back end
Spring Boot and Java


## Requirements
- Java 21+
- Node.js 24+
- npm

Currently uses mock DAOs with no database.


## Running the application
cd backend
./mvnw spring-boot:run
cd frontend
npm install
npm run dev
Frontend: http://localhost:5173
Backend:  http://localhost:8080


## Furture addtions
-database for data storage


## potential additions
-search result paging
-users and authentication
-input validation
-additional statistics
-comparing documents