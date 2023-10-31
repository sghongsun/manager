package me.sghong.manager.app.manage.request;

import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TermsDeleteRequest {
    @Positive(message = "잘못 된 값입니다.")
    private int idx;
}
