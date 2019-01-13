package ratingplayground.logic.activities.stubs;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.data.domain.Pageable;

import ratingplayground.logic.activities.ActivityEntity;
import ratingplayground.logic.activities.ActivityService;

//@Service
public class DummyActivityService implements ActivityService{

	@PostConstruct
	public void init() {

	}

	@Override
	public ActivityEntity getActivityByIdentifiers(String elementId, String type, String playerEmail) {

		return null;
	}
	
	public void cleanup() {
		
	}

	@Override
	public List<ActivityEntity> findAllByElementIdEqualsAndTypeEquals(String elementId, String type, Pageable p) {

		return null;
	}

	@Override
	public ActivityEntity startActivity(String userPlayground, String email, ActivityEntity activity) {
		// TODO Auto-generated method stub
		return null;
	}
}
