# Text-statistics-analysis-app
A web application for searching and analysing large collections of text. It supports text search, frequency analysis, n-gram analysis, and document/project management. Built with React, TypeScript, Java, Spring Boot. This project was made mainly as an exercise to familiarise myself with React and TypeScript.

## Features
-Create and manage projects 
-Upload and manage .txt files 
-Files can be assigned to projects 
-Use regular expressions to search for setences in project files 
-Highlight search matches in sentences 
-Character, word, bigram and trigram counts for search results

## Screenshots
### Project management page
<img width="1901" height="939" alt="image" src="https://github.com/user-attachments/assets/9736f4ff-c3ec-4aef-8194-ea1a8a7897bc" />

### Project page
<img width="1901" height="938" alt="image" src="https://github.com/user-attachments/assets/e4d10eac-5636-40eb-a7d2-1256c8191517" />

### Search page
<img width="1909" height="941" alt="image" src="https://github.com/user-attachments/assets/b9ba9450-f3ca-43ac-8db6-bb3f21683513" />

## Architecture
### Front end
React and TypeScript

### back end
Spring Boot and Java

## Requirements
Java 21+
Node.js 24+
npm
Currently uses mock DAOs with no database.

## Running the application
cd text-statistics-analysis-app/backend 
./mvnw spring-boot:run 
cd text-statistics-analysis-app/src
npm install 
npm run dev 
Frontend: http://localhost:5173 
Backend: http://localhost:8080

## Furture addtions
-database for data storage

## potential additions
-search result paging 
-users and authentication 
-input validation 
-additional statistics 
-comparing documents
