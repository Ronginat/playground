package ratingplayground.layout.elements;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.stream.Stream;

import javax.annotation.PostConstruct;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import ratingplayground.logic.AppConstants;
import ratingplayground.logic.elements.ElementEntity;
import ratingplayground.logic.elements.ElementService;
import ratingplayground.logic.elements.Location;
import ratingplayground.logic.users.UserEntity;
import ratingplayground.logic.users.UserService;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=WebEnvironment.RANDOM_PORT)
public class ElementControllerTests {
	@LocalServerPort
	private int port;
	private String url;
	private RestTemplate restTemplate;
	private ObjectMapper jsonMapper;
	@Autowired
	private ElementService elementService;
	@Autowired
	private UserService userService;
	
	private final String userPlayground = "ratingplayground";
	private final String emailManager = "demo_manager@gmail.com";
	private final String emailPlayer = "demo_player@gmail.com";
	
	@PostConstruct
	public void init() {
		this.restTemplate = new RestTemplate();
		this.url = "http://localhost:" + port + "/playground/elements/";
		
		// Jackson init
		this.jsonMapper = new ObjectMapper();
	}
	
	@Before
	public void setup() {
		// register a new verified manager
		UserEntity user = new UserEntity();
		user.setPlayground(userPlayground);
		user.setEmail(emailManager);
		user.setRole(AppConstants.MANAGER);
		
		userService.testRegisterUser(user);
		
		// register a new verified player		
		user.setPlayground(userPlayground);
		user.setEmail(emailPlayer);
		user.setRole(AppConstants.PLAYER);
		
		userService.testRegisterUser(user);
	}

	@After
	public void teardown() {
		this.elementService.cleanup();
		this.userService.cleanup();
	}

	@Test
	public void testServerIsBootingCorrectly() throws Exception {
		
	}
	
	//============================================Feature: Create and store new element=======================================
	@Test
	public void testCreateElementSuccessfully()throws Exception{
		// given a user with manager role
		UserEntity manager = getUserForTesting(userPlayground, emailManager);
		
		// given database is empty
		String playground = "ratingplayground";
		String name = "element";
		double x = 10;
		double y = 10;
		
		// when - POST
		ElementTO postElement = new ElementTO();
		postElement.setPlayground(playground);
		postElement.setName(name);
		postElement.setLocation(new Location(x, y));
		
		ElementTO actualReturnedValue = this.restTemplate.postForObject(getUrlWithUserParams(manager), postElement, ElementTO.class);
		
		assertThat(actualReturnedValue)
				.isNotNull()
				.extracting("playground", "name")
				.containsExactly(playground, name);
		
		//save the generated id
		String id = actualReturnedValue.getId();
		postElement.setId(id);
		
		ElementEntity expectedOutcome = postElement.toEntity();
		
		// then
		assertThat(this.elementService.getElementByIdentifiers(null, manager.getPlayground(), manager.getEmail(), playground, id))
		.isNotNull()
		.usingComparator((e1,e2)->{
			int rv = (e1.getPlayground() + e1.getId()).compareTo(e2.getPlayground() + e2.getId());
			if (rv == 0) {
				rv = new Double(e1.getX()).compareTo(e2.getX());
				if (rv == 0) {
					rv = new Double(e1.getY()).compareTo(e2.getY());
				}
			}
			return rv;
		})
		.isEqualTo(expectedOutcome);
	}
	
	@Test(expected=Exception.class)
	public void testCreateElementWithoutManagerRole() throws Exception {
		// given a user with player role
		UserEntity player = getUserForTesting(userPlayground, emailPlayer);
		
		// given database is empty
		String playground = "ratingplayground";
		String name = "element";
		double x = 10;
		double y = 10;
		
		// when - POST
		ElementTO postElement = new ElementTO();
		postElement.setPlayground(playground);
		postElement.setName(name);
		postElement.setLocation(new Location(x, y));
		
		this.restTemplate.postForObject(getUrlWithUserParams(player), postElement, ElementTO.class);
		
	}
	
