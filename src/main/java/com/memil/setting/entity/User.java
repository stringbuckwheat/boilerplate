package com.memil.setting.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"username"})
@Getter
public class User {
    @Id
    private String username;

    private String password;
}
