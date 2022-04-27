package Utils;

import java.lang.reflect.Field;
import java.util.List;

import io.cucumber.core.backend.TestCaseState;
import io.cucumber.java.Scenario;
import io.cucumber.plugin.event.Result;

public class CucumberFailureResult {
	
	public String getFailureMessage(Scenario scenario) {
	    Result failResult = null;

	    try {
	        // Get the delegate from the scenario
	        Field delegate = scenario.getClass().getDeclaredField("delegate");
	        delegate.setAccessible(true);
	        TestCaseState testCaseState = (TestCaseState) delegate.get(scenario);
	       

	        // Get the test case results from the delegate
	        Field stepResults = testCaseState.getClass().getDeclaredField("stepResults");
	        stepResults.setAccessible(true);
	        List<Result> results = (List<Result>) stepResults.get(testCaseState);

	        for(Result result : results) {
	            if(result.getStatus().name().equalsIgnoreCase("FAILED")) {
	                failResult = result;
	            }
	        }
	    } catch (NoSuchFieldException | IllegalAccessException e) {
	        e.printStackTrace();
	    }

	    return (failResult != null) ? failResult.getError().getLocalizedMessage() : "";
	}
	

}
