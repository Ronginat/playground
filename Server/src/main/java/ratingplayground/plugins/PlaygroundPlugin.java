package ratingplayground.plugins;

import ratingplayground.logic.activities.ActivityEntity;

public interface PlaygroundPlugin {
	public Object invokeOperation (ActivityEntity activity); 
}
