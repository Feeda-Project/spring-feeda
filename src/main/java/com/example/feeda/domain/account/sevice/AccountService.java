package com.example.feeda.domain.account.sevice;

import com.example.feeda.domain.account.dto.LogInRequestDTO;
import com.example.feeda.domain.account.dto.SignUpRequestDTO;
import com.example.feeda.domain.account.dto.UpdatePasswordRequestDTO;
import com.example.feeda.domain.account.dto.UserResponseDTO;

public interface AccountService {
    UserResponseDTO signup(SignUpRequestDTO requestDTO);

    void deleteAccount(Long id, String password);

    UserResponseDTO updatePassword(Long id, UpdatePasswordRequestDTO requestDTO);

    UserResponseDTO login(LogInRequestDTO requestDTO);
}
