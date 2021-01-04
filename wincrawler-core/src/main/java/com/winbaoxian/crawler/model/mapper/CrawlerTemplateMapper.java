package com.winbaoxian.crawler.model.mapper;

import com.winbaoxian.crawler.model.dto.CrawlerTemplateDTO;
import com.winbaoxian.crawler.model.entity.CrawlerTemplateEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 20:09
 */
@Mapper(componentModel = "spring")
public interface CrawlerTemplateMapper {

    CrawlerTemplateMapper INSTANCE = Mappers.getMapper(CrawlerTemplateMapper.class);

    CrawlerTemplateDTO toDTO(CrawlerTemplateEntity entity);

    CrawlerTemplateEntity toEntity(CrawlerTemplateDTO dto);

}
