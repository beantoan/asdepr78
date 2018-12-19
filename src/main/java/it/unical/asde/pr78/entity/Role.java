package it.unical.asde.pr78.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        name = "roles"
)
@EntityListeners(AuditingEntityListener.class)
public final class Role extends BaseEntity {

    public static final String ROLE_SECRETARY = "ROLE_SECRETARY";
    public static final String ROLE_PROFESSOR = "ROLE_PROFESSOR";
    public static final String ROLE_STUDENT = "ROLE_STUDENT";

    @Column(name = "`desc`")
    private String desc;

    @Column(
            name = "`name`",
            nullable = false
    )
    @NotNull
    private String name;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
