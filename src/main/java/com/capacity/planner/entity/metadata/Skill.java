package com.capacity.planner.entity.metadata;

import com.capacity.planner.enums.SkillEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.*;

/**
 * Created by ankush.a on 05/04/17.
 */
@Entity
@Table(name = "skill")
@TableGenerator(name = "idGen",table = "ID_GEN")
@Data
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE,generator = "idGen")
    @JsonProperty
    private Long id;

    @Column(name = "skill_name")
    @Enumerated(EnumType.STRING)
    @JsonProperty
    private SkillEnum skillName;

    @Override
    public String toString() {
        return "Skill{" +
                "skillName=" + skillName +
                ", id=" + id +
                '}';
    }
}
