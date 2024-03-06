package study.datajpa.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

@MappedSuperclass
public class JpaBaseEntity {

    @Column(updatable = false) //생성일은 수정할 일이 없기 때문에 막아준다.
    private LocalDateTime createDate;
    private LocalDateTime updateDate;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createDate = now;
        updateDate = now; //업데이트에도 넣는 이유는 updateDate가 비어져 있으면 후에 쿼리를 날릴 때에 null값으로 인해 지저분해진다.
    }

    @PreUpdate
    public void preUpdate(){
        updateDate = LocalDateTime.now();
    }
}
