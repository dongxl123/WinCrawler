package com.winbaoxian.crawler.repository;

import com.winbaoxian.crawler.model.entity.CrawlerResultEntity;
import com.winbaoxian.crawler.model.entity.CrawlerTaskEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-6-28 10:47:13
 */
public interface CrawlerResultRepository extends JpaRepository<CrawlerResultEntity, Long>, JpaSpecificationExecutor<CrawlerTaskEntity> {

    boolean existsByTaskIdAndKeyNameAndDeletedFalse(Long taskId, String keyName);

    List<CrawlerResultEntity> findByTaskIdAndDeletedFalseOrderByIdAsc(Long taskId);

    List<CrawlerResultEntity> findByTaskIdAndIdGreaterThanEqualAndDeletedFalse(Long taskId, Long startId, Pageable pageable);

    CrawlerResultEntity findByTaskIdAndKeyNameAndDeletedFalse(Long taskId, String keyName);

}
