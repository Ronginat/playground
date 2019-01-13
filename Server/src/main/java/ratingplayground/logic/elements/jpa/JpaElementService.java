package ratingplayground.logic.elements.jpa;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ratingplayground.aop.annotations.ManagerVerification;
import ratingplayground.aop.annotations.UserVerification;
import ratingplayground.dal.ElementDao;
import ratingplayground.logic.AppConstants;
import ratingplayground.logic.elements.ElementEntity;
import ratingplayground.logic.elements.ElementService;

@Service
public class JpaElementService implements ElementService{
	private ElementDao elements;
	//private String delimiter = "@@";
	private IdGeneratorDao idGenerator;
	
	@Autowired
	public JpaElementService(ElementDao elements, IdGeneratorDao idGenerator) {
		this.elements = elements;
		this.idGenerator = idGenerator;
	}


	@Override
	@UserVerification
	@Transactional(readOnly=true)
	public ElementEntity getElementByIdentifiers(Boolean isManager, String userPlayground, String email, String playground, String id) {
		
		String key = playground + AppConstants.DELIMITER + id;
		return isManager ? 
				this.elements.findById(key)
				.orElseThrow(() -> 
				new RuntimeException("getElementByIdentifiers - element not found")) 
				:
				this.elements.findByPkAndExpirationDateIsNullOrPkEqualsAndExpirationDateAfter(key, key, new Date())
				.orElseThrow(() -> 
					new RuntimeException("getElementByIdentifiers - element not found"));
	}

	@Override
	@UserVerification
	@Transactional(readOnly=true)
	public List<ElementEntity> getAllElements(Boolean isManager, String userPlayground, String email, int size, int page) {
		
		return isManager ?
				this.elements.findAll(PageRequest.of(page, size))
						.getContent()
				:
				this.elements.findAllByExpirationDateIsNullOrExpirationDateAfter(
						new Date(), PageRequest.of(page, size))
						.getContent();
	}

	@Override
	@UserVerification
	@Transactional(readOnly=true)
	public List<ElementEntity> getNearestElementsByDistance(Boolean isManager, String userPlayground, String email, double x, double y, double distance, int size, int page) {
	
		return isManager ?
				this.elements.
				findAllByXBetweenAndYBetween(
						x - distance, x + distance, y - distance, y + distance, PageRequest.of(page, size)
						).getContent()
				:
				this.elements.
				findAllByXBetweenAndYBetweenAndExpirationDateIsNullOrXBetweenAndYBetweenAndExpirationDateAfter(
						x - distance, x + distance, y - distance, y + distance,
						x - distance, x + distance, y - distance, y + distance,
						new Date(), PageRequest.of(page, size))
					.getContent();
	}

	@Override
	@UserVerification
	@Transactional(readOnly=true)
	public List<ElementEntity> getElementsByAttribute(Boolean isManager, String userPlayground, String email, String attribute, String value, int size, int page) {		
		if(value == null || attribute == null) {
			throw new RuntimeException("getElementByAttribute - null values");
		}
		if(!attribute.equals("name") && !attribute.equals("type")) {
			throw new RuntimeException("getElementByAttribute - attribute not supported");
		}
		else {
			switch (attribute) {
			case "name":

				return isManager ?
						this.elements.findAllByNameEquals(
								value, PageRequest.of(page, size))
						:
						this.elements.findAllByNameEqualsAndExpirationDateIsNullOrNameEqualsAndExpirationDateAfter(
								value, value, new Date(), PageRequest.of(page, size));				
			case "type":
				
				return isManager ?
						this.elements.findAllByType(
								value, PageRequest.of(page, size))
						:
						this.elements.findAllByTypeEqualsAndExpirationDateIsNullOrTypeEqualsAndExpirationDateAfter(
								value, value, new Date(), PageRequest.of(page, size));
			default:
				throw new RuntimeException("getElementByAttribute - attribute not supported");
			}
		}
	}

	@Override
	@ManagerVerification
	@Transactional(readOnly=false)
	public ElementEntity addNewElement(String userPlayground, String email, ElementEntity element) {
		if(element == null || element.getPlayground() == null || element.getId() != null) {
			throw new RuntimeException("addNewElement - null values");
		}
		if (!this.elements.existsById(element.getPk())) {
			IdGenerator tmp = this.idGenerator.save(new IdGenerator()); 
			Long dummyId = tmp.getId();
			this.idGenerator.delete(tmp);
			
			element.setId("" + dummyId);
			
			return this.elements.save(element);
		}else {
			throw new RuntimeException("addNewElement - element already exist");
		}
	}

	@Override
	@ManagerVerification
	@Transactional(readOnly=false)
	public void updateElement(String userPlayground, String email, String playground, String id, ElementEntity element) {
		if(playground == null || id == null || element == null) {
			throw new RuntimeException("updateElement - null values");
		}
		if(!playground.equals(element.getPlayground()) && !id.equals(element.getId())) {
			throw new RuntimeException("updateElement - values not match");
		}
		
		String key = playground + AppConstants.DELIMITER + id;
		ElementEntity existing = this.elements.findById(key)
				.orElseThrow(() -> 
				new RuntimeException("updateElement - element not found"));
		
		// problem with nested Aspects
		// working, i think better not to use it
		//ElementEntity existing = this.getElementByIdentifiers(true, userPlayground, email, playground, id);
		
		if (existing == null) {
			throw new RuntimeException("updateElement - element not exists");
		}
		
		if (element.getAttributes() != null && !element.getAttributes().isEmpty()) {
			existing.setAttributes(element.getAttributes());
		}
		if (element.getX() != null && element.getX() != existing.getX()) {
			existing.setX(element.getX());
		}
		if (element.getY() != null && element.getY() != existing.getY()) {
			existing.setY(element.getY());
		}
		if (element.getName() != null && !element.getName().equals(existing.getName())) {
			existing.setName(element.getName());
		}
		if (element.getType() != null && !element.getType().equals(existing.getType())) {
			existing.setType(element.getType());
		}
		if (element.getExpirationDate() != null && !element.getExpirationDate().equals(existing.getExpirationDate())) {
			existing.setExpirationDate(element.getExpirationDate());
		}

		this.elements.save(existing);
	}

	@Override
	@Transactional(readOnly=false)
	public void cleanup() {
		this.elements.deleteAll();
	}

}
