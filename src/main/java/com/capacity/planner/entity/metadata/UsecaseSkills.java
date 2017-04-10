package com.capacity.planner.entity.metadata;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by ankush.a on 05/04/17.
 */
@Entity
@Table(name="usecase_skills")
@TableGenerator(name = "idGen",table = "ID_GEN")
@Data
public class UsecaseSkills {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "idGen")
    @JsonProperty
    private Long id;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "usecase_id")
    @JsonProperty
    private Usecase usecase;

    @ManyToOne( cascade = CascadeType.ALL)
    @JoinColumn(name = "skill_id")
    @JsonProperty
    private Skill skill;

    @Override
    public String toString() {
        return "UsecaseSkills{" +
                "skill=" + skill +
                '}';
    }
}
