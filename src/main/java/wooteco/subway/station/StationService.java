package wooteco.subway.station;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class StationService {

    private final StationDao stationDao;

    public StationService(StationDao stationDao) {
        this.stationDao = stationDao;
    }

    @Transactional
    public StationResponse createStation(StationRequest stationRequest) {
        validateNotToDuplicateName(stationRequest.getName());
        StationEntity stationEntity = new StationEntity(stationRequest.getName());
        StationEntity newStationEntity = stationDao.save(stationEntity);
        return new StationResponse(newStationEntity.getId(), newStationEntity.getName());
    }

    private void validateNotToDuplicateName(String name) {
        if (stationDao.hasStationWithName(name)) {
            throw new IllegalArgumentException("이미 존재하는 이름입니다.");
        }
    }

    @Transactional
    public List<StationResponse> showStations() {
        List<StationEntity> stations = stationDao.findAll();
        return stations.stream()
            .map(stationEntity -> new StationResponse(stationEntity.getId(),
                stationEntity.getName()))
            .collect(Collectors.toList());
    }

    @Transactional
    public void delete(Long id) {
        validateToExistId(id);
        stationDao.delete(id);
    }

    private void validateToExistId(Long id) {
        if (!stationDao.hasStationWithId(id)) {
            throw new IllegalArgumentException("존재하지 않는 ID입니다.");
        }
    }
}
