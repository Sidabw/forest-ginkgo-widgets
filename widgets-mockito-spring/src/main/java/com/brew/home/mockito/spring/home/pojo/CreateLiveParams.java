package com.brew.home.mockito.spring.home.pojo;

import lombok.Data;

import javax.validation.constraints.Min;

/**
 * @author shaogz
 */
@Data
public class CreateLiveParams {

    @Min(value = 12)
    private Integer roomId;

}
