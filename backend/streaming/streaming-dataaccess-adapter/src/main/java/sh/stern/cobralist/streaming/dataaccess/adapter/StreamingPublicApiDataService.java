package sh.stern.cobralist.streaming.dataaccess.adapter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sh.stern.cobralist.party.persistence.dataaccess.UserRepository;
import sh.stern.cobralist.party.persistence.domain.User;
import sh.stern.cobralist.party.persistence.exceptions.UserNotFoundException;
import sh.stern.cobralist.streaming.dataaccess.port.StreamingDataService;

@Service
public class StreamingPublicApiDataService implements StreamingDataService {

    private final UserRepository userRepository;

    @Autowired
    public StreamingPublicApiDataService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public String getUsersProviderId(Long id) {
        final User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return user.getProviderId();
    }
}
