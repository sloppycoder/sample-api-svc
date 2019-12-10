package net.vino9.sample;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;

@Entity
@Data
public class Message implements Serializable {
    @Id
    private String id;
    private String content;
}
