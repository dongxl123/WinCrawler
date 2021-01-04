package com.winbaoxian.crawler.service;

import com.winbaoxian.crawler.model.dto.CrawlerResultDTO;
import com.winbaoxian.crawler.model.dto.CrawlerTaskDTO;
import com.winbaoxian.crawler.model.dto.CrawlerTemplateDTO;
import com.winbaoxian.crawler.model.dto.SystemConfigDTO;
import com.winbaoxian.crawler.model.entity.CrawlerResultEntity;
import com.winbaoxian.crawler.model.entity.CrawlerTaskEntity;
import com.winbaoxian.crawler.model.entity.CrawlerTemplateEntity;
import com.winbaoxian.crawler.model.entity.SystemConfigEntity;
import com.winbaoxian.crawler.model.mapper.CrawlerResultMapper;
import com.winbaoxian.crawler.model.mapper.CrawlerTaskMapper;
import com.winbaoxian.crawler.model.mapper.CrawlerTemplateMapper;
import com.winbaoxian.crawler.model.mapper.SystemConfigMapper;
import com.winbaoxian.crawler.repository.*;
import com.winbaoxian.crawler.utils.BeanMergeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 17:20
 */

@Service
@Slf4j
public class WinCrawlerService {

    @Resource
    private WinCrawlerRepository winTestNGRepository;
    @Resource
    private CrawlerTaskRepository crawlerTaskRepository;
    @Resource
    private CrawlerTemplateRepository crawlerTemplateRepository;
    @Resource
    private SystemConfigRepository systemConfigRepository;
    @Resource
    private CrawlerResultRepository crawlerResultRepository;

    public CrawlerTaskDTO[] getTaskList(String sql) {
        List<CrawlerTaskEntity> entityList = winTestNGRepository.getTaskList(sql);
        return CrawlerTaskMapper.INSTANCE.toDTOArray(entityList);
    }

    public SystemConfigDTO getSystemConfigByKey(String key) {
        SystemConfigEntity entity = systemConfigRepository.findFirstByKeyAndDeletedFalse(key);
        return SystemConfigMapper.INSTANCE.toDTO(entity);
    }

    public Map<String, String> getSystemConfigMap() {
        List<SystemConfigEntity> entity = systemConfigRepository.findAllByDeletedFalse();
        return SystemConfigMapper.INSTANCE.toMap(entity);
    }

    public CrawlerTemplateDTO getCrawlerTemplate(Long id) {
        CrawlerTemplateEntity entity = crawlerTemplateRepository.findOne(id);
        return CrawlerTemplateMapper.INSTANCE.toDTO(entity);
    }

    public boolean checkCrawlerResultExists(Long taskId, String keyName) {
        return crawlerResultRepository.existsByTaskIdAndKeyNameAndDeletedFalse(taskId, keyName);
    }

    public CrawlerResultDTO getCrawlerResult(Long taskId, String keyName) {
        CrawlerResultEntity entity = crawlerResultRepository.findByTaskIdAndKeyNameAndDeletedFalse(taskId, keyName);
        return CrawlerResultMapper.INSTANCE.toDTO(entity);
    }

    @Transactional
    public void saveCrawlerResult(CrawlerResultDTO resultDTO) {
        if (resultDTO.getTaskId() == null || StringUtils.isBlank(resultDTO.getKeyName())) {
            return;
        }
        CrawlerResultEntity persistent = crawlerResultRepository.findByTaskIdAndKeyNameAndDeletedFalse(resultDTO.getTaskId(), resultDTO.getKeyName());
        if (persistent == null) {
            CrawlerResultEntity entity = CrawlerResultMapper.INSTANCE.toEntity(resultDTO);
            crawlerResultRepository.save(entity);
        } else {
            BeanMergeUtils.INSTANCE.copyProperties(resultDTO, persistent);
            crawlerResultRepository.save(persistent);
        }
    }

    public List<CrawlerResultDTO> getCrawlerResultList(Long taskId) {
        List<CrawlerResultEntity> entities = crawlerResultRepository.findByTaskIdAndDeletedFalseOrderByIdAsc(taskId);
        return CrawlerResultMapper.INSTANCE.toDTOList(entities);
    }

    public List<CrawlerResultDTO> getCrawlerResultList(Long taskId, long startId, int page, int step) {
        Sort sort = new Sort(Sort.Direction.ASC, "id");
        Pageable pageable = new PageRequest(page, step, sort);
        List<CrawlerResultEntity> entities = crawlerResultRepository.findByTaskIdAndIdGreaterThanEqualAndDeletedFalse(taskId, startId, pageable);
        return CrawlerResultMapper.INSTANCE.toDTOList(entities);
    }

}
