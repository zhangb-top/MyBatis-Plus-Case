package com.skyblue.service;

import com.fengwenyi.api.result.ResponseTemplate;
import com.skyblue.dto.CodeGeneratorDTO;

public interface MainService {
    ResponseTemplate<Void> codeGenerator(CodeGeneratorDTO codeGeneratorDTO);
}
