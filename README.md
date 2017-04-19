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

Currently, the application uses a set of integration tests to check everything is working. 
Having finer-grained unit tests is a bit overkill for such trivial application code (as most functionality is provided by combining elements of the frameworks and libs).
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
The app currently uses an in-memory DB which will drop all data when the app is closed.

## Developing
Java application uses a standard maven project which packages the entire app as a fat jar as per Dropwizard recommendations.

The frontend is a node/npm project and lives at:
`$PROJECT_ROOT/src/main/resources/assets`

Run `npm start` from there to start watching .js files for changes.

## Built with
### Dropwizard - <http://www.dropwizard.io/1.1.0/docs/>
### Guice via dropwizard-guicey - <https://github.com/xvik/dropwizard-guicey>
### Hibernate via dropwizard-hibernate - <http://www.dropwizard.io/1.1.0/docs/manual/hibernate.html>
### Mithril.js - <https://mithril.js.org/>
### Bootstrap v4 alpha - <https://v4-alpha.getbootstrap.com/>

## Ideas for improvements:
- Refactor some of the code that deals with maintaining the priority of Todos out into a service and test this logic separately.
- Add support for multiple lists of Todos.
- Bring in config profiles to start the app different in tests/dev/prod (assets bundle/location for example)
- Implement a js test framework for the frontend.
- Hook up maven and node to work nicely together (running npm start script etc)
- Create a config for a persistent DB.
- Setup some nice logging.
## Contributing

## Authors
- **Simon Casey** - Initial work - <https://github.com/simoncasey> 

## Acknowledgements