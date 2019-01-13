package ratingplayground.layout.elements;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ratingplayground.logic.InputValidation;
import ratingplayground.logic.elements.ElementService;


@CrossOrigin
@RestController
public class ElementController {
	private ElementService elementService;
	
	@Autowired
	public void setElementService(ElementService elementService) {
		this.elementService = elementService;
	}

	
////////////////////// [ GET ] ////////////////////////////////////////
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/{playground}/{id}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO getElement (
			@PathVariable("userPlayground") String userPlayground ,
			@PathVariable("email") String email,
			@PathVariable("playground") String playground,
			@PathVariable("id") String id) throws Exception {
		
		return new ElementTO(this.elementService.getElementByIdentifiers(null, userPlayground, email, playground, id));
	}

	
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/all",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] getAllElements (
			@PathVariable("userPlayground") String userPlayground ,
			@PathVariable("email") String email,
			@RequestParam(name="size", required=false, defaultValue="10")int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) throws Exception {
		
		//InputValidation.validateEmailAddress(email);
		
		return this.elementService.getAllElements(null, userPlayground, email, size, page)
				.stream()
				.map(ElementTO::new)
				.collect(Collectors.toList())
				.toArray(new ElementTO[0]);
	}
	
	
	
	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/near/{x}/{y}/{distance}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] findNearestElementsByDistance (
			@PathVariable("userPlayground") String userPlayground ,
			@PathVariable("email") String email,
			@PathVariable("x") double x,
			@PathVariable("y") double y,
			@PathVariable("distance") double distance,
			@RequestParam(name="size", required=false, defaultValue="10")int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) throws Exception {
		
//		InputValidation.validateEmailAddress(email);

		return this.elementService.getNearestElementsByDistance(null, userPlayground, email, x, y, distance, size, page)
				.stream()
				.map(ElementTO::new)
				.collect(Collectors.toList())
				.toArray(new ElementTO[0]);
	}
	
	

	@RequestMapping(
			method=RequestMethod.GET,
			path="/playground/elements/{userPlayground}/{email}/search/{attributeName}/{value}",
			produces=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO[] searchElements (
			@PathVariable("userPlayground") String userPlayground ,
			@PathVariable("email") String email,
			@PathVariable("attributeName") String attributeName,
			@PathVariable("value") String value,
			@RequestParam(name="size", required=false, defaultValue="10")int size,
			@RequestParam(name="page", required=false, defaultValue="0") int page) throws Exception {
		
//		InputValidation.validateEmailAddress(email);

		return this.elementService.getElementsByAttribute(null, userPlayground, email, attributeName, value, size, page)
				.stream()
				.map(ElementTO::new)
				.collect(Collectors.toList())
				.toArray(new ElementTO[0]);
	}
	
	
	
	////////////////////// [ POST ] ////////////////////////////////////////
	
	@RequestMapping(
			method=RequestMethod.POST,
			path="/playground/elements/{userPlayground}/{email}",
			produces=MediaType.APPLICATION_JSON_VALUE,
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public ElementTO createNewElement (
			@RequestBody ElementTO elementTO,
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email) throws Exception {
		
//		InputValidation.validateEmailAddress(email);

		return new ElementTO(this.elementService.addNewElement(userPlayground, email, elementTO.toEntity()));
	}
	
	
	////////////////////// [ PUT ] ////////////////////////////////////////

	@RequestMapping(
			method=RequestMethod.PUT,
			path="/playground/elements/{userPlayground}/{email}/{playground}/{id}",
			consumes=MediaType.APPLICATION_JSON_VALUE)
	public void updateElement (
			@PathVariable("userPlayground") String userPlayground,
			@PathVariable("email") String email,
			@PathVariable("playground") String playground,
			@PathVariable("id") String id,
			@RequestBody ElementTO elementTO) throws Exception {
	
//		InputValidation.validateEmailAddress(email);
		
		this.elementService.updateElement(userPlayground, email, playground, id, elementTO.toEntity());
	}
	
	
}
