package com.karmai.blog.mapper;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @Author zhaokang03
 * @Date 2023/2/10 14:55
 */
@Repository
public interface ArticleEs extends ElasticsearchRepository<com.karmai.blog.entity.ArticleEs,Integer> {
}
