package com.winbaoxian.crawler.model.mapper;

import com.winbaoxian.crawler.model.dto.CrawlerTaskDTO;
import com.winbaoxian.crawler.model.entity.CrawlerTaskEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 20:09
 */
@Mapper(componentModel = "spring")
public interface CrawlerTaskMapper {

    CrawlerTaskMapper INSTANCE = Mappers.getMapper(CrawlerTaskMapper.class);

    CrawlerTaskDTO toDTO(CrawlerTaskEntity entity);

    CrawlerTaskEntity toEntity(CrawlerTaskDTO dto);

    CrawlerTaskDTO[] toDTOArray(List<CrawlerTaskEntity> entityList);
}
