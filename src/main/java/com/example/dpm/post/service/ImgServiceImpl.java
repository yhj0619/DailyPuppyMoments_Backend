package com.example.dpm.post.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.dpm.post.model.ImgEntity;
import com.example.dpm.post.repository.ImgRepository;

import java.io.File;
import java.io.IOException;
@Service
public class ImgServiceImpl implements ImgService{
	private static final String FOLDER_PATH = "c:\\images\\"; // 이미지 저장 경로

    @Autowired
    private ImgRepository imgRepository; // ImgEntity를 저장하기 위한 리포지토리
    
    @Override
	public ImgEntity uploadImage(MultipartFile image) throws IOException {
        // 파일 저장 경로 및 파일 이름 설정
        String filePath = FOLDER_PATH + image.getOriginalFilename();

        // 파일을 지정한 경로에 저장
        File destinationFile = new File(filePath);
        image.transferTo(destinationFile); // 파일 저장

        // ImgEntity 생성 및 저장
        ImgEntity imgEntity = new ImgEntity();
        imgEntity.setFilePath(filePath);
        
        return imgRepository.save(imgEntity); // 데이터베이스에 저장 후 반환
    }
}
