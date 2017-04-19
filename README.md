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
To only run the java tests:
```bash
mvn test
```
The frontend for the application requires node/npm to build. 
A compiled app.js is included in this repo so you shouldn't need to do anything.

## Developing
Java application uses standard maven project.
The frontend is a node/npm project and lives at:
`$PROJECT_ROOT/src/main/resources/assets`

## Built with

## Next:

## Contributing

## Authors

## Acknowledgements