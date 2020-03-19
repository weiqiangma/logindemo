package com.example.logindemo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.logindemo.entity.Item;
import com.example.logindemo.entity.Packages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface IPackageService extends IService<Packages> {

    int importAll();
}
