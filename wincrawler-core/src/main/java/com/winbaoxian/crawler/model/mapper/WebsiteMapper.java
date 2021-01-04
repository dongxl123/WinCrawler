package com.winbaoxian.crawler.model.mapper;

import com.winbaoxian.crawler.model.dto.WebsiteDTO;
import com.winbaoxian.crawler.model.entity.WebsiteEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * @author dongxuanliang252
 * @date 2019-7-18 15:10:06
 */
@Mapper(componentModel = "spring")
public interface WebsiteMapper {

    WebsiteMapper INSTANCE = Mappers.getMapper(WebsiteMapper.class);

    WebsiteDTO toDTO(WebsiteEntity entity);

    WebsiteEntity toEntity(WebsiteDTO dto);

}
