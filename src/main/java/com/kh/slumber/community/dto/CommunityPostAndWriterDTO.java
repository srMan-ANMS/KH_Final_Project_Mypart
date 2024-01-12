package com.kh.slumber.community.dto;

import java.util.List;

import com.kh.slumber.common.model.vo.PageInfo;
import com.kh.slumber.community.model.vo.CommunityPostAndWriter;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * controller-service간 데이터 전송을 위한 객체.
 * DTO 설계시, 데이터의 무결성을 보장하기 위해 SETTER 사용하지 않음.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
// @Setter
@ToString
public class CommunityPostAndWriterDTO {
    private List<CommunityPostAndWriter> list;
    private PageInfo pi;
    private String board;
    private int page;
}
