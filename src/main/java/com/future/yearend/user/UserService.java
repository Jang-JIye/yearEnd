package com.future.yearend.user;

import com.future.yearend.common.UserRoleEnum;
import com.future.yearend.memo.Memo;
import com.future.yearend.memo.MemoRepository;
import com.future.yearend.memo.MemoResponseDto;
import com.future.yearend.photo.Photo;
import com.future.yearend.photo.S3Repository;
import com.future.yearend.util.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final MemoRepository memoRepository;
    private final S3Repository s3Repository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @Value("${ADMIN_TOKEN}")
    private String ADMIN_TOKEN;

    public ResponseEntity<String> signup(HttpServletResponse response, LoginRequestDto loginRequestDto) {
        String username = loginRequestDto.getUsername();
        String phoneNum = loginRequestDto.getPhoneNum();
        Optional<User> existUser = userRepository.findByUsernameAndPhoneNum(username, phoneNum);
        UserRoleEnum userRole;
        // 이미 회원인 경우
        if (existUser.isPresent()) {
            String token = jwtUtil.createToken(username, existUser.get().getUserRole(), phoneNum);
            response.addHeader("Authorization", token);
        } else {
            // 회원가입
            if (loginRequestDto.getAdminToken() != null && loginRequestDto.getAdminToken().equals(ADMIN_TOKEN)) {
                userRole = UserRoleEnum.ADMIN;
            } else {
                userRole = UserRoleEnum.USER;
            }
            User user = new User(username, phoneNum, userRole);
            userRepository.save(user);
            //로그인
            if (!loginRequestDto.getPhoneNum().equals(user.getPhoneNum())) {
                throw new IllegalArgumentException("핸드폰 뒷번호 4자리가 일치하지 않습니다.");
            }
            String token = jwtUtil.createToken(username, userRole,phoneNum);
            response.addHeader("Authorization", token);
        }
        User findUser = findUser(username);
        if (findUser.getUserRole().equals(UserRoleEnum.ADMIN)) {
            return ResponseEntity.status(HttpStatus.OK) //HttpStatus.OK 로 하면 header에 /admin이 잘 들어가 있음.
                    .header("Location", "/admin")
                    .body("admin 페이지로 이동합니다.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(username+"님 반가워요!");
    }

    public ResponseEntity<UserResponseDto> getUsernameAndPhoneNum(User user) {
        User existUser = checkUser(user); // 유저 확인
        checkAuthority(existUser, user); //권한 확인

        UserResponseDto userResponseDto = new UserResponseDto(existUser);
        return ResponseEntity.ok(userResponseDto);
    }

    public ResponseEntity<UserAllResponseDto> getUserByUsername(String username, User user) {
        User existUser = checkUser(user); // 유저 확인
        checkAuthority(existUser, user); //권한 확인
        if (!user.getUserRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("어드민 계정이 아닙니다.");
        }
        User findUser = findUser(username);
        List<Memo> memoList = memoRepository.findAllByUser(findUser);
        List<Photo> photoList = s3Repository.findAllByUser(findUser);
        UserAllResponseDto userAllResponseDto = new UserAllResponseDto(findUser, memoList, photoList);
        return ResponseEntity.ok(userAllResponseDto);
    }

    public ResponseEntity<UserAllResponseDto> getUserByNickname(String nickname, User user) {
        User existUser = checkUser(user); // 유저 확인
        checkAuthority(existUser, user); //권한 확인
        if (!user.getUserRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("어드민 계정이 아닙니다.");
        }
        Memo findMemo = findMemo(nickname);
        User findUser = findUser(findMemo.getUsername());
        UserAllResponseDto userAllResponseDto = new UserAllResponseDto(findUser);
        return ResponseEntity.ok(userAllResponseDto);
    }

    public List<UserResponseDto> getAllUsers(User user) {
        User existUser = checkUser(user); // 유저 확인
        checkAuthority(existUser, user); //권한 확인
        if (!user.getUserRole().equals(UserRoleEnum.ADMIN)) {
            throw new IllegalArgumentException("어드민 계정이 아닙니다.");
        }
        List<User> userList = userRepository.findAll();
        return userList.stream().map(UserResponseDto::new).collect(Collectors.toList());
    }

    // 사용자 확인 메서드
    private User checkUser(User user) {
        return userRepository.findById(user.getId()).
                orElseThrow(() -> new IllegalArgumentException("해당 사용자는 존재하지 않습니다."));
    }

    // 권한
    private void checkAuthority(User existUser, User user) {
        if (user.getUserRole().equals(UserRoleEnum.USER) && !existUser.getId().equals(user.getId())) {
            throw new IllegalArgumentException("사용자가 일치하지 않습니다.");
        }
    }

    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                ()-> new IllegalArgumentException("해당 작성자는 존재하지 않습니다.")
        );
    }

    private Memo findMemo(String nickname) {
        return (Memo) memoRepository.findByNickname(nickname).orElseThrow(
                () -> new IllegalArgumentException("해당 메모는 존재하지 않습니다.")
        );
    }
}
