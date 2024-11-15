### Running TreeMatch API
Fastest way is to open and run the project in the Intellij IDE
1. Ensure Java 17 is installed inside the IDE
    - File -> Settings -> Project Structure -> SDKs -> Click '+' to add Java 17
2. Run the `TreeMatchApplication.java` file inside Intellij as a normal Java application using the Java 17
3. The API should now be available via the URL `http://localhost:8092`

### Calling Endpoints
As required by the specifications, there are 2 endpoints available for consumption.
1. `GET http://localhost:8092/api/begin`
2. `POST http://localhost:8092/api/answer`
   - Sample body: `{ "step_id": 1, "answer": "courtyard" }` 

Authentication for this API has been kept simple and is using basic auth. 

For the endpoints to be consumable, add the header `Authorization: Basic dXNlcjpwYXNzd29yZA==`

### Tests
Some integration tests have been provided as part of this task to verify the correctness of the API.

Run the test `TreeMatchApiIntegrationTest.java` for some test cases that verify the tree matching correctness 

Run the test `QuizJsonLoaderServiceTest.java` for some test cases that verify the validity and correctness of the `questions.json` file. 

There are 2 extra files provided `questions-invalid-ids.json` and `questions-cycle.json` that assist with the validity testing
