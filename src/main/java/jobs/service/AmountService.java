package jobs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jobs.model.Job;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AmountService {

    public String createResponse(List<Job> jobs) {
        Map<String, Integer> map = new HashMap<>();
        for (Job job : jobs) {
            String user = job.getUser();
            if (map.containsKey(user)) {
                Integer amount = map.get(user) + job.getAmount();
                map.put(user, amount);
            } else {
                map.put(user, job.getAmount());
            }
        }
        String json = null;
        try {
            json = new ObjectMapper().writeValueAsString(map);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return json;
    }
}
