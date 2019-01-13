package ratingplayground.dal;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import ratingplayground.logic.elements.ElementEntity;

public interface ElementDao extends PagingAndSortingRepository<ElementEntity, String>{
	
	/*public List<ElementEntity> findAllByNameEquals(
			@Param("name") String name,
			Pageable pageable);
	
	public List<ElementEntity> findAllByTypeEquals(
			@Param("type") String type,
			Pageable pageable);*/
	
	// manager get by distance
	public Page<ElementEntity> findAllByXBetweenAndYBetween(
			@Param("x1") double x1,
			@Param("x2") double x2,
			@Param("y1") double y1,
			@Param("y2") double y2,
			Pageable pageable);
	
	// player get by distance
	public Page<ElementEntity> findAllByXBetweenAndYBetweenAndExpirationDateIsNullOrXBetweenAndYBetweenAndExpirationDateAfter(
			@Param("x11") double x11,
			@Param("x12") double x12,
			@Param("y11") double y11,
			@Param("y12") double y12,
			@Param("x21") double x21,
			@Param("x22") double x22,
			@Param("y21") double y21,
			@Param("y22") double y22,
			@Param("date") Date date,
			Pageable pageable);
	
	// manager get all is the default findAll()
	
	// player get all
	public Page<ElementEntity> findAllByExpirationDateIsNullOrExpirationDateAfter(
			@Param("date") Date date,
			Pageable pageable);
	
	// player find by id
	public Optional<ElementEntity> findByPkAndExpirationDateIsNullOrPkEqualsAndExpirationDateAfter(
			@Param("id1") String id1,
			@Param("id2") String id2,
			@Param("date") Date date);
	
	// manager get by attribute 'name'
		public List<ElementEntity> findAllByNameEquals(
				@Param("name") String name,
				Pageable pageable);
	
	// player get by attribute 'name'
	public List<ElementEntity> findAllByNameEqualsAndExpirationDateIsNullOrNameEqualsAndExpirationDateAfter(
			@Param("name1") String name1,
			@Param("name2") String name2,
			@Param("date") Date date,
			Pageable pageable);
	
	// manager get by attribute 'type'
	public List<ElementEntity> findAllByType(
			@Param("type") String type,
			Pageable pageable);
	
	// player get by attribute 'type'
	public List<ElementEntity> findAllByTypeEqualsAndExpirationDateIsNullOrTypeEqualsAndExpirationDateAfter(
			@Param("type1") String type1,
			@Param("type2") String type2,
			@Param("date") Date date,
			Pageable pageable);
}
