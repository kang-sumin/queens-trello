package com.practice.queenstrello.domain.workspace.controller;

import com.practice.queenstrello.config.JwtSecurityFilter;
import com.practice.queenstrello.domain.auth.AuthUser;
import com.practice.queenstrello.domain.user.entity.User;
import com.practice.queenstrello.domain.user.entity.UserRole;
import com.practice.queenstrello.domain.workspace.service.MasterRequestService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MasterRequestController.class)
@MockBean(JpaMetamodelMappingContext.class)
class MasterRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MasterRequestService masterRequestService;

    @MockBean
    private JwtSecurityFilter jwtSecurityFilter;


    @Test
    @WithMockUser
    void saveMasterRequest_요청_성공() throws Exception {
        // given
        AuthUser authUser = new AuthUser(1L,"test@test.com","nickname", UserRole.ROLE_USER);
        String responseMessage = "nickname 님의 MASTER 권한 변경 요청이 정상적으로 신청되었습니다.";

        // when
        when(masterRequestService.saveMasterRequest(authUser)).thenReturn(responseMessage);

        // then
        mockMvc.perform(get("/users/master-request"))
                .andExpect(status().isOk()) // 응답 상태가 200 OK 인지 확인
                .andExpect(content().string(responseMessage));  // 응답 본문에 기대한 문자열이 포함되어 있는지 확인

    }
}