package com.yu.blog.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoProfile {
    public Integer id;
    public String connected_at;
    public Properties properties;
    public KakaoAccount kakao_account;

    @Getter
    @Setter
    @ToString
    public class Properties {
        public String nickname;
        public String profile_image;
        public String thumbnail_image;
    }
    @Getter
    @Setter
    @ToString
    public class KakaoAccount {
        public Boolean profile_needs_agreement;
        public Profile profile;
        public Boolean has_email;
        public Boolean email_needs_agreement;
        public Boolean is_email_valid;
        public Boolean is_email_verified;
        public String email;
        @Getter
        @Setter
        @ToString
        public class Profile {
            public String nickname;
            public String thumbnail_image_url;
            public String profile_image_url;
        }
    }
}

