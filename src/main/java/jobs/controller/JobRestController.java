package jobs.controller;

import jobs.model.Job;
import jobs.service.AmountService;
import jobs.service.JobDataBaseService;
import jobs.service.SaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class JobRestController {

    private final SaxService saxService;
    private final JobDataBaseService dbService;
    private final AmountService amountService;

    @Autowired
    public JobRestController(SaxService saxService, JobDataBaseService dbService, AmountService amountService) {
        this.saxService = saxService;
        this.dbService = dbService;
        this.amountService = amountService;
    }

    @GetMapping("/statistics")
    public List<Job> getStatistics(@RequestParam(required = false) String user,
                                   @RequestParam(required = false) String type,
                                   @RequestParam(required = false) String device,
                                   @RequestParam(required = false) String timeFrom,
                                   @RequestParam(required = false) String timeTo
    ) {

        return dbService.getQuery(user, type, device, timeFrom, timeTo);
    }

    @PostMapping("/jobs")
    public String addJobs(@RequestBody String input) {

        List<Job> jobs = saxService.parseRequest(input);
        dbService.saveAllJob(jobs);
        return amountService.createResponse(jobs);
    }
}
