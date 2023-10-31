package com.skyblue.controller;

import com.fengwenyi.api.result.ResponseTemplate;
import com.skyblue.dto.CodeGeneratorDTO;
import com.skyblue.service.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/main")
@CrossOrigin
@Slf4j
public class MainController {
    @Autowired
    private MainService mainService;

    /**
     * 生成mybatis-plus代码
     *
     * @return result
     */
    @PostMapping("/code-generator")
    public ResponseTemplate<Void> codeGenerator(@RequestBody CodeGeneratorDTO codeGeneratorDTO) {
        log.info("生成mybatis-plus代码：{}", codeGeneratorDTO);
        return mainService.codeGenerator(codeGeneratorDTO);
    }
}
