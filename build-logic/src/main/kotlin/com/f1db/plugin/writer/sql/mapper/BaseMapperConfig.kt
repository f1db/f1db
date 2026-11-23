package com.f1db.plugin.writer.sql.mapper

import org.mapstruct.MapperConfig
import org.mapstruct.ReportingPolicy

@MapperConfig(
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    uses = [StringNormalizer::class]
)
interface BaseMapperConfig
