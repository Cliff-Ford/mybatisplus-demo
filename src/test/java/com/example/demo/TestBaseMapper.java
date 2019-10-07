package com.example.demo;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.mapper.CityMapper;
import com.example.demo.pojo.City;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class TestBaseMapper {

    @Autowired
    private CityMapper cityMapper;

    @Test
    public void testSelectListWithoutWrapper() {
        log.info(String.valueOf(cityMapper.selectList(null)));
    }

    @Test
    public void testSelectListWithWrapper() {
        QueryWrapper<City> cityQueryWrapper = new QueryWrapper<City>().le("id", 300);
        log.info(String.valueOf(cityMapper.selectList(cityQueryWrapper)));
    }

    @Test
    public void testSelectCountWithoutWrapper() {
        log.info(String.valueOf(cityMapper.selectCount(null)));
    }

    @Test
    public void testSelectCountWithWrapper() {
        QueryWrapper<City> cityQueryWrapper = new QueryWrapper<City>().le("id", 300);
        log.info(String.valueOf(cityMapper.selectCount(cityQueryWrapper)));
    }

    @Test
    public void testSelectOne() {
        //selectOne的查询结果数不是小于等于一条时会报错
        //如果根据id查询，通常用selectById
        //这里可能是联合索引/逻辑主键的一个使用
        QueryWrapper<City> cityQueryWrapper = new QueryWrapper<City>()
                .eq("id", 300)
                .eq("countryCode", "BRA");
        log.info(String.valueOf(cityMapper.selectOne(cityQueryWrapper)));
    }

    @Test
    public void testSelectByMap() {
        HashMap<String, Object> cityQueryMap = new HashMap<>();
        cityQueryMap.put("district", "Rio Grande do Sul");
        log.info(String.valueOf(cityMapper.selectByMap(cityQueryMap)));
        //log.info(String.valueOf(cityMapper.selectByMap(null)));
        log.info(String.valueOf(cityMapper.selectByMap(new HashMap<>())));
    }

    @Test
    public void testSelectById() {
        log.info(String.valueOf(cityMapper.selectById(1)));
    }

    @Test
    public void testSelectBatchIds() {
        log.info(String.valueOf(cityMapper.selectBatchIds(Arrays.asList(1, 2, 3, 4, 5))));
        //log.info(String.valueOf(cityMapper.selectBatchIds(null)));
        //log.info(String.valueOf(cityMapper.selectBatchIds(new ArrayList<>())));
    }

    @Test
    @Transactional
    @Rollback
    public void testInsert() {
        Integer beforeInsert = cityMapper.selectCount(null);
        City city = City.builder().name("HaHa").countryCode("ARM").population(4800).build();
        cityMapper.insert(city);
        Integer afterInsert = cityMapper.selectCount(null);
        log.info("id --> " + city.getId());
        log.info("beforeInsert --> " + beforeInsert);
        log.info("afterInsert --> " + afterInsert);
        Assert.assertEquals(1, afterInsert-beforeInsert);
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteById() {
        City cityBeforeDelete = cityMapper.selectById(3);
        //deleteById方法返回受影响的条数
        int num = cityMapper.deleteById(cityBeforeDelete.getId());
        log.info(String.format("删除了%s条", num));
        City cityAfterDelete = cityMapper.selectById(cityBeforeDelete.getId());
        log.info(String.valueOf(cityAfterDelete));
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteByMap() {
        HashMap<String, Object> deleteMap = new HashMap<>();
        deleteMap.put("name", "haha");
        int num = cityMapper.deleteByMap(deleteMap);
        log.info(String.format("删除了%d条", num));
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteWithoutWrapper() {
        int num = cityMapper.delete(null);
        log.info(String.format("删除了%d条", num));
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteWithWrapper() {
        QueryWrapper<City> cityQueryWrapper = new QueryWrapper<City>().eq("name", "haha");
        int num = cityMapper.delete(cityQueryWrapper);
        log.info(String.format("删除了%d条", num));
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteBatchIds() {
        int num = cityMapper.deleteBatchIds(Arrays.asList(1, 2, 3, 4, 5));
        log.info(String.format("删除了%d条", num));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateById1() {
        City beforeUpdate = cityMapper.selectById(1);
        log.info(String.valueOf(beforeUpdate));
        City updateCity = City.builder().id(1).name("haha").build();
        //updateCity对象中未设置值的字段保留原记录的值，而不是将原记录设置为空值，对比testUpdateById2会更好理解
        int num = cityMapper.updateById(updateCity);
        log.info(String.format("跟新了%d条", num));
        City afterUpdate = cityMapper.selectById(1);
        log.info(String.valueOf(afterUpdate));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateById2() {
        City beforeUpdate = cityMapper.selectById(1);
        log.info(String.valueOf(beforeUpdate));
        beforeUpdate.setName("haha");
        //beforeUpdate对象对目标字段的值做出了修改，对比testUpdateById1阅读
        int num = cityMapper.updateById(beforeUpdate);
        log.info(String.format("跟新了%d条", num));
        City afterUpdate = cityMapper.selectById(1);
        log.info(String.valueOf(afterUpdate));
    }


    @Test
    @Transactional
    @Rollback
    public void testUpdateWithoutWrapper() {
        City cityForUpdate = City.builder().name("a").build();
        //严禁这种超危动作，测试用
        int num = cityMapper.update(cityForUpdate, null);
        log.info(String.format("更新了%d条", num));
    }


    @Test
    @Transactional
    @Rollback
    public void testUpdateWithoutQueryWrapper() {
        City cityForUpdate = City.builder().name("a").build();
        QueryWrapper<City> cityQueryWrapper = new QueryWrapper<City>().eq("name", "haha");
        int num = cityMapper.update(cityForUpdate, cityQueryWrapper);
        log.info(String.format("更新了%d条", num));
        log.info(String.valueOf(cityMapper.selectList(new QueryWrapper<City>().eq("name","a"))));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateWithoutUpdateWrapper() {
        City cityForUpdate = City.builder().name("a").build();
        UpdateWrapper<City> cityUpdateWrapper = new UpdateWrapper<City>().eq("name", "haha");
        int num = cityMapper.update(cityForUpdate, cityUpdateWrapper);
        log.info(String.format("更新了%d条", num));
        log.info(String.valueOf(cityMapper.selectList(new QueryWrapper<City>().eq("name","a"))));
    }


    @Test
    public void testSelectMaps() {
        List<Map<String, Object>> maps = cityMapper.selectMaps(new QueryWrapper<City>().eq("name", "haha"));
        log.info(String.valueOf(maps));
        //每一个key都是表中的字段名
        log.info(String.valueOf(maps.get(0).keySet()));
    }

    @Test
    public void testSelectObjects() {
        List<Object> cities = cityMapper.selectObjs(new QueryWrapper<City>().eq("name", "haha"));
        //只返回id，有点奇怪
        log.info(String.valueOf(cities));
        if(cities.size() > 0){
            log.info(String.valueOf(cities.get(0)));
        }
    }


    @Test
    public void testSelectPage() {
        Page<City> cityPage = new Page<>(2,4);
        QueryWrapper<City> cityQueryWrapper = new QueryWrapper<City>().eq("name", "haha");
        IPage<City> cityIPage = cityMapper.selectPage(cityPage, cityQueryWrapper);
        if (cityIPage != null){
            log.info(String.valueOf(cityIPage.getPages()));
            log.info(String.valueOf(cityIPage.getTotal()));
            log.info(String.valueOf(cityIPage.getCurrent()));
            log.info(String.valueOf(cityIPage.getRecords()));
            log.info(String.valueOf(cityIPage.getSize()));
        }
    }

    @Test
    public void testSelectMapsPage() {
        Page<City> cityPage = new Page<>(2,4);
        QueryWrapper<City> cityQueryWrapper = new QueryWrapper<City>().eq("name", "haha");
        IPage<Map<String, Object>> mapsPage = cityMapper.selectMapsPage(cityPage, cityQueryWrapper);
        if(mapsPage != null){
            log.info(String.format("当前页：%s", mapsPage.getCurrent()));
            log.info(String.format("当前页条数：%d", mapsPage.getRecords().size()));
            log.info(String.format("当前页记录：%s", mapsPage.getRecords()));
            log.info(String.format("总页数：%s", mapsPage.getPages()));
            log.info(String.format("总条数：%s", mapsPage.getTotal()));
            log.info(String.format("每页最大条数：%s", mapsPage.getSize()));
        }
    }

}
