package ratingplayground.logic.elements;

import java.util.List;

import ratingplayground.logic.AppConstants;

public interface ElementService {
	/**
	 * must be a <b>verified</b> user to use this method
	 * @param isManager - Boolean type, passed as null and get value from {@link ratingplayground.aop.aspects.VerificationAspect} 
	 * @param userPlayground - the user's playground
	 * @param email - the user's email
	 * @param playground - playground of the element (in most cases the element is from our playground)
	 * @param id - id of the element
	 * @return {@link ElementEntity} if found one with given identifiers
	 * @throws RuntimeException when element not found
	 */
	public ElementEntity getElementByIdentifiers(Boolean isManager, String userPlayground, String email, String playground, String id);
	

	/**
	 * must be a <b>verified</b> user to use this method
	 * @param isManager - Boolean type, passed as null and get value from {@link ratingplayground.aop.aspects.VerificationAspect} 
	 * @param userPlayground - the user's playground
	 * @param email - the user's email
	 * @param size - pagination parameter, size of page
	 * @param page - - pagination parameter, index of page
	 * @return array of elements {@link ElementEntity} ordered by {@link ElementEntity#getCreationDate()} <b>DESC</b>
	 * player will get only elements with valid {@link ElementEntity#getExpirationDate()}
	 * manager will get all exiting elements 
	 */
	public List<ElementEntity> getAllElements (Boolean isManager, String userPlayground, String email, int size, int page);
	
	/**
	 * must be a <b>verified</b> user to use this method
	 * @param isManager - Boolean type, passed as null and get value from {@link ratingplayground.aop.aspects.VerificationAspect} 
	 * @param userPlayground - the user's playground
	 * @param email - the user's email
	 * @param x - x location, from {@link ElementEntity#getX()}
	 * @param y - y location, from {@link ElementEntity#getY()}
	 * @param distance - distance
	 * @param size - pagination parameter, size of page
	 * @param page - pagination parameter, index of page
	 * @return array of elements  {@link ElementEntity} near requested location (x,y) by (input) distance
	 * player will get only elements with valid {@link ElementEntity#getExpirationDate()}
	 * manager will get all exiting elements
	 */
	public List<ElementEntity> getNearestElementsByDistance (Boolean isManager, String userPlayground, String email, double x, double y, double distance, int size, int page);

	/**
	 * must be a <b>verified</b> user to use this method
	 * currently available only for {@link ElementEntity#getName()} and {@link ElementEntity#getType()}
	 * @param isManager - Boolean type, passed as null and get value from {@link ratingplayground.aop.aspects.VerificationAspect} 
	 * @param userPlayground - the user's playground
	 * @param email - the user's email
	 * @param attribute - element attribute to be searched
	 * @param value - value of this attribute to search by
	 * @param size - pagination parameter, size of page
	 * @param page - pagination parameter, index of page
	 * @return array of elements  {@link ElementEntity} with attribute value equals to input value
	 * player will get only elements with valid {@link ElementEntity#getExpirationDate()}
	 * manager will get all exiting elements
	 * @throws RuntimeException null values for attribute or value
	 * @throws RuntimeException attribute not supported
	 */
	public List<ElementEntity> getElementsByAttribute (Boolean isManager, String userPlayground, String email, String attribute, String value, int size, int page);
	
	/**
	 * must be a <b>verified</b> user and <b>{@link AppConstants#MANAGER}</b> Role to use this method. <br>
	 * Creates a new element in current playground {@link AppConstants#PLAYGROUND}
	 * @param userPlayground - the user's playground
	 * @param email - the user's email
	 * @param element - element to be added
	 * @return the new element that added to DB
	 * @throws RuntimeException null values for element, element.playground or element.id
	 * @throws RuntimeException element already exists
	 */
	//manager only
	public ElementEntity addNewElement (String userPlayground, String email, ElementEntity element);

	/**
	 * must be a <b>verified</b> user and <b>{@link AppConstants#MANAGER}</b> Role to use this method. <br>
	 * Updates a new element in current playground {@link AppConstants#PLAYGROUND}
	 * @param userPlayground - the user's playground
	 * @param email - the user's email
	 * @param playground - playground of requested element
	 * @param id - id of requested element
	 * @param element - element to be updated
	 * @throws RuntimeException null values for element, playground or id
	 * @throws RuntimeException values not match between given playground, id and given element.playground, element.id
	 * @throws RuntimeException element not exists
	 */
	//manager only
	public void updateElement(String userPlayground, String email, String playground, String id, ElementEntity element);
	
	public void cleanup();
}
