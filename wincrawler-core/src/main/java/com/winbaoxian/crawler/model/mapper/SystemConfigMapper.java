package com.winbaoxian.crawler.model.mapper;

import com.winbaoxian.crawler.model.dto.SystemConfigDTO;
import com.winbaoxian.crawler.model.entity.SystemConfigEntity;
import org.apache.commons.collections.CollectionUtils;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author dongxuanliang252
 * @date 2019-02-26 20:09
 */
@Mapper(componentModel = "spring")
public interface SystemConfigMapper {

    SystemConfigMapper INSTANCE = Mappers.getMapper(SystemConfigMapper.class);

    SystemConfigDTO toDTO(SystemConfigEntity entity);

    SystemConfigEntity toEntity(SystemConfigDTO dto);

    default Map<String, String> toMap(List<SystemConfigEntity> entityList) {
        Map<String, String> map = new HashMap<>();
        if (CollectionUtils.isNotEmpty(entityList)) {
            for (SystemConfigEntity entity : entityList) {
                map.put(entity.getKey(), entity.getValue());
            }
        }
        return map;
    }
}
