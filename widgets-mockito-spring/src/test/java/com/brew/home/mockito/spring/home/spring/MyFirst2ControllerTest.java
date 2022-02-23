package com.brew.home.mockito.spring.home.spring;

import com.brew.home.mockito.spring.home.entity.Live;
import com.brew.home.mockito.spring.home.repository.LiveRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author shaogz
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class MyFirst2ControllerTest {

    /**
     * 配合@AutoConfigureMockMvc，，表示我们要使用的是模拟的网络环境，也就不是真实的网络环境，这样做可以让访问速度快一些。
     * 都是spring的注解
     */
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private LiveRepository liveRepository;

    /*
    @Transactional 在测试下的缺省行为就是执行完就回滚
    @DataJpaTest 自身就包含了这个 Annotation，所以不用特别声明。
    都是spring的注解
    */
    @Test
    void contextLoads() throws Exception {

        //你敢信下面这个代码的格式是IDEA特意调过的。。。。
        String reqBody = "{\"roomId\":12333}";
        mockMvc.perform(MockMvcRequestBuilders.post("/live-room")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reqBody))
            .andExpect(status().isCreated());


        String reqBody2 = "{\"roomId\":1}";
        mockMvc.perform(MockMvcRequestBuilders.post("/live-room")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(reqBody2))
            .andExpect(status().is4xxClientError());
    }

    /**
     * 真实的全流程测试
     * @throws Exception
     */
    @Test
    void contextLoads2() throws Exception {
        String reqBody = "{\"roomId\":12333}";
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/live-room-real")
                                                  .contentType(MediaType.APPLICATION_JSON)
                                                  .content(reqBody))
            .andExpect(status().isCreated()).andReturn();
        //get id from header
        String localtion = mvcResult.getResponse().getHeader("Location");
        String realLiveId = localtion.substring(localtion.indexOf("real") + 5);
        System.out.println("assert success, go live id from header-location:: " + realLiveId);

        Optional<Live> realLiveEntity = liveRepository.findById(Integer.parseInt(realLiveId));
        assert realLiveEntity.isPresent();
        System.out.println("assert success, go live entity using liveId " + realLiveId + " in liveRepository");
        assert realLiveEntity.get().getRoomId() == 12333;
        System.out.println("assert success, all success, all done!");
    }

}
