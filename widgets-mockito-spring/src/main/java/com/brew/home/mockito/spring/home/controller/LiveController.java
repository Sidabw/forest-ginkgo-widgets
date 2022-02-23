package com.brew.home.mockito.spring.home.controller;

import com.brew.home.mockito.spring.home.pojo.CreateLiveParams;
import com.brew.home.mockito.spring.home.service.ILiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

/**
 * @author shaogz
 */
@RestController
public class LiveController {

    private ILiveService liveService;

    @PostMapping(value = "/live-room")
    public ResponseEntity<Void> createLive(@RequestBody @Valid CreateLiveParams createLiveParams) {
        //失败的请求，返回400： curl -X POST "http://localhost:8021/live-room" -H 'Content-Type: application/json' -d '{"roomId":3}' -i
        //成功的请求，返回201：curl -X POST "http://localhost:8021/live-room" -H 'Content-Type: application/json' -d '{"roomId":12333}' -i
        System.out.println(createLiveParams);

        /*
        如下写法，返回头信息如下，创建的资源默认就是：live-room/{id}
        HTTP/1.1 201
        Location: http://localhost:8021/live-room/111
        Content-Length: 0
        Date: Wed, 23 Feb 2022 07:19:21 GM
        */
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(111).toUri();
        return ResponseEntity.created(uri).build();
    }


    @PostMapping(value = "/live-room-real")
    public ResponseEntity<Void> createLiveReal(@RequestBody @Valid CreateLiveParams createLiveParams) {
        Integer liveId = liveService.createLiveByRoomId(createLiveParams.getRoomId());
        System.out.println("create live by controller -> service -> repository :: " + liveId);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(liveId).toUri();
        return ResponseEntity.created(uri).build();
    }

    @Autowired
    public void setLiveService(ILiveService liveService) {
        this.liveService = liveService;
    }
}
