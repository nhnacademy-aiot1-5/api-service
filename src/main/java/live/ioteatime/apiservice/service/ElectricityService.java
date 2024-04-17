package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.DailyElectricity;

import java.time.LocalDate;
import java.util.List;

public interface ElectricityService<T> {
    T getElectricityByDate(LocalDate localDate);

    List<DailyElectricity> getElectricitiesByDate(LocalDate localDate);
}
