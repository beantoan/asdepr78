package it.unical.asde.pr78.entity;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.*;

@Entity
@Table(
        name = "questions"
)
@EntityListeners(AuditingEntityListener.class)
public final class Question extends BaseEntity {

    public static final int TYPE_SHORT_ANSWER = 1;
    public static final int TYPE_PARAGRAPH = 2;
    public static final int TYPE_MULTI_CHOICE = 3;
    public static final int TYPE_CHECKBOXES = 4;
    public static final int TYPE_DROPDOWN = 5;

    public static Map<Integer, String> getTypes() {
        Map<Integer, String> types = new HashMap<>();

        types.put(TYPE_SHORT_ANSWER, "Short answer");
        types.put(TYPE_PARAGRAPH, "Paragraph");
        types.put(TYPE_MULTI_CHOICE, "Multiple choice");
        types.put(TYPE_CHECKBOXES, "Checkboxes");
        types.put(TYPE_DROPDOWN, "Dropdown");

        return types;
    }

    @Column(
            name = "`title`",
            nullable = false
    )
    @NotNull(message = "Please provide the title")
    @Size(max = 150, message = "The title can not exceed 150 characters")
    private String title;

    @Column(name = "`desc`")
    private String desc;

    @Column(
            name = "`type`",
            nullable = false,
            columnDefinition = "TINYINT UNSIGNED"
    )
    @NotNull(message = "Please choose a type")
    private Integer type;

    @Column(
            name = "`is_required`",
            nullable = false,
            columnDefinition = "TINYINT(1) UNSIGNED"
    )
    @NotNull
    @ColumnDefault("1")
    private Boolean isRequired = true;

    @Column(
            name = "`point`",
            nullable = false,
            columnDefinition = "TINYINT UNSIGNED"
    )
    @NotNull(message = "Please provide the point")
    @Min(value = 0, message = "The point should be greater than or equal to 0")
    private Integer point;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "exam_id",
            nullable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Exam exam;

    @OneToMany(
            mappedBy = "question",
            fetch = FetchType.EAGER,
            cascade = {CascadeType.ALL}
    )
    private List<Choice> choices = new ArrayList<>();

    @Transient
    private List<Answer> answers = new ArrayList<>();

    @Column(name = "`text_solution`")
    private String textSolution;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Boolean getRequired() {
        return isRequired;
    }

    public void setRequired(Boolean required) {
        isRequired = required;
    }

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public List<Choice> getChoices() {
        return choices;
    }

    public void setChoices(List<Choice> choices) {
        this.choices = choices;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public String getTextSolution() {
        return textSolution;
    }

    public void setTextSolution(String textSolution) {
        this.textSolution = textSolution;
    }

    public String getAnswerValue() {
        if (getAnswers().isEmpty()) {
            return null;
        }

        switch (getType()) {
            case TYPE_SHORT_ANSWER:
            case TYPE_PARAGRAPH:
                return StringUtils.strip(getAnswers().get(0).getValue());
            case TYPE_MULTI_CHOICE:
            case TYPE_CHECKBOXES:
            case TYPE_DROPDOWN:
            default:
                List<Long> choiceIds = Arrays.asList(getAnswers().stream()
                        .map(a -> Long.valueOf(a.getValue()))
                        .toArray(Long[]::new));

                List<Integer> choiceIndexes = this.getChoiceIndexes(choiceIds);

                choiceIndexes.sort(Comparator.comparingInt(Integer::intValue));

                return StringUtils.join(choiceIndexes, ", ");
        }
    }

    public String getSolutionValue() {
        switch (getType()) {
            case TYPE_SHORT_ANSWER:
            case TYPE_PARAGRAPH:
                return StringUtils.strip(getTextSolution());
            case TYPE_MULTI_CHOICE:
            case TYPE_CHECKBOXES:
            case TYPE_DROPDOWN:
            default:

                List<Long> correctChoiceIds = getCorrectChoiceIds();

                if (correctChoiceIds.isEmpty()) {
                    return null;
                }

                List<Integer> choiceIndexes = this.getChoiceIndexes(correctChoiceIds);

                choiceIndexes.sort(Comparator.comparingInt(Integer::intValue));

                return StringUtils.join(choiceIndexes, ", ");
        }
    }

    public List<Long> getCorrectChoiceIds() {
        return Arrays.asList(getCorrectChoices().stream()
                .map(Choice::getId)
                .toArray(Long[]::new));
    }

    public List<Choice> getCorrectChoices() {
        return Arrays.asList(getChoices().stream()
                .filter(Choice::getCorrect)
                .toArray(Choice[]::new));
    }

    private List<Integer> getChoiceIndexes(List<Long> choiceIds) {
        List<Integer> choiceIndexes = new ArrayList<>();

        for (int i = 0; i < getChoices().size(); i++) {
            Choice c = getChoices().get(i);

            if (choiceIds.contains(c.getId())) {
                choiceIndexes.add(i + 1);
            }
        }

        return choiceIndexes;
    }

    public boolean isAnswersReviewed() {
        for (Answer answer : getAnswers()) {
            if (!answer.getReviewed()) {
                return false;
            }
        }

        return true;
    }

    public boolean isAnswersCorrect() {
        if (getAnswers().isEmpty()) {
            return false;
        }

        switch (getType()) {
            case TYPE_SHORT_ANSWER:
            case TYPE_PARAGRAPH:
                return getAnswers().get(0).getCorrect();
            case TYPE_MULTI_CHOICE:
            case TYPE_CHECKBOXES:
            case TYPE_DROPDOWN:
            default:
                List<Choice> correctChoices = getCorrectChoices();

                if (!correctChoices.isEmpty() && getAnswers().size() != correctChoices.size()) {
                    return false;
                }

                for (Answer answer : getAnswers()) {
                    if (!answer.getCorrect()) {
                        return false;
                    }
                }

                return true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Question question = (Question) o;
        return getId().equals(question.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), title, desc, type, isRequired, point, exam, choices, answers);
    }

    @Override
    public String toString() {
        return "Question{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", type=" + type +
                ", isRequired=" + isRequired +
                ", point=" + point +
                ", choices=" + choices +
                ", answers=" + answers +
                ", textSolution=" + textSolution +
                '}';
    }
}
