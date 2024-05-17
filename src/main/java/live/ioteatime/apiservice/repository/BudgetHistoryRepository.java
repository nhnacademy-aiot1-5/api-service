package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.BudgetHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BudgetHistoryRepository extends JpaRepository<BudgetHistory, Integer> {
    List<BudgetHistory> findAllByOrganization_IdOrderByChangeTimeDesc(int organizationId);
}
