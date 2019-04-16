package jobs.service;

import jobs.model.Job;
import jobs.repository.JobRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class Dservice {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Job> getQuery(Map<String, Object> map) throws ParseException {
        DateFormat format = new SimpleDateFormat("dd.MM.yyyy");

        Session session = entityManager.unwrap(Session.class);
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Job> criteria = builder.createQuery(Job.class);
        Root<Job> root = criteria.from(Job.class);

        Set<String> set = map.keySet();

        for(String key : set){
            Object value = map.get(key);
            if (value.toString().matches("\\d{2}.\\d{2}.\\d{4}")) {
                value = format.parse(value.toString());
            }

            if(key.contains(">")){
                key = key.replaceAll("[>]", "");
                if(value instanceof Date){
                    criteria.where(builder.greaterThan(root.get(key), (Date)value));
                }
                else {
                    criteria.where(builder.ge(root.get(key), (Number)value));
                }
            }
            else if(key.contains("<")){
                key = key.replaceAll("[<]", "");
                if(value instanceof Date){
                    criteria.where(builder.lessThan(root.get(key), (Date)value));
                }
                else {
                    criteria.where(builder.le(root.get(key), (Number)value));
                }
            }
            else {
                criteria.where(builder.equal(root.get(key), value));
            }
        }
        Query<Job> query = session.createQuery(criteria);

        return query.getResultList();
    }
}
