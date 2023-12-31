package com.future.yearend.photo;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.transfer.Copy;
import com.future.yearend.common.UserRoleEnum;
import com.future.yearend.memo.MemoResponseDto;
import com.future.yearend.user.User;
import com.future.yearend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.DateFormatSymbols;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Repository s3Repository;
    private final UserRepository userRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public ResponseEntity<String> uploadPhoto(MultipartFile file, String month, User user) {
        User existsUser = findUser(user.getId());

        String uniqueFileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        String folderPath = month;
        String photoKey = folderPath + "/" + uniqueFileName;
        String photoURL = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + photoKey;
        String photoContentType = file.getContentType();

        try {
            // S3 버킷에 파일 업로드
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(photoContentType);
            amazonS3Client.putObject(bucket + "/" + folderPath, uniqueFileName, file.getInputStream(), metadata);

            // 데이터베이스에 사진 정보 저장
            Photo photo = new Photo(photoURL, uniqueFileName, photoContentType, month, existsUser);
            s3Repository.save(photo);

            return ResponseEntity.ok(photoURL);
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace(); // 로그 또는 예외 처리
            return null; // 실패
        }
    }

    public List<PhotoResponseDto> getPhotos() {
        int photoNum = 12;
        List<Photo> photoList = s3Repository.findTop12ByOrderByCreatedAtDesc(PageRequest.of(0, photoNum));
        if (photoList == null || photoList.size() < photoNum) {
            return photoList.stream().map(PhotoResponseDto::new).collect(Collectors.toList());
        } else {
            return photoList.subList(0, photoNum).stream().map(PhotoResponseDto::new).collect(Collectors.toList());
        }
    }

    public ResponseEntity<PhotoResponseDto> getPhoto(Long id) {
        Photo photo = findPhoto(id);
        PhotoResponseDto photoResponseDto = new PhotoResponseDto(photo);
        return ResponseEntity.ok(photoResponseDto);
    }

    public List<PhotoResponseDto> getMonthPhotos(String month) {
        List<Photo> monthPhotoList = s3Repository.findAllByMonthOrderByCreatedAtDesc(month);

        if (monthPhotoList == null || monthPhotoList.isEmpty()) {
            String defaultPhotoURL = defaultURL(month);
            PhotoResponseDto defaultPhoto = new PhotoResponseDto(defaultPhotoURL, month);
            return Collections.singletonList(defaultPhoto);
        }
        return monthPhotoList.stream()
                .map(PhotoResponseDto::new)
                .collect(Collectors.toList());
    }

    public List<PhotoResponseDto> getLatestPhotoOfEachMonth() {
        List<PhotoResponseDto> latestPhotosByMonth = new ArrayList<>();

        for (int i = 1; i <= 12; i++) {
            String month = String.valueOf(i);
            List<Photo> monthPhotoList = s3Repository.findLatestPhotosByMonth(month);
            if (monthPhotoList == null || monthPhotoList.isEmpty()) {
                String defaultPhotoURL = defaultURL(month);

                latestPhotosByMonth.add(new PhotoResponseDto(defaultPhotoURL, month));
            } else {
                Photo latestPhotoOfTheMonth = monthPhotoList.stream()
                        .max(Comparator.comparing(Photo::getCreatedAt))
                        .orElse(null);
                if (latestPhotoOfTheMonth != null) {
                    latestPhotosByMonth.add(new PhotoResponseDto(latestPhotoOfTheMonth));
                } else {
                    String defaultPhotoURL = defaultURL(month);
                    latestPhotosByMonth.add(new PhotoResponseDto(defaultPhotoURL, month));
                }
            }
        }
        return latestPhotosByMonth;
    }


    public List<PhotoResponseDto> getUserPhotos(User user) {
        User existsUser = findUser(user.getId());
        List<Photo> photoList = s3Repository.findAllByUser(existsUser);

        return photoList.stream().map(PhotoResponseDto::new).collect(Collectors.toList());
    }

    public ResponseEntity<String> deletePhoto(Long id, User user) {
        User existsUser = findUser(user.getId());
        Photo photo = findPhoto(id);
        if (existsUser.getUserRole().equals(UserRoleEnum.USER) && !photo.getUser().equals(existsUser)) {
            throw new IllegalArgumentException("해당 이미지의 작성자와 다릅니다.");
        }
        s3Repository.delete(photo);
        return ResponseEntity.ok("삭제 성공!");
    }
//----------------------------------------------------------------------------------------------------------------------
    private Photo findPhoto(Long id) {
        return s3Repository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 이미지가 존재하지 않습니다.")
        );
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("해당 작성자는 존재하지 않습니다.")
        );
    }

    private String defaultURL(String month) {
        String defaultPhotoURL;
        switch (month) {
            case "1" : defaultPhotoURL = "https://s3-yearend.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%8F%B4%EB%8D%94/1.jpg";
                break;
            case "2" : defaultPhotoURL = "https://s3-yearend.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%8F%B4%EB%8D%94/2.jpg";
                break;
            case "3" : defaultPhotoURL = "https://s3-yearend.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%8F%B4%EB%8D%94/3.jpg";
                break;
            case "4" : defaultPhotoURL = "https://s3-yearend.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%8F%B4%EB%8D%94/4.jpg";
                break;
            case "5" : defaultPhotoURL = "https://s3-yearend.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%8F%B4%EB%8D%94/5.jpg";
                break;
            case "6" : defaultPhotoURL = "https://s3-yearend.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%8F%B4%EB%8D%94/6.jpg";
                break;
            case "7" : defaultPhotoURL = "https://s3-yearend.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%8F%B4%EB%8D%94/7.jpg";
                break;
            case "8" : defaultPhotoURL = "https://s3-yearend.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%8F%B4%EB%8D%94/8.jpg";
                break;
            case "9" : defaultPhotoURL = "https://s3-yearend.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%8F%B4%EB%8D%94/9.jpg";
                break;
            case "10" : defaultPhotoURL = "https://s3-yearend.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%8F%B4%EB%8D%94/10.jpg";
                break;
            case "11" : defaultPhotoURL = "https://s3-yearend.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%8F%B4%EB%8D%94/11.jpg";
                break;
            case "12" : defaultPhotoURL = "https://s3-yearend.s3.ap-northeast-2.amazonaws.com/%EC%83%88+%ED%8F%B4%EB%8D%94/12.jpg";
                break;
            default:
                throw new IllegalArgumentException("잘못된 이미지 GET 방식입니다.");
        }
        return defaultPhotoURL;
    }
}
