package com.academy.fintech.scoring.grpc.origination.v1;

import com.academy.fintech.scoring.ScoringRequest;
import com.academy.fintech.scoring.public_interface.dto.ScoringRequestDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface OriginationGrpcMapper {

    ScoringRequestDto toScoringRequestDto(ScoringRequest scoringRequest);
}
