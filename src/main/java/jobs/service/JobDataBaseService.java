package jobs.service;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import jobs.model.Job;
import jobs.repository.JobRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Repository
public class JobDataBaseService {

    @PersistenceContext
    private EntityManager entityManager;

    private final JobRepository jobRepository;

    @Autowired
    public JobDataBaseService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public List<Job> getAll() {
        return jobRepository.findAll();
    }

    public void saveJob(Job job) {
        jobRepository.save(job);
    }

    public void saveAllJob(List<Job> jobs) {
        jobRepository.saveAll(jobs);
    }

    public List<Job> getQuery(String user, String type, String device, String timeFrom, String timeTo) {

        DateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        Long longTimeFrom = null;
        Long longTimeTo = null;
        try {
            if (timeFrom != null) {
                longTimeFrom = format.parse(timeFrom).getTime();
            }
            if (timeTo != null) {
                longTimeTo = format.parse(timeTo).getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Job> criteria = builder.createQuery(Job.class);
        Root<Job> root = criteria.from(Job.class);

        criteria.select(root);
        if (user != null) {
            criteria.where(builder.equal(root.get("user"), user));
        }
        if (type != null) {
            criteria.where(builder.equal(root.get("type"), type));
        }
        if (device != null) {
            criteria.where(builder.equal(root.get("device"), device));
        }
        if (type != null) {
            criteria.where(builder.equal(root.get("type"), type));
        }
        if (longTimeFrom != null) {
            criteria.where(builder.ge(root.get("longDate"), longTimeFrom));
        }
        if (longTimeTo != null) {
            criteria.where(builder.le(root.get("longDate"), longTimeTo));
        }
        criteria.orderBy(builder.asc(root.get("longDate")));
        Query<Job> query = session.createQuery(criteria);
        List<Job> jobs = query.getResultList();
        jobs.forEach(Job::convertDate);
        return jobs;
    }
}












