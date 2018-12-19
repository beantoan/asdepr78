package it.unical.asde.pr78.repository;

import it.unical.asde.pr78.entity.Answer;
import it.unical.asde.pr78.entity.Question;
import it.unical.asde.pr78.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findAllByQuestionIn(List<Question> questions);

    List<Answer> findAllByStudentAndQuestion(User student, Question question);
}