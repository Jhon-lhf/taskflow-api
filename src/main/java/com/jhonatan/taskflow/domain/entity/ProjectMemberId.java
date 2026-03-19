package com.jhonatan.taskflow.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectMemberId implements Serializable {
    /*
    ProjectMemberId: Es una clase auxiliar que le dice a Hibernate:
    "La identidad de un miembro no es un ID único (1, 2, 3...),
    sino la combinación del project_id y el user_id".

    Esto evita que un mismo usuario se una dos veces al mismo proyecto.
     */
    private Long project;
    private Long user;
}
