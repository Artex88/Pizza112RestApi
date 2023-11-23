package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientInfoRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.ClientDetail;

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
            throw new UsernameNotFoundException("Client not found");
        return new ClientDetail(client.get());
    }
}
