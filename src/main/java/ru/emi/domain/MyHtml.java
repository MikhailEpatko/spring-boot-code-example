package ru.emi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Data
@Table(name = "my_html")
@NoArgsConstructor
@AllArgsConstructor
public class MyHtml {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "html")
    private String html;
    @Column(name = "may_be_shown")
    private int mayBeShown;
}
