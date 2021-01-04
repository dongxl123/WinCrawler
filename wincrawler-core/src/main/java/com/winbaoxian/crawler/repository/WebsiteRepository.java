package com.winbaoxian.crawler.repository;

import com.winbaoxian.crawler.model.entity.WebsiteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author dongxuanliang252
 * @date 2019-6-28 10:47:13
 */
public interface WebsiteRepository extends JpaRepository<WebsiteEntity, Long>, JpaSpecificationExecutor<WebsiteEntity> {


}
