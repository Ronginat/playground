package ratingplayground.dal;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import ratingplayground.logic.activities.ActivityEntity;

public interface ActivityDao extends PagingAndSortingRepository<ActivityEntity, String>{
	
	public List<ActivityEntity> findAllByElementIdEqualsAndTypeEquals(
			@Param("elementId") String elementId,
			@Param("type") String type,
			Pageable pageable);
	
	public List<ActivityEntity> findAllByElementIdEqualsAndTypeEquals(
			@Param("elementId") String elementId,
			@Param("type") String type);
	
	public List<ActivityEntity> findAllByElementIdEqualsAndElementPlaygroundEqualsAndTypeEquals(
			@Param("elementId") String elementId,
			@Param("elementPlayground") String elementPlayground,
			@Param("type") String type);
	
	public List<ActivityEntity> findByPlayerEmailEqualsAndTypeEquals(
			@Param("playerEmail") String playerEmail,
			@Param("type") String type,
			Pageable pageable);
	
	public Optional<ActivityEntity> findByPlayerEmailEqualsAndPlayerPlaygroundEqualsAndElementIdEqualsAndElementPlaygroundEqualsAndTypeEquals(
			@Param("playerEmail") String playerEmail,
			@Param("playerPlayground") String playerPlayground,
			@Param("elementId") String elementId,
			@Param("elementPlayground") String elementPlayground,
			@Param("type") String type);
	
	public List<ActivityEntity> findByElementIdEqualsAndTypeEqualsAndPlayerEmailEquals(
			@Param("elementId") String elementId,
			@Param("type") String type, 
			@Param("playerEmail") String playerEmail);
}