package com.example.feeda.domain.account.sevice;

import com.example.feeda.domain.account.dto.LogInRequestDTO;
import com.example.feeda.domain.account.dto.UpdatePasswordRequestDTO;
import com.example.feeda.domain.account.dto.UserResponseDTO;
import com.example.feeda.domain.account.dto.SignUpRequestDTO;
import com.example.feeda.domain.account.entity.Account;
import com.example.feeda.domain.account.repository.AccountRepository;
import com.example.feeda.domain.profile.entity.Profile;
import com.example.feeda.domain.profile.repository.ProfileRepository;
import com.example.feeda.exception.CustomResponseException;
import com.example.feeda.exception.enums.ResponseError;
import com.example.feeda.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;
    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public UserResponseDTO signup(SignUpRequestDTO requestDTO) {
        if(accountRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new CustomResponseException(ResponseError.EMAIL_ALREADY_EXISTS);
        }

        if(profileRepository.findByNickname(requestDTO.getNickName()).isPresent()) {
            throw new CustomResponseException(ResponseError.NICKNAME_ALREADY_EXISTS);
        }

        Account account = new Account(requestDTO.getEmail(), requestDTO.getPassword());
        account.setPassword(passwordEncoder.encode(account.getPassword()));

        Profile profile = new Profile(requestDTO.getNickName(), requestDTO.getBirth(), requestDTO.getBio());

        // 양방향 연결
        account.setProfile(profile);
        profile.setAccount(account);

        Account saveProfile = accountRepository.save(account);

        return new UserResponseDTO(saveProfile);
    }

    @Override
    @Transactional
    public void deleteAccount(Long id, String password) {
        Account account = getAccountById(id);

        if(!passwordEncoder.matches(password, account.getPassword())) {
            throw new CustomResponseException(ResponseError.INVALID_PASSWORD);
        }

        accountRepository.delete(account);
    }

    @Override
    @Transactional
    public UserResponseDTO updatePassword(Long id, UpdatePasswordRequestDTO requestDTO) {
        Account account = getAccountById(id);

        if(!passwordEncoder.matches(requestDTO.getOldPassword(), account.getPassword())) {
            throw new CustomResponseException(ResponseError.INVALID_PASSWORD);
        }

        account.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));

        // DB 에 변경 사항 강제 반영
        accountRepository.flush();

        return new UserResponseDTO(account);
    }

    @Override
    public UserResponseDTO login(LogInRequestDTO requestDTO) {
        return new UserResponseDTO(accountRepository.findByEmail(requestDTO.getEmail())
                .filter(findAccount -> passwordEncoder.matches(requestDTO.getPassword(), findAccount.getPassword()))
                .orElseThrow(() -> new CustomResponseException(ResponseError.INVALID_EMAIL_OR_PASSWORD))
        );
    }


    /* 유틸(?): 서비스 내에서만 사용 */

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() ->
                new CustomResponseException(ResponseError.ACCOUNT_NOT_FOUND)
        );
    }
}
