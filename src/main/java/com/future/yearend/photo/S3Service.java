package com.future.yearend.photo;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.future.yearend.user.User;
import com.future.yearend.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.awt.geom.RectangularShape;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3Repository s3Repository;
    private final UserRepository userRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public ResponseEntity<String> uploadPhoto(MultipartFile file, String username) {
        User user = findUser(username);

        String photoName = file.getOriginalFilename();
        String photoURL = "https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/" + photoName;
        String photoContentType = file.getContentType();

        try {
            // S3 버킷에 파일 업로드
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(photoContentType);
            amazonS3Client.putObject(bucket, photoName, file.getInputStream(), metadata);

            // 데이터베이스에 사진 정보 저장
            Photo photo = new Photo(photoURL, photoName, photoContentType, user);
            s3Repository.save(photo);

            return ResponseEntity.ok(photoURL);
        } catch (IOException e) {
            // 예외 처리
            e.printStackTrace(); // 로그 또는 예외 처리
            return null; // 실패
        }
    }

    public List<Photo> getPhotos() {
        int photoNum = 12;
        List<Photo> photoList = s3Repository.findTop12ByOrderByCreatedAtDesc(PageRequest.of(0, photoNum));
        if (photoList == null || photoList.size() < photoNum) {
            return photoList;
        } else {
            return photoList.subList(0, photoNum);
        }
    }

    public Optional<Photo> getPhoto(Long id) {
        Photo photo = findPhoto(id);
        return s3Repository.findById(id);
    }

    public ResponseEntity<String> deletePhoto(Long id, String username) {
        User user = findUser(username);
        Photo photo = findPhoto(id);
        if (user != photo.getUser()) {
            throw new IllegalArgumentException("해당 이미지의 작성자와 다릅니다.");
        }
        s3Repository.delete(photo);
        return ResponseEntity.ok("삭제 성공!");
    }
    private Photo findPhoto(Long id) {
        return s3Repository.findById(id).orElseThrow(
                ()-> new IllegalArgumentException("해당 이미지가 존재하지 않습니다.")
        );
    }
    private User findUser(String username) {
        return userRepository.findByUsername(username).orElseThrow(
                ()-> new IllegalArgumentException("해당 작성자는 존재하지 않습니다.")
        );
    }

}
