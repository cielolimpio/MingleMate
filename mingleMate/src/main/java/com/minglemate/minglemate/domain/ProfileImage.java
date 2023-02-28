package com.minglemate.minglemate.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "profile_images")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProfileImage extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private int index;

    @Column(nullable = false, length = 200)
    private String filename;

    @Column(name = "original_s3_path", nullable = false, length = 200)
    private String originalS3Path;
    @Column(name = "thumbnail_s3_path", nullable = false, length = 200)
    private String thumbnailS3Path;


    public static ProfileImage createProfileImage(Member member, int index, String filename, String originalS3Path, String thumbnailS3Path) {
        ProfileImage profileImage = new ProfileImage();
        profileImage.member = member;
        profileImage.index = index;
        profileImage.filename = filename;
        profileImage.originalS3Path = originalS3Path;
        profileImage.thumbnailS3Path = thumbnailS3Path;

        return profileImage;
    }

    public void updateIndex(int index) {
        this.index = index;
    }
}
