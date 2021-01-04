package com.winbaoxian.crawler.repository;

import com.winbaoxian.crawler.model.entity.SystemConfigEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-6-28 10:47:13
 */
public interface SystemConfigRepository extends JpaRepository<SystemConfigEntity, Long>, JpaSpecificationExecutor<SystemConfigEntity> {

    SystemConfigEntity findFirstByKeyAndDeletedFalse(String key);

    List<SystemConfigEntity> findAllByDeletedFalse();

}
