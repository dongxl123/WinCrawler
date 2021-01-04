package com.winbaoxian.crawler.model.mapper;

import com.winbaoxian.crawler.model.dto.CrawlerResultDTO;
import com.winbaoxian.crawler.model.entity.CrawlerResultEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 20:09
 */
@Mapper(componentModel = "spring")
public interface CrawlerResultMapper {

    CrawlerResultMapper INSTANCE = Mappers.getMapper(CrawlerResultMapper.class);

    CrawlerResultDTO toDTO(CrawlerResultEntity entity);

    CrawlerResultEntity toEntity(CrawlerResultDTO dto);

    List<CrawlerResultDTO> toDTOList(List<CrawlerResultEntity> entities);
}
