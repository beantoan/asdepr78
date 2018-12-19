package it.unical.asde.pr78.service;

import it.unical.asde.pr78.entity.*;
import it.unical.asde.pr78.exception.InvalidExamException;
import it.unical.asde.pr78.repository.ChoiceRepository;
import it.unical.asde.pr78.repository.ExamRepository;
import it.unical.asde.pr78.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ExamServiceImpl implements ExamService {

    private Logger logger = Logger.getLogger(ExamServiceImpl.class.getName());

    @Autowired
    private ExamRepository examRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ChoiceRepository choiceRepository;

    @Override
    public Exam findById(Long id) {
        return this.examRepository.findById(id).get();
    }

    @Override
    public Exam findByIdAndProfessor(Long id, User user) throws InvalidExamException {
        Exam exam = this.examRepository.findByIdAndProfessor(id, user);

        if (exam == null) {
            String error = "The exam is not found";
            throw new InvalidExamException(error);
        }

        return exam;
    }

    @Override
    public List<Exam> findAllAvailableForStudent(List<Submission> submissions) {
        List<Long> submissionIds = Arrays.asList(submissions.stream()
                .map(s -> s.getExam().getId())
                .toArray(Long[]::new));

        if (submissionIds.isEmpty()) {
            return this.examRepository.findAllOpen();
        }

        return this.examRepository.findAllOpen(submissionIds);
    }

    @Override
    public List<Exam> findAllByProfessor(User user) {
        return this.examRepository.findAllByProfessor(user);
    }

    @Override
    public Exam saveOrUpdate(User professor, Exam exam, BindingResult bindingResult) throws InvalidExamException {
        if (bindingResult.hasErrors()) {
            String error = "You did not provide the valid data. Please check and submit again.";
            throw new InvalidExamException(error);
        }

        exam.setProfessor(professor);

        this.processExamData(exam);

        List<Question> questionsToDelete = new ArrayList<>();
        List<Choice> choicesToDelete = new ArrayList<>();

        Exam examToSave = this.processExamForEditing(professor, exam, questionsToDelete, choicesToDelete);

        this.saveData(exam, questionsToDelete, choicesToDelete);

        return examToSave;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    protected void saveData(Exam exam, List<Question> questionsToDelete, List<Choice> choicesToDelete)
            throws InvalidExamException {
        try {
            this.choiceRepository.deleteAll(choicesToDelete);
            this.questionRepository.deleteAll(questionsToDelete);
            this.examRepository.save(exam);
        } catch (Exception e) {
            e.printStackTrace();

            String error = "There are some problems when saving the exam. Please try again later.";

            throw new InvalidExamException(error);
        }
    }

    /**
     * Process questions and choices for exam
     *
     * @param exam
     */
    private void processExamData(Exam exam) {
        if (exam.getQuestions() != null) {
            for (Question question : exam.getQuestions()) {
                question.setExam(exam);

                switch (question.getType()) {
                    case Question.TYPE_SHORT_ANSWER:
                    case Question.TYPE_PARAGRAPH:
                        question.setChoices(new ArrayList<>());
                        break;
                    case Question.TYPE_MULTI_CHOICE:
                    case Question.TYPE_CHECKBOXES:
                    case Question.TYPE_DROPDOWN:
                        question.setTextSolution(null);

                        if (question.getChoices() != null) {
                            for (Choice choice : question.getChoices()) {
                                choice.setQuestion(question);
                            }
                        }
                        break;
                }
            }
        }
    }

    private Exam processExamForEditing(User professor, Exam exam,
                                       List<Question> questionsToDelete, List<Choice> choicesToDelete)
            throws InvalidExamException {

        if (exam.getId() != null) {
            Exam existedExam = this.examRepository.findByIdAndProfessor(exam.getId(), professor);

            if (existedExam == null) {
                String error = String.format("The exam '%s' is not found", exam.getTitle());
                throw new InvalidExamException(error);
            }

            if (!existedExam.isEditable()) {
                logger.info(existedExam.toString());

                String error = String.format("You can not edit the exam '%s'. Because it is active now.", exam.getTitle());
                throw new InvalidExamException(error);
            }

            existedExam.setTitle(exam.getTitle());
            existedExam.setStartedAt(exam.getStartedAt());
            existedExam.setFinishedAt(exam.getFinishedAt());
            existedExam.setDuration(exam.getDuration());
            existedExam.setDesc(exam.getDesc());

            this.processQuestionsForExam(existedExam, exam, questionsToDelete, choicesToDelete);

            logger.info("processExamForEditing: questionsToDelete=" + questionsToDelete.toString());
            logger.info("processExamForEditing: choicesToDelete=" + choicesToDelete.toString());

            return existedExam;
        }

        return exam;
    }

    /**
     * Save new questions
     * Update the existed questions
     * Removed the lacked question
     *
     * @param existedExam
     * @param exam
     * @param questionsToDelete
     * @param choicesToDelete
     * @throws InvalidExamException
     */
    private void processQuestionsForExam(Exam existedExam, Exam exam,
                                         List<Question> questionsToDelete, List<Choice> choicesToDelete)
            throws InvalidExamException {

        if (exam.getQuestions() == null || exam.getQuestions().isEmpty()) {
            questionsToDelete.addAll(existedExam.getQuestions());
            existedExam.setQuestions(new ArrayList<>());
        } else {
            if (existedExam.getQuestions() == null || existedExam.getQuestions().isEmpty()) {
                for (Question question : exam.getQuestions()) {
                    if (question.getId() != null) {
                        logger.info("processQuestionsForExam1");
                        logger.info(existedExam.toString());
                        logger.info(question.toString());

                        String error = String.format("You can not edit the exam '%s'. Because it contains an invalid question '%s'.",
                                exam.getTitle(), question.getTitle());
                        throw new InvalidExamException(error);
                    }
                }

                existedExam.setQuestions(exam.getQuestions());
            } else {
                List<Question> updatableQuestions = new ArrayList<>();

                for (Question question : exam.getQuestions()) {
                    if (question.getId() == null) {
                        updatableQuestions.add(question);
                    } else {
                        Question existedQuestion = null;

                        for (Question q : existedExam.getQuestions()) {
                            if (question.getId().equals(q.getId())) {
                                existedQuestion = q;
                                break;
                            }
                        }

                        if (existedQuestion == null) {
                            logger.info("processQuestionsForExam2");
                            logger.info(existedExam.toString());
                            logger.info(question.toString());

                            String error = String.format("You can not edit the exam '%s'. Because it contains an invalid question '%s'.",
                                    exam.getTitle(), question.getTitle());
                            throw new InvalidExamException(error);
                        } else {
                            existedQuestion.setTitle(question.getTitle());
                            existedQuestion.setPoint(question.getPoint());
                            existedQuestion.setRequired(question.getRequired());
                            existedQuestion.setDesc(question.getDesc());
                            existedQuestion.setType(question.getType());
                            existedQuestion.setTextSolution(question.getTextSolution());

                            this.processChoicesForQuestion(exam, existedQuestion, question, choicesToDelete);
                        }

                        updatableQuestions.add(existedQuestion);
                    }
                }

                existedExam.getQuestions().removeAll(updatableQuestions);
                questionsToDelete.addAll(existedExam.getQuestions());

                existedExam.setQuestions(updatableQuestions);
            }
        }

        logger.info("processQuestionsForExam: existedExam=" + existedExam.toString());
    }

    /**
     * Save new choices
     * Update the existed choices
     * Removed the lacked choices
     *
     * @param exam
     * @param existedQuestion
     * @param question
     * @param choicesToDelete
     * @throws InvalidExamException
     */
    private void processChoicesForQuestion(Exam exam, Question existedQuestion, Question question,
                                           List<Choice> choicesToDelete)
            throws InvalidExamException {

        if (question.getChoices() == null || question.getChoices().isEmpty()) {
            choicesToDelete.addAll(existedQuestion.getChoices());
            existedQuestion.setChoices(new ArrayList<>());
        } else {
            if (existedQuestion.getChoices() == null || existedQuestion.getChoices().isEmpty()) {
                for (Choice choice : question.getChoices()) {
                    if (choice.getId() != null) {
                        logger.info("processChoicesForQuestion1");
                        logger.info(question.toString());
                        logger.info(choice.toString());

                        String error = String.format("You can not edit the exam '%s'. Because the question '%s' contains an invalid choice '%s'.",
                                exam.getTitle(), question.getTitle(), choice.getTitle());
                        throw new InvalidExamException(error);
                    }
                }

                existedQuestion.setChoices(question.getChoices());
            } else {
                List<Choice> updatableChoices = new ArrayList<>();

                for (Choice choice : question.getChoices()) {
                    if (choice.getId() == null) {
                        updatableChoices.add(choice);
                    } else {
                        Choice existedChoice = null;

                        for (Choice c : existedQuestion.getChoices()) {
                            if (choice.getId().equals(c.getId())) {
                                existedChoice = c;
                                break;
                            }
                        }

                        if (existedChoice == null) {
                            logger.info("processChoicesForQuestion2");
                            logger.info(question.toString());
                            logger.info(choice.toString());

                            String error = String.format("You can not edit the exam '%s'. Because the question '%s' contains an invalid choice '%s'.",
                                    exam.getTitle(), question.getTitle(), choice.getTitle());
                            throw new InvalidExamException(error);
                        } else {
                            existedChoice.setTitle(choice.getTitle());
                            existedChoice.setCorrect(choice.getCorrect());
                        }

                        updatableChoices.add(existedChoice);
                    }
                }

                existedQuestion.getChoices().removeAll(updatableChoices);
                choicesToDelete.addAll(existedQuestion.getChoices());

                existedQuestion.setChoices(updatableChoices);
            }
        }

        logger.info("processChoicesForQuestion: existedQuestion=" + existedQuestion.toString());
    }
}
