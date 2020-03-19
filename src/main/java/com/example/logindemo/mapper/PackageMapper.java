package com.example.logindemo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.logindemo.entity.Item;
import com.example.logindemo.entity.Packages;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageMapper extends BaseMapper<Packages> {
}
