package com.training.microservices;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.training.microservices.exception.EmployeeNotFoundException;
import com.training.microservices.exception.InternalServerErrorException;
import com.training.microservices.model.Credentials;
import com.training.microservices.model.Employee;
import com.training.microservices.model.StatusRepo;
import com.training.microservices.service.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestEmployeeController {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private EmployeeService employeeService;
	
	StatusRepo value;
	Employee e1 = new Employee();
	Employee e2 = new Employee();
	
	@Before
	public void setup(){
		
		e1.setUserId(1);
		e1.setUsername("anthony1");
		e1.setPassword("anthony");
		e1.setFullName("Anthony DSouza");
		e1.setEmail("a.d@email.com");
		e1.setDob("18dec1993");
		e1.setGender("male");
		e1.setSecurityQuestion("what is your favourite place?");
		e1.setSecurityAnswer("Singapore");
		
		e2.setUserId(2);
		e2.setUsername("neha");
		e2.setPassword("neha123");
		e2.setFullName("Neha Sinha");
		e2.setEmail("neha.sinha@email.com");
		e2.setDob("18may1993");
		e2.setGender("female");
		e2.setSecurityQuestion("what is your favourite language?");
		e2.setSecurityAnswer("Java");

		Employee[] allEmp = new Employee[]{e1, e2};
		
		value = new StatusRepo("Success!", "Details of all employees.", allEmp);
	}
	
	@Test
	public void getAllEmployeeControllerSuccessTest() throws Exception {
		
		Mockito.when(employeeService.getAllEmployeeService()).thenReturn(value);
		
		mockMvc.perform(MockMvcRequestBuilders.get("/EmpMgt/getAllEmpDetails").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(content().json("{\"status\":\"Success!\",\"message\":\"Details of all employees.\","
				+ "\"data\":[{\"userId\":1,\"username\":\"anthony1\",\"password\":\"anthony\","
				+ "\"fullName\":\"Anthony DSouza\",\"email\":\"a.d@email.com\",\"dob\":\"18dec1993\","
				+ "\"gender\":\"male\",\"securityQuestion\":\"what is your favourite place?\","
				+ "\"securityAnswer\":\"Singapore\"},{\"userId\":2,\"username\":\"neha\","
				+ "\"password\":\"neha123\",\"fullName\":\"Neha Sinha\",\"email\":\"neha.sinha@email.com\","
				+ "\"dob\":\"18may1993\",\"gender\":\"female\","
				+ "\"securityQuestion\":\"what is your favourite language?\","
				+ "\"securityAnswer\":\"Java\"}]}"));
	}

	@Test
	public void getAllEmployeeControllerFailureTest() throws Exception {
		
		Mockito.when(employeeService.getAllEmployeeService()).thenThrow(new EmployeeNotFoundException("No Employee Found"));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/EmpMgt/getAllEmpDetails").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isBadRequest())
		.andExpect(content().json("{\"status\":\"Failed!\",\"message\":\"No Employee Found\","
				+ "\"data\":null}"));
	}
	@Test
	public void getEmployeeControllerSuccessTest() throws Exception {
		
		Mockito.when(employeeService.getEmployeeService(Matchers.anyInt())).thenReturn(new StatusRepo("Success!", "Details of employee.", new Employee[]{e1}));
	
		mockMvc.perform(MockMvcRequestBuilders.get("/EmpMgt/getByEmpId/1")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("{\"status\":\"Success!\",\"message\":\"Details of employee.\","
											+ "\"data\":[{\"userId\": 1,\"username\":\"anthony1\","
											+ "\"password\":\"anthony\",\"fullName\":\"Anthony DSouza\","
											+ "\"email\":\"a.d@email.com\",\"dob\":\"18dec1993\","
											+ "\"gender\":\"male\",\"securityQuestion\":\"what is your favourite place?\","
											+ "\"securityAnswer\":\"Singapore\"}]}"));
	}
	
	@Test
	public void getEmployeeControllerFailureTest() throws Exception {
		
		Mockito.when(employeeService.getEmployeeService(Matchers.anyInt())).thenThrow(new EmployeeNotFoundException("No Employee Found"));
		
		mockMvc.perform(MockMvcRequestBuilders.get("/EmpMgt/getByEmpId/20").accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest()).andExpect(content().json("{\"status\": \"Failed!\",\"message\": \"No Employee Found\",\"data\": null}"));
	}
	
	@Test
	public void addEmployeeControllerSuccessTest() throws Exception {
		
		Mockito.when(employeeService.addEmployeeService(Matchers.any(Employee.class)))
				.thenReturn(new StatusRepo("Success!", "Employee added.", new Employee[]{e1}));
		
		mockMvc.perform(MockMvcRequestBuilders.put("/EmpMgt/addEmp").contentType(MediaType.APPLICATION_JSON).content("{\"username\":\"anthony1\","
											+ "\"password\":\"anthony\",\"fullName\":\"Anthony DSouza\","
											+ "\"email\":\"a.d@email.com\",\"dob\":\"18dec1993\","
											+ "\"gender\":\"male\",\"securityQuestion\":\"what is your favourite place?\","
											+ "\"securityAnswer\":\"Singapore\"}").accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
				.andExpect(content().json("{\"status\":\"Success!\",\"message\":\"Employee added.\","
											+ "\"data\":[{\"userId\": 1,\"username\":\"anthony1\","
											+ "\"password\":\"anthony\",\"fullName\":\"Anthony DSouza\","
											+ "\"email\":\"a.d@email.com\",\"dob\":\"18dec1993\","
											+ "\"gender\":\"male\",\"securityQuestion\":\"what is your favourite place?\","
											+ "\"securityAnswer\":\"Singapore\"}]}"));
	}
	
	@Test
	public void addEmployeeControllerFailureTest() throws Exception {
		Mockito.when(employeeService.addEmployeeService(Matchers.any(Employee.class))).thenThrow(new InternalServerErrorException("Internal Server Error"));
		
		mockMvc.perform(MockMvcRequestBuilders.put("/EmpMgt/addEmp").contentType(MediaType.APPLICATION_JSON).content("{\"password\":\"12345\",\"fullName\":\"\",\"email\":\"pam.s@email.com\",\"dob\":\"Apri 5 1889\",\"gender\":\"female\",\"securityQuestion\":\"what is your favourite food?\",\"securityAnswer\":\"biryani\"}").accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isInternalServerError()).andExpect(content().json("{\"status\": \"Failed!\",\"message\": \"There is some issue at server side. Please check the log.\",\"data\": null}"));
	}
	
	@Test
	public void deleteEmployeeControllerSuccessTest() throws Exception {
		Mockito.when(employeeService.deleteEmployeeService(Matchers.anyInt())).thenReturn(new StatusRepo("Success","Employee deleted successfully!",null));
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/EmpMgt/deleteEmp/1")).andExpect(status().isOk()).andExpect(content().json("{\"status\":\"Success\",\"message\":\"Employee deleted successfully!\",\"data\":null}"));
	}
	
	@Test
	public void deleteEmployeeControllerFailureTest() throws Exception {
		
		Mockito.when(employeeService.deleteEmployeeService(Matchers.anyInt())).thenThrow(new EmployeeNotFoundException("No Employee Found"));
		
		mockMvc.perform(MockMvcRequestBuilders.delete("/EmpMgt/deleteEmp/1")).andExpect(status().isBadRequest()).andExpect(content().json("{\"status\":\"Failed!\",\"message\":\"No Employee Found\",\"data\":null}"));
	}
	
	@Test
	public void checkLoginControllerSuccessTest() throws Exception {
		Mockito.when(employeeService.checkLoginService(Matchers.any(Credentials.class)))
				.thenReturn(new StatusRepo("Success!", "Employee has authenticated successfully", null));
		mockMvc.perform(MockMvcRequestBuilders.post("/EmpMgt/checkLogin")
				.contentType(MediaType.APPLICATION_JSON).content("{\"user\":\"anthony1\",\"pass\":\"anthony\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
		.andExpect(content().json("{\"status\":\"Success!\",\"message\":\"Employee has authenticated successfully\",\"data\":null}"));
	}
	
	@Test
	public void checkLoginControllerFailureTest() throws Exception {
		Mockito.when(employeeService.checkLoginService(Matchers.any(Credentials.class)))
		.thenThrow(new InternalServerErrorException("Internal Server Error"));
		
		mockMvc.perform(MockMvcRequestBuilders.post("/EmpMgt/checkLogin")
				.contentType(MediaType.APPLICATION_JSON).content("{\"user\":\"anthon\",\"pass\":\"anthony\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError())
		.andExpect(content().json("{\"status\":\"Failed!\",\"message\":\"There is some issue at server side. Please check the log.\",\"data\":null}"));
	}
}
