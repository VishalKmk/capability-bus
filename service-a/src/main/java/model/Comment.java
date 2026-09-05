package model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "comments")
public class Comment extends PanacheEntity {
    public UUID userId;
    public UUID recipientId;
    public Instant createdAt;
}