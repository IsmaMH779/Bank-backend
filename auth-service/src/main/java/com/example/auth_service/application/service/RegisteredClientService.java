package com.example.auth_service.application.service;

import com.example.auth_service.adapters.out.ClientAppRepository;
import com.example.auth_service.adapters.out.mapper.ClientAppMapper;
import com.example.auth_service.domain.exception.ObjectNotFoundException;
import com.example.auth_service.domain.model.ClientApp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;

@Service
public class RegisteredClientService implements RegisteredClientRepository {

    private ClientAppRepository clientAppRepository;

    @Autowired
    public RegisteredClientService(ClientAppRepository clientAppRepository) {
        this.clientAppRepository = clientAppRepository;
    }

    @Override
    public void save(RegisteredClient registeredClient) {}

    @Override
    public RegisteredClient findById(String id) {
        ClientApp clientApp = clientAppRepository.findByClientId(id)
                .orElseThrow( () -> new ObjectNotFoundException("Client not found"));

        return ClientAppMapper.toRegisteredClient(clientApp);
    }

    @Override
    public RegisteredClient findByClientId(String clientId) {
        return findById(clientId);
    }
}
