package webproject.easydent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import webproject.easydent.entities.Location;
import webproject.easydent.repositories.LocationRepository;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public Location saveLocation(Location location){
        return locationRepository.save(location);
    }
}
