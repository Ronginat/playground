package ratingplayground.logic.elements.stubs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Service;

import ratingplayground.logic.elements.ElementEntity;
import ratingplayground.logic.elements.ElementService;

//@Service
public class DummyElementService implements ElementService{
	private Map<String, ElementEntity> elements;
	private String delimiter = "@@";

	@PostConstruct
	public void init() {
		this.elements = Collections.synchronizedMap(new HashMap<>());
	}
	
	@Override
	public ElementEntity getElementByIdentifiers(Boolean isManager, String userPlayground, String email, String playground, String id) {
		ElementEntity rv = this.elements.get(playground + delimiter + id);
		if(rv == null) {
			throw new RuntimeException("getElementByIdentifiers");
		}
		return rv;
	}

	@Override
	public List<ElementEntity> getAllElements(Boolean isManager, String userPlayground, String email, int size, int page) {
		return new ArrayList<>(
				this.elements.values())
				.stream()
				.skip(size * page)
				.limit(size)
				.collect(Collectors.toList());
	}

	@Override
	public List<ElementEntity> getNearestElementsByDistance(Boolean isManager, String userPlayground, String email, double x, double y, double distance, int size, int page) {
		return new ArrayList<>(this.elements.values())
				.stream()
				.filter(e -> calcDistance(e.getX(), e.getY(), x, y) <= distance)
				.skip(size * page)
				.limit(size)
				.collect(Collectors.toList());
	}

	@Override
	public List<ElementEntity> getElementsByAttribute(Boolean isManager, String userPlayground, String email, String attribute, String value, int size, int page) {
		if(value == null || attribute == null) {
			throw new RuntimeException("getElementByAttribute - null values");
		}
		if(!attribute.equals("name") && !attribute.equals("type")) {
			throw new RuntimeException("getElementByAttribute - attribute not supported");
		}
		else {
			return new ArrayList<>(this.elements.values())
					.stream()
					.filter(e -> {
						switch (attribute) {
						case "name":
							return value.equals(e.getName());				
						case "type":
							return value.equals(e.getType());
						default:
							throw new RuntimeException("getElementByAttribute - attribute not supported");
						}
					})
					.skip(size * page)
					.limit(size)
					.collect(Collectors.toList());
		}
	}

	@Override
	public ElementEntity addNewElement(String userPlayground, String email, ElementEntity element) {
		if(element == null || element.getPlayground() == null || element.getId() == null) {
			throw new RuntimeException("addNewElement - null values");
		}
		if(this.elements.containsKey(element.getPlayground() + delimiter + element.getId())) {
			throw new RuntimeException("addNewElement - element already exist");
		}
		this.elements.put(element.getPlayground() + delimiter + element.getId(), element);
		return element;
	}

	@Override
	public void updateElement(String userPlayground, String email, String playground, String id, ElementEntity element) {
		synchronized (this.elements) {
			if(playground == null || id == null || element == null) {
				throw new RuntimeException("updateElement - null values");
			}
			if(!playground.equals(element.getPlayground()) && !id.equals(element.getId())) {
				throw new RuntimeException("updateElement - values not match");
			}
			if(elements.containsKey(playground + delimiter + id)){
				elements.put(element.getPlayground() + delimiter + element.getId(), element);
			}else {
				throw new RuntimeException("updateElement - element not found");
			}
		}
	}

	@Override
	public void cleanup() {
		this.elements.clear();
	}
	
	private double calcDistance (double x1, double y1, double x2, double y2) {
		return  Math.sqrt((y2 - y1) * (y2 - y1) + (x2 - x1) * (x2 - x1));
	}

}
