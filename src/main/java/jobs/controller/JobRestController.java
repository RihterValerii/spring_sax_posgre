package jobs.controller;

import jobs.model.Job;
import jobs.service.AmountService;
import jobs.service.Dservice;
import jobs.service.JobDataBaseService;
import jobs.service.SaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
public class JobRestController {

    private final SaxService saxService;
    private final JobDataBaseService dbService;
    private final AmountService amountService;
    private final Dservice dservice;

    @Autowired
    public JobRestController(SaxService saxService, JobDataBaseService dbService, AmountService amountService, Dservice dsrvice) {
        this.saxService = saxService;
        this.dbService = dbService;
        this.amountService = amountService;
        this.dservice = dsrvice;
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
        jobs.forEach(job -> job.setDate2(new java.sql.Date(new Date().getTime())));
        dbService.saveAllJob(jobs);
        return amountService.createResponse(jobs);
    }

    @PostMapping("/add")
    public List<Job> get(@RequestBody String input) {

        List<Job> jobs = saxService.parseRequest(input);
        jobs.forEach(job -> job.setDate2(new java.sql.Date(new Date().getTime())));
        dbService.saveAllJob(jobs);
        return dbService.getAll();
    }

    @PostMapping("/search")
    public List<Job> search(@RequestBody Map<String, Object> map) throws ParseException {

        return dservice.getQuery(map);
    }
}
