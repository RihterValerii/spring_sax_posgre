package jobs.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import jobs.model.Job;

public interface JobRepository extends JpaRepository<Job, Long> {

}
