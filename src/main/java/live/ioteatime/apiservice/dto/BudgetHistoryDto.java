package live.ioteatime.apiservice.dto;

import java.time.LocalDateTime;

public interface BudgetHistoryDto {

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
