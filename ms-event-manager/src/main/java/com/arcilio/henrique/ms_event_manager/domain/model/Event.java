package com.arcilio.henrique.ms_event_manager.domain.model;

import com.arcilio.henrique.ms_event_manager.domain.enums.Uf;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.Objects;

@Document("events")
@Getter @Setter @ToString
public class Event {
    @Id
    private String id;
    private String eventName;
    private LocalDateTime dateTime;
    private String cep;
    private String logradouro;
    private String bairro;
    private String cidade;
    private Uf uf;

    public Event(String eventName, LocalDateTime dateTime, String cep) {
        this.eventName = eventName;
        this.dateTime = dateTime;
        this.cep = cep;
    }


    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Event event)) return false;
        return Objects.equals(id, event.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
