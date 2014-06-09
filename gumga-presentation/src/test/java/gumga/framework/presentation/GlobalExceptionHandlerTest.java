package gumga.framework.presentation;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import gumga.framework.presentation.controller.ThrowCustomExceptionsController;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { SpringConfig.class })
@WebAppConfiguration
public class GlobalExceptionHandlerTest {

	private MockMvc mockMvc;

	@Mock
	private ThrowCustomExceptionsController fakeController;

	@Resource
	private WebApplicationContext context;

	@Before
	public void setup() {
		MockitoAnnotations.initMocks(this);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
	}

	@Test
	public void should_return_401_status_on_throw_a_unauthorizedException() throws Exception {
		this.mockMvc.perform(get("/teste/401").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isUnauthorized()) //
				.andExpect(MockMvcResultMatchers.jsonPath("$.code", is("Unauthorized"))) //
				.andExpect(MockMvcResultMatchers.jsonPath("$.details", is("Message defined on unauthorized exception"))); //
	}

	@Test
	public void should_return_404_status_on_throw_a_NotFoundException() throws Exception {
		this.mockMvc.perform(get("/teste/404").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isNotFound()) //
				.andExpect(MockMvcResultMatchers.jsonPath("$.code", is("ResourceNotFound"))) //
				.andExpect(MockMvcResultMatchers.jsonPath("$.details", is("Message defined on notFound exception"))); //
	}

	@Test
	public void should_return_403_status_on_throw_a_ForbiddenException() throws Exception {
		this.mockMvc.perform(get("/teste/403").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isForbidden()) //
				.andExpect(MockMvcResultMatchers.jsonPath("$.code", is("Forbidden"))) //
				.andExpect(MockMvcResultMatchers.jsonPath("$.details", is("Message defined on forbidden exception"))); //
	}

	@Test
	public void should_return_409_status_on_throw_a_ConflictException() throws Exception {
		this.mockMvc.perform(get("/teste/409").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)) //
				.andExpect(status().isConflict()) //
				.andExpect(MockMvcResultMatchers.jsonPath("$.code", is("Conflict"))) //
				.andExpect(MockMvcResultMatchers.jsonPath("$.details", is("Message defined on conflict exception"))); //
	}

}
