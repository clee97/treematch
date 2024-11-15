package com.treematch;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.treematch.model.ApiAnswerRequest;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TreeMatchApiIntegrationTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void testBeginOperation() throws Exception {
		var apiResp = mockMvc.perform(get("/api/begin").with(httpBasic("user", "password")));
		assertQuestionStep(apiResp, 1, "Do you have a courtyard, garden, or a farm?", Matchers.containsInAnyOrder("courtyard", "garden", "farm"));
	}

	/**
	 * Tests the basic path
	 * Step: 1, Question: "Do you have a courtyard, garden, or a farm?", Answer: "courtyard"
	 * Step: 2, Question: "Do you like cooking?", Answer: "yes"
	 * Step: 3, Question: "Do you like spicy food?", Answer: "no"
	 * Match: "Bay tree"
	 */
	@Test
	public void testBasicPath() throws Exception {
		var apiResp = callBeginEndpoint();
		assertQuestionStep(apiResp, 1, "Do you have a courtyard, garden, or a farm?", Matchers.containsInAnyOrder("courtyard", "garden", "farm"));

		apiResp = callAnswerEndpoint(1, "courtyard");
		assertQuestionStep(apiResp, 2, "Do you like cooking?", Matchers.containsInAnyOrder("no", "yes"));

		apiResp = callAnswerEndpoint(2, "yes");
		assertQuestionStep(apiResp, 3, "Do you like spicy food?", Matchers.containsInAnyOrder("no", "yes"));

		apiResp = callAnswerEndpoint(3, "no");
		assertResultStep(apiResp, "Bay tree", "A robust small tree with small, fragrant, dark green leaves that are great for use in stews and curries.");
	}

	/**
	 * Tests a bit more complex path
	 * Step: 1, Question: "Do you have a courtyard, garden, or a farm?", Answer: "garden"
	 * Step: 5, Question: "Do you want a fruiting tree?", Answer: "yes"
	 * Step: 6, Question: "On a scale of 1-10, how much do you like citrus?", Answer: "5"
	 * Step: 7, Question: "Are you an awesome cool person?", Answer: "no"
	 * Match: "Orange tree"
	 */
	@Test
	public void testComplexPath() throws Exception {
		var apiResp = callBeginEndpoint();
		assertQuestionStep(apiResp, 1, "Do you have a courtyard, garden, or a farm?", Matchers.containsInAnyOrder("courtyard", "garden", "farm"));

		apiResp = callAnswerEndpoint(1, "garden");
		assertQuestionStep(apiResp, 5, "Do you want a fruiting tree?", Matchers.containsInAnyOrder("no", "yes"));

		apiResp = callAnswerEndpoint(5, "yes");
		assertQuestionStep(apiResp, 6, "On a scale of 1-10, how much do you like citrus?", Matchers.containsInAnyOrder("1", "2", "3", "4", "5", "6", "7", "8", "9", "10"));

		apiResp = callAnswerEndpoint(6, "5");
		assertQuestionStep(apiResp, 7, "Are you an awesome cool person?", Matchers.containsInAnyOrder("no", "yes"));

		apiResp = callAnswerEndpoint(7, "no");
		assertResultStep(apiResp, "Orange tree", "Is the fruit named for the colour or the colour named for the fruit?");
	}

	private ResultActions callBeginEndpoint() throws Exception {
		var mockBasicAuth = httpBasic("user", "password");
		return mockMvc.perform(get("/api/begin").with(mockBasicAuth));
	}

	private ResultActions callAnswerEndpoint(long stepId, @NonNull String answer) throws Exception {
		var mockBasicAuth = httpBasic("user", "password");
		return mockMvc.perform(
			post("/api/answer")
				.with(mockBasicAuth)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(new ApiAnswerRequest(stepId, answer)))
		);
	}

	private void assertResultStep(@NonNull ResultActions apiResp, @NonNull String expectedName, @NonNull String expectedDescription) throws Exception {
		apiResp.andExpect(status().isOk())
			.andExpect(jsonPath("$.match").exists())
			.andExpect(jsonPath("$.match.name").value(expectedName))
			.andExpect(jsonPath("$.match.description").value(expectedDescription));
	}

	private void assertQuestionStep(
		@NonNull ResultActions apiResp,
		long expectedStepId,
		@NonNull String expectedQuestion,
		@NonNull Matcher <Iterable<? extends String>> expectedAnswers
	) throws Exception {
		apiResp.andExpect(status().isOk())
			.andExpect(jsonPath("$.question").exists())
			.andExpect(jsonPath("$.question.step_id").value(expectedStepId))
			.andExpect(jsonPath("$.question.question").value(expectedQuestion))
			.andExpect(jsonPath("$.question.answers").isArray())
			.andExpect(jsonPath("$.question.answers", expectedAnswers));
	}

}
