package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.Exam;
import it.unical.asde.pr78.entity.Submission;
import it.unical.asde.pr78.entity.User;
import it.unical.asde.pr78.exception.InvalidExamException;
import org.springframework.validation.BindingResult;

import java.util.List;

public interface ExamService {
    Exam findById(Long id);

    Exam findByIdAndProfessor(Long id, User user) throws InvalidExamException;

    List<Exam> findAllAvailableForStudent(List<Submission> submissions);

    List<Exam> findAllByProfessor(User user);

    Exam saveOrUpdate(User professor, Exam exam, BindingResult bindingResult) throws InvalidExamException;
}
