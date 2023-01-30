package ru.edu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeInterrupt implements Serializable {

    @EmbeddedId
    private TimeInterruptID timeInterruptID;

    private Instant dateEnd;

    //private Long idCause;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "idCause", foreignKey = @ForeignKey(name = "FK_TIME_INTERRUPT_CAUSE_INTERRUPT"))
    private CauseInterrupt causeInterrupt;

}
