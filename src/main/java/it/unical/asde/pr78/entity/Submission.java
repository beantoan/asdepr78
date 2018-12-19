package it.unical.asde.pr78.entity;

import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Entity
@Table(
        name = "submissions",
        indexes = {
                @Index(columnList = "exam_id,user_id", unique = true)
        }
)
@EntityListeners(AuditingEntityListener.class)
public final class Submission extends BaseEntity {

    public static final int STATUS_STARTED = 1;
    public static final int STATUS_SUBMITTED = 2;
    public static final int STATUS_REVIEWING = 3;
    public static final int STATUS_REVIEWED = 4;

    @Column(name = "started_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startedAt;

    @Column(name = "finished_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date finishedAt;

    @Column(
            name = "`point`",
            nullable = false,
            columnDefinition = "TINYINT UNSIGNED"
    )
    private int point;

    @Column(
            name = "`correct_count`",
            nullable = false,
            columnDefinition = "TINYINT UNSIGNED"
    )
    @ColumnDefault("0")
    private int correctCount;

    @Column(
            name = "`incorrect_count`",
            nullable = false,
            columnDefinition = "TINYINT UNSIGNED"
    )
    @ColumnDefault("0")
    private int incorrectCount;

    @Column(
            name = "`status`",
            nullable = false,
            columnDefinition = "TINYINT UNSIGNED"
    )
    @NotNull
    @ColumnDefault("1")
    private int status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "exam_id",
            nullable = false
    )
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User student;

    public Submission() {
    }

    public Submission(Exam exam, User student) {
        this.exam = exam;
        this.student = student;
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

    public int getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public void setCorrectCount(int correctCount) {
        this.correctCount = correctCount;
    }

    public int getIncorrectCount() {
        return incorrectCount;
    }

    public void setIncorrectCount(int incorrectCount) {
        this.incorrectCount = incorrectCount;
    }

    public Exam getExam() {
        return exam;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
    }

    public User getStudent() {
        return student;
    }

    public void setStudent(User student) {
        this.student = student;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public long getRemainingTime() {
        long diffInMilliseconds = Calendar.getInstance().getTime().getTime() - getStartedAt().getTime();

        return getExam().getDuration() - TimeUnit.MINUTES.convert(diffInMilliseconds, TimeUnit.MILLISECONDS);
    }

    public String getStatusLabel() {
        switch (getStatus()) {
            case STATUS_SUBMITTED:
                return "Submitted";
            case STATUS_REVIEWING:
                return "In review";
            case STATUS_REVIEWED:
                return "Reviewed";
            default:
                return "In progress";
        }
    }
}
