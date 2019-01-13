package ratingplayground.logic;

import java.util.Random;

import org.springframework.stereotype.Service;



@Service
public class NumberGenerator implements NumberGeneratorService{

	
	@Override
	public int generateVerificationCode() {
		Random r = new Random();
		int low = 1000;
		int high = 10000;
		int result = r.nextInt(high-low) + low;
		return result;
	}
	
	
	
	
	
		
}