	@Test(expected=Exception.class)
	public void testCreateElementWithIdInBody() throws Exception{
		// given a user with manager role
		UserEntity manager = getUserForTesting(userPlayground, emailManager);
		
		String playground = "ratingplayground";
		String id = "element";
		
		// given the database is empty
		ElementEntity toAdd = new ElementEntity();
		toAdd.setPlayground(playground);
		toAdd.setId(id);
		
		//this.elementService.addNewElement(manager.getPlayground(), manager.getEmail(), toAdd);
		
		// when - POST
		ElementTO postElement = new ElementTO(toAdd);
		
		this.restTemplate.postForObject(getUrlWithUserParams(manager), postElement, ElementTO.class);
		
		// then response <> 2xx
	}
	
	//============================================Feature: Update an existing element=======================================
	@Test
	public void  testUpdateElementSuccessfully()throws Exception{
		// given a user with manager role
		UserEntity manager = getUserForTesting(userPlayground, emailManager);
		
		String playground = "ratingplayground";
		
		// given the database contains { "id":"1", "playground":"ratingplayground", "x": 10, "y": 10 }

		String entityJson = "{\"playground\":\"ratingplayground\", \"x\": 10, \"y\": 10}";
		
		ElementEntity oldElement = this.jsonMapper.readValue(entityJson, ElementEntity.class);
		
		String id = this.elementService.addNewElement(manager.getPlayground(), manager.getEmail(), oldElement).getId();
		
		// when - PUT
		String elementToString = "{\"id\":\"1\", \"playground\":\"ratingplayground\", \"location\":{\"x\": 5, \"y\": 5}}";
		ElementTO to = this.jsonMapper.readValue(elementToString, ElementTO.class);
		
		this.restTemplate.put(getUrlWithUserParams(manager) + "/{playground}/{id}", to, playground, id);
		
		ElementEntity actualEntity = this.elementService.getElementByIdentifiers(null, manager.getPlayground(), manager.getEmail(), playground, id);
		
		// then
		assertThat(actualEntity)
				.isNotNull()
				.extracting("playground", "id", "x", "y")
				.containsExactly(playground, id, 5.0, 5.0);
		
	}
	
	@Test(expected=Exception.class)
	public void testUpdateElementWithPlayerRole() throws Exception {
		// given a user with player
		UserEntity player = getUserForTesting(userPlayground, emailPlayer);
		// need a manager to create the expired element
		UserEntity manager = getUserForTesting(userPlayground, emailManager);
		
		String playground = "ratingplayground";
		
		// given the database contains { "id":"1", "playground":"ratingplayground", "x": 10, "y": 10 }

		String entityJson = "{\"playground\":\"ratingplayground\", \"x\": 10, \"y\": 10}";
		
		ElementEntity oldElement = this.jsonMapper.readValue(entityJson, ElementEntity.class);
		
		String id = this.elementService.addNewElement(manager.getPlayground(), manager.getEmail(), oldElement).getId();
		
		// when - PUT
		String elementToString = "{\"id\":\"1\", \"playground\":\"ratingplayground\", \"location\":{\"x\": 5, \"y\": 5}}";
		ElementTO to = this.jsonMapper.readValue(elementToString, ElementTO.class);
		
		this.restTemplate.put(getUrlWithUserParams(player) + "/{playground}/{id}", to, playground, id);
		
	}
	
	@Test(expected=Exception.class)
	public void testUpdateElementWithNonExistingElement() throws Exception{
		// given a user with manager role
		UserEntity manager = getUserForTesting(userPlayground, emailManager);
		
		// given the database is empty
		String playground = "ratingplayground";
		String id = "test";
		
		String ElementToString = "{\"id\":\"test\", \"playground\":\"raitingplayground\", \"location\":{\"x\": 5, \"y\": 5}}";
		ElementTO to = this.jsonMapper.readValue(ElementToString, ElementTO.class);
		
		// when - PUT
		this.restTemplate.put(getUrlWithUserParams(manager) + "/{playground}/{id}", to, playground, id);
		
		// then response <> 2xx
	}
	
