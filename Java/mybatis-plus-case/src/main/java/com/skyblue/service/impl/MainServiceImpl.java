package com.skyblue.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.fengwenyi.api.result.ResponseTemplate;
import com.fengwenyi.apistarter.utils.Asserts;
import com.skyblue.bo.CodeGeneratorBO;
import com.skyblue.dto.CodeGeneratorDTO;
import com.skyblue.generator.AutoGenerator;
import com.skyblue.service.MainService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class MainServiceImpl implements MainService {
    @Override
    public ResponseTemplate<Void> codeGenerator(CodeGeneratorDTO cgDTO) {
        // 将前端传递的数据进行转换
        CodeGeneratorBO cgBO = BeanUtil.copyProperties(cgDTO,
                CodeGeneratorBO.class);
        log.info("后端处理后的数据：{}", cgBO);

        // 处理数据库的相关数据
        handlerDB(cgDTO, cgBO);
        // 处理表格相关的数据
        handlerTable(cgDTO, cgBO);

        // 处理核心逻辑代码
        return execute(cgBO);
    }

    /**
     * 处理数据库的相关数据
     *
     * @param cgDTO 前端传递的数据
     * @param cgBO  后端处理后的数据
     */
    private void handlerDB(CodeGeneratorDTO cgDTO, CodeGeneratorBO cgBO) {
        String dbUrl = "";
        String driver = "";
        String username = cgDTO.getUsername();
        String password = cgDTO.getPassword();


        String dbTypeName = cgDTO.getDbTypeName();
        String dbName = cgDTO.getDbName();

        if (StrUtil.isNotEmpty(cgDTO.getDbTypeName())
                || DbType.getDbType(dbTypeName).equals(DbType.MYSQL)) {
            // mysql
            dbUrl = "jdbc:mysql://" + cgDTO.getHost() + "/" + dbName;
            driver = "com.mysql.cj.jdbc.Driver";
        } else if (DbType.getDbType(dbTypeName).equals(DbType.ORACLE)) {
            // oracle
            dbUrl = "jdbc:oracle:thin:@" + cgDTO.getHost() + ":" + dbName;
            driver = "oracle.jdbc.OracleDriver";
        } else {
            // 其他数据库（全局异常处理）
            Asserts.fail("暂不支持" + dbTypeName + "数据库");
        }

        // 填充后端处理后的数据
        cgBO.setDbUrl(dbUrl)
                .setDriver(driver)
                .setUsername(username)
                .setPassword(password);
    }

    /**
     * 处理表格相关的数据
     *
     * @param cgDTO 前端传递的数据
     * @param cgBO  后端处理后的数据
     */
    private void handlerTable(CodeGeneratorDTO cgDTO, CodeGeneratorBO cgBO) {
        cgBO.setTableNames(split(cgDTO.getTableNames()))
                .setFieldPrefixes(split(cgDTO.getFieldPrefixes()))
                .setExcludeTableNames(split(cgDTO.getExcludeTableNames()))
                .setIgnoreColumns(split(cgDTO.getIgnoreColumns()));
    }

    /**
     * 分割前端传递的有关数据库表相关的数据
     *
     * @param value 字符串
     * @return 数组
     */
    private String[] split(String value) {
        if (StrUtil.isEmpty(value)) {
            return new String[]{};
        }
        List<String> valueList = new ArrayList<>();
        String[] values;
        if (value.contains(",")) {
            values = value.split(",");
        } else if (value.contains("\n")) {
            values = value.split("\n");
        } else {
            values = value.split(" ");
        }
        for (String str : values) {
            str = str.trim();
            if (StringUtils.hasText(str)) {
                valueList.add(str);
            }
        }
        String[] result = new String[valueList.size()];
        return valueList.toArray(result);
    }

    /**
     * 处理核心逻辑代码
     *
     * @param cgBO cgBO
     */
    private ResponseTemplate<Void> execute(CodeGeneratorBO cgBO) {
        try {
            new AutoGenerator(cgBO).execute();
            return ResponseTemplate.success();
        } catch (Exception e) {
            Asserts.fail("代码生成失败");
            log.error("代码生成失败：{}", e.getMessage());
        }
        return ResponseTemplate.fail();
    }
}
