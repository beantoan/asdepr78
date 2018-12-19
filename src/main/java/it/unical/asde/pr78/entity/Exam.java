package it.unical.asde.pr78.entity;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "exams"
)
@EntityListeners(AuditingEntityListener.class)
public final class Exam extends BaseEntity {

    @Column(
            name = "`title`",
            nullable = false
    )
    @NotEmpty(message = "Please provide the title")
    private String title;

    @Column(
            name = "`desc`",
            columnDefinition = "TEXT"
    )
    private String desc;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(name = "started_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Please choose the opening time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date startedAt;

    @Column(name = "finished_at", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @NotNull(message = "Please choose the closing time")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedAt;

    @Column(
            name = "`duration`",
            nullable = false,
            columnDefinition = "INT UNSIGNED"
    )
    @ColumnDefault("0")
    @Min(value = 10, message = "The duration should be greater than or equal to 10 minutes")
    private Integer duration;

    @Column(
            name = "`submitted_count`",
            nullable = false,
            columnDefinition = "INT UNSIGNED"
    )
    @ColumnDefault("0")
    private Integer submittedCount = 0;

    @Column(
            name = "`reviewed_count`",
            nullable = false,
            columnDefinition = "INT UNSIGNED"
    )
    @ColumnDefault("0")
    private Integer reviewedCount = 0;

    @OneToMany(
            mappedBy = "exam",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.ALL}
    )
    private List<Question> questions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User professor;

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Date startedAt) {
        this.startedAt = startedAt;
    }

    public Date getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(Date finishedAt) {
        this.finishedAt = finishedAt;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }

    public User getProfessor() {
        return professor;
    }

    public void setProfessor(User professor) {
        this.professor = professor;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public Integer getSubmittedCount() {
        return submittedCount;
    }

    public void setSubmittedCount(Integer submittedCount) {
        this.submittedCount = submittedCount;
    }

    public Integer getReviewedCount() {
        return reviewedCount;
    }

    public void setReviewedCount(Integer reviewedCount) {
        this.reviewedCount = reviewedCount;
    }

    public boolean isEditable() {
        if (getId() == null) {
            return true;
        }

        Date now = Calendar.getInstance().getTime();

        return now.getTime() < getStartedAt().getTime();
    }

    public boolean isExpired() {
        if (getId() == null) {
            return false;
        }

        Date now = Calendar.getInstance().getTime();

        return now.getTime() > getFinishedAt().getTime();
    }

    @Override
    public String toString() {
        return "Exam{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", startedAt=" + startedAt +
                ", finishedAt=" + finishedAt +
                ", duration=" + duration +
                ", questions=" + questions +
                '}';
    }
}
