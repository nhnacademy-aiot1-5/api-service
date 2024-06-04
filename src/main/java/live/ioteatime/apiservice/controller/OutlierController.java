package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.domain.Outlier;
import live.ioteatime.apiservice.dto.OutlierDto;
import live.ioteatime.apiservice.service.OutlierService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Outlier", description = "이상치 API")
public class OutlierController {

    private final OutlierService outlierService;

    /**
     * 해결되지 않은 이상치 값 리스트를 불러오는 컨트롤러이다.
     * @return
     */
    @GetMapping("/outlier")
    @Operation(summary = "해결되지 않은 이상치 리스트 조회", description = "해결되지 않은 이상치 리스트를 조회합니다.")
    public ResponseEntity<List<OutlierDto>> getOutlierByOrganizationId(@RequestParam(name = "organizationId") int organizationId){
        return ResponseEntity.status(HttpStatus.OK).body(outlierService.getOutlierByOrganizationId(organizationId));
    }

    @PostMapping("/outlier")
    @Operation(summary = "이상치 등록", description = "이상치 발생 시각, 센서 정보, 위치, 이상치 값을 데이터베이스에 저장합니다.")
    public ResponseEntity<Outlier> createOutlier(@RequestBody OutlierDto outlierDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(outlierService.createOutlier(outlierDto));
    }

    @PutMapping("/outlier")
    @Operation(summary = "이상치 상태 변경", description = "이상치를 해결된 상태로 변경합니다.")
    public ResponseEntity<Integer> updateOutlier(@RequestParam(name = "id") int id, @RequestParam(name = "flag") int flag){
        return ResponseEntity.status(HttpStatus.OK).body(outlierService.updateOutlier(id, flag));
    }

}
