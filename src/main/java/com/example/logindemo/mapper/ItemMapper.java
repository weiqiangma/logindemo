package com.example.logindemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.logindemo.entity.Item;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemMapper extends BaseMapper<Item> {
}
