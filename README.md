# todo-dw

A todo (yes another) app to practice/demo new frameworks etc.

## How to run

The application is java-based and uses maven to build/run.

To compile (from project root):
```bash
mvn clean package
```
To then run (from project root):
```bash
java -jar target/todo-dw-1.0-SNAPSHOT.jar server todo-dw.yml
```
The application is then accessed at <http://localhost:8080/>

To only run the java tests:
```bash
mvn test
```
The frontend for the application requires node/npm to build. 
A compiled app.js is included in this repo so you shouldn't need to do anything.

## Usage
The input box will create a new Todo with the summary you type in.
The list of Todos is sorted with most important at the top to least important at the bottom.
When created, a Todo is added to the top of the list (most important). 
Use the up/down arrows on left to change the priority of a Todo and move it within the list.
Toggling the checkbox on the right will change the Complete/Incomplete status of the Todo.
Clicking on the summary of a Todo will open a modal to allow you to edit the summary and add an optional description to the Todo.

## Developing
Java application uses standard maven project.
The frontend is a node/npm project and lives at:
`$PROJECT_ROOT/src/main/resources/assets`

## Built with

## Next:

## Contributing

## Authors

## Acknowledgements