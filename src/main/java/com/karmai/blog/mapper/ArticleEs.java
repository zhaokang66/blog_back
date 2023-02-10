package com.karmai.blog.mapper;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Author zhaokang03
 * @Date 2023/2/10 14:55
 */
public interface ArticleEs extends ElasticsearchRepository<ArticleEs,Integer> {
}
