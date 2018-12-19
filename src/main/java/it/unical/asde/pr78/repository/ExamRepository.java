package it.unical.asde.pr78.repository;

import it.unical.asde.pr78.entity.Exam;
import it.unical.asde.pr78.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    @Query(value = "SELECT e.* FROM exams e WHERE e.id NOT IN ?1 AND NOW() BETWEEN e.started_at AND e.finished_at", nativeQuery = true)
    List<Exam> findAllOpen(List<Long> excludedIds);

    @Query(value = "SELECT e.* FROM exams e WHERE NOW() BETWEEN e.started_at AND e.finished_at", nativeQuery = true)
    List<Exam> findAllOpen();

    @Query(value = "SELECT * FROM exams WHERE id = ?1 AND NOW() BETWEEN started_at AND finished_at LIMIT 1", nativeQuery = true)
    Exam findOpen(Long id);

    List<Exam> findAllByProfessor(User user);

    Exam findByIdAndProfessor(Long id, User user);
}