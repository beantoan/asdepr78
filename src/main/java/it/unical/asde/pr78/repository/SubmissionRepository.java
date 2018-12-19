package it.unical.asde.pr78.repository;

import it.unical.asde.pr78.entity.Exam;
import it.unical.asde.pr78.entity.Submission;
import it.unical.asde.pr78.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {
    Submission findByExamAndStudent(Exam exam, User student);

    List<Submission> findAllByStudent(User student);

    List<Submission> findAllByExamAndStatusIn(Exam exam, List<Integer> statuses);

    Submission findByIdAndExamAndStatus(Long id, Exam exam, int status);

    Submission findByIdAndExamAndStatusIn(Long submissionId, Exam exam, List<Integer> statuses);
}