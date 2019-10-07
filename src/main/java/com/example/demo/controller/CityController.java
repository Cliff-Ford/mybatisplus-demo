package com.example.demo.controller;

import com.example.demo.mapper.CityMapper;
import com.example.demo.pojo.City;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class CityController {

    @Autowired
    private CityMapper cityMapper;

    @RequestMapping("/city/1")
    public City getFirstCity(){
        log.info("interface /city/1 is ok !");
        return cityMapper.getFirstCity();
    }

    @RequestMapping("/city/2")
    public City getSecondCity(){
        log.info("interface /city/2 is ok !");
        return cityMapper.getSecondCity();
    }

    @RequestMapping("/city/3")
    public City getThirdCity(){
        log.info("interface /city/3 is ok !");
        return cityMapper.selectById(3);
    }

    @RequestMapping("/city/{id}")
    public City getCityById(@PathVariable Integer id){
        log.info("id is " + id);
        return cityMapper.selectById(id);
    }

}
