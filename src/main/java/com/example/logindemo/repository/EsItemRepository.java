package com.example.logindemo.repository;

import com.example.logindemo.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface EsItemRepository extends ElasticsearchRepository<Item,Integer> {

    Page<Item> findAll(Pageable pageable);

    Page<Item> findByTitle(String title, Pageable pageable);

    List<Item> findItemsByTitle(String title);

    Page<Item> findByTitleContaining(String title, Pageable pageable);

    Page<Item> findByTitleOrPrice(String keyworld1, String keyworld2, Pageable pageable);

    Page<Item> findItemsByTitleOrPrice(String keyworld1, String keyworld2, Pageable pageable);

}