	//============================================Feature: get specific element=======================================
	@Test
	public void  testGetSpecificElementSuccessfully() throws Exception{
		// given a user with manager role
		UserEntity manager = getUserForTesting(userPlayground, emailManager);
		
		String playground = "ratingplayground";
		String name = "demo";
		
		// given the database contains { "id": "1", "playground":"ratingplayground", "name": "demo" }		
		ElementEntity toAdd = new ElementEntity();
		toAdd.setPlayground(playground);
		toAdd.setName(name);
		
		String id = this.elementService.addNewElement(manager.getPlayground(), manager.getEmail(), toAdd).getId();
		
		// when - GET
		ElementTO actualElement = this.restTemplate.getForObject(getUrlWithUserParams(manager) + "/{playground}/{id}",
				ElementTO.class, playground, id); 
		
		// then
		assertThat(actualElement)
				.isNotNull()
				.extracting("playground", "id", "name")
				.containsExactly(playground, id, name);
	}
	
	@Test(expected=Exception.class)
	public void testGetSpecificExpiredElementWithPlayerRole() throws Exception {
		// given a user with player role
		UserEntity player = getUserForTesting(userPlayground, emailPlayer);
		// need a manager to create the expired element
		UserEntity manager = getUserForTesting(userPlayground, emailManager);
		
		String playground = "ratingplayground";
		String name = "demo";
		
		// given the database contains { "id": "1", "playground":"ratingplayground", "name": "demo" }		
		ElementEntity toAdd = new ElementEntity();
		toAdd.setPlayground(playground);
		toAdd.setName(name);
		toAdd.setExpirationDate(new Date());
		
		String id = this.elementService.addNewElement(manager.getPlayground(), manager.getEmail(), toAdd).getId();
		
		Thread.sleep(50);
		
		// when - GET
		this.restTemplate.getForObject(getUrlWithUserParams(player) + "/{playground}/{id}", ElementTO.class, playground, id); 
	}
	
	@Test(expected=Exception.class)
	public void testGetSpecificMessageWithInvalidName() throws Exception{
		UserEntity manager = getUserForTesting(userPlayground, emailManager);
		
		String playground = "ratingplayground";
		String id = "demo";
		
		// given the database is empty
		
		// when - GET
		this.restTemplate.getForObject(getUrlWithUserParams(manager) + "/{playground}/{id}",
				ElementTO.class, playground, id);
		
		// then the response <> 2xx
	}
	
	//============================================Feature: get all elements=======================================
	@Test
	public void testGetAllElementsSuccessfullyWithNoPaginationParameters()throws Exception {
		// given a user with manager role
		UserEntity manager = getUserForTesting(userPlayground, emailManager);
		
		// given and the database contains [ {"name": "demo1"}, {"name": "demo2"}, {"name": "demo3"} ]
		Stream.of("demo1", "demo2", "demo3")
				.map(ElementEntity::new)
				.forEach(s -> this.elementService.addNewElement(manager.getPlayground(), manager.getEmail(), s));
		
		// when - GET
		ElementTO[] actualElements = this.restTemplate.getForObject(getUrlWithUserParams(manager) + "/all", ElementTO[].class);
		
		// then
		assertThat(actualElements)
				.isNotNull()
				.hasSize(3);
	}
	
	
	@Test(expected=Exception.class)
	public void testGetAllElementsWithFaultPaginationParameters() throws Exception{
		// given a user with manager role
		UserEntity manager = getUserForTesting(userPlayground, emailManager);
		
		// given the database is empty
		
		// when - GET
		this.restTemplate.getForObject(getUrlWithUserParams(manager) + "/all?page={page}", ElementTO[].class, -1);
		
		// then response <> 2xx
	}
	
