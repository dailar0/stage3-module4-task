package com.mjc.school.repository.model;

import com.mjc.school.repository.annotation.validation.Length;
import com.mjc.school.repository.annotation.validation.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Tag implements BaseEntity<Long> {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Length(min = 3,max = 15)
    @NotNull
    private String name;
}
