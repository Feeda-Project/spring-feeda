package com.example.feeda.domain.account;

import com.example.feeda.domain.account.dto.LogInRequestDTO;
import com.example.feeda.domain.account.dto.UpdatePasswordRequestDTO;
import com.example.feeda.domain.account.dto.UserResponseDTO;
import com.example.feeda.domain.account.dto.SignUpRequestDTO;
import com.example.feeda.domain.account.entity.Account;
import com.example.feeda.domain.profile.entity.Profile;
import com.example.feeda.security.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;



@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponseDTO signup(SignUpRequestDTO requestDTO) {
        if(accountRepository.findByEmail(requestDTO.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 이메일 입니다. : " + requestDTO.getEmail());
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

    @Transactional
    public void deleteAccount(Long id, String password) {
        Account account = getAccountById(id);

        if(!passwordEncoder.matches(password, account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        accountRepository.delete(account);
    }

    @Transactional
    public UserResponseDTO updatePassword(Long id, UpdatePasswordRequestDTO requestDTO) {
        Account account = getAccountById(id);

        if(!passwordEncoder.matches(requestDTO.getOldPassword(), account.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }

        account.setPassword(passwordEncoder.encode(requestDTO.getNewPassword()));

        // DB 에 변경 사항 강제 반영
        accountRepository.flush();

        return new UserResponseDTO(account);
    }


    public UserResponseDTO login(LogInRequestDTO requestDTO) {
        return new UserResponseDTO(accountRepository.findByEmail(requestDTO.getEmail())
                .filter(findAccount -> passwordEncoder.matches(requestDTO.getPassword(), findAccount.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 일치하지 않습니다."))
        );
    }


    /* 유틸(?): 서비스 내에서만 사용 */

    public Account getAccountById(Long id) {
        return accountRepository.findById(id).orElseThrow(() ->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "해당 id 의 유저가 존재하지 않습니다. : " + id)
        );
    }
}
