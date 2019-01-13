package ratingplayground.logic.activities;

import java.util.List;

import org.springframework.data.domain.Pageable;

public interface ActivityService {
	public ActivityEntity startActivity(String userPlayground, String email, ActivityEntity activity);
	public ActivityEntity getActivityByIdentifiers(String elementId, String type, String playerEmail);
	public List<ActivityEntity> findAllByElementIdEqualsAndTypeEquals(String elementId, String type, Pageable p);
	public void cleanup();
}
