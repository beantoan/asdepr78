package it.unical.asde.pr78.entity;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Table(
        name = "answers"
)
@EntityListeners(AuditingEntityListener.class)
public final class Answer extends BaseEntity {

    @Column(
            name = "`value`",
            nullable = false
    )
    @NotNull
    private String value;

    @Column(
            name = "`is_correct`",
            columnDefinition = "TINYINT(1) UNSIGNED"
    )
    private Boolean isCorrect;

    @Column(
            name = "`is_reviewed`",
            nullable = false,
            columnDefinition = "TINYINT(1) UNSIGNED"
    )
    @ColumnDefault("0")
    private Boolean isReviewed = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "question_id",
            nullable = false
    )
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Question question;

    public Answer() {
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }

    public Boolean getReviewed() {
        return isReviewed;
    }

    public void setReviewed(Boolean reviewed) {
        isReviewed = reviewed;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + getId() +
                ", value='" + value + '\'' +
                ", isCorrect=" + isCorrect +
                ", createdAt=" + createdAt +
                ", question=" + question +
                '}';
    }
}
