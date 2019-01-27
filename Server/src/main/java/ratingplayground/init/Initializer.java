package ratingplayground.init;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import ratingplayground.logic.AppConstants;
import ratingplayground.logic.elements.ElementEntity;
import ratingplayground.logic.elements.ElementService;
import ratingplayground.logic.users.UserEntity;
import ratingplayground.logic.users.UserService;

@Component
@Profile("demo")
public class Initializer {
	private UserService userService;
	private ElementService elementService;

	
	@Autowired
	public Initializer(UserService userService, ElementService elementService) {
		super();
		this.userService = userService;
		this.elementService = elementService;
	}
	
	@PostConstruct
	public void init() {
		
		String playground = "ratingplayground";
		
		// Add a manager
		this.userService.testRegisterUser(new UserEntity("manager@walla.com", playground, "manager", "dummy", AppConstants.MANAGER, 0));
		
		
		// Add player
		this.userService.testRegisterUser(new UserEntity("player@walla.com", playground, "player", "mr. dummy", AppConstants.PLAYER, 0));
		
		// Add a movie
		Map<String, Object> attribues = new HashMap<>();
		attribues.put("image", "https://cdn.brainpop.com/socialstudies/worldhistory/titanic/screenshot1.png");
		
		this.elementService.addNewElement(playground, "manager@walla.com", 
				new ElementEntity(playground, null, 0.0, 0.0, "Titanic", new Date(),
				null, "movie", attribues, playground, "manager@walla.com"));
		
		// Add a movie
		attribues.put("image", "https://images-na.ssl-images-amazon.com/images/I/51wcLP6H6nL._SY300_QL70_.jpg");
		attribues.put("info", "http://rickriordan.com/book/the-lightning-thief/");
		this.elementService.addNewElement(playground, "manager@walla.com", 
				new ElementEntity(playground, null, 0.0, 0.0, "Lightning Thief", new Date(),
				null, "book", attribues, playground, "manager@walla.com"));
		
		// Add a book
		/*attribues.put("image", "https://prodimage.images-bn.com/pimages/9781338099133_p0_v5_s550x406.jpg");
		attribues.put("info", "https://en.wikipedia.org/wiki/Harry_Potter_and_the_Cursed_Child");
		this.elementService.addNewElement(playground, "manager@walla.com", 
				new ElementEntity(playground, null, 0.0, 0.0, "Harry Potter play", new Date(),
				null, "book", attribues, playground, "manager@walla.com"));*/
		
		// Add a movie
		attribues.put("image", "https://resizing.flixster.com/GeCymI5724T-lDfHZO_K8X0Nk4A=/300x300/v1.aDs2NTk3O2o7MTc5NTU7MTIwMDs0OTA7NzAw");
		attribues.put("info", "https://www.rottentomatoes.com/m/the_lord_of_the_rings_the_return_of_the_king/");
		this.elementService.addNewElement(playground, "manager@walla.com", 
				new ElementEntity(playground, null, 0.0, 0.0, "The Return of the King", new Date(),
				null, "book", attribues, playground, "manager@walla.com"));
		
		
		
		// Add a manager
		this.userService.testRegisterUser(new UserEntity("ratingtest123@walla.com", playground, "playa", "", AppConstants.PLAYER, 0));
		
		this.userService.testRegisterUser(new UserEntity("managerEmail@gmail.com", playground, "snow white", "", AppConstants.MANAGER, 0));

		// Add player
		this.userService.testRegisterUser(new UserEntity("playerEmail@gmail.com", playground, "nevermind", "", AppConstants.PLAYER, 0));
		
		
		// Add a message board
		Map<String, Object> attributes = new HashMap<String,Object>();
		attributes.put("image", "https://newsfeed.org/wp-content/uploads/Newsfeed-logo.png");
		this.elementService.addNewElement(playground, "managerEmail@gmail.com", 
				new ElementEntity(playground, null, 0.0, 0.0, "Message Board", new Date(),
				null, "MessageBoard", attributes, playground, "managerEmail@gmail.com"));
		// Add a movie
		attributes.put("image", "http://www.infamousinspiration.com/wp-content/uploads/2017/06/Iron-Man-The-Avengers-Poster-2-300x300.jpg");
		attributes.put("info", "https://www.imdb.com/title/tt0371746/");
		this.elementService.addNewElement(playground, "managerEmail@gmail.com", 
				new ElementEntity(playground, null, 0.0, 0.0, "Iron Man", new Date(),
				null, "movie", attributes, playground, "managerEmail@gmail.com"));
		
		// Add a book
		attributes.put("image", "https://mybooksphere.com/wp-content/uploads/2018/09/51HSkTKlauL._SX346_BO1204203200_.jpg");
		attributes.put("info", "https://en.wikipedia.org/wiki/Harry_Potter_and_the_Philosopher%27s_Stone");
		this.elementService.addNewElement(playground, "managerEmail@gmail.com", 
				new ElementEntity(playground, null, 0.0, 0.0, "Harry Potter", new Date(),
				null, "book", attributes, playground, "managerEmail@gmail.com"));
		
	}

}
