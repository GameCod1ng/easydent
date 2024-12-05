package webproject.easydent.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import webproject.easydent.entities.Location;
import webproject.easydent.entities.User;
import webproject.easydent.repositories.LocationRepository;
import webproject.easydent.vos.CustomOAuth2User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public Location saveLocation(Location location){
        return locationRepository.save(location);
    }

}