	//============================================Feature: get all elements in given radius=======================================
	@Test
	public void testGetAllElementsInGivenRadiusSuccessfullyWithNoPaginationParameters() throws Exception{
		// given a user with manager role
		UserEntity manager = getUserForTesting(userPlayground, emailManager);
		
		// given the database contains [ {"x":0, "y":0}, {"x":5,"y":5}, {"x":2, "y":2} ]
		ElementEntity e1 = new ElementEntity();
		e1.setX(0d);
		e1.setY(0d);
		ElementEntity e2 = new ElementEntity();
		e2.setX(5d);
		e2.setY(5d);
		ElementEntity e3 = new ElementEntity();
		e3.setX(2d);
		e3.setY(2d);
		
		Stream.of(e1, e2, e3)
				.forEach(e -> this.elementService.addNewElement(manager.getPlayground(), manager.getEmail(), e));
		
		// when - GET
		ElementTO[] actualElements = this.restTemplate.getForObject(getUrlWithUserParams(manager) + "/near/{x}/{y}/{distance}", ElementTO[].class, 1, 1, 3);
		
		// then
		assertThat(actualElements)
				.isNotNull()
				.hasSize(2);
	}
	
	@Test(expected=Exception.class)
	public void testGetAllElementsInGivenRadiusWithFaultPaginationParameters() throws Exception{
		// given a user with manager role
		UserEntity manager = getUserForTesting(userPlayground, emailManager);
		
		// given the database is empty
		
		// when - GET
		this.restTemplate.getForObject(getUrlWithUserParams(manager) + "/near/{x}/{y}/{distance}?page={page}", ElementTO[].class, 1, 1, 3, -1);
		
		// then response <> 2xx
	}
	
	@Test(expected=Exception.class)
	public void testGetAllElementsInGivenRadiusWithStringInsteadDoubleParameters() throws Exception{
		// given a user with manager role
		UserEntity manager = getUserForTesting(userPlayground, emailManager);
		
		// given the database is empty
		
		// when - GET
		this.restTemplate.getForObject(getUrlWithUserParams(manager) + "/near/{x}/{y}/{distance}", ElementTO[].class, 1, "test", 3);
		
		// then response <> 2xx
	}
	
	//============================================Feature: Search for elements by attribute value=======================================
	@Test
	public void  testGetAllSearchedElementsSuccessfullyWithNoPaginationParameters() throws Exception{
		// given a user with manager role
		UserEntity manager = getUserForTesting(userPlayground, emailManager);

		// given the database contains [ {"name":"movie1"},	{"name":"movie2"}, {"name":"movie3"} ]
		ElementEntity e1 = new ElementEntity();
		e1.setPlayground("ratingplayground");
		e1.setName("movie1");
		ElementEntity e2 = new ElementEntity();
		e2.setPlayground("ratingplayground");
		e2.setName("movie2");
		ElementEntity e3 = new ElementEntity();
		e3.setPlayground("ratingplayground");
		e3.setName("movie3");
		
		Stream.of(e1, e2, e3)
		.forEach(e -> this.elementService.addNewElement(manager.getPlayground(), manager.getEmail(), e));
		
		// when - GET
		ElementTO[] actualElements = this.restTemplate.getForObject(getUrlWithUserParams(manager) + "/search/{attributeName}/{value}", ElementTO[].class, "name", "movie2");
		
		// then
		assertThat(actualElements)
				.isNotNull()
				.hasSize(1);
	}
	
	@Test(expected=Exception.class)
	public void testGetAllSearchedElementsWithFaultPaginationParameters() throws Exception{
		// given a user with manager role
		UserEntity manager = getUserForTesting(userPlayground, emailManager);

		// given the database is empty
		
		// when - GET
		this.restTemplate.getForObject(getUrlWithUserParams(manager) + "/search/{attributeName}/{value}?page={page}", ElementTO[].class, "name", "movie2", -1);
		
		// then response <> 2xx
	}

	@Test(expected=Exception.class)
	public void testGetAllSearchedElementsWithFaultAttributeParameter() throws Exception{
		// given a user with manager role
		UserEntity manager = getUserForTesting(userPlayground, emailManager);

		// given the database is empty
		
		// when - GET
		this.restTemplate.getForObject(getUrlWithUserParams(manager) + "/search/{attributeName}/{value}", ElementTO[].class, "id", "test");
		
		// then response <> 2xx
	}

	
	private UserEntity getUserForTesting(String playground, String email) {
		return this.userService.getUser(playground, email);		
	}
	
	private String getUrlWithUserParams(UserEntity user) {
		return this.url + user.getPlayground() + "/" + user.getEmail();
	}
	
	
	
}
