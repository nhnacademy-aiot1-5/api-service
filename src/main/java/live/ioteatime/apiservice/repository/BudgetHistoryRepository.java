package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.BudgetHistory;
import live.ioteatime.apiservice.dto.BudgetHistoryDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetHistoryRepository extends JpaRepository<BudgetHistory, Integer> {
    List<BudgetHistoryDto> findAllByOrganization_IdOrderByChangeTimeDesc(int organizationId);
}
