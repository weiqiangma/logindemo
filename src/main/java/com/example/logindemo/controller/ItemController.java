package com.example.logindemo.controller;

import com.example.logindemo.dto.request.ItemRequest;
import com.example.logindemo.entity.Item;
import com.example.logindemo.repository.EsItemRepository;
import com.example.logindemo.repository.ItemRepository;
import com.example.logindemo.service.ItemService;
import com.example.logindemo.util.CommonResponse;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping(value = "/api/item")
@Api(tags = "商品控制器")
public class ItemController {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    ItemService itemService;

    @Autowired
    EsItemRepository esItemRepository;

    @Autowired
    ItemRepository itemRepository;

    @ApiOperation(value = "获取所有商品信息",notes = "获取所有商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页码",required = true,dataType = "int"),
            @ApiImplicitParam(name = "pageSize",value = "每页条数",required = true,dataType = "int")
    })
    @RequestMapping(value = "/getAllItems",method = RequestMethod.GET)
    public CommonResponse getAllItems(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize, @RequestParam("sortProperty") String sortProperty) {
        RBucket rBucket1 = redissonClient.getBucket("allItems" + pageNum);
        if(rBucket1.get() != null){
            return new CommonResponse("0","OK",rBucket1.get());
        }else {
            Page<Item> page = itemService.findAllItem(PageRequest.of(pageNum,pageSize,Sort.by(Sort.Direction.DESC,sortProperty)));
            //RBucket rBucket2 = redissonClient.getBucket("allItems" + pageNum);
            //rBucket2.set(page);
            //rBucket2.expire(2,TimeUnit.MINUTES);
            return new CommonResponse("0","OK",page);
        }
    }


    @ApiOperation(value = "获取所有商品信息",notes = "分页获取所有商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNum",value = "页码",required = true,dataType = "int"),
            @ApiImplicitParam(name = "pageSize",value = "每页条数",required = true,dataType = "int")
    })
    @RequestMapping(value = "/getItems",method = RequestMethod.GET)
    public CommonResponse getItems(@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        Iterable<Item> iterable = esItemRepository.findAll();
        List<Item> list = new ArrayList<>();
        for(Item item : iterable){
            list.add(item);
        }
        return new CommonResponse("0","OK",list);
    }

    @ApiOperation(value = "根据关键字查询商品信息",notes = "根据关键字查询商品信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "keyworld", value = "关键字", required = false, dataType = "String", example = "0"),
            @ApiImplicitParam(name = "pageNum",value = "页码",required = true,dataType = "int", example = "0"),
            @ApiImplicitParam(name = "pageSize",value = "每页条数",required = true,dataType = "int", example = "0")
    })
    @RequestMapping(value = "/getItemsByKeyWorld",method = RequestMethod.GET)
    public CommonResponse getItemsByKeyWorld(@RequestParam("title") String title,@RequestParam("pageNum") Integer pageNum, @RequestParam("pageSize") Integer pageSize) {
        Page<Item> page = itemService.findByTitle(title, PageRequest.of(pageNum,pageSize));
       return new CommonResponse("0","OK",page);
    }

}
