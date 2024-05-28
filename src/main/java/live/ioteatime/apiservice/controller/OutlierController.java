package live.ioteatime.apiservice.controller;

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
public class OutlierController {
    private final OutlierService outlierService;

    /**
     * 해결되지 않은 이상치 값 리스트를 불러오는 컨트롤러이다.
     * @return
     */
    @GetMapping("/outlier")
    public ResponseEntity<List<OutlierDto>> getUnsolvedOutlier(){
        return ResponseEntity.status(HttpStatus.OK).body(outlierService.getUnsolvedOutlier());
    }

    @PostMapping("/outlier")
    public ResponseEntity<Integer> createOutlier(@RequestBody OutlierDto outlierDto){
        return ResponseEntity.status(HttpStatus.CREATED).body(outlierService.createOutlier(outlierDto));
    }

    @PutMapping("/outlier")
    public ResponseEntity<Integer> updateOutlier(@RequestParam(name = "id") int id, @RequestParam(name = "flag") int flag){
        return ResponseEntity.status(HttpStatus.OK).body(outlierService.updateOutlier(id, flag));
    }
}
