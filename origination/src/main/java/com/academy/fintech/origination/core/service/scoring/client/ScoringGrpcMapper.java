package com.academy.fintech.origination.core.service.scoring.client;

import com.academy.fintech.origination.public_interface.scoring.ScoringRequestDto;
import com.academy.fintech.origination.public_interface.scoring.ScoringResponseDto;
import com.academy.fintech.scoring.ScoringRequest;
import com.academy.fintech.scoring.ScoringResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ScoringGrpcMapper {
    ScoringRequest toScoringRequest(ScoringRequestDto scoringRequestDto);

    ScoringResponseDto toScoringResponse(ScoringResponse scoringResponse);
}
