package ru.urfu.pizzaSite.RestApiPizzaApplication.services;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ru.urfu.pizzaSite.RestApiPizzaApplication.dto.ClientInfoDTO;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.Client;
import ru.urfu.pizzaSite.RestApiPizzaApplication.model.ClientInfo;
import ru.urfu.pizzaSite.RestApiPizzaApplication.repositories.ClientInfoRepository;
import ru.urfu.pizzaSite.RestApiPizzaApplication.security.JWTUtil;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.ImageSaveException;
import ru.urfu.pizzaSite.RestApiPizzaApplication.util.exceptions.NotFoundException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Consumer;

@Service
public class ClientInfoService {

    private final ClientInfoRepository clientInfoRepository;

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/webp");

    private static final long MAX_IMAGE_SIZE = 204800;
    private final JWTUtil jwtUtil;

    @Autowired
    public ClientInfoService(ClientInfoRepository clientInfoRepository, JWTUtil jwtUtil) {
        this.clientInfoRepository = clientInfoRepository;
        this.jwtUtil = jwtUtil;
    }

    @Transactional(readOnly = true)
    public ClientInfo findByPhoneNumber(String phoneNumber){
        Optional<ClientInfo> clientInfoOptional = clientInfoRepository.findByPhoneNumber(phoneNumber);
        if (clientInfoOptional.isPresent())
            return clientInfoOptional.get();
        throw new NotFoundException("Client with this phone number does not exist");
    }

    @Transactional
    public Client updateClientInfo(Client client, ClientInfoDTO clientInfoDTO){
        updateFieldIfNotNull(clientInfoDTO.getPhoneNumber(), client::setPhoneNumber);
        ClientInfo clientInfo = client.getClient_info();
        BeanUtils.copyProperties(clientInfoDTO, clientInfo, getNullPropertyNames(clientInfoDTO));
        return client;
    }
    @Transactional
    public void updateAvatar(ClientInfo clientInfo, MultipartFile imageFile) throws IOException {
        String contentType = validateImageAndGetContentType(imageFile);
        String absolutePath = "src/main/resources/static/images/Avatars/";
        String customPath = clientInfo.getId() + "." + contentType;
        try {
            String photoName = clientInfo.getImageName();
            if (photoName != null){
                Files.delete(Paths.get(absolutePath + photoName));
            }
            Files.write(Paths.get(absolutePath + customPath), imageFile.getBytes());
            clientInfo.setImageName(customPath);
            clientInfoRepository.save(clientInfo);
        } catch (IOException e) {
            throw new IOException();
        }
    }

    private String validateImageAndGetContentType(MultipartFile imageFile){
        if (imageFile.isEmpty() || imageFile.getSize() == 0)
            throw new ImageSaveException();
        String contentType = imageFile.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(contentType))
            throw new ImageSaveException();
        if (imageFile.getSize() > MAX_IMAGE_SIZE)
            throw new ImageSaveException();
        return contentType.substring(6);
    }
    private String[] getNullPropertyNames(Object source) {
        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] pds = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor pd : pds) {
            if (pd.getName().equals("class") || src.getPropertyValue(pd.getName()) == null) {
                emptyNames.add(pd.getName());
            }
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }

    private <T> void updateFieldIfNotNull(T newValue, Consumer<T> updateFunction){
        if (newValue != null)
            updateFunction.accept(newValue);
    }

    public Map<String, String> fillClientInfoJSON(ClientInfo clientInfo) {
        Map<String,String> json = new HashMap<>();
        json.put("name", clientInfo.getName());
        json.put("surname", clientInfo.getSurname());
        json.put("patronymic", clientInfo.getPatronymic());
        json.put("phoneNumber", clientInfo.getPhoneNumber());
        json.put("imageName", clientInfo.getImageName());
        if (clientInfo.getDateOfBirth() == null)
            json.put("dateOfBirth", null);
        else
            json.put("dateOfBirth", clientInfo.getDateOfBirth().toString());
        json.put("email", clientInfo.getEmail());
        return json;
    }

    @Transactional(readOnly = true)
    public boolean isClientExist(String phoneNumber){
        return clientInfoRepository.findByPhoneNumber(phoneNumber).isPresent();
    }
    @Transactional
    public void save(ClientInfo clientInfo){
        clientInfoRepository.save(clientInfo);
    }

    public String getPhoneNumberFromToken(String token){
        String jwt = token.substring(7);
        return jwtUtil.validateTokenAndRetrieveClaim(jwt);
    }
}
