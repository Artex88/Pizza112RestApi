package ru.urfu.pizzaSite.RestApiPizzaApplication.services.Client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.ClientDetail;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;

import java.util.Optional;

@Service
public class ClientDetailService implements UserDetailsService {
    private final ClientRepository clientRepository;

    @Autowired
    public ClientDetailService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        Optional<Client> client = clientRepository.findByPhoneNumber(phoneNumber);
        if (client.isEmpty())
            throw new NotFoundException("Client with this phone number does not exist");
        return new ClientDetail(client.get());
    }
}
