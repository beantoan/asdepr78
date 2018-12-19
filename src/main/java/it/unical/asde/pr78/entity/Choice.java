package it.unical.asde.pr78.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        name = "choices"
)
@EntityListeners(AuditingEntityListener.class)
public final class Choice extends BaseEntity {

    @Column(
            name = "`title`",
            nullable = false
    )
    @NotNull(message = "Please provide the title")
    private String title;

    @Column(
            name = "`is_correct`",
            nullable = false,
            columnDefinition = "TINYINT(1) UNSIGNED"
    )
    @ColumnDefault("0")
    private Boolean isCorrect = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "question_id",
            nullable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    @Override
    public String toString() {
        return "Choice{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", isCorrect=" + isCorrect +
                '}';
    }
}
