package com.example.logindemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.logindemo.dto.request.ItemRequest;
import com.example.logindemo.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Optional;

public interface ItemService extends IService<Item> {

    /**
     * 从数据库中导入所有商品到ES
     */
    int importAll();

    Optional<Item> findById(Integer id);

    /**
     * 根据id删除商品
     */
    void delete(Integer id);

    /**
     * 根据id创建商品
     */
    Item create(Integer id);

    /**
     * 批量删除商品
     */
    void delete(List<Integer> ids);

    /**
     * 根据关键字搜索名称或者副标题
     */
    Page<Item> search(String keyword, Integer pageNum, Integer pageSize);

    /**
     * 根据id查询商品
     */
    Page<Item> findAllItem(Pageable pageable);

    /**
     * 根据商品title查询
     * @param title
     * @param pageable
     * @return
     */
    Page<Item> findByTitle(String title, Pageable pageable);
}
