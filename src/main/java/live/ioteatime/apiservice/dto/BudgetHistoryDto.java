package live.ioteatime.apiservice.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public interface BudgetHistoryDto {
    @Schema(description = "아이디입니다.")
    int getId();

    LocalDateTime getChangeTime();

    Long getBudget();

    interface OrganizationDto {
        int getId();

        String getName();

        Long getElectricityBudget();

        String organizationCode();
    }
}
