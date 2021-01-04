package com.winbaoxian.crawler.repository;

import com.winbaoxian.crawler.model.entity.CrawlerTaskEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 17:10
 */
@Repository
public class WinCrawlerRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<CrawlerTaskEntity> getTaskList(String sql) {
        Query query = entityManager.createNativeQuery(sql, CrawlerTaskEntity.class);
        return query.getResultList();
    }

}
