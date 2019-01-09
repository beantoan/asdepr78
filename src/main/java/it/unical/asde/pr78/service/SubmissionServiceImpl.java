package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.Answer;
import it.unical.asde.pr78.entity.Exam;
import it.unical.asde.pr78.entity.Submission;
import it.unical.asde.pr78.entity.User;
import it.unical.asde.pr78.exception.InvalidExamException;
import it.unical.asde.pr78.repository.AnswerRepository;
import it.unical.asde.pr78.repository.ExamRepository;
import it.unical.asde.pr78.repository.SubmissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

@Service
public class SubmissionServiceImpl implements SubmissionService {

    private Logger logger = Logger.getLogger(SubmissionServiceImpl.class.getName());

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Override
    public Submission createOrContinue(User user, Long id) throws InvalidExamException {
        Submission submission = this.getValidSubmission(user, id);

        Date now = Calendar.getInstance().getTime();

        if (submission.getStartedAt() == null) {
            submission.setStartedAt(now);
            submission.setStatus(Submission.STATUS_STARTED);
        }

        this.submissionRepository.save(submission);

        return submission;
    }

    /**
     * Need to check:
     * - Exam should exist
     * - User didn't finish exam
     * - If exam has been started, the duration from startedAt to now can not be greater or equal than duration of exam
     *
     * @param user
     * @param id
     * @return
     * @throws InvalidExamException
     */
    @Override
    public Submission getValidSubmission(User user, Long id) throws InvalidExamException {
        Exam exam = this.examRepository.findOpen(id);

        if (exam == null) {
            throw new InvalidExamException("The exam could not be found. Please try again.");
        }

        Submission submission = this.submissionRepository.findByExamAndStudent(exam, user);

        if (submission != null) {
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");

            if (submission.getStatus() == Submission.STATUS_STARTED) {
                Calendar expectedEndDate = Calendar.getInstance();
                expectedEndDate.setTime(submission.getStartedAt());
                expectedEndDate.add(Calendar.MINUTE, exam.getDuration());

                long expectedEndTime = expectedEndDate.getTime().getTime();

                Date now = Calendar.getInstance().getTime();

                if (now.getTime() > expectedEndTime) {
                    String error = String.format("You can not continue the exam '%s'. Because you have started at %s. But you did not complete within %d minutes.",
                            exam.getTitle(), format.format(submission.getStartedAt()), exam.getDuration());
                    throw new InvalidExamException(error);
                }

                if (now.getTime() > exam.getFinishedAt().getTime() ||
                        exam.getFinishedAt().getTime() > expectedEndTime) {
                    String error = String.format("You can not continue the exam '%s'. Because you have started at %s. But the exam has been closed at %s.",
                            exam.getTitle(), format.format(submission.getStartedAt()), format.format(exam.getFinishedAt()));
                    throw new InvalidExamException(error);
                }
            } else {
                String error = String.format("You can not continue the exam '%s'. Because the exam has been closed at %s.",
                        exam.getTitle(), format.format(submission.getFinishedAt()));
                throw new InvalidExamException(error);
            }
        } else {
            submission = new Submission(exam, user);
        }

        return submission;
    }

    @Override
    public List<Submission> findAllByExam(Exam exam) {
        List<Integer> availableStatuses = Arrays.asList(Submission.STATUS_SUBMITTED, Submission.STATUS_REVIEWING, Submission.STATUS_REVIEWED);

        return this.submissionRepository.findAllByExamAndStatusIn(exam, availableStatuses);
    }

    @Override
    public List<Submission> findAllByStudent(User student) {
        return this.submissionRepository.findAllByStudent(student);
    }

    @Override
    public Submission findForProfessorToReview(Exam exam, Long submissionId) throws InvalidExamException {
        List<Integer> toReviewStatuses = Arrays.asList(Submission.STATUS_SUBMITTED, Submission.STATUS_REVIEWING);

        Submission submission = this.submissionRepository.findByIdAndExamAndStatusIn(submissionId, exam, toReviewStatuses);

        if (submission == null) {
            String error = String.format("The submission for exam '%s' has been already reviewed or invalid", exam.getTitle());
            throw new InvalidExamException(error);
        }

        return submission;
    }

    @Override
    public void markAsReviewed(Exam exam, Submission submission) throws InvalidExamException {
        List<Answer> answers = this.answerRepository.findAllByQuestionIn(exam.getQuestions());

        if (submission.getStatus() != Submission.STATUS_REVIEWING) {
            String error = String.format("The submission for exam '%s' has been already reviewed or invalid.", exam.getTitle());
            throw new InvalidExamException(error);
        }

        for (Answer answer : answers) {
            if (!answer.getReviewed()) {
                String message = String.format("The answers of exam '%s' are not fully reviewed. Please check them before marking as reviewed.",
                        exam.getTitle());

                throw new InvalidExamException(message);
            }
        }

        this.updateStatus(submission, Submission.STATUS_REVIEWED);

        exam.setReviewedCount(exam.getReviewedCount() + 1);

        this.examRepository.save(exam);
    }

    @Override
    public void markAsReviewing(Exam exam, Submission submission) throws InvalidExamException {
        if (submission.getStatus() == Submission.STATUS_REVIEWING) {
            return;
        }

        if (submission.getStatus() != Submission.STATUS_SUBMITTED) {
            String error = String.format("The submission for exam '%s' has been already reviewed or invalid.", exam.getTitle());
            throw new InvalidExamException(error);
        }

        this.updateStatus(submission, Submission.STATUS_REVIEWING);
    }

    @Override
    public Submission findReviewing(Exam exam, Long submissionId) throws InvalidExamException {
        Submission submission = this.submissionRepository.findByIdAndExamAndStatus(submissionId, exam, Submission.STATUS_REVIEWING);

        if (submission == null) {
            String error = String.format("The submission for exam '%s' has been already reviewed or invalid.", exam.getTitle());
            throw new InvalidExamException(error);
        }

        return submission;
    }

    @Override
    public Submission findReviewed(Exam exam, Long submissionId) throws InvalidExamException {
        Submission submission = this.submissionRepository.findByIdAndExamAndStatus(submissionId, exam, Submission.STATUS_REVIEWED);

        if (submission == null) {
            String error = String.format("The submission for exam '%s' has been not reviewed or invalid.", exam.getTitle());
            throw new InvalidExamException(error);
        }

        return submission;
    }

    private void updateStatus(Submission submission, int status) {
        submission.setStatus(status);
        this.submissionRepository.save(submission);
    }
}
