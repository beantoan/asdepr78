package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.*;
import it.unical.asde.pr78.exception.InvalidAnswerException;
import it.unical.asde.pr78.repository.AnswerRepository;
import it.unical.asde.pr78.repository.ExamRepository;
import it.unical.asde.pr78.repository.SubmissionRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.logging.Logger;

@Service
public class AnswerServiceImpl implements AnswerService {

    private Logger logger = Logger.getLogger(AnswerServiceImpl.class.getName());

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private SubmissionRepository submissionRepository;

    @Autowired
    private QuestionService questionService;


    @Autowired
    private ExamRepository examRepository;

    @Override
    public List<Answer> findAllByQuestions(List<Question> questions) {
        return this.answerRepository.findAllByQuestionIn(questions);
    }

    /**
     * Need to check the questionIds of submitted answers with the questions of the exam
     */
    @Override
    public List<Answer> submitAnswers(User student, Map<Long, String[]> answers, Submission submission)
            throws InvalidAnswerException {
        Exam exam = submission.getExam();
        List<Question> questions = exam.getQuestions();
        List<Answer> refinedAnswers = this.buildAnswers(answers, student, questions);

        for (Answer answer : refinedAnswers) {
            if (answer.getQuestion() == null) {
                String error = String.format("You have answered for some invalid questions. Maybe these questions do not belong to the exam '%s'",
                        submission.getExam().getTitle());
                throw new InvalidAnswerException(error);
            }
        }

        logger.info(refinedAnswers.toString());

        Date now = Calendar.getInstance().getTime();
        submission.setFinishedAt(now);
        submission.setStatus(Submission.STATUS_SUBMITTED);

        this.evaluateAnswers(submission, refinedAnswers, questions);

        exam.setSubmittedCount(exam.getSubmittedCount() + 1);

        try {
            this.saveAnswersAndSubmission(refinedAnswers, submission, exam);
        } catch (Exception e) {
            e.printStackTrace();

            throw new InvalidAnswerException("There is an unexpected error when saving your answers. Please try again.");
        }

        return refinedAnswers;
    }

    @Override
    public Question updateCorrectness(Exam exam, Submission submission, Long questionId, boolean isCorrect)
            throws InvalidAnswerException {
        Question question = null;

        for (Question q : exam.getQuestions()) {
            if (q.getId().equals(questionId)) {
                question = q;
                break;
            }
        }

        if (question == null) {
            String error = String.format("The question of exam '%s' is not valid", exam.getTitle());
            throw new InvalidAnswerException(error);
        }

        List<Answer> answers = this.answerRepository.findAllByStudentAndQuestion(submission.getStudent(), question);

        if (answers == null || answers.isEmpty()) {
            String error = String.format("No answer is found for question '%s' by student ''%s",
                    question.getTitle(), submission.getStudent().getFullName());
            throw new InvalidAnswerException(error);
        }

        question.setAnswers(answers);

        if (question.isAnswersReviewed()) {
            String error = String.format("The answers of student '%s' for question '%s' have been reviewed",
                    submission.getStudent().getFullName(), question.getTitle());
            throw new InvalidAnswerException(error);
        }

        for (Answer answer : answers) {
            answer.setCorrect(isCorrect);
            answer.setReviewed(true);
        }

        if (isCorrect) {
            submission.setCorrectCount(submission.getCorrectCount() + 1);
            submission.setPoint(submission.getPoint() + question.getPoint());
        } else {
            submission.setIncorrectCount(submission.getIncorrectCount() + 1);
        }

        try {
            this.saveAnswersAndSubmission(answers, submission, null);
        } catch (Exception e) {
            e.printStackTrace();

            throw new InvalidAnswerException("There is an unexpected error when saving data. Please try again.");
        }

        return question;
    }

    /**
     * Evaluate the correctness of answers automatically
     *
     * @param submission
     * @param answers
     * @param questions
     */
    private void evaluateAnswers(Submission submission, List<Answer> answers, List<Question> questions) {
        this.questionService.mapAnswersToQuestions(questions, answers);

        for (Question question : questions) {
            String answerValue = question.getAnswerValue();
            String solutionValue = question.getSolutionValue();

            if (StringUtils.isBlank(answerValue)) {
                submission.setIncorrectCount(submission.getIncorrectCount() + 1);
            } else if (StringUtils.isNotBlank(solutionValue)) {
                List<Long> correctChoiceIds = question.getCorrectChoiceIds();

                switch (question.getType()) {
                    case Question.TYPE_SHORT_ANSWER:
                    case Question.TYPE_PARAGRAPH:
                        for (Answer answer : question.getAnswers()) {
                            if (StringUtils.isNotBlank(answer.getValue()) &&
                                    answer.getValue().equals(question.getTextSolution())) {
                                answer.setCorrect(true);
                            } else {
                                answer.setCorrect(false);
                            }

                            answer.setReviewed(true);
                        }

                        break;
                    case Question.TYPE_MULTI_CHOICE:
                    case Question.TYPE_CHECKBOXES:
                    case Question.TYPE_DROPDOWN:

                        for (Answer answer : question.getAnswers()) {
                            if (StringUtils.isNotBlank(answer.getValue()) &&
                                    correctChoiceIds.contains(Long.valueOf(answer.getValue()))) {
                                answer.setCorrect(true);
                            } else {
                                answer.setCorrect(false);
                            }

                            answer.setReviewed(true);
                        }

                        break;
                }

                answerValue = StringUtils.upperCase(answerValue);
                solutionValue = StringUtils.upperCase(solutionValue);

                if (solutionValue.equals(answerValue)) {
                    submission.setCorrectCount(submission.getCorrectCount() + 1);
                    submission.setPoint(submission.getPoint() + question.getPoint());
                } else {
                    submission.setIncorrectCount(submission.getIncorrectCount() + 1);
                }
            }
        }
    }

    /**
     * Build a list of Answers from array of String
     *
     * @param rawAnswers
     * @param student
     * @param questions
     * @return
     */
    private List<Answer> buildAnswers(Map<Long, String[]> rawAnswers, User student, List<Question> questions) {
        logger.info(questions.toString());

        List<Answer> refinedAnswers = new ArrayList<>();

        if (rawAnswers != null) {
            rawAnswers.forEach((questionId, answerValues) -> {
                if (answerValues != null) {
                    Question currentQuestion = null;

                    for (Question question : questions) {
                        if (question.getId().equals(questionId)) {
                            currentQuestion = question;
                            break;
                        }
                    }

                    for (String answerValue : answerValues) {
                        if (StringUtils.isNotBlank(answerValue)) {
                            Answer answer = new Answer();

                            answer.setStudent(student);
                            answer.setValue(answerValue);
                            answer.setQuestion(currentQuestion);

                            refinedAnswers.add(answer);
                        }
                    }
                }
            });
        }

        return refinedAnswers;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void saveAnswersAndSubmission(List<Answer> answers, Submission submission, Exam exam) {
        this.answerRepository.saveAll(answers);
        this.submissionRepository.save(submission);

        if (exam != null) {
            this.examRepository.save(exam);
        }
    }
}
