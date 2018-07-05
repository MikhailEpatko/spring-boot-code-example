package ru.emi.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.emi.util.LocalDateTimeAttributeConverter;
import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity(name = "id")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IdPostgres {

    @Id
    @Column(name = "id")
    private String id;
    private boolean b2b;
    private String email;
    private String phone;
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime created;
    @Convert(converter = LocalDateTimeAttributeConverter.class)
    private LocalDateTime updated;
}
