package Utils;

import StepDefinitions.DTO;

public class ChangingStepSpeech {
	
	
	public static String speechChange(String speech) {
		String stepSpeech = "";
		stepSpeech = (DTO.CurrentStepSpeech.isEmpty()|| !DTO.CurrentStepSpeech.equals(speech) )?speech:"And";
		DTO.CurrentStepSpeech = speech;
		return stepSpeech;
	}

}
