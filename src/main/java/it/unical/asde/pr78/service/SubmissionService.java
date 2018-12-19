package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.Exam;
import it.unical.asde.pr78.entity.Submission;
import it.unical.asde.pr78.entity.User;
import it.unical.asde.pr78.exception.InvalidExamException;

import java.util.List;

public interface SubmissionService {
    Submission createOrContinue(User user, Long id) throws InvalidExamException;

    Submission getValidSubmission(User user, Long id) throws InvalidExamException;

    List<Submission> findAllByExam(Exam exam);

    List<Submission> findAllByStudent(User student);

    Submission findForProfessorToReview(Exam exam, Long submissionId) throws InvalidExamException;

    void markAsReviewed(Exam exam, Submission submission) throws InvalidExamException;

    void markAsReviewing(Exam exam, Submission submission) throws InvalidExamException;

    Submission findReviewing(Exam exam, Long submissionId) throws InvalidExamException;

    Submission findReviewed(Exam exam, Long submissionId) throws InvalidExamException;
}
