package com.training.microservices;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import com.training.microservices.DAO.EmployeeDAO;
import com.training.microservices.exception.EmployeeNotFoundException;
import com.training.microservices.exception.InternalServerErrorException;
import com.training.microservices.model.Credentials;
import com.training.microservices.model.Employee;
import com.training.microservices.model.StatusRepo;
import com.training.microservices.service.EmployeeService;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TestEmployeeService {

	@TestConfiguration
	static class TestEmployeeServiceContextConfiguration {
		@Bean
		public EmployeeService employeeService() {
			return new EmployeeService();
		}
	}
	
	@Autowired
	private EmployeeService employeeService;
	
	@MockBean
	private EmployeeDAO employeeDAO;
	
	Employee e1 = new Employee();
	Employee e2 = new Employee();
	
	StatusRepo statusRepo;
	List<Employee> employeeList = new ArrayList<>();
	
	Credentials cred = new Credentials();;
	
	@Before
	public void setUp() {
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

		employeeList.add(e1);
		employeeList.add(e2);
		Mockito.when(employeeDAO.findAll()).thenReturn(employeeList);
		Optional<Employee> e = Optional.of(e1);
		Mockito.when(employeeDAO.findById(Matchers.anyInt())).thenReturn(e);
		Mockito.when(employeeDAO.save(Matchers.any(Employee.class))).thenReturn(e1);
		
		cred.setUser("anthony1");
		cred.setPass("anthony");
	}
	
	Employee[] employeeArray = new Employee[]{e1, e2};
	
	@Test
	public void getAllEmployeeServiceSuccessTest() {
		StatusRepo statusRepoExpected = new StatusRepo("Success!","Details of all employees.",employeeArray);
		StatusRepo statusRepoResult = employeeService.getAllEmployeeService();
		assertEquals(statusRepoExpected.getMessage(), statusRepoResult.getMessage());
		assertEquals(statusRepoExpected.getStatus(), statusRepoResult.getStatus());
		assertArrayEquals(statusRepoExpected.getData(), statusRepoResult.getData());
	}
	
	@Test(expected=EmployeeNotFoundException.class)
	public void getAllEmployeeServiceFailureTest() throws Exception {
		doThrow(new EmployeeNotFoundException("No Employee Found")).when(employeeDAO).findAll();
		
		employeeService.getAllEmployeeService();
	}
	
	@Test
	public void getEmployeeServiceSuccessTest() {
		StatusRepo statusRepoExpected = new StatusRepo("Success!", "Details of employee.",new Employee[]{e1});
		int userId = 1;
		assertEquals(statusRepoExpected.getMessage(), employeeService.getEmployeeService(userId).getMessage());
		assertEquals(statusRepoExpected.getStatus(), employeeService.getEmployeeService(userId).getStatus());
		assertArrayEquals(statusRepoExpected.getData(), employeeService.getEmployeeService(userId).getData());
	}
	
	@Test(expected=EmployeeNotFoundException.class)
	public void getEmployeeServiceFailureTest() {
		
		doThrow(new EmployeeNotFoundException("No Employee Found")).when(employeeDAO).findById(Matchers.anyInt());
		employeeService.getEmployeeService(100);
	}
	
	@Test
	public void addEmployeeServiceSuccessTest() {
		StatusRepo statusRepoExpected = new StatusRepo("Success!", "Employee added.",new Employee[]{e1});
		assertEquals(statusRepoExpected.getMessage(), employeeService.addEmployeeService(e1).getMessage());
		assertEquals(statusRepoExpected.getStatus(), employeeService.addEmployeeService(e1).getStatus());
		assertArrayEquals(statusRepoExpected.getData(), employeeService.addEmployeeService(e1).getData());
	}
	
	@Test(expected = InternalServerErrorException.class)
	public void addEmployeeServiceFailureTest() {
		doThrow(new InternalServerErrorException("Internal Server Error")).when(employeeDAO).save(Matchers.any(Employee.class));
		employeeService.addEmployeeService(e1);
	}
	
	@Test
	public void deleteEmployeeServiceSuccessTest() {
		doNothing().when(employeeDAO).deleteById(Matchers.anyInt());
		StatusRepo statusRepoExpected = new StatusRepo("Success", "Employee deleted successfully!", null);
		assertEquals(statusRepoExpected.getMessage(), employeeService.deleteEmployeeService(1).getMessage());
		assertEquals(statusRepoExpected.getStatus(), employeeService.deleteEmployeeService(1).getStatus());
		assertArrayEquals(statusRepoExpected.getData(), employeeService.deleteEmployeeService(1).getData());
	}
	
	@Test(expected = EmployeeNotFoundException.class)
	public void deleteEmployeeServiceFailureTest() {
		doThrow(new EmployeeNotFoundException("No Employee Found")).when(employeeDAO).deleteById(Matchers.anyInt());
		employeeService.deleteEmployeeService(100);
	}
	
	@Test
	public void checkLoginServiceSuccessTest() {
		Mockito.when(employeeDAO.findEmployeeByUsername(Matchers.anyString())).thenReturn(e1);
		StatusRepo expected =  new StatusRepo("Success!", "Employee has authenticated successfully", null);
		StatusRepo result = employeeService.checkLoginService(cred);
		assertEquals(expected.getMessage(), result.getMessage());
		assertEquals(expected.getStatus(), result.getStatus());
		assertArrayEquals(expected.getData(), result.getData());
	}
	
	@Test(expected = InternalServerErrorException.class)
	public void checkLoginServiceFailureTest() {
		doThrow(new InternalServerErrorException("Internal Server Error"))
			.when(employeeDAO).findEmployeeByUsername(Mockito.anyString());
		
		employeeService.checkLoginService(cred);
	}
	
	@Test
	public void checkLoginServiceWrongDataTest() {
		
		StatusRepo expected = new StatusRepo("Failure!", "Internal Server Error.", null);
		cred = null;
		StatusRepo result = employeeService.checkLoginService(cred);
		
		assertEquals(expected.getMessage(), result.getMessage());
		assertEquals(expected.getStatus(), result.getStatus());
		assertArrayEquals(expected.getData(), result.getData());
	}
}









