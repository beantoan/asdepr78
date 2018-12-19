package it.unical.asde.pr78.form;

import java.util.Map;

public class AnswerForm {
    private Map<Long, String[]> answers;

    public Map<Long, String[]> getAnswers() {
        return answers;
    }

    public void setAnswers(Map<Long, String[]> answers) {
        this.answers = answers;
    }

    @Override
    public String toString() {
        return "AnswerForm{" +
                "answers=" + answers +
                '}';
    }
}
