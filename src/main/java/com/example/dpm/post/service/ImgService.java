package com.example.dpm.post.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.example.dpm.post.model.ImgEntity;

public interface ImgService {

	ImgEntity uploadImage(MultipartFile image) throws IOException;

}
