package ru.edu.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.time.Instant;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TimeInterrupt implements Serializable {

    @EmbeddedId
    private TimeInterruptID timeInterruptID;

    private Instant dateEnd;

    @ManyToOne(cascade = CascadeType.ALL)
    private CauseInterrupt causeInterrupt;

}
