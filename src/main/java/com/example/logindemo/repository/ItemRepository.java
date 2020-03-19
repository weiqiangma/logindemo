package com.example.logindemo.repository;

import com.example.logindemo.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ItemRepository extends PagingAndSortingRepository<Item, Integer> {

}
