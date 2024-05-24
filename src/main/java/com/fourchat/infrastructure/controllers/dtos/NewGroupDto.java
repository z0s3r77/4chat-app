package com.fourchat.infrastructure.controllers.dtos;

import lombok.Builder;

import java.util.List;

@Builder
public record NewGroupDto(List<String> participantsIds,
                          List<String> groupAdminIds,
                          String groupName,
                          String description) {
}
