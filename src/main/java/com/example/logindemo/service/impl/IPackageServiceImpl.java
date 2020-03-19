package com.example.logindemo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.logindemo.entity.Item;
import com.example.logindemo.entity.Packages;
import com.example.logindemo.mapper.ItemMapper;
import com.example.logindemo.mapper.PackageMapper;
import com.example.logindemo.repository.EsItemRepository;
import com.example.logindemo.repository.PackageRepository;
import com.example.logindemo.service.IPackageService;
import com.example.logindemo.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.util.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class IPackageServiceImpl extends ServiceImpl<PackageMapper, Packages>  implements IPackageService {

    @Autowired
    PackageMapper packageMapper;

    @Autowired
    PackageRepository packageRepository;

    @Override
    public int importAll() {
        List<Packages> packagesList = packageMapper.selectList(new QueryWrapper<>());
        Iterable<Packages> esItemIterable = packageRepository.saveAll(packagesList);
        Iterator<Packages> iterator =esItemIterable.iterator();
        int result = 0;
        while(iterator.hasNext()){
            result++;
            iterator.next();
        }
        return result;
    }
}
