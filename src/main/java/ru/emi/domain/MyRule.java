package ru.emi.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.emi.util.LocalDateAttributeConverter;
import javax.persistence.*;
import java.time.LocalDate;

@Entity(name = "MyRule")
@Table(name = "my_rule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MyRule implements Comparable<MyRule> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "priority")
    private int priority;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate created;
    @Column(name = "updated")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Convert(converter = LocalDateAttributeConverter.class)
    private LocalDate updated;
//   ... other fields below ...

    @OneToOne(fetch = FetchType.LAZY, cascade=CascadeType.ALL)
    @JoinColumn(name = "html_id")
    private MyHtml myHtml;

    @Override
    public int compareTo(MyRule rule) {
        if(this.priority != rule.getPriority()) {
            return rule.getPriority() - this.priority;
        }
        return this.created.compareTo(rule.getCreated());
    }
}
