package com.brew.home.mockito.spring.home.spring;

import com.brew.home.mockito.spring.home.entity.Live;
import com.brew.home.mockito.spring.home.repository.LiveRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

// @SpringBootTest
@DataJpaTest
@ExtendWith(SpringExtension.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class MyFirst1JpaTest {

    @Autowired
    private LiveRepository liveRepository;

    @Test
    void contextLoads() throws InterruptedException {
        //需要注意的是，虽然回滚后数据不会重新插入到数据库中，但是自增主键还是会涨上去
        Live live = new Live();
        live.setRoomId(123);
        live.setUserId(123);
        live.setStatus(123);
        live.setRecordVideoId(123);
        live.setRecordVideoStatus(123);
        live.setTemplateType(123);
        live.setSourceNodeId(123);
        live.setSourceType(123);

        liveRepository.save(live);
        System.out.println(live.getId());

        TimeUnit.SECONDS.sleep(10);
        System.out.println("finished");
        Optional<Live> byId1 = liveRepository.findById(live.getId());
        if (byId1.isPresent()) {
            System.out.println(byId1);
        } else {
            System.out.println("not present");
        }
    }

}
