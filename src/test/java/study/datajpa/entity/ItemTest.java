package study.datajpa.entity;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import study.datajpa.repository.ItemRepository;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ItemTest {

    @Autowired
    ItemRepository itemRepository;

    @Test
    public void newTest(){
        Item item = new Item("A");
        itemRepository.save(item); //이렇게 GeneratedValue가 아닌 id를 문자열로 주는 경우에는 persist가 아닌 merge가 동작해 버린다.
        /*
        @Transactional
        @Override
        public <S extends T> S save(S entity) {

            Assert.notNull(entity, "Entity must not be null");

            if (entityInformation.isNew(entity)) { //Long일 경우 null, long이면 0을 기준으로 판단..문자열이면 무조건 false가 나온다.
                entityManager.persist(entity);
                return entity;
            } else {
                return entityManager.merge(entity);
            }
        }
        */
    }

}