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

### Additional Considerations
1. Some unit tests of the services with a mocking framework like mockito could definitely be added in addition to the existing integration tests
2. Request and Response logging of each http request should definitely be added in a production API environment. 
We can achieve this by using Spring's in-built `CommonsRequestLoggingFilter` or provide our own custom implementation by implementing a Spring filter
3. Distributed tracing using a correlation id can be added as part of the logging to identify request and response pairs
4. In a production environment with many quizzes, questions and steps to support, a relational database such as MySQL or PostgresSQL should be used to persist the elements inside `questions.json`.
This should also be paired with Hibernate as a persistence provider with corresponding entity and repository classes to facilitate the fetching and mapping of data programmatically
5. For a more secure API, JWT tokens can be used as the auth replacement.