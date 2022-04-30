package Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import StepDefinitions.DTO;

@Controller
public class RestController {
	
	@Autowired
	@Lazy
	SimpMessagingTemplate template ;
	
	@CrossOrigin(origins = {"*"})
	@GetMapping("/start/{key}")
	public void sendKey(@PathVariable String key) {
		System.out.println("key should be displaying now11");
		System.out.println(key);
		DTO.keyValue = key;
		template.convertAndSend("/topic/log", "Hello");
	}
	
	@CrossOrigin(origins = {"*"})
	@Scheduled(fixedRate = 10)
	public void start() {
		//System.out.println("key should be displaying now22");
		template.convertAndSend("/topic/log", DTO.log.toArray(new String[DTO.log.size()]));
	}
	
	

}
