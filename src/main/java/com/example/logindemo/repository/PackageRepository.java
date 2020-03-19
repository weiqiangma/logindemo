package com.example.logindemo.repository;

import com.example.logindemo.entity.Packages;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface PackageRepository extends ElasticsearchRepository<Packages,Integer> {
}
