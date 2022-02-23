package com.brew.home.mockito.spring.home.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

/**
 * from zeus-api: Live实体, 只拿了需要用到的字段
 * @author shaogz
 */
@Entity
@Getter @Setter @ToString @RequiredArgsConstructor
public class Live {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "room_id")
    private Integer roomId;

    @Column(name = "user_id")
    private Integer userId;

    private Integer status;

    @Column(name = "record_video_id")
    private Integer recordVideoId;

    @Column(name = "record_video_status")
    private Integer recordVideoStatus;

    @Column(name = "template_type")
    private Integer templateType;

    @Column(name = "source_node_id")
    private Integer sourceNodeId;

    @Column(name = "source_type")
    private Integer sourceType;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Live live = (Live)o;
        return id != null && Objects.equals(id, live.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
